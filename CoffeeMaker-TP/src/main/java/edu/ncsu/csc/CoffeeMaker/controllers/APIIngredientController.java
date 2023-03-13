package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update, delete and get operations for the Ingredient.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author sanjit verma
 * @author Meles Meles
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIIngredientController extends APIController {

    /**
     * InventoryService object, to be autowired in by Spring to allow for
     * manipulating the Inventory model
     */
    @Autowired
    private InventoryService  inventoryService;

    /**
     * IngredientService object, to be autowired in by Spring to allow for
     * manipulating the Ingredient model
     */
    @Autowired
    private IngredientService ingredientService;

    /**
     * REST API method to provide POST access to adding ingredient into the
     * ingredient system
     *
     * @param ingredient
     *            is the object which is added in the existing inventory
     * @return response to the request
     */
    @PostMapping ( BASE_PATH + "/ingredients" )
    public ResponseEntity addIngredient ( @RequestBody final Ingredient ingredient ) {
        final List<Ingredient> ingredients = ingredientService.findAll();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getIngredientName().equals( ingredient.getIngredientName() ) ) {
                return new ResponseEntity(
                        errorResponse(
                                "Ingredient with the name " + ingredient.getIngredientName() + " already exists" ),
                        HttpStatus.CONFLICT );
            }
        }

        ingredientService.save( ingredient );
        return new ResponseEntity( successResponse( ingredient.getIngredientName() + " successfully created" ),
                HttpStatus.OK );

    }

    /**
     * REST API method to provide POST access to adding ingredient into the
     * inventory system
     *
     * @param ingredient
     *            is the object which is added in the existing inventory
     * @return a response to the request
     */
    @PostMapping ( BASE_PATH + "/ingredients/inventory" )
    public ResponseEntity addIngredientInventory ( @RequestBody final Ingredient ingredient ) {
        final Inventory inven = inventoryService.getInventory();
        if ( !inven.addIngredient( ingredient ) ) {
            return new ResponseEntity(
                    errorResponse( "Ingredient with the name " + ingredient.getIngredientName() + " already exists" ),
                    HttpStatus.CONFLICT );
        }
        inventoryService.save( inven );
        return new ResponseEntity( successResponse( ingredient.getIngredientName() + " successfully created" ),
                HttpStatus.OK );

    }

    /**
     * REST API method to provide get access to all ingredient in the system
     *
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients" )
    public List<Ingredient> getIngredient () {
        return ingredientService.findAll();
    }

    /**
     * REST API method to provide get access to the Ingredient system by their
     * id.
     *
     * @param id
     *            is an identifier for an Ingredient
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity getIngredient ( @PathVariable ( "id" ) final Long id ) {
        final Ingredient ingredient = ingredientService.findById( id );
        if ( ingredient == null ) {
            return new ResponseEntity( errorResponse( "No Ingredient found with the Id " + id ), HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( ingredient, HttpStatus.OK );
    }

    /**
     * REST API method to provide Ingredient deletion in the ingredient system
     * with an ingredient identifier which is id.
     *
     * @param id
     *            is an identifier for an Ingredient
     * @return response the request
     */
    @DeleteMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity deleteIngredient ( @PathVariable ( "id" ) final Long id ) {
        final Ingredient ingredient = ingredientService.findById( id );
        if ( ingredient == null ) {
            return new ResponseEntity( errorResponse( "No Ingredient found with the Id " + id ), HttpStatus.NOT_FOUND );
        }
        ingredientService.delete( ingredient );
        return new ResponseEntity( successResponse( "Ingredient with Id " + id + "is Deleted" ), HttpStatus.OK );

    }

    /**
     * REST API method to update the units of the ingredient with a given id
     * identifier and an updated ingredient.
     *
     * @param id
     *            is an identifier for an Ingredient
     * @param ingredient
     *            amounts to be added to ingredient
     * @return a reposnse to the request
     */
    @PutMapping ( BASE_PATH + "/ingredients/{id}" )
    public ResponseEntity updateIngredient ( @PathVariable ( "id" ) final Long id,
            @RequestBody final Ingredient ingredient ) {
        final Ingredient temp = ingredientService.findById( id );
        if ( temp == null ) {
            return new ResponseEntity( errorResponse( "No Ingredient found with the Id " + id ), HttpStatus.NOT_FOUND );
        }
        final int prevAmount = temp.getAmount();
        temp.setIngredientName( ingredient.getIngredientName() );
        temp.setAmount( prevAmount + ingredient.getAmount() );
        ingredientService.save( temp );
        return new ResponseEntity( successResponse( "Ingredient with the " + id + " amount is updated" ),
                HttpStatus.OK );

    }

}
