package edu.ncsu.csc.CoffeeMaker.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.status.Status;

/**
 * OrderRepository is used to provide CRUD operations for the Order model.
 * Spring will generate appropriate code with JPA.
 * 
 * @author Harris Khan 
 * @author Meles Meles
 *
 */
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    /**
     * Returns a list of orders with the specified status.
     *
     * @param orderStatus
     *            the status of the orders to retrieve
     * @return a list of orders with the specified status
     */
    List<CustomerOrder> findByOrderStatus ( Status orderStatus );

    /**
     * Returns a list of orders with the specified order owner.
     *
     * @param orderOwner
     *            the owner of the orders to retrieve
     * @return a list of orders with the specified owner
     */
    List<CustomerOrder> findByOrderOwner ( String orderOwner );
}
