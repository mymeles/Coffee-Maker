package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

/**
 * A class that represents an Ingredient with fields of name and amount for the
 * coffeemaker system.
 *
 * @author Sanjit Verma
 *
 */
@Entity
public class Ingredient extends DomainObject {

    /** id for inventory entry */
    @Id
    @GeneratedValue
    private Long    id;

    /**
     * the name of an Ingredient
     */
    private String  ingredientName;

    /** amount of Ingredient */
    @Min ( 0 )
    private Integer amount;

    /**
     * creates a default Ingredient for coffeemaker.
     */
    public Ingredient () {
        this.ingredientName = "";
    }

    /**
     * A constructor used to create Ingredients with specific amounts.
     *
     * @param ingredientName
     *            a string type for the ingredient name
     * @param amount
     *            is an Integer for the ingredient amount=
     */
    public Ingredient ( final String ingredientName, final Integer amount ) {
        setIngredientName( ingredientName );
        setAmount( amount );
    }

    /**
     * it is a method which sets the name of the ingredient with the given
     * parameter.
     *
     * @param ingredientName
     *            is the name the ingredient is set to
     */
    public void setIngredientName ( final String ingredientName ) {
        this.ingredientName = ingredientName;
    }

    /**
     * it is a method which sets the amount of an ingredient with the passed
     * parameter
     *
     * @param amount
     *            is the amount an ingredient is set to.
     */
    public void setAmount ( final Integer amount ) {
        // if ( amount < 0 ) {
        // throw new IllegalArgumentException( "Units of coffee must be a
        // positive integer" );
        // }
        this.amount = amount;
    }

    /**
     * is is a method to retrieve the name of an ingredient
     *
     * @return a String: the name of an Ingredient
     */
    public String getIngredientName () {
        return ingredientName;
    }

    /**
     * it is a method to retrieve the amount of an Ingredient.
     *
     * @return an Integer: the amount of an ingredient
     */
    public Integer getAmount () {
        return amount;
    }

    /**
     * Returns the ID of the entry in the DB
     *
     * @return long: the id of an Ingredient
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

    @Override
    public String toString () {
        return ingredientName + ": " + amount;
    }

    @Override
    public int hashCode () {
        return Objects.hash( amount, id, ingredientName );
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
        final Ingredient other = (Ingredient) obj;
        return Objects.equals( amount, other.amount ) && Objects.equals( id, other.id )
                && Objects.equals( ingredientName, other.ingredientName );
    }

}
