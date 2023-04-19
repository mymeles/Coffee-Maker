package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.Order;
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
public class OrderTest {

    // @Autowired
    // private OrderService orderService;
    //
    // @BeforeEach
    // void setUp () {
    // orderService.deleteAll();
    // }

    @Test
    public void testOrderConstructor () {
        final Recipe temp = new Recipe();
        final Order test = new Order( "test", temp, 10, "Completed" );

        Assertions.assertEquals( test.getAmountPaid(), 10 );
        Assertions.assertEquals( test.getOwnerName(), "test" );
        Assertions.assertEquals( test.getRecipeOrdered(), temp );
        Assertions.assertEquals( test.getStatus(), "Completed" );

    }

    @Test
    public void testOrderSetters () {
        final Recipe temp = new Recipe();
        final Order test = new Order();

        test.setOwnerName( "test" );
        test.setRecipeOrdered( temp );
        test.setAmountPaid( 10 );
        test.setStatus( "Completed" );

        Assertions.assertEquals( test.getAmountPaid(), 10 );
        Assertions.assertEquals( test.getOwnerName(), "test" );
        Assertions.assertEquals( test.getRecipeOrdered(), temp );
        Assertions.assertEquals( test.getStatus(), "Completed" );
    }

    @Test
    public void testUserToString () {
        final Recipe temp = new Recipe();
        temp.setName( "temp" );
        final Order test = new Order( "test", temp, 10, "Completed" );

        final String string = "test: temp";

        Assertions.assertEquals( string, test.toString() );
    }

    @Test
    public void testUserEquals () {
        final Recipe temp = new Recipe();
        final Order test = new Order( "test", temp, 10, "Completed" );
        final Order test1 = new Order( "test", temp, 10, "Completed" );

        Assertions.assertEquals( test, test1 );
        Assertions.assertEquals( test.hashcode(), test1.hashcode() );

    }

}
