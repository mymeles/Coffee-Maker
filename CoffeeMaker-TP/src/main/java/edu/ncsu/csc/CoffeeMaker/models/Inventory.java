package edu.ncsu.csc.CoffeeMaker.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 * Inventory for the coffee maker. Inventory is tied to the database using
 * Hibernate libraries. See InventoryRepository and InventoryService for the
 * other two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Inventory extends DomainObject {
    // TODO dup;icate ingredients must be checked
    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long             id;

    /**
     * ingredients is a list to hold arbitrarily many ingredients.
     */
    @OneToMany ( cascade = CascadeType.ALL, fetch = FetchType.EAGER )
    private List<Ingredient> ingredients;

    /**
     * Empty constructor for Hibernate
     */
    public Inventory () {
        this.ingredients = new ArrayList<Ingredient>();
    }

    /**
     * Use this to create inventory with specified amts.
     *
     * @param ingredients
     *            is a parameter used to set the inventory.
     */
    public Inventory ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Set the ID of the Inventory (Used by Hibernate)
     *
     * @param id
     *            the ID
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Sets the ingredients in the inventory
     *
     * @param ingredients
     *            a list of ingredients
     */
    public void setIngredients ( final List<Ingredient> ingredients ) {
        this.ingredients = ingredients;
    }

    /**
     * Returns the ingredients of the inventory
     *
     * @return list of ingredients
     */
    public List<Ingredient> getIngredients () {
        return ingredients;
    }

    /**
     * Returns true if there are enough ingredients to make the beverage.
     *
     * @param r
     *            recipe to check if there are enough ingredients
     * @return true if enough ingredients to make the beverage
     */
    public boolean enoughIngredients ( final Recipe r ) {
        final List<Ingredient> recipeIngredients = r.getIngredients();
        for ( int i = 0; i < recipeIngredients.size(); i++ ) {
            final Ingredient tempIngredient = recipeIngredients.get( i );
            final int amount = tempIngredient.getAmount();
            for ( int j = 0; j < ingredients.size(); j++ ) {
                final Ingredient currentIngredient = ingredients.get( j );
                if ( tempIngredient.getIngredientName().equals( currentIngredient.getIngredientName() )
                        && currentIngredient.getAmount() < amount ) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Removes the ingredients used to make the specified recipe. Assumes that
     * the user has checked that there are enough ingredients to make
     *
     * @param r
     *            recipe to make
     * @return true if recipe is made.
     */
    public boolean useIngredients ( final Recipe r ) {
        final List<Ingredient> recipeIngredients = r.getIngredients();
        if ( enoughIngredients( r ) ) {
            for ( int i = 0; i < recipeIngredients.size(); i++ ) {
                final Ingredient tempIngredient = recipeIngredients.get( i );
                final int amount = tempIngredient.getAmount();
                for ( int j = 0; j < ingredients.size(); j++ ) {
                    final Ingredient currentIngredient = ingredients.get( j );
                    if ( tempIngredient.getIngredientName().equals( currentIngredient.getIngredientName() ) ) {
                        ingredients.get( j ).setAmount( currentIngredient.getAmount() - amount );
                        break;
                    }
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Finds the ingredient in the inventory given name
     *
     * @param ingredient
     *            ingredient name to find
     * @return ingredient object given name
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
     * Adds ingredients to the inventory
     *
     * @param ingredientList
     *            a list of ingredients added to inventory.
     * @return true if successful, false if not
     */
    public boolean addIngredients ( final List<Ingredient> ingredientList ) {

        for ( int i = 0; i < ingredientList.size(); i++ ) {
            final Ingredient tempIngredient = ingredientList.get( i );
            final int amount = tempIngredient.getAmount();
            if ( amount < 0 ) {
                throw new IllegalArgumentException( "Amount cannot be negative" );
            }
            for ( int j = 0; j < ingredients.size(); j++ ) {
                final Ingredient currentIngredient = ingredients.get( j );
                if ( tempIngredient.getIngredientName().equals( currentIngredient.getIngredientName() ) ) {
                    ingredients.get( j ).setAmount( amount + currentIngredient.getAmount() );
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
     * Adds an ingredient to the inventory given the name
     *
     * @param ingredient
     *            Ingredient that is being added to inventory
     * @return true if ingredient is added, false otherwise
     */
    public boolean addIngredient ( final Ingredient ingredient ) {
        if ( null != findIngredientByName( ingredient.getIngredientName() ) ) {
            return false;
        }

        ingredients.add( ingredient );
        return true;
    }

    /**
     * Returns a string describing the current contents of the inventory.
     *
     * @return String
     */
    @Override
    public String toString () {
        final StringBuffer buf = new StringBuffer();
        for ( int i = 0; i < ingredients.size(); i++ ) {
            final Ingredient tempIngredient = ingredients.get( i );
            buf.append( tempIngredient.getIngredientName() );
            buf.append( ": " );
            buf.append( tempIngredient.getAmount() );
            buf.append( "\n" );
        }
        return buf.toString();
    }

}
