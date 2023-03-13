package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

// Essentially, this says that rather than using the standard JUnit 5 runner
// that the rest of our tests use,
// use the modified Spring one that will ensure that all necessary components
// are loaded.
/**
 * ensuring all the necessary componenets are loaded from spring.
 */
@ExtendWith ( SpringExtension.class ) // ensuring that spring boots testing suit
                                      // is used
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;
    // notes for the above code
    // This is more of the special Spring testing functionality, which gives us
    // a way to
    // interact with the REST API endpoints even without explicitly starting the
    // server first.

    /** This is used to make calls to the REST API's we are testing */
    @Autowired
    private WebApplicationContext context;

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService      service;

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService     iservice;

    /**
     * RecipeService object, to be autowired in by Spring to allow for
     * manipulating the Recipe model
     */
    @Autowired
    private RecipeService         rservice;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        service.deleteAll();
        rservice.deleteAll();
    }

    /**
     * a Test method for the REST API method call for getting a recipe.
     *
     * @throws Exception
     *             is thrown from the database of invalid values(eg. if units
     *             are less then zero).
     */
    @Test
    @Transactional
    public void testApiGetRecipe () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        if ( !recipe.contains( "Mocha" ) ) {
            // create a new Mocha recipe
            final Recipe r1 = new Recipe();
            r1.setName( "Mocha" );
            r1.setPrice( 0 );
            r1.addIngredient( new Ingredient( "Coffee", 2 ) );
            r1.addIngredient( new Ingredient( "milk", 2 ) );
            r1.addIngredient( new Ingredient( "sugar", 2 ) );
            r1.addIngredient( new Ingredient( "chocolate", 2 ) );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );
        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
    }

    /**
     * This test method is used to check for the REST API call of updating an
     * inventory and also making coffee from that inventory.
     *
     * @throws Exception
     *             is thrown from the database of invalid values(eg. if units
     *             are less then zero).
     */
    @Test
    @Transactional
    public void testApiUpdateInventoryAndMakeCofee () throws Exception {
        // add a series of ingredients in inventory using an add to inventory
        // ingredient api call
        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "Coffee", 10 ) ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "Milk", 10 ) ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "Sugar", 10 ) ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "Chocolate", 10 ) ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "Vanilla", 10 ) ) ) ).andExpect( status().isOk() );

        // Adding inventory to the database
        final Inventory in = new Inventory();
        in.addIngredient( new Ingredient( "new1", 10 ) );
        in.addIngredient( new Ingredient( "new2", 10 ) );
        in.addIngredient( new Ingredient( "new3", 10 ) );
        in.addIngredient( new Ingredient( "new4", 10 ) );
        in.addIngredient( new Ingredient( "Milk", 10 ) );
        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( in ) ) ).andExpect( status().isOk() );

        final Recipe r1 = new Recipe();
        r1.setName( "Mocha" );
        r1.setPrice( 69 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "Milk", 2 ) );
        r1.addIngredient( new Ingredient( "Sugar", 2 ) );
        r1.addIngredient( new Ingredient( "Chocolate", 2 ) );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r1 ) ) ).andExpect( status().isOk() );

        mvc.perform( post( "/api/v1/makecoffee/Mocha" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 100 ) ) ).andDo( print() ).andExpect( status().isOk() );

    }

    /**
     * This Test method is used to test the REST API call to get ingredients by
     * id.
     *
     * @throws Exception
     *             is thrown from the database of invalid values(eg. if units
     *             are less then zero).
     */
    @Test
    @Transactional
    public void testApiAddIngredient () throws Exception {

        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing1", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing2", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing3", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing4", 2 ) ) ) ).andExpect( status().isOk() );

        // checking for conflcit by adding a duplicate ingredient
        mvc.perform( post( "/api/v1/ingredients/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing4", 2 ) ) ) ).andExpect( status().isConflict() );

        final Inventory inventory = service.getInventory();
        final String i1 = mvc
                .perform( get( "/api/v1/ingredients/" + inventory.findIngredientByName( "ing1" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( i1.contains( "ing1" ) );

        final String i2 = mvc
                .perform( get( "/api/v1/ingredients/" + inventory.findIngredientByName( "ing2" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( i2.contains( "ing2" ) );

        final String i3 = mvc
                .perform( get( "/api/v1/ingredients/" + inventory.findIngredientByName( "ing3" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( i3.contains( "ing3" ) );

        final String i4 = mvc
                .perform( get( "/api/v1/ingredients/" + inventory.findIngredientByName( "ing4" ).getId() ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( i4.contains( "ing4" ) );

        // Testing Ingredient Id that do not exist
        final Long lg = (long) 5;
        mvc.perform( get( "/api/v1/ingredients/" + lg ) ).andDo( print() ).andExpect( status().isNotFound() )
                .andReturn().getResponse().getContentAsString();
    }

    /**
     * a Test method for the REST API method call for Deleting an Ingredients
     *
     * @throws Exception
     *             is thrown from the database of invalid values(eg. if units
     *             are less then zero).
     */
    @Test
    @Transactional
    public void testApiDeleteIngredients () throws Exception {

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing1", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing2", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing3", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing4", 2 ) ) ) ).andExpect( status().isOk() );

        final List<Ingredient> ing = iservice.findAll();
        Assertions.assertEquals( 4, ing.size() );

        mvc.perform( delete( "/api/v1/ingredients/" + ing.get( 0 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 3, iservice.findAll().size() );

        mvc.perform( delete( "/api/v1/ingredients/" + ing.get( 1 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals( 2, iservice.findAll().size() );

        mvc.perform( delete( "/api/v1/ingredients/" + ing.get( 2 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 1, iservice.findAll().size() );

        mvc.perform( delete( "/api/v1/ingredients/" + ing.get( 3 ).getId() ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertEquals( 0, iservice.findAll().size() );
        // Testing Ingredient Id that do not exist
        final Long lg = (long) 5;
        mvc.perform( delete( "/api/v1/ingredients/" + lg ) ).andDo( print() ).andExpect( status().isNotFound() )
                .andReturn().getResponse().getContentAsString();

    }

    /**
     * a Test method for the REST API method call for adding an Ingredient but
     * with a duplicate value. Inaddition to that it is also testing the get all
     * ingredients API call.
     *
     * @throws Exception
     *             is thrown from the database of invalid values(eg. if units
     *             are less then zero).
     */
    @Test
    @Transactional
    public void testApiAddIngredientDupliacate () throws Exception {

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing1", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing2", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing3", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing4", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing4", 2 ) ) ) ).andExpect( status().isConflict() );

        // testing apiGetIngredients by getting all the values
        final String ingList = mvc.perform( get( "/api/v1/ingredients" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( ingList.contains( "ing1" ) );
        Assertions.assertTrue( ingList.contains( "ing2" ) );
        Assertions.assertTrue( ingList.contains( "ing3" ) );
        Assertions.assertTrue( ingList.contains( "ing4" ) );

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
    public void testApiUpdateIngredient () throws Exception {

        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing1", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing2", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing3", 2 ) ) ) ).andExpect( status().isOk() );
        mvc.perform( post( "/api/v1/ingredients" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing4", 2 ) ) ) ).andExpect( status().isOk() );
        final List<Ingredient> ingredients = iservice.findAll();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            mvc.perform( put( "/api/v1/ingredients/" + ingredients.get( i ).getId() )
                    .contentType( MediaType.APPLICATION_JSON ).content(
                            TestUtils.asJsonString( new Ingredient( ingredients.get( i ).getIngredientName(), 2 ) ) ) )
                    .andExpect( status().isOk() );
        }
        final List<Ingredient> ingredients2 = iservice.findAll();
        for ( int i = 0; i < ingredients2.size(); i++ ) {
            Assertions.assertEquals( 4, ingredients2.get( i ).getAmount() );

        }

        final Long lg = (long) 5;
        mvc.perform( put( "/api/v1/ingredients/" + lg ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( new Ingredient( "ing1", 2 ) ) ) ).andExpect( status().isNotFound() );

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

        final Recipe retrieved = rservice.findByName( "Latte" );

        for ( int i = 0; i < retrieved.getIngredients().size(); i++ ) {
            Assertions.assertEquals( "Coffee" + ( i + 1 ), retrieved.getIngredients().get( i ).getIngredientName(),
                    "Testing ingredient name. Failed at index " + i );
            Assertions.assertEquals( i + 1, retrieved.getIngredients().get( i ).getAmount(),
                    "Testing ingredient amount. Failed at index " + i );
        }

        Assertions.assertEquals( 1, rservice.count(), "Editing a recipe shouldn't duplicate it" );

    }

}
