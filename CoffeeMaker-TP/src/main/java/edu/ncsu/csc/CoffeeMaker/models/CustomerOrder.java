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
 * 
 * @author Meles Meles
 *
 */
@Entity
public class CustomerOrder extends DomainObject {

    /** Recipe id */
    @Id 
    @GeneratedValue
    private Long             id;

    /** Recipe name */
    @ManyToOne
    private Recipe           recipe;
    
    /**
     * The role of the user (either CUSTOMER or STAFF)
     */
    @Enumerated ( EnumType.STRING )
    private Status   orderStatus;

    public CustomerOrder() {
    	this.recipe = null;
    	this.orderStatus = null;
    }

	public CustomerOrder(Recipe recipe, Status order_status) {
		setRecipe(recipe);
		setOrderStatus(order_status);
	}
	
	
	public Recipe getRecipe() {
		return recipe;
	}

	public void setRecipe(Recipe recipe) {
		if(recipe == null) {
			throw new IllegalArgumentException("Ordes Recipe can't be null");
		}
		this.recipe = recipe;
	}

	public Status getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Status orderStatus) {
		if(orderStatus == null) {
			throw new IllegalArgumentException("Ordes Status can't be null");
		}
		this.orderStatus = orderStatus;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, orderStatus, recipe);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerOrder other = (CustomerOrder) obj;
		return Objects.equals(id, other.id) && orderStatus == other.orderStatus && Objects.equals(recipe, other.recipe);
	}
	
	

}
