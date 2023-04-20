package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.models.status.Status;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the recipe class
 *
 * @author sanket
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class CustomerOrderTest {
    /** recipe service object for db **/
    @Autowired
    private RecipeService        rService;

    @Autowired
    private UserService          userService;

    @Autowired
    private CustomerOrderService orderService;

    CustomerOrder                ord1;
    CustomerOrder                ord2;
    CustomerOrder                ord3;

    // @Autowired
    // final IngredientService ingredientService = new IngredientService();
    /**
     * Deletes recipes in db
     */
    @BeforeEach
    public void setup () {
        rService.deleteAll();
        userService.deleteAll();

        // set-up recipes to add to orders
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        rService.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        rService.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "MilkShake" );
        r3.setPrice( 1 );
        r3.addIngredient( new Ingredient( "Coffee", 1 ) );
        r3.addIngredient( new Ingredient( "milk", 1 ) );
        r3.addIngredient( new Ingredient( "sugar", 1 ) );
        r3.addIngredient( new Ingredient( "chocolate", 1 ) );
        rService.save( r3 );

        final List<Recipe> recipes = rService.findAll();
        assertEquals( 3, recipes.size() );

        // setup orders to add to users

        ord1 = new CustomerOrder( recipes.get( 0 ).getName(), Status.Order_Placed, "test" );
        ord2 = new CustomerOrder( recipes.get( 1 ).getName(), Status.Order_Placed, "test" );
        ord3 = new CustomerOrder( recipes.get( 2 ).getName(), Status.Order_Placed, "test" );

        // setup users
        final User user1 = new User();
        user1.setUsername( "usr1" );
        user1.setPassword( "password123" );
        user1.setRole( Role.CUSTOMER );
        // user1.setCustomerOrder(ord1); // Add this line
        userService.save( user1 );

        final User user2 = new User();
        user2.setUsername( "usr2" );
        user2.setPassword( "password123" );
        user2.setRole( Role.CUSTOMER );
        // user2.setCustomerOrder(ord2); // Add this line
        userService.save( user2 );

        final User user3 = new User();
        user3.setUsername( "usr3" );
        user3.setPassword( "password123" );
        user3.setRole( Role.CUSTOMER );
        // user3.setCustomerOrder(ord3); // Add this line
        userService.save( user3 );
    }

    /**
     * Tests adding a recipe
     */
    @Test
    @Transactional
    public void testOrderPresistance () {
        final User u = userService.findByUsername( "usr3" );
        assertEquals( "usr3", u.getUsername() );
        List<User> usr = userService.findAll();
        assertEquals( 3, usr.size() );

        usr.get( 0 ).setCustomerOrder( ord1 );
        usr.get( 1 ).setCustomerOrder( ord2 );
        usr.get( 2 ).setCustomerOrder( ord3 );

        userService.save( usr.get( 0 ) );
        userService.save( usr.get( 1 ) );
        userService.save( usr.get( 2 ) );

        usr = userService.findAll();
        assertEquals( 3, usr.size() );
        assertEquals( 3, orderService.count() );
        final List<CustomerOrder> listOrd = orderService.findAll();
        assertEquals( 3, listOrd.size() );
        assertEquals( usr.get( 0 ).getCustomerOrder().getRecipe(), "Black Coffee" );
        assertEquals( usr.get( 1 ).getCustomerOrder().getRecipe(), "Mocha" );
        assertEquals( usr.get( 2 ).getCustomerOrder().getRecipe(), "MilkShake" );
        assertEquals( usr.get( 0 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );
        assertEquals( usr.get( 1 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );
        assertEquals( usr.get( 2 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );

    }

    /**
     * Tests adding a recipe
     */
    @Test
    @Transactional
    public void testCustomerOrderStatusChange () {
        List<User> usr = userService.findAll();
        assertEquals( 3, usr.size() );

        usr.get( 0 ).setCustomerOrder( ord1 );
        usr.get( 1 ).setCustomerOrder( ord2 );
        usr.get( 2 ).setCustomerOrder( ord3 );

        userService.save( usr.get( 0 ) );
        userService.save( usr.get( 1 ) );
        userService.save( usr.get( 2 ) );

        usr = userService.findAll();
        assertEquals( 3, usr.size() );
        assertEquals( 3, orderService.count() );
        assertEquals( usr.get( 0 ).getCustomerOrder().getRecipe(), "Black Coffee" );
        assertEquals( usr.get( 1 ).getCustomerOrder().getRecipe(), "Mocha" );
        assertEquals( usr.get( 2 ).getCustomerOrder().getRecipe(), "MilkShake" );
        assertEquals( usr.get( 0 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );
        assertEquals( usr.get( 1 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );
        assertEquals( usr.get( 2 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );

        final List<CustomerOrder> listOrd = orderService.findAll();
        listOrd.get( 0 ).setOrderStatus( Status.Order_Completed );
        listOrd.get( 1 ).setOrderStatus( Status.Order_Completed );
        listOrd.get( 2 ).setOrderStatus( Status.Order_Completed );

        orderService.saveAll( listOrd );
        assertEquals( 3, orderService.count() );
        usr = userService.findAll();
        assertEquals( usr.get( 0 ).getCustomerOrder().getRecipe(), "Black Coffee" );
        assertEquals( usr.get( 1 ).getCustomerOrder().getRecipe(), "Mocha" );
        assertEquals( usr.get( 2 ).getCustomerOrder().getRecipe(), "MilkShake" );
        assertEquals( usr.get( 0 ).getCustomerOrder().getOrderStatus(), Status.Order_Completed );
        assertEquals( usr.get( 1 ).getCustomerOrder().getOrderStatus(), Status.Order_Completed );
        assertEquals( usr.get( 2 ).getCustomerOrder().getOrderStatus(), Status.Order_Completed );

    }

    /**
     * Tests adding a recipe
     */
    @Test
    @Transactional
    public void testCustomerOrderDeletion () {
        List<User> usr = userService.findAll();
        assertEquals( 3, usr.size() );

        usr.get( 0 ).setCustomerOrder( ord1 );
        usr.get( 1 ).setCustomerOrder( ord2 );
        usr.get( 2 ).setCustomerOrder( ord3 );

        userService.save( usr.get( 0 ) );
        userService.save( usr.get( 1 ) );
        userService.save( usr.get( 2 ) );

        usr = userService.findAll();
        assertEquals( 3, usr.size() );
        assertEquals( 3, orderService.count() );
        assertEquals( usr.get( 0 ).getCustomerOrder().getRecipe(), "Black Coffee" );
        assertEquals( usr.get( 1 ).getCustomerOrder().getRecipe(), "Mocha" );
        assertEquals( usr.get( 2 ).getCustomerOrder().getRecipe(), "MilkShake" );
        assertEquals( usr.get( 0 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );
        assertEquals( usr.get( 1 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );
        assertEquals( usr.get( 2 ).getCustomerOrder().getOrderStatus(), Status.Order_Placed );

        final List<CustomerOrder> listOrd = orderService.findAll();
        listOrd.get( 0 ).setOrderStatus( Status.Order_Completed );
        listOrd.get( 1 ).setOrderStatus( Status.Order_Completed );
        listOrd.get( 2 ).setOrderStatus( Status.Order_Completed );

        orderService.saveAll( listOrd );
        assertEquals( 3, orderService.count() );
        usr = userService.findAll();
        assertEquals( usr.get( 0 ).getCustomerOrder().getRecipe(), "Black Coffee" );
        assertEquals( usr.get( 1 ).getCustomerOrder().getRecipe(), "Mocha" );
        assertEquals( usr.get( 2 ).getCustomerOrder().getRecipe(), "MilkShake" );
        assertEquals( usr.get( 0 ).getCustomerOrder().getOrderStatus(), Status.Order_Completed );
        assertEquals( usr.get( 1 ).getCustomerOrder().getOrderStatus(), Status.Order_Completed );
        assertEquals( usr.get( 2 ).getCustomerOrder().getOrderStatus(), Status.Order_Completed );

        // Delete Customer order after it has been picked up.
        userService.deleteCustomerOrder( usr.get( 0 ) );
        assertEquals( 2, orderService.count() );
        usr = userService.findAll();
        assertNull( usr.get( 0 ).getCustomerOrder() );

        userService.deleteCustomerOrder( usr.get( 1 ) );
        assertEquals( 1, orderService.count() );
        usr = userService.findAll();
        assertNull( usr.get( 0 ).getCustomerOrder() );

        userService.deleteCustomerOrder( usr.get( 2 ) );
        assertEquals( 0, orderService.count() );
        usr = userService.findAll();
        assertNull( usr.get( 0 ).getCustomerOrder() );

    }

    @Test
    @Transactional
    public void checkOrderErrors () {
        final CustomerOrder or = new CustomerOrder();

        try {
            or.setRecipe( null );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( e.getMessage(), "Order's recipe can't be null" );
        }

        try {
            or.setOrderStatus( null );
            fail();
        }
        catch ( final IllegalArgumentException e ) {
            assertEquals( e.getMessage(), "Order's status can't be null" );
        }

    }

    @SuppressWarnings ( "unlikely-arg-type" )
    @Test
    @Transactional
    public void testEquals () {
        assertTrue( ord1.equals( ord1 ) );
        assertFalse( ord1.equals( ord2 ) );
        assertFalse( ord1.equals( userService ) );

    }

}
