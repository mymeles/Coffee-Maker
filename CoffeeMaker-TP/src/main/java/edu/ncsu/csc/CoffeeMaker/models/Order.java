package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

public class Order {
    Long   id;
    String ownerName;
    Recipe recipeOrdered;
    int    amountPaid;
    String status;

    public Order () {
        this.ownerName = "";
        this.recipeOrdered = null;
        this.amountPaid = 0;
        this.status = "";
    }

    public Order ( final String ownerName, final Recipe recipeOrdered, final int amountPaid, final String status ) {
        setOwnerName( ownerName );
        setRecipeOrdered( recipeOrdered );
        setAmountPaid( amountPaid );
        setStatus( status );
    }

    public Long getId () {
        return id;
    }

    public void setId ( final Long id ) {
        this.id = id;
    }

    public String getOwnerName () {
        return ownerName;
    }

    public void setOwnerName ( final String ownerName ) {
        this.ownerName = ownerName;
    }

    public Recipe getRecipeOrdered () {
        return recipeOrdered;
    }

    public void setRecipeOrdered ( final Recipe recipeOrdered ) {
        this.recipeOrdered = recipeOrdered;
    }

    public int getAmountPaid () {
        return amountPaid;
    }

    public void setAmountPaid ( final int amountPaid ) {
        this.amountPaid = amountPaid;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus ( final String status ) {
        this.status = status;
    }

    @Override
    public String toString () {
        return ownerName + ": " + recipeOrdered.getName();
    }

    public int hashcode () {
        return Objects.hash( status, amountPaid, recipeOrdered, ownerName, id );
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
        final Order other = (Order) obj;
        return Objects.equals( ownerName, other.ownerName ) && Objects.equals( id, other.id )
                && Objects.equals( recipeOrdered, other.recipeOrdered )
                && Objects.equals( amountPaid, other.amountPaid );
    }
}
