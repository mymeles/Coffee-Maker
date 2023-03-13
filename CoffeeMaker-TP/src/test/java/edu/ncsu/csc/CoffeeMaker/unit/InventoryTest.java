package edu.ncsu.csc.CoffeeMaker.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

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
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * Tests the Inventory
 *
 * @author sanket
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class InventoryTest {
    /** inventory service object for db **/
    @Autowired
    private InventoryService inventoryService;

    /**
     * Sets up each testing method
     */
    @BeforeEach
    public void setup () {
        final Inventory ivt = inventoryService.getInventory();
        final Ingredient coffeIngredient = new Ingredient( "Coffee", 100 );
        final Ingredient milkIngredient = new Ingredient( "milk", 95 );
        final Ingredient sugarIngredient = new Ingredient( "sugar", 90 );
        final Ingredient vanillIngredient = new Ingredient( "vanilla", 85 );
        final Ingredient caramelIngredient = new Ingredient( "caramel", 80 );

        final ArrayList<Ingredient> lists = new ArrayList<Ingredient>();
        lists.add( coffeIngredient );
        lists.add( milkIngredient );
        lists.add( sugarIngredient );
        lists.add( vanillIngredient );
        lists.add( caramelIngredient );

        ivt.setIngredients( lists );
        inventoryService.save( ivt );
    }

    /**
     * Tests setting the inventory
     */
    @Test
    @Transactional
    public void testSetInventory () {
        final Inventory i = inventoryService.getInventory();
        assertEquals( 5, i.getIngredients().size() );

        Assertions.assertEquals( 100, (int) i.getIngredients().get( 0 ).getAmount() );
        Assertions.assertEquals( 95, (int) i.getIngredients().get( 1 ).getAmount() );
        Assertions.assertEquals( 90, (int) i.getIngredients().get( 2 ).getAmount() );
        Assertions.assertEquals( 85, (int) i.getIngredients().get( 3 ).getAmount() );
        Assertions.assertEquals( 80, (int) i.getIngredients().get( 4 ).getAmount() );

        Assertions.assertEquals( "Coffee", i.getIngredients().get( 0 ).getIngredientName() );
        Assertions.assertEquals( "milk", i.getIngredients().get( 1 ).getIngredientName() );
        Assertions.assertEquals( "sugar", i.getIngredients().get( 2 ).getIngredientName() );
        Assertions.assertEquals( "vanilla", i.getIngredients().get( 3 ).getIngredientName() );
        Assertions.assertEquals( "caramel", i.getIngredients().get( 4 ).getIngredientName() );

    }

    /**
     * Tests adding to inventory
     */
    @Test
    @Transactional
    public void testAddInventory1 () {
        Inventory ivt = inventoryService.getInventory();
        // System.out.println( ivt.toString() );
        final Ingredient ingredient1 = new Ingredient( "three", 20 );
        final Ingredient ingredient2 = new Ingredient( "two", 20 );
        final Ingredient ingredient3 = new Ingredient( "one", 20 );
        final Ingredient ingredient4 = new Ingredient( "Coffee", 20 );
        final Ingredient ingredient5 = new Ingredient( "milk", 20 );
        final ArrayList<Ingredient> lists = new ArrayList<Ingredient>();
        lists.add( ingredient4 );
        lists.add( ingredient5 );
        lists.add( ingredient1 );
        lists.add( ingredient2 );
        lists.add( ingredient3 );

        ivt.addIngredients( lists );

        System.out.println( ivt.toString() );

        /* Save and retrieve again to update with DB */
        inventoryService.save( ivt );

        ivt = inventoryService.getInventory();

        Assertions.assertEquals( 120, (int) ivt.findIngredientByName( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 115, (int) ivt.findIngredientByName( "milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for Milk" );
        Assertions.assertEquals( 20, (int) ivt.findIngredientByName( "one" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for one" );
        Assertions.assertEquals( 20, (int) ivt.findIngredientByName( "two" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for two" );
        Assertions.assertEquals( 20, (int) ivt.findIngredientByName( "three" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for three" );

    }

    /**
     * Tests if there is enough ingredients in inventory
     */
    @Test
    @Transactional
    public void testEnoughIngredient () {
        final Inventory ivt = inventoryService.getInventory();
        Assertions.assertEquals( "Coffee", ivt.getIngredients().get( 0 ).getIngredientName() );
        Assertions.assertEquals( "milk", ivt.getIngredients().get( 1 ).getIngredientName() );
        Assertions.assertEquals( "sugar", ivt.getIngredients().get( 2 ).getIngredientName() );
        Assertions.assertEquals( "vanilla", ivt.getIngredients().get( 3 ).getIngredientName() );
        Assertions.assertEquals( "caramel", ivt.getIngredients().get( 4 ).getIngredientName() );

        final Recipe rcp = new Recipe();
        rcp.setName( "Expensive" );
        rcp.addIngredient( new Ingredient( "Coffee", 5 ) );
        rcp.addIngredient( new Ingredient( "milk", 5 ) );
        rcp.addIngredient( new Ingredient( "vanilla", 5 ) );
        rcp.addIngredient( new Ingredient( "caramel", 5 ) );

        Assertions.assertTrue( ivt.useIngredients( rcp ) );

        Assertions.assertEquals( 95, (int) ivt.findIngredientByName( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 90, (int) ivt.findIngredientByName( "milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for Milk" );
        Assertions.assertEquals( 80, (int) ivt.findIngredientByName( "vanilla" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for one" );
        Assertions.assertEquals( 75, (int) ivt.findIngredientByName( "caramel" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for two" );
        Assertions.assertEquals( 90, (int) ivt.findIngredientByName( "sugar" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for three" );

        rcp.addIngredient( new Ingredient( "sugar", 5 ) );

        Assertions.assertTrue( ivt.useIngredients( rcp ) );

        Assertions.assertEquals( 90, (int) ivt.findIngredientByName( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 85, (int) ivt.findIngredientByName( "milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for Milk" );
        Assertions.assertEquals( 75, (int) ivt.findIngredientByName( "vanilla" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for one" );
        Assertions.assertEquals( 70, (int) ivt.findIngredientByName( "caramel" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for two" );
        Assertions.assertEquals( 85, (int) ivt.findIngredientByName( "sugar" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for three" );

    }

    /**
     * Tests if there isn't enough ingredients in inventory
     */
    @Test
    @Transactional
    public void testNotEnoughIngredient () {
        final Inventory ivt = inventoryService.getInventory();
        Assertions.assertEquals( "Coffee", ivt.getIngredients().get( 0 ).getIngredientName() );
        Assertions.assertEquals( "milk", ivt.getIngredients().get( 1 ).getIngredientName() );
        Assertions.assertEquals( "sugar", ivt.getIngredients().get( 2 ).getIngredientName() );
        Assertions.assertEquals( "vanilla", ivt.getIngredients().get( 3 ).getIngredientName() );
        Assertions.assertEquals( "caramel", ivt.getIngredients().get( 4 ).getIngredientName() );

        final Recipe rcp = new Recipe();
        rcp.setName( "Expensive" );
        rcp.addIngredient( new Ingredient( "Coffee", 5 ) );
        rcp.addIngredient( new Ingredient( "milk", 5 ) );
        rcp.addIngredient( new Ingredient( "vanilla", 400 ) );
        rcp.addIngredient( new Ingredient( "caramel", 5 ) );

        Assertions.assertFalse( ivt.useIngredients( rcp ) );

        Assertions.assertEquals( 100, (int) ivt.findIngredientByName( "Coffee" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for coffee" );
        Assertions.assertEquals( 95, (int) ivt.findIngredientByName( "milk" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for Milk" );
        Assertions.assertEquals( 85, (int) ivt.findIngredientByName( "vanilla" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for one" );
        Assertions.assertEquals( 80, (int) ivt.findIngredientByName( "caramel" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for two" );
        Assertions.assertEquals( 90, (int) ivt.findIngredientByName( "sugar" ).getAmount(),
                "Adding to the inventory should result in correctly-updated values for three" );
    }

    /**
     * This test checks the toString
     *
     * @author sanjitverma
     */
    @Test
    @Transactional
    public void testToString () {
        final Inventory ivt = inventoryService.getInventory();
        final String str = "Coffee: 100\nmilk: 95\nsugar: 90\nvanilla: 85\ncaramel: 80\n";

        assertEquals( ivt.toString(), str );
    }

}
