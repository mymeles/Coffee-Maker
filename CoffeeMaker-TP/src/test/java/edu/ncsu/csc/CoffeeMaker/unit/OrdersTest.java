package edu.ncsu.csc.CoffeeMaker.unit;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Order;
import edu.ncsu.csc.CoffeeMaker.models.Orders;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;

/**
 * Tests the User class
 *
 * @author epkite
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
public class OrdersTest {

    // @Autowired
    // private OrderService orderService;
    //
    // @BeforeEach
    // void setUp () {
    // orderService.deleteAll();
    // }

    @Test
    public void testOrdersConstructor () {
        final Recipe temp = new Recipe();
        final Order test = new Order( "test", temp, 10, "Completed" );
        final Order test2 = new Order( "test1", temp, 10, "Completed" );
        final Order test3 = new Order( "test", temp, 10, "Completed" );
        final Order test4 = new Order( "test2", temp, 10, "Completed" );

        final Orders o = new Orders();
        final List<Order> l = new ArrayList<Order>();
        final Orders compare = new Orders( l );
        o.addOrder( test );
        o.addOrder( test2 );
        o.addOrder( test3 );
        o.addOrder( test4 );

        compare.addOrder( test );
        compare.addOrder( test3 );

        Assertions.assertEquals( o.getOrdersByUser( "test" ), compare.getOrdersByUser( "test" ) );

    }

    @Test
    public void testAddOrders () {
        final Recipe temp = new Recipe();
        final Order test = new Order( "test", temp, 10, "Completed" );
        final Order test2 = new Order( "test1", temp, 10, "Completed" );
        final Order test3 = new Order( "test", temp, 10, "Completed" );
        final Order test4 = new Order( "test2", temp, 10, "Completed" );

        final Orders o = new Orders();
        final List<Order> l = new ArrayList<Order>();
        o.addOrder( test );
        o.addOrder( test2 );

        l.add( test3 );
        l.add( test4 );
        o.addOrders( l );

        Assertions.assertTrue( o.getOrders().contains( test3 ) );
        Assertions.assertTrue( o.getOrders().contains( test4 ) );

    }

    @Test
    public void testToString () {
        final Recipe temp = new Recipe();
        temp.setName( "temp" );
        final Order test = new Order( "test", temp, 10, "Completed" );
        final Order test2 = new Order( "test2", temp, 10, "Completed" );

        final Orders o = new Orders();
        o.addOrder( test );
        o.addOrder( test2 );

        Assertions.assertEquals( o.toString(), "test: temp\ntest2: temp\n" );
    }
}
