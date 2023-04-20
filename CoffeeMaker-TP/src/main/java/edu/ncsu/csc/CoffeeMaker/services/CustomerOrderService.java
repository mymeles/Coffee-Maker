package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerOrderRepository;

/**
 * TODO class comments go here
 *
 * @author Meles Meles
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

}
