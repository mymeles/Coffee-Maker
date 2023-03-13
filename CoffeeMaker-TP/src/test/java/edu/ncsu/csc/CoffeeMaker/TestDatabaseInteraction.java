package edu.ncsu.csc.CoffeeMaker;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests database relations with recipe object.
 *
 * @author sanket nain
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class TestDatabaseInteraction {
    /** Interacts and helps save recipe to database */
    @Autowired
    private RecipeService recipeService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        recipeService.deleteAll();
    }

    /**
     * Tests the RecipeService class
     */
    @Test
    @Transactional
    public void testRecipes () {
        final Recipe r = new Recipe();
        r.setName( "Recipe1" );
        r.setPrice( 3 );
        r.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 2 ) );
        r.addIngredient( new Ingredient( "Milk", 2 ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Recipe2" );
        r2.setPrice( 3 );
        r2.setPrice( 3 );
        r2.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "Sugar", 2 ) );
        r2.addIngredient( new Ingredient( "Milk", 2 ) );

        recipeService.save( r );
        recipeService.save( r2 );
        final List<Recipe> dbRecipes = recipeService.findAll();

        Assertions.assertEquals( 2, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );
        final Recipe dbRecipe2 = dbRecipes.get( 1 );

        assertEquals( r.getName(), dbRecipe.getName() );
        assertEquals( r.getPrice(), dbRecipe.getPrice() );
        assertEquals( r.findIngredientByName( "Chocolate" ), dbRecipe.findIngredientByName( "Chocolate" ) );
        assertEquals( r.findIngredientByName( "Milk" ), dbRecipe.findIngredientByName( "Milk" ) );
        assertEquals( r.findIngredientByName( "Sugar" ), dbRecipe.findIngredientByName( "Sugar" ) );
        assertEquals( r.findIngredientByName( "Coffee" ), dbRecipe.findIngredientByName( "Coffee" ) );

        assertEquals( r2.getName(), dbRecipe2.getName() );
        assertEquals( r2.getPrice(), dbRecipe2.getPrice() );
        assertEquals( r2.getName(), dbRecipe2.getName() );
        assertEquals( r2.getPrice(), dbRecipe2.getPrice() );
        assertEquals( r2.findIngredientByName( "Chocolate" ), dbRecipe2.findIngredientByName( "Chocolate" ) );
        assertEquals( r2.findIngredientByName( "Milk" ), dbRecipe2.findIngredientByName( "Milk" ) );
        assertEquals( r2.findIngredientByName( "Sugar" ), dbRecipe2.findIngredientByName( "Sugar" ) );
        assertEquals( r2.findIngredientByName( "Coffee" ), dbRecipe2.findIngredientByName( "Coffee" ) );
    }

    /**
     * Tests getting recipe by name
     */
    @Test
    @Transactional
    public void testGetRecipeByName () {
        final Recipe r = new Recipe();
        r.setName( "Recipe1" );
        r.setPrice( 3 );
        r.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 2 ) );
        r.addIngredient( new Ingredient( "Milk", 2 ) );

        recipeService.save( r );
        assertEquals( r, recipeService.findByName( r.getName() ) );
        r.setName( "Random" );

    }

    /**
     * Tests editing recipe.
     */
    @Test
    @Transactional
    public void testEditRecipe () {
        final Recipe r = new Recipe();
        r.setName( "Recipe1" );
        r.setPrice( 3 );
        r.addIngredient( new Ingredient( "Chocolate", 2 ) );
        r.addIngredient( new Ingredient( "Coffee", 2 ) );
        r.addIngredient( new Ingredient( "Sugar", 2 ) );
        r.addIngredient( new Ingredient( "Milk", 2 ) );

        recipeService.save( r );

        assertEquals( r, recipeService.findByName( r.getName() ) );
        final List<Recipe> dbRecipes = recipeService.findAll();

        assertEquals( 1, dbRecipes.size() );

        final Recipe dbRecipe = dbRecipes.get( 0 );
        dbRecipe.setName( "Random" );
        dbRecipe.setPrice( 2 );
        recipeService.save( dbRecipe );

        assertEquals( "Random", dbRecipe.getName() );
        assertTrue( 2 == dbRecipe.getPrice() );
        assertEquals( r.findIngredientByName( "Chocolate" ), dbRecipe.findIngredientByName( "Chocolate" ) );
        assertEquals( r.findIngredientByName( "Milk" ), dbRecipe.findIngredientByName( "Milk" ) );
        assertEquals( r.findIngredientByName( "Sugar" ), dbRecipe.findIngredientByName( "Sugar" ) );
        assertEquals( r.findIngredientByName( "Coffee" ), dbRecipe.findIngredientByName( "Coffee" ) );
    }

}
