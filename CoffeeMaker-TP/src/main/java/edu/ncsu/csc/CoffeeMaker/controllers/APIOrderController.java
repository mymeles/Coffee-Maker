package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.status.Status;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Orders.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Meles Meles
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIOrderController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService     inventoryService;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private CustomerOrderService orderService;

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService          userService;

    /**
     * recipeService object, to be autowired in by Spring to allow for
     * manipulating the recipe model
     */
    @Autowired
    private RecipeService        recipeService;

    /**
     * REST API method to provide GET access to all customer orders in the
     * system
     *
     * @return JSON representation of all recipies
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<CustomerOrder> getOrders () {
        return orderService.findAll();
    }

    /**
     * REST API method to provide GET access to a specific order, as indicated
     * by the path variable provided (the id of the order desired)
     *
     * @param id
     *            order id
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity getOrder ( @PathVariable ( "id" ) final Long id ) {
        System.out.println( "------------" + id );
        final CustomerOrder ord = orderService.findById( id );
        return null == ord
                ? new ResponseEntity( errorResponse( "No Order found with the id " + id ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( ord, HttpStatus.OK );
    }

    /**
     * REST API method that provides POST access by connecting an order to a
     * customer.
     *
     * @param name
     *            customer username
     * @param amt
     *            payment amount
     * @param ord
     *            customer order
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/orders/{name}/{amt}/{recipe_name}" )
    public ResponseEntity placeOrder ( @PathVariable final String name, @PathVariable final int amt,
            @PathVariable final String recipe_name ) {

        final Recipe r = recipeService.findByName( recipe_name );
        final User usr = userService.findByUsername( name );
        if ( usr == null ) {
            System.out.println( "Check user --------- " + name );
            return new ResponseEntity( errorResponse( "Customer with the name " + name + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        if ( r == null ) {
            System.out.println( "Check recipe --------- " + recipe_name );
            return new ResponseEntity( errorResponse( "Recipe with the name " + recipe_name + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        // Now handle a customer with an order and if there exits then return a
        // message
        if ( usr.getCustomerOrder() != null ) {
            return new ResponseEntity( errorResponse( "Customer can only place one order at a time " ),
                    HttpStatus.CONFLICT );
        }

        // handle the money and make sure it is enough
        if ( amt < r.getPrice() ) {
            return new ResponseEntity( errorResponse( "Insufficient amount" ), HttpStatus.PAYMENT_REQUIRED );
        }
        else {
            usr.setCustomerOrder( new CustomerOrder( recipe_name, Status.Order_Placed, name ) );
            userService.save( usr );
            return new ResponseEntity<String>( successResponse( String.valueOf( amt - ( r.getPrice() ) ) ),
                    HttpStatus.OK );
        }
    }

    /**
     * REST API method that provides DELETE access by removing an order from the
     * active list when it is picked up
     *
     * @param id
     *            order id
     * @return response to the request
     */
    @DeleteMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity pickUpOrder ( @PathVariable ( "id" ) final Long id ) {
        final User usr = userService.findById( id );
        // error for a customer not existing handles
        if ( usr == null ) {
            return new ResponseEntity( errorResponse( "Customer with the Id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        // Now handle a customer with an order and if there exits then return a
        // message
        if ( usr.getCustomerOrder() == null ) {
            return new ResponseEntity( errorResponse( "Customer already picked-up thier order" ), HttpStatus.CONFLICT );
        }

        else {
            userService.deleteCustomerOrder( usr );
            return new ResponseEntity<String>( successResponse( "Order Picked-Up" ), HttpStatus.OK );
        }
    }

    /**
     * REST API method that provides PUT access by updating the order's status
     * to complete
     *
     * @param id
     *            order id
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/orders/{id}" )
    public ResponseEntity completeOrder ( @PathVariable ( "id" ) final Long id ) {
        final CustomerOrder ord = orderService.findById( id );
        // error for a customer not existing handles
        if ( ord == null ) {
            return new ResponseEntity( errorResponse( "Order with the Id " + id + "does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        final boolean order_flag = completeOrderHelper( ord );
        if ( order_flag ) {
            ord.setOrderStatus( Status.Order_Completed );
            orderService.save( ord );
            return new ResponseEntity<String>( successResponse( "Order Completed" ), HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "Not enough inventory" ), HttpStatus.CONFLICT );
        }
    }

    /**
     * A helper function to help complete the order by making the coffee in the
     * Inventory
     *
     * @param ord
     *            order being completed
     * @return true if the inventory has enough ingredients
     */
    public boolean completeOrderHelper ( final CustomerOrder ord ) {
        final Inventory inventory = inventoryService.getInventory();

        // get the recipe
        final Recipe temp = recipeService.findByName( ord.getRecipe() );

        if ( inventory.useIngredients( temp ) ) {
            inventoryService.save( inventory );
            return true;
        }
        else {
            return false;
        }
    }

}
