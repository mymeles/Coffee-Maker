package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the recipe api
 *
 * @author sanket
 * @author Meles Meles 
 * @author Harris Khan
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;
    /** provides configuration for web application **/
    @Autowired
    private WebApplicationContext context;
    /** recipe service object that does basic db actions **/
    @Autowired
    private RecipeService         service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Ensures that a recipe object can make api call
     *
     * @throws Exception
     *             if api call cannot be made
     */
    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( new Ingredient( "Coffee", 10 ) );
        r.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r.addIngredient( new Ingredient( "Milk", 10 ) );
        r.addIngredient( new Ingredient( "Sugar", 10 ) );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    /**
     * Tests the api for recipe object
     *
     * @throws Exception
     *             if api call cannot be made
     */
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( new Ingredient( "Coffee", 10 ) );
        recipe.addIngredient( new Ingredient( "Chocolate", 10 ) );
        recipe.addIngredient( new Ingredient( "Milk", 10 ) );
        recipe.addIngredient( new Ingredient( "Sugar", 10 ) );
        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    /**
     * Uses api call to add recipe
     *
     * @throws Exception
     *             if api call cannot be made
     */
    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";

        final Recipe r1 = new Recipe();
        r1.addIngredient( new Ingredient( "Coffee", 10 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r1.addIngredient( new Ingredient( "Milk", 10 ) );
        r1.addIngredient( new Ingredient( "Sugar", 10 ) );
        r1.setPrice( 50 );
        r1.setName( name );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.addIngredient( new Ingredient( "Coffee", 10 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r2.addIngredient( new Ingredient( "Milk", 10 ) );
        r2.addIngredient( new Ingredient( "Sugar", 10 ) );
        r2.setPrice( 50 );
        r2.setName( name );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    /**
     * Uses api call to add more than 2 recipes
     *
     * @throws Exception
     *             if api call cannot be made
     */
    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.addIngredient( new Ingredient( "Coffee", 10 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r1.addIngredient( new Ingredient( "Milk", 10 ) );
        r1.addIngredient( new Ingredient( "Sugar", 10 ) );
        r1.setPrice( 50 );
        r1.setName( "Coffee" );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.addIngredient( new Ingredient( "Coffee", 10 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r2.addIngredient( new Ingredient( "Milk", 10 ) );
        r2.addIngredient( new Ingredient( "Sugar", 10 ) );
        r2.setPrice( 50 );
        r2.setName( "Mocha" );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.addIngredient( new Ingredient( "Coffee", 10 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r3.addIngredient( new Ingredient( "Milk", 10 ) );
        r3.addIngredient( new Ingredient( "Sugar", 10 ) );
        r3.setPrice( 50 );
        r3.setName( "Latte" );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = new Recipe();
        r4.addIngredient( new Ingredient( "Coffee", 10 ) );
        r4.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r4.addIngredient( new Ingredient( "Milk", 10 ) );
        r4.addIngredient( new Ingredient( "Sugar", 10 ) );
        r4.setPrice( 50 );
        r4.setName( "fourth Recepie" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    /**
     * Uses api call to delete recipe
     *
     * @throws Exception
     *             if api call cannot be made
     */
    @Test
    @Transactional
    public void testDeleteRecipe () throws Exception {

        /* Tests to make sure that recipes is deleted */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.addIngredient( new Ingredient( "Coffe", 10 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r1.addIngredient( new Ingredient( "Milk", 10 ) );
        r1.addIngredient( new Ingredient( "Sugar", 10 ) );
        r1.setPrice( 50 );
        r1.setName( "Coffee" );

        final Recipe r2 = new Recipe();
        r2.addIngredient( new Ingredient( "Coffe", 10 ) );
        r2.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r2.addIngredient( new Ingredient( "Milk", 10 ) );
        r2.addIngredient( new Ingredient( "Sugar", 10 ) );
        r2.setPrice( 50 );
        r2.setName( "Mocha" );

        final Recipe r3 = new Recipe();
        r3.addIngredient( new Ingredient( "Coffe", 10 ) );
        r3.addIngredient( new Ingredient( "Chocolate", 10 ) );
        r3.addIngredient( new Ingredient( "Milk", 10 ) );
        r3.addIngredient( new Ingredient( "Sugar", 10 ) );
        r3.setPrice( 50 );
        r3.setName( "Latte" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r3 ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );
        mvc.perform( delete( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );
        Assertions.assertEquals( 2, service.count(), "deleting the coffee recipe and testing " );
        mvc.perform( delete( "/api/v1/recipes/Coffee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().is4xxClientError() );

        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        System.out.println( recipe );
        assertFalse( recipe.contains( "Coffee" ) );

        mvc.perform( delete( "/api/v1/recipes/Mocha" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 1, service.count(), "checking the size of the recipe database after deletion" );

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( recipe.contains( "Mocha" ) );

        mvc.perform( delete( "/api/v1/recipes/Latte" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        Assertions.assertEquals( 0, service.count(), "checking the size of the recipe database after deletion" );

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertFalse( recipe.contains( "Latte" ) );

    }
    
    /**
     * a Test method for the REST API method call for updating an Ingredients
     * amount.
     *
     * @throws Exception
     *             is thrown from the database of invalid values(eg. if units
     *             are less then zero).
     */
    @Test
    @Transactional
    public void testApiEditIngredient () throws Exception {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Latte" );
        r1.setPrice( 60 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 1 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        final List<Ingredient> ingredients = List.of( new Ingredient( "Coffee1", 1 ), new Ingredient( "Coffee2", 2 ),
                new Ingredient( "Coffee3", 3 ), new Ingredient( "Coffee4", 4 ), new Ingredient( "Coffee5", 5 ) );

        final Recipe newRecipe = new Recipe();
        newRecipe.setIngredietns( ingredients );
        newRecipe.setName( "Latte" );
        newRecipe.setPrice( 60 );

        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( newRecipe ) ) ).andExpect( status().isOk() );
        newRecipe.setName( "balh" );

        mvc.perform( put( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( newRecipe ) ) ).andExpect( status().isNotFound() );

        final Recipe retrieved = service.findByName( "Latte" );

        for ( int i = 0; i < retrieved.getIngredients().size(); i++ ) {
            Assertions.assertEquals( "Coffee" + ( i + 1 ), retrieved.getIngredients().get( i ).getIngredientName(),
                    "Testing ingredient name. Failed at index " + i );
            Assertions.assertEquals( i + 1, retrieved.getIngredients().get( i ).getAmount(),
                    "Testing ingredient amount. Failed at index " + i );
        }

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

}