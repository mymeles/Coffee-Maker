package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import edu.ncsu.csc.CoffeeMaker.models.status.Status;

/**
 * Class containing the orders of customers that holds a recipe and the order's
 * status
 *
 * @author Meles Meles
 *
 */
@Entity
public class CustomerOrder extends DomainObject {

    /** Recipe id */
    @Id
    @GeneratedValue
    private Long   id;

    /** Recipe name */
    @ManyToOne
    private Recipe recipe;

    /**
     * The role of the user (either CUSTOMER or STAFF)
     */
    @Enumerated ( EnumType.STRING )
    private Status orderStatus;

    /**
     * CustomOrder constructor
     */
    public CustomerOrder () {
        this.recipe = null;
        this.orderStatus = null;
    }

    /**
     * CustomOrder constructor
     *
     */
    public CustomerOrder ( final Recipe recipe, final Status order_status ) {
        setRecipe( recipe );
        setOrderStatus( order_status );
    }

    /**
     * Returns the recipe connected to the order
     *
     * @return order recipe
     */
    public Recipe getRecipe () {
        return recipe;
    }

    /**
     * Sets the recipe for the order
     *
     * @param recipe
     *            recipe connected to the order
     * @throws IAE
     *             if the recipe is null
     */
    public void setRecipe ( final Recipe recipe ) {
        if ( recipe == null ) {
            throw new IllegalArgumentException( "Ordes Recipe can't be null" );
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
     *            current status of the order
     * @throws IAE
     *             is the status is null
     */
    public void setOrderStatus ( final Status orderStatus ) {
        if ( orderStatus == null ) {
            throw new IllegalArgumentException( "Ordes Status can't be null" );
        }
        this.orderStatus = orderStatus;
    }

    /**
     * Sets the order id
     *
     * @param id
     *            id of the order
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
        return Objects.hash( id, orderStatus, recipe );
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
                && Objects.equals( recipe, other.recipe );
    }

}
