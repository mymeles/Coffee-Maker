package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Min;

/**
 * Recipe for the coffee maker. Recipe is tied to the database using Hibernate
 * libraries. See RecipeRepository and RecipeService for the other two pieces
 * used for database support.
 *
 * @author Meles Meles.
 */
@Entity
public class Recipe extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    private String           name;

    /** Recipe price */
    @Min ( 0 )
    private Integer          price;

    /**
     * ingredients is a list to hold arbitrarily many ingredients.
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /**
     * Creates a default recipe for the coffee maker.
     */
    public Recipe () {
        this.name = "";
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * a method which adds ingredients into the recipes ingredient list.
     *
     * @param ingredient
     *            is a value added to the recipes ingredient list.
     */
    public void addIngredient ( final Ingredient ingredient ) {
        // TODO there needs to be some sort of checking for the ingredients
        // before they are added
        ingredients.add( ingredient );
    }
    // add a list of ingredients.

    /**
     * a method to retrieve the list containing the recipes ingredients.
     *
     * @return a List of ingredients in the recipe
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * This method sets the list of ingrediets in a recipe ingredient.
     *
     * @param ing
     *            is a List of ingredients
     */
    public void setIngredietns ( final List<Ingredient> ing ) {
        this.ingredients = ing;
    }

    /**
     * Check if all ingredient fields in the recipe are 0
     *
     * @return true if all ingredient fields are 0, otherwise return false
     */
    public boolean checkRecipe () {
        for ( int i = 0; i < ingredients.size(); i++ ) {
            if ( ingredients.get( i ).getAmount() != 0 ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the ID of the Recipe
     *
     * @return the ID
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Recipe (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns name of the recipe.
     *
     * @return Returns the name.
     */
    public String getName () {
        return name;
    }

    /**
     * Sets the recipe name.
     *
     * @param name
     *            The name to set.
     */
    public void setName ( final String name ) {
        this.name = name;
    }

    /**
     * Returns the price of the recipe.
     *
     * @return Returns the price.
     */
    public Integer getPrice () {
        return price;
    }

    /**
     * Sets the recipe price.
     *
     * @param price
     *            The price to set.
     */
    public void setPrice ( final Integer price ) {
        this.price = price;
    }

    /**
     * Updates the current recipe with values from given recipe
     *
     * @param newRecipe
     *            a recipe object that updates current object
     * @return false if recipe is updated
     */
    public boolean updateRecipe ( final Recipe newRecipe ) {
        setPrice( newRecipe.getPrice() );
        updateRecipeIngredient( newRecipe.getIngredients() );
        return false;
    }

    /**
     * Finds a specific ingredient in the recipe given name
     *
     * @param ingredient
     *            the name of the ingredient trying to be found
     * @return an ingredient which is found in recipe
     */
    public Ingredient findIngredientByName ( final String ingredient ) {
        for ( int j = 0; j < ingredients.size(); j++ ) {
            if ( ingredient.equals( ingredients.get( j ).getIngredientName() ) ) {
                return ingredients.get( j );
            }
        }
        return null;
    }

    /**
     * Updates the recipe ingredients
     *
     * @param newIngredients
     *            list of new ingredients to be updated onto current recipe
     * @return true if ingredient is updated
     */
    public boolean updateRecipeIngredient ( final List<Ingredient> newIngredients ) {
        for ( int i = 0; i < newIngredients.size(); i++ ) {
            final Ingredient tempIngredient = newIngredients.get( i );
            final int amount = tempIngredient.getAmount();
            if ( amount < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }
            for ( int j = 0; j < ingredients.size(); j++ ) {
                final Ingredient currentIngredient = ingredients.get( j );
                if ( tempIngredient.getIngredientName().equals( currentIngredient.getIngredientName() ) ) {
                    ingredients.get( j ).setAmount( amount );
                    break;
                }
                else if ( findIngredientByName( tempIngredient.getIngredientName() ) == null ) {
                    ingredients.add( tempIngredient );
                    break;
                }
            }
        }

        return true;
    }

    /**
     * Returns the name of the recipe.
     *
     * @return String
     */
    @Override
    public String toString () {
        return name;
    }

    @Override
    public int hashCode () {
        return Objects.hash( id, ingredients, name, price );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Recipe other = (Recipe) obj;
        return Objects.equals( id, other.id ) && Objects.equals( ingredients, other.ingredients )
                && Objects.equals( name, other.name ) && Objects.equals( price, other.price );
    }

}
