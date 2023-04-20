package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.status.Status;

/**
 * Class containing the orders of customers that holds a recipe, the order's
 * status, and the owner of the order.
 *
 */
@Entity
public class CustomerOrder extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long   id;

    /** Name of the recipe that was ordered **/
    private String recipe;

    /**
     * The status of the order, Order_Placed, Order_Made, Order_Complete
     */
    @Enumerated ( EnumType.STRING )
    private Status orderStatus;

    /** Owner of the order **/
    private String orderOwner;

    /**
     * CustomOrder constructor
     */
    public CustomerOrder () {
        this.recipe = null;
        this.orderStatus = null;
        this.orderOwner = null;
    }

    /**
     * CustomOrder constructor
     *
     * @param recipe
     *            the recipe ordered
     * @param orderStatus
     *            the status of the order
     * @param orderOwner
     *            the owner of the order
     */
    public CustomerOrder ( final String recipe, final Status orderStatus, final String orderOwner ) {
        setRecipe( recipe );
        setOrderStatus( orderStatus );
        setOrderOwner( orderOwner );
    }

    /**
     * Returns the recipe connected to the order
     *
     * @return the recipe ordered
     */
    public String getRecipe () {
        return recipe;
    }

    /**
     * Sets the recipe for the order
     *
     * @param recipe
     *            the recipe to set
     * @throws IllegalArgumentException
     *             if the recipe is null
     */
    public void setRecipe ( final String recipe ) {
        if ( recipe == null ) {
            throw new IllegalArgumentException( "Order's recipe can't be null" );
        }
        this.recipe = recipe;
    }

    /**
     * Returns the order status
     *
     * @return the status of the order
     */
    public Status getOrderStatus () {
        return orderStatus;
    }

    /**
     * Sets the status of the order
     *
     * @param orderStatus
     *            the status to set
     * @throws IllegalArgumentException
     *             if the status is null
     */
    public void setOrderStatus ( final Status orderStatus ) {
        if ( orderStatus == null ) {
            throw new IllegalArgumentException( "Order's status can't be null" );
        }
        this.orderStatus = orderStatus;
    }

    /**
     * Returns the owner of the order
     *
     * @return the owner of the order
     */
    public String getOrderOwner () {
        return orderOwner;
    }

    /**
     * Sets the owner of the order
     *
     * @param orderOwner
     *            the owner of the order
     * @throws IllegalArgumentException
     *             if the order owner is null
     **/
    public void setOrderOwner ( final String orderOwner ) {
        if ( orderOwner == null ) {
            throw new IllegalArgumentException( "Order's owner can't be null" );
        }
        this.orderOwner = orderOwner;
    }

    /**
     * Sets the id of the Customer Order ( this is for Hibernate )
     *
     * @param id
     *            the id of the order
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the order id
     */
    @Override
    public Long getId () {
        return id;
    }

    @Override
    public int hashCode () {
        return Objects.hash( id, orderStatus, recipe, orderOwner );
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
        final CustomerOrder other = (CustomerOrder) obj;
        return Objects.equals( id, other.id ) && orderStatus == other.orderStatus
                && Objects.equals( recipe, other.recipe ) && Objects.equals( orderOwner, other.orderOwner );
    }
}
