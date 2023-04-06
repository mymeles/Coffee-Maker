package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests the User class
 *
 * @author harriskhan
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class UserTest {

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp () {
        userService.deleteAll();
    }

    @Test
    void testUserConstructorAndGetters () {
        final User user = new User( "testUser", "password123", Role.CUSTOMER );
        Assertions.assertEquals( "testUser", user.getUsername() );
        Assertions.assertEquals( "password123", user.getPassword() );
        Assertions.assertEquals( Role.CUSTOMER, user.getRole() );
    }

    @Test
    void testUserSetters () {
        final User user = new User();
        user.setUsername( "testUser" );
        user.setPassword( "password123" );
        user.setRole( Role.STAFF );

        Assertions.assertEquals( "testUser", user.getUsername() );
        Assertions.assertEquals( "password123", user.getPassword() );
        Assertions.assertEquals( Role.STAFF, user.getRole() );
    }

    @Test
    void testUserToString () {
        final User user = new User( "testUser", "password123", Role.CUSTOMER );
        final String expectedString = "User [id=null, username=testUser, password=password123, role=CUSTOMER]";
        Assertions.assertEquals( expectedString, user.toString() );
    }

    @Test
    void testUserEqualsAndHashCode () {
        final User user1 = new User( "testUser", "password123", Role.CUSTOMER );
        final User user2 = new User( "testUser", "password123", Role.CUSTOMER );
        final User user3 = new User( "anotherUser", "password123", Role.CUSTOMER );
        final User user4 = new User( "testUser", "differentPassword", Role.CUSTOMER );
        final User user5 = new User( "testUser", "password123", Role.STAFF );

        Assertions.assertEquals( user1, user1 );
        Assertions.assertEquals( user1, user2 );
        Assertions.assertNotEquals( user1, user3 );
        Assertions.assertNotEquals( user1, user4 );
        Assertions.assertNotEquals( user1, user5 );
        Assertions.assertNotEquals( user1, null );
        Assertions.assertNotEquals( user1, "not a User object" );

        Assertions.assertEquals( user1.hashCode(), user2.hashCode() );
        Assertions.assertNotEquals( user1.hashCode(), user3.hashCode() );
        Assertions.assertNotEquals( user1.hashCode(), user4.hashCode() );
        Assertions.assertNotEquals( user1.hashCode(), user5.hashCode() );
    }
}
