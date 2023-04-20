package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.status.Status;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerOrderRepository;

/**
 * TODO class comments go here
 *
 */
@Component
@Transactional
public class CustomerOrderService extends Service<CustomerOrder, Long> {

    /**
     * OrderRepository, to be autowired in by Spring and provide CRUD operations
     * on Inventory model.
     */
    @Autowired
    private CustomerOrderRepository orderRepository;

    @Override
    protected JpaRepository<CustomerOrder, Long> getRepository () {
        return orderRepository;
    }

    /**
     * Returns a list of orders with the specified status.
     *
     * @param orderStatus
     *            the status of the orders to retrieve
     * @return a list of orders with the specified status
     */
    public List<CustomerOrder> findByOrderStatus ( final Status orderStatus ) {
        return orderRepository.findByOrderStatus( orderStatus );
    }

    /**
     * Returns a list of orders with the specified order owner.
     *
     * @param orderOwner
     *            the owner of the orders to retrieve
     * @return a list of orders with the specified owner
     */
    public List<CustomerOrder> findByOrderOwner ( final String orderOwner ) {
        return orderRepository.findByOrderOwner( orderOwner );
    }
}
