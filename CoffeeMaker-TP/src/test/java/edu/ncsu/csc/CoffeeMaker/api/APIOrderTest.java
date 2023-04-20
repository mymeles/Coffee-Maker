package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.models.status.Status;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIOrderTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    @Autowired
    private MockMvc              mvc;

    /** recipe service object for db **/
    @Autowired
    private RecipeService        rService;

    @Autowired
    private UserService          userService;

    @Autowired
    private CustomerOrderService orderService;

    /** inventory service object for db **/
    @Autowired
    private InventoryService     inventoryService;

    CustomerOrder                ord1;
    CustomerOrder                ord2;
    CustomerOrder                ord3;

    User                         user1;
    User                         user2;
    User                         user3;

    // @Autowired
    // final IngredientService ingredientService = new IngredientService();
    @BeforeEach
    public void setup () {
        rService.deleteAll();
        userService.deleteAll();
        // assertNotNull(mvc);
        // mvc = MockMvcBuilders.standaloneSetup( new APIOrderController()
        // ).build();

        // setup inventory
        final Inventory ivt = inventoryService.getInventory();
        final Ingredient coffeIngredient = new Ingredient( "Coffee", 100 );
        final Ingredient milkIngredient = new Ingredient( "milk", 100 );
        final Ingredient sugarIngredient = new Ingredient( "sugar", 100 );
        final Ingredient caramelIngredient = new Ingredient( "chocolate", 100 );

        final ArrayList<Ingredient> lists = new ArrayList<Ingredient>();
        lists.add( coffeIngredient );
        lists.add( milkIngredient );
        lists.add( sugarIngredient );
        lists.add( caramelIngredient );

        ivt.setIngredients( lists );
        inventoryService.save( ivt );

        // set-up recipes to add to orders
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 5 ) );
        r1.addIngredient( new Ingredient( "milk", 5 ) );
        r1.addIngredient( new Ingredient( "sugar", 5 ) );
        r1.addIngredient( new Ingredient( "chocolate", 5 ) );
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
        user1 = new User();
        user1.setUsername( "usr1" );
        user1.setPassword( "password123" );
        user1.setRole( Role.CUSTOMER );
        userService.save( user1 );

        user2 = new User();
        user2.setUsername( "usr2" );
        user2.setPassword( "password123" );
        user2.setRole( Role.CUSTOMER );
        userService.save( user2 );

        user3 = new User();
        user3.setUsername( "usr3" );
        user3.setPassword( "password123" );
        user3.setRole( Role.CUSTOMER );
        userService.save( user3 );
    }

    @Test
    @Transactional
    public void testApiPlaceOrders () throws Exception {
        final List<User> ursList = userService.getAllUsers();
        final List<Recipe> recipeList = rService.findAll();
        assertEquals( recipeList.size(), 3 );
        // paymemt required error check
        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 0 ).getUsername() + "/" + 0 + "/" + recipeList.get( 0 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isPaymentRequired() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 0 ).getUsername() + "/" + 5 + "/" + recipeList.get( 0 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 1 ).getUsername() + "/" + 5 + "/" + recipeList.get( 1 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 2 ).getUsername() + "/" + 5 + "/" + recipeList.get( 2 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertEquals( 3, orderService.count() );

        // Check for Users with orders alreadu existing
        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 2 ).getUsername() + "/" + 5 + "/" + recipeList.get( 2 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() );

        // check with invalid user names
        mvc.perform( post( "/api/v1/orders/" + "fake" + "/" + 5 + "/" + recipeList.get( 2 ).getName() )
                .contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isNotFound() );

        mvc.perform( post( "/api/v1/orders/" + ursList.get( 2 ).getUsername() + "/" + 5 + "/" + "recipe" )
                .contentType( MediaType.APPLICATION_JSON ) ).andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void testApiGetOrders () throws Exception {
        final List<User> ursList = userService.getAllUsers();
        final List<Recipe> recipeList = rService.findAll();
        assertEquals( recipeList.size(), 3 );
        for ( int i = 0; i < ursList.size(); i++ ) {
            System.out.println( ursList.get( i ).getId() );
        }

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 0 ).getUsername() + "/" + 5 + "/" + recipeList.get( 0 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 1 ).getUsername() + "/" + 5 + "/" + recipeList.get( 1 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 2 ).getUsername() + "/" + 5 + "/" + recipeList.get( 2 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertEquals( 3, orderService.count() );

        final String lst = mvc.perform( get( "/api/v1/orders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( lst.contains( "Mocha" ) );
        assertTrue( lst.contains( "Black Coffee" ) );
        assertTrue( lst.contains( "MilkShake" ) );
    }

    @Test
    @Transactional
    public void testApiCompleteOrder () throws Exception {
        final List<User> ursList = userService.getAllUsers();
        final List<Recipe> recipeList = rService.findAll();
        assertEquals( recipeList.size(), 3 );
        for ( int i = 0; i < ursList.size(); i++ ) {
            System.out.println( ursList.get( i ).getId() );
        }

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 0 ).getUsername() + "/" + 5 + "/" + recipeList.get( 0 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final List<User> ursList1 = userService.getAllUsers();

        // Now all the orders are in for the users
        // It is time to make the order
        final Long id = ursList1.get( 0 ).getCustomerOrder().getId();
        mvc.perform( put( "/api/v1/orders/" + id ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final List<CustomerOrder> ordList = orderService.findAll();
        assertEquals( ordList.get( 0 ).getOrderStatus(), Status.Order_Completed );

        final Inventory inv = inventoryService.getInventory();

        for ( int i = 0; i < inv.getIngredients().size(); i++ ) {
            assertEquals( (int) inv.getIngredients().get( i ).getAmount(), ( 95 ) );
        }

        // incalid idd for the order
        mvc.perform( put( "/api/v1/orders/" + 0 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );

        // check for insufficent value fr invetory to compelte an order.
        for ( int i = 0; i < 19; i++ ) {
            inv.useIngredients( rService.findByName( ord1.getRecipe() ) );
        }
        inventoryService.save( inv );

        mvc.perform( put( "/api/v1/orders/" + id ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() );
    }

    @Test
    @Transactional
    public void testApiPickUpOrder () throws Exception {
        final List<User> ursList = userService.getAllUsers();
        final List<Recipe> recipeList = rService.findAll();
        assertEquals( recipeList.size(), 3 );
        // order is placed
        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 0 ).getUsername() + "/" + 5 + "/" + recipeList.get( 0 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Now all the orders are in for the users
        // It is time to make the order
        final Long id = ursList.get( 0 ).getCustomerOrder().getId();
        mvc.perform( put( "/api/v1/orders/" + id ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        final List<CustomerOrder> ordList = orderService.findAll();
        assertEquals( ordList.get( 0 ).getOrderStatus(), Status.Order_Completed );

        mvc.perform( delete( "/api/v1/orders/" + ursList.get( 0 ).getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertNull( userService.findById( ursList.get( 0 ).getId() ).getCustomerOrder() );

        // check for error with incorrect user id
        mvc.perform( delete( "/api/v1/orders/" + 0 ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
        mvc.perform( delete( "/api/v1/orders/" + ursList.get( 0 ).getId() ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() );

    }

    @Test
    @Transactional
    public void testApiGetOrder () throws Exception {
        final List<User> ursList = userService.getAllUsers();
        final List<Recipe> recipeList = rService.findAll();
        assertEquals( recipeList.size(), 3 );
        for ( int i = 0; i < ursList.size(); i++ ) {
            System.out.println( ursList.get( i ).getId() );
        }

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 0 ).getUsername() + "/" + 5 + "/" + recipeList.get( 0 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 1 ).getUsername() + "/" + 5 + "/" + recipeList.get( 1 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        mvc.perform( post(
                "/api/v1/orders/" + ursList.get( 2 ).getUsername() + "/" + 5 + "/" + recipeList.get( 2 ).getName() )
                        .contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        assertEquals( 3, orderService.count() );

        final List<CustomerOrder> ordList = orderService.findAll();

        // checking the getorder method so get order by id
        final String black_coffee = mvc.perform( get( "/api/v1/orders/" + ordList.get( 0 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final String mocha = mvc.perform( get( "/api/v1/orders/" + ordList.get( 1 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        final String milk_shake = mvc.perform( get( "/api/v1/orders/" + ordList.get( 2 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        assertTrue( mocha.contains( "Mocha" ) );
        assertTrue( black_coffee.contains( "Black Coffee" ) );
        assertTrue( milk_shake.contains( "MilkShake" ) );

        // Trying to get an order with invalid ID
        mvc.perform( get( "/api/v1/orders/" + 0 ) ).andDo( print() ).andExpect( status().isNotFound() ).andReturn()
                .getResponse().getContentAsString();
    }
}
