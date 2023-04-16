package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;



import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.controllers.APIOrderController;
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
    private MockMvc               mvc;
    
	 /** recipe service object for db **/
    @Autowired
    private RecipeService rService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CustomerOrderService orderService;
    
    /** inventory service object for db **/
    @Autowired
    private InventoryService inventoryService;
    
    CustomerOrder ord1;
    CustomerOrder ord2;
    CustomerOrder ord3;

    // @Autowired
    // final IngredientService ingredientService = new IngredientService();
    @BeforeEach
    public void setup () {
        rService.deleteAll();
        userService.deleteAll();
        //assertNotNull(mvc);
        mvc = MockMvcBuilders.standaloneSetup(new APIOrderController()).build();

        // setup inventory 
        final Inventory ivt = inventoryService.getInventory();
        final Ingredient coffeIngredient = new Ingredient( "Coffee", 100 );
        final Ingredient milkIngredient = new Ingredient( "milk", 100 );
        final Ingredient sugarIngredient = new Ingredient( "sugar", 100 );
        final Ingredient vanillIngredient = new Ingredient( "vanilla", 100 );
        final Ingredient caramelIngredient = new Ingredient( "caramel", 100 );

        final ArrayList<Ingredient> lists = new ArrayList<Ingredient>();
        lists.add( coffeIngredient );
        lists.add( milkIngredient );
        lists.add( sugarIngredient );
        lists.add( vanillIngredient );
        lists.add( caramelIngredient );

        ivt.setIngredients( lists );
        inventoryService.save( ivt );
        
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
        assertEquals(3, recipes.size());
        
        // setup orders to add to users 
        
         ord1 = new CustomerOrder(recipes.get(0), Status.Order_Placed);
         ord2 = new CustomerOrder(recipes.get(1), Status.Order_Placed);
         ord3 = new CustomerOrder(recipes.get(2), Status.Order_Placed);
        
     // setup users
        final User user1 = new User();
        user1.setUsername( "usr1" );
        user1.setPassword( "password123" );
        user1.setRole( Role.CUSTOMER );
        //user1.setCustomerOrder(ord1); // Add this line
        userService.save(user1);

        final User user2 = new User();
        user2.setUsername( "usr2" );
        user2.setPassword( "password123" );
        user2.setRole( Role.CUSTOMER );
       // user2.setCustomerOrder(ord2); // Add this line
        userService.save(user2);

        final User user3 = new User();
        user3.setUsername( "usr3" );
        user3.setPassword( "password123" );
        user3.setRole( Role.CUSTOMER );
        //user3.setCustomerOrder(ord3); // Add this line
        userService.save(user3);
    }
    
    @Test
    @AutoConfigureMockMvc 
    public void testApiGetOrders() throws Exception{
    	List<User> ursList = userService.findAll();
    	for(int i = 0; i < ursList.size(); i++) {
    		System.out.println(ursList.get(i).getId());
    	}
    	
    		mvc.perform( post( "/api/v1/orders/" + ursList.get(1).getUsername() + "/" + 5).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString(ord1) ) )
            .andExpect( status().isOk() );
    		mvc.perform( post( "/api/v1/orders/" + ursList.get(1).getUsername() + "/" + 5).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString(ord2) ) )
            .andExpect( status().isOk() ).andReturn().getResponse().getErrorMessage();
    		
    		mvc.perform( post( "/api/v1/orders/" + ursList.get(2).getUsername() + "/" + 5).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString(ord3) ) )
            .andExpect( status().isOk() ).andReturn().getResponse().getErrorMessage();
    		
    		assertEquals(3, orderService.count());
    }


}
