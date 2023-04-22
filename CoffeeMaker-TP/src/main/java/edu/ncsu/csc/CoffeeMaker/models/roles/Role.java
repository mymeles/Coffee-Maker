/**
 *
 */
package edu.ncsu.csc.CoffeeMaker.models.roles;

/**
 * The Role enumeration defines the two possible roles for users: CUSTOMER or
 * STAFF. This enumeration is used to manage access control within the
 * CoffeeMaker application.
 *
 * @author Harris Khan
 */
public enum Role {
    /**
     * The CUSTOMER role represents a regular user of the CoffeeMaker
     * application. Customers can view recipes, place orders, pick-up orders,
     * and view their order history.
     */
    CUSTOMER,
    /**
     * The STAFF role represents an employee of the CoffeeMaker establishment.
     * Staff members can perform managing orders, adding and removing menu
     * items, and managing user accounts.
     */
    STAFF,
    /**
     * The MANAGER role represents a MANAGER of the CoffeeMaker establishment.
     * Managers can perform functionality that is available to both customers and staff members
     */
    MANAGER
}
