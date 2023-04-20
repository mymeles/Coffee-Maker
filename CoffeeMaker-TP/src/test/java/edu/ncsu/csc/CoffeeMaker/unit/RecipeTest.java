package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the recipe class
 *
 * @author sanket
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class RecipeTest {
    /** recipe service object for db **/
    @Autowired
    private RecipeService service;

    // @Autowired
    // final IngredientService ingredientService = new IngredientService();
    /**
     * Deletes recipes in db 
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Tests adding a recipe
     */
    @Test
    @Transactional
    public void testAddRecipe () {
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        Assertions.assertEquals( 2, recipes.size(),
                "Creating two recipes should result in two recipes in the database" );
        Assertions.assertEquals( r1, recipes.get( 0 ), "The retrieved recipe should match the created one" );
        Assertions.assertEquals( r2, recipes.get( 1 ), "The retrieved recipe should match the created one" );

        final Recipe returnedR1 = recipes.get( 0 );
        final int[] ingredientValues = { 0, 0, 0, 0 };
        for ( int i = 0; i < returnedR1.getIngredients().size(); i++ ) {
            Assertions.assertEquals( ingredientValues[0], returnedR1.getIngredients().get( i ).getAmount(),
                    "The retrieved recipe ingredients should match the created one" );

        }

        final Recipe returnedR2 = recipes.get( 1 );
        final int[] ingredientValues2 = { 1, 1, 1, 1 };
        for ( int i = 0; i < returnedR2.getIngredients().size(); i++ ) {
            Assertions.assertEquals( ingredientValues2[0], returnedR2.getIngredients().get( i ).getAmount(),
                    "The retrieved recipe ingredients should match the created one" );

        }

    }

    /**
     * Tests adding no recipes
     */
    @Test
    @Transactional
    public void testNoRecipes () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( -12 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );

        final List<Recipe> recipes = List.of( r1, r2 );

        try {
            service.saveAll( recipes );
            Assertions.assertEquals( 0, service.count(),
                    "Trying to save a collection of elements where one is invalid should result in neither getting saved" );
        }
        catch ( final Exception e ) {
            Assertions.assertTrue( e instanceof ConstraintViolationException );
        }
    }

    /**
     * Tests adding recipes
     */
    @Test
    @Transactional
    public void testAddRecipe1 () {

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 1 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );
        service.save( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
        Assertions.assertNotNull( service.findByName( "Coffee" ) );

    }

    /* Test2 is done via the API for different validation */
    /**
     * Tests adding recipe for different validation
     */
    @Test
    @Transactional
    public void testAddRecipe3 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( -50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "milk", 1 ) );
        r1.addIngredient( new Ingredient( "sugar", 1 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( "Coffee" ),
                    "A recipe was able to be created with a negative price" );
        }
        catch ( final ConstraintViolationException cvee ) {
            Assertions.assertTrue( cvee instanceof ConstraintViolationException );
        }

    }

    /**
     * Tests adding recipes
     */
    @Test
    @Transactional
    public void testAddRecipe4 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = new Recipe();
        r1.setName( name );
        r1.setPrice( 50 );
        r1.addIngredient( new Ingredient( "Coffee", -3 ) );
        r1.addIngredient( new Ingredient( "milk", 1 ) );
        r1.addIngredient( new Ingredient( "sugar", 1 ) );
        r1.addIngredient( new Ingredient( "chocolate", 2 ) );
        try {
            service.save( r1 );

            Assertions.assertNull( service.findByName( name ),
                    "A recipe was able to be created with a negative amount of coffee" );
        }
        catch ( final ConstraintViolationException cvee ) {
            Assertions.assertTrue( cvee instanceof ConstraintViolationException );
        }

    }

    /**
     * Tests adding recipes
     */
    @Test
    @Transactional
    public void testAddRecipe13 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 12 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );

        service.save( r2 );

        Assertions.assertEquals( 2, service.count(),
                "Creating two recipes should result in two recipes in the database" );

    }

    /**
     * Tests adding recipes
     */
    @Test
    @Transactional
    public void testAddRecipe14 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 12 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );
        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 60 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "milk", 2 ) );
        r3.addIngredient( new Ingredient( "sugar", 2 ) );
        r3.addIngredient( new Ingredient( "chocolate", 1 ) );

        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

    }

    /**
     * Persistence DB Testing for delete functionality
     *
     *
     */
    @Test
    @Transactional
    public void testDeleteRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 50 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "milk", 1 ) );
        r1.addIngredient( new Ingredient( "sugar", 1 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        Assertions.assertEquals( null, service.findByName( "r1" ) );
        Assertions.assertEquals( null, service.findById( r1.getId() ) );
        Assertions.assertFalse( service.existsById( r1.getId() ) );

        Assertions.assertNull( service.findById( null ) );
    }

    /**
     * Second Persistence DB Test for delete functionality
     *
     */
    @Test
    @Transactional
    public void testDeleteRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 12 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );
        final Recipe r3 = new Recipe();
        r3.setName( "Latte" );
        r3.setPrice( 60 );
        r3.addIngredient( new Ingredient( "Coffee", 3 ) );
        r3.addIngredient( new Ingredient( "milk", 2 ) );
        r3.addIngredient( new Ingredient( "sugar", 2 ) );
        r3.addIngredient( new Ingredient( "chocolate", 1 ) );

        service.save( r3 );
        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.deleteAll();

        Assertions.assertEquals( 0, service.count(), "service.deleteAll() should remove everything" );

        assertEquals( null, service.findByName( "r1" ) );
        assertEquals( null, service.findById( r1.getId() ) );
        assertFalse( service.existsById( r1.getId() ) );

        assertEquals( null, service.findByName( "r2" ) );
        assertEquals( null, service.findById( r2.getId() ) );
        assertFalse( service.existsById( r2.getId() ) );

        assertEquals( null, service.findByName( "r3" ) );
        assertEquals( null, service.findById( r3.getId() ) );
        assertFalse( service.existsById( r3.getId() ) );

        assertNull( service.findById( null ) );

    }

    /**
     * Tests editing recipe
     */
    @Test
    @Transactional
    public void testEditRecipe1 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Latte" );
        r1.setPrice( 60 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 1 ) );

        service.save( r1 );

        r1.setPrice( 70 );

        service.save( r1 );

        final Recipe retrieved = service.findByName( "Latte" );

        Assertions.assertEquals( 70, (int) retrieved.getPrice() );
        Assertions.assertEquals( 3, (int) retrieved.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 2, (int) retrieved.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 2, (int) retrieved.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 1, (int) retrieved.getIngredients().get( 3 ).getAmount() );

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /**
     * Tests editing recipe
     */
    @Test
    @Transactional
    public void testEditRecipe2 () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Latte" );
        r1.setPrice( 60 );
        r1.addIngredient( new Ingredient( "Coffee", 3 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 1 ) );

        final List<Ingredient> ingredients = List.of( new Ingredient( "Coffee1", 1 ), new Ingredient( "Coffee2", 2 ),
                new Ingredient( "Coffee3", 3 ), new Ingredient( "Coffee4", 4 ), new Ingredient( "Coffee5", 5 ) );

        service.save( r1 );
        final Recipe newRecipe = new Recipe();
        newRecipe.setIngredietns( ingredients );
        newRecipe.setName( "Latte" );
        newRecipe.setPrice( 60 );

        final Recipe retrivedRecipe = service.findByName( newRecipe.getName() );
        service.delete( retrivedRecipe );
        Assertions.assertEquals( 0, service.count(), "There should exists no recipes" );
        service.save( newRecipe );

        final Recipe retrieved = service.findByName( "Latte" );

        for ( int i = 0; i < retrieved.getIngredients().size(); i++ ) {
            Assertions.assertEquals( "Coffee" + ( i + 1 ), retrieved.getIngredients().get( i ).getIngredientName(),
                    "Testing ingredient name. Failed at index " + i );
            Assertions.assertEquals( i + 1, retrieved.getIngredients().get( i ).getAmount(),
                    "Testing ingredient amount. Failed at index " + i );

        }

        Assertions.assertEquals( 1, service.count(), "Editing a recipe shouldn't duplicate it" );

    }

    /**
     * Test checking recipe values
     */
    @Test
    @Transactional
    public void testcheckRecipe () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no bRecipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Coffee", 0 ) );
        r1.addIngredient( new Ingredient( "milk", 0 ) );
        r1.addIngredient( new Ingredient( "sugar", 0 ) );
        r1.addIngredient( new Ingredient( "chocolate", 0 ) );

        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Mocha" );
        r2.setPrice( 0 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        final List<Recipe> recipes = service.findAll();
        assertTrue( recipes.get( 0 ).checkRecipe() );
        assertFalse( recipes.get( 1 ).checkRecipe() );

    }

    /**
     * Tests updating a recipe
     */
    @Test
    @Transactional
    public void testUpdateRecipe () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 2 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Coffee2" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );

        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        final List<Recipe> recipes = service.findAll();
        recipes.get( 0 ).updateRecipe( recipes.get( 1 ) );
        assertEquals( 1, recipes.get( 0 ).getPrice() );
        assertEquals( 1, recipes.get( 0 ).getIngredients().get( 0 ).getAmount() );
        assertEquals( 1, recipes.get( 0 ).getIngredients().get( 1 ).getAmount() );
        assertEquals( 1, recipes.get( 0 ).getIngredients().get( 2 ).getAmount() );
        assertEquals( 1, recipes.get( 0 ).getIngredients().get( 3 ).getAmount() );

    }

    /**
     * Tests tostring for recipe
     */
    @Test
    @Transactional
    public void testToString () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 2 ) );
        service.save( r1 );

        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final List<Recipe> recipes = service.findAll();
        assertEquals( "Coffee", recipes.get( 0 ).toString() );
    }

    /**
     * This method tests the hashCode
     *
     * @author sanjitverma
     */
    @Test
    @Transactional
    public void testHashCode () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 2 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Coffee" );
        r2.setPrice( 0 );
        r2.addIngredient( new Ingredient( "Coffee", 2 ) );
        r2.addIngredient( new Ingredient( "milk", 2 ) );
        r2.addIngredient( new Ingredient( "sugar", 2 ) );
        r2.addIngredient( new Ingredient( "chocolate", 2 ) );

        service.save( r2 );

        final List<Recipe> recipes = service.findAll();
        System.out.println( System.getProperty( "java.runtime.version" ) );
        assertEquals( recipes.get( 0 ).hashCode(), recipes.get( 0 ).hashCode() );

    }

    /**
     * This method tests the equals method
     *
     * @author sanjitverma
     */
    @Test
    @Transactional
    public void testEquals () {
        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = new Recipe();
        r1.setName( "Coffee" );
        r1.setPrice( 0 );
        r1.addIngredient( new Ingredient( "Coffee", 2 ) );
        r1.addIngredient( new Ingredient( "milk", 2 ) );
        r1.addIngredient( new Ingredient( "sugar", 2 ) );
        r1.addIngredient( new Ingredient( "chocolate", 2 ) );
        service.save( r1 );

        final Recipe r2 = new Recipe();
        r2.setName( "Coffee2" );
        r2.setPrice( 1 );
        r2.addIngredient( new Ingredient( "Coffee", 1 ) );
        r2.addIngredient( new Ingredient( "milk", 1 ) );
        r2.addIngredient( new Ingredient( "sugar", 1 ) );
        r2.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );

        final Recipe r3 = new Recipe();
        r3.setName( "Coffee2" );
        r3.setPrice( 1 );
        r3.addIngredient( new Ingredient( "Coffee", 1 ) );
        r3.addIngredient( new Ingredient( "milk", 1 ) );
        r3.addIngredient( new Ingredient( "sugar", 1 ) );
        r3.addIngredient( new Ingredient( "chocolate", 1 ) );
        service.save( r2 );

        final List<Recipe> recipes = service.findAll();

        assertTrue( recipes.get( 0 ).equals( r1 ) );

        assertTrue( recipes.get( 1 ).equals( r2 ) );

        assertFalse( recipes.get( 1 ).equals( r3 ) );

    }

}
