package edu.ncsu.csc.CoffeeMaker.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import edu.ncsu.csc.CoffeeMaker.TestConfig;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;
import edu.ncsu.csc.CoffeeMaker.userAuthentication.SecurityEncoder;

/**
 * Tests the User class
 *
 * @author harriskhan
 * @author sanjitverma
 * @author James Leach
 *
 */
@ExtendWith ( SpringExtension.class )
@EnableAutoConfiguration
@SpringBootTest ( classes = TestConfig.class )
class UserTest {
    PasswordEncoder     passwordEncoder = null;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp () {
        userService.deleteAll();
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Test
    void testUserConstructorAndGetters () {
        final User user = new User( "testUser", "password123", Role.CUSTOMER );
        Assertions.assertEquals( "testUser", user.getUsername() );
        Assertions.assertTrue( SecurityEncoder.checkPassword( "password123", user.getPassword() ) );
        Assertions.assertEquals( Role.CUSTOMER, user.getRole() );
    }

    @Test
    void testUserSetters () {
        final User user = new User();
        user.setUsername( "testUser" );
        user.setPassword( "password123" );
        user.setRole( Role.STAFF );

        Assertions.assertEquals( "testUser", user.getUsername() );
        Assertions.assertTrue( SecurityEncoder.checkPassword( "password123", user.getPassword() ) );
        Assertions.assertEquals( Role.STAFF, user.getRole() );
    }

    @Test
    void testUserToString () {
        final User user = new User( "testUser", "password123", Role.CUSTOMER );
        final String expectedString = "User [id=null, username=testUser, role=CUSTOMER]";
        Assertions.assertEquals( expectedString, user.toString() );
    }

    @Test
    void testUserEqualsAndHashCode () {
        final User user1 = new User( "testUser", "password123", Role.CUSTOMER );
        final User user2 = new User( "testUser", "password1234", Role.CUSTOMER );

        Assertions.assertEquals( user1, user1 );
        Assertions.assertNotEquals( user1, user2 );

    }
}
