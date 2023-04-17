package edu.ncsu.csc.CoffeeMaker.models;

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
        return "";
    }

    public int hashcode () {
        return 0;
    }

    @Override
    public boolean equals ( final Object obj ) {
        return false;
    }
}
