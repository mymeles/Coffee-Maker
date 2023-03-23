package edu.ncsu.csc.CoffeeMaker.unit;

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

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Test class for the Ingredient POJO. This class also tests how The ingredient
 * class interacts with the database. such as saving a single ingredient, saving
 * a list of ingredients or deleting them.
 *
 * @author Meles Meles
 *
 */
@SuppressWarnings ( "unlikely-arg-type" )
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class IngredientTest {

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService service;

    /**
     * setup run before every test
     */
    @BeforeEach
    public void setup () {
        service.deleteAll();
    }

    /**
     * Testing hashcode for the Ingredient Class.
     */
    @Test
    @Transactional
    void testHashCode () {
        final Ingredient i1 = new Ingredient( "Coffee", 0 );
        final Ingredient i2 = new Ingredient( "Coffee", 0 );
        final Ingredient i3 = new Ingredient( "Coffee1", 0 );
        Assertions.assertTrue( i1.hashCode() == i2.hashCode() );
        Assertions.assertFalse( i1.hashCode() == i3.hashCode() );
    }

    /**
     * Testing the equals method in Ingredient.equals()
     */
    @Test
    @Transactional
    void testEquals () {
        final Ingredient i1 = new Ingredient( "Coffee", 0 );
        final Ingredient i2 = new Ingredient( "Coffee", 0 );
        final Ingredient i4 = new Ingredient( "Coffee", 1 );
        Assertions.assertTrue( i1.equals( i2 ) );
        Assertions.assertTrue( i1.equals( i1 ) );
        // Assertions.assertFalse( i1.equals( null ) );
        Assertions.assertFalse( i1.equals( new Recipe() ) );
        Assertions.assertFalse( i1.equals( i4 ) );

    }

    /**
     * Testing Ingredient.getAmount() and Ingredient.getId().
     */
    @Test
    @Transactional
    void testIngredient () {
        final Recipe r1 = new Recipe();
        r1.setName( "Black Coffee" );
        r1.setPrice( 1 );
        final Ingredient i1 = new Ingredient( "Coffee", 0 );
        final Long idLong = (long) 1000;
        i1.setId( idLong );
        Assertions.assertEquals( i1.getId(), idLong );
        final Ingredient i2 = new Ingredient( "milk", 0 );
        i2.setAmount( 5 );
        Assertions.assertEquals( i2.getAmount(), 5 );
        final Ingredient i3 = new Ingredient( "sugar", 0 );
        i3.setIngredientName( "sugar2" );
        Assertions.assertEquals( i3.getIngredientName(), "sugar2" );

    }

    /**
     * Testing Ingredient interaction with in the database by saving and
     * deleting.
     */
    @Test
    @Transactional
    void testIngredientToDatabase () {
        final Ingredient i1 = new Ingredient( "Coffee", 0 );
        final Ingredient i2 = new Ingredient( "Coffee1", 1 );
        final Ingredient i3 = new Ingredient( "Coffee2", 2 );
        final Ingredient i4 = new Ingredient( "Coffee3", 3 );
        service.saveAll( List.of( i1, i2, i3, i4 ) );
        Assertions.assertEquals( 4, service.findAll().size() );

        final Ingredient[] ingredients1 = { i1, i2, i3, i4 };
        final List<Ingredient> ingredients = service.findAll();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            Assertions.assertEquals( ingredients1[i].getAmount(), ingredients.get( i ).getAmount() );
            Assertions.assertEquals( ingredients1[i].getIngredientName(), ingredients.get( i ).getIngredientName() );
        }

        // testing if ingredients are being deleted properly
        service.delete( i1 );
        Assertions.assertEquals( 3, service.findAll().size() );
        service.delete( i2 );
        Assertions.assertEquals( 2, service.findAll().size() );
        service.delete( i3 );
        Assertions.assertEquals( 1, service.findAll().size() );
        service.delete( i4 );
        Assertions.assertEquals( 0, service.findAll().size() );

    }
}
