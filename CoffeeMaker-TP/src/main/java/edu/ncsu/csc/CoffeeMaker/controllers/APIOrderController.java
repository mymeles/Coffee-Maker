package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
     * @return JSON representation of all orders
     */
    @GetMapping ( BASE_PATH + "/orders" )
    public List<CustomerOrder> getOrders () {
        return orderService.findAll();
    }

    /**
     * REST API method to provide GET access to all customer orders in the
     * system
     *
     * @return JSON representation of all orders in a specific status
     */
    @GetMapping ( BASE_PATH + "/orders/status/placed" )
    public List<CustomerOrder> getPlacedOrders () {
        return orderService.findByOrderStatus( Status.Order_Placed );
    }

    /**
     * REST API method to provide GET access to all customer orders in the
     * system
     *
     * @return JSON representation of all orders in a specific status
     */
    @GetMapping ( BASE_PATH + "/orders/status/fulfilled" )
    public List<CustomerOrder> getFulfilledOrders () {
        return orderService.findByOrderStatus( Status.Order_Fulfilled );
    }

    /**
     * REST API method to provide GET access to all customer orders in the
     * system
     *
     * @return JSON representation of all orders in a specific status
     */
    @GetMapping ( BASE_PATH + "/orders/status/completed" )
    public List<CustomerOrder> getCompleteOrders () {
        return orderService.findByOrderStatus( Status.Order_Completed );
    }

    /**
     * REST API method that provides PUT access by updating the order's status
     * to complete
     *
     * @param id
     *            order id
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/orders/fulfill/{id}" )
    public ResponseEntity fulfillOrder ( @PathVariable ( "id" ) final Long id ) {
        // try to find an order with that id
        final CustomerOrder ord = orderService.findById( id );

        // error for a customerOrder not existing handles
        if ( ord == null ) {
            return new ResponseEntity( errorResponse( "Order with the Id " + id + "does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        // determine if we can make the recipe
        final boolean order_flag = fulfillOrderHelper( ord );

        if ( order_flag ) {
            ord.setOrderStatus( Status.Order_Fulfilled );
            orderService.save( ord );
            return new ResponseEntity<String>( successResponse( "Order fulfilled and ready for pick up!" ),
                    HttpStatus.OK );
        }
        else {
            return new ResponseEntity( errorResponse( "Not enough inventory" ), HttpStatus.CONFLICT );
        }
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

        // handle the money and make sure it is enough
        if ( amt < r.getPrice() ) {
            return new ResponseEntity( errorResponse( "Insufficient amount" ), HttpStatus.PAYMENT_REQUIRED );
        }
        else {
            final CustomerOrder temp = new CustomerOrder( recipe_name, Status.Order_Placed, name );
            orderService.save( temp );
            return new ResponseEntity<String>( successResponse( String.valueOf( amt - ( r.getPrice() ) ) ),
                    HttpStatus.OK );
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
    public boolean fulfillOrderHelper ( final CustomerOrder ord ) {
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

    /**
     * REST API method that provides PUT access by updating an order from the
     * active list when it is picked up
     *
     * @param id
     *            order id
     * @return response to the request
     */
    @PutMapping ( BASE_PATH + "/orders/pickup/{id}" )
    public ResponseEntity pickUpOrder ( @PathVariable ( "id" ) final Long id ) {
        // try to find an order with that id
        final CustomerOrder ord = orderService.findById( id );

        // error for a customerOrder not existing handles
        if ( ord == null ) {
            return new ResponseEntity( errorResponse( "Order with the Id " + id + "does not exist" ),
                    HttpStatus.NOT_FOUND );
        }
        final User usr = userService.findByUsername( ord.getOrderOwner() );
        // error for a customer not existing handles
        if ( usr == null ) {
            return new ResponseEntity( errorResponse( "Customer with the Id " + id + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        // update the order status and save the order
        ord.setOrderStatus( Status.Order_Completed );
        orderService.save( ord );

        if ( ord.getOrderStatus() == Status.Order_Placed ) {
            return new ResponseEntity( errorResponse( "Order is not fulfilled" ), HttpStatus.CONFLICT );
        }
        else {
            return new ResponseEntity<String>( successResponse( "Order Picked-Up" ), HttpStatus.OK );
        }
    }

}
