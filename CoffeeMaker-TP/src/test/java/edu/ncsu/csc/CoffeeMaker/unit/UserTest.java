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
 * The UserTest class contains the unit tests for the User class. It includes
 * tests for the User constructor, getters and setters, and equals and hashCode
 * methods. The tests are implemented using JUnit 5 framework. The
 * SpringExtension class is used to integrate with the Spring framework. The
 * TestConfig class is used to provide the required dependencies for the tests.
 * 
 * @author harriskhan
 * @author sanjitverma
 * @author James Leach
 */
@ExtendWith(SpringExtension.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
class UserTest {
	PasswordEncoder passwordEncoder = null;

	/** user service for database interactions */
	@Autowired
	private UserService userService;

	/**
	 * This method is executed before each test method to set up the required
	 * dependencies. It deletes all existing users and creates a password encoder
	 * for the test.
	 */
	@BeforeEach
	void setUp() {
		userService.deleteAll();
		passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * This method tests the User constructor and getters by creating a new User
	 * instance, setting the values and then asserting that they are returned
	 * correctly by the getters.
	 */
	@Test
	void testUserConstructorAndGetters() {
		final User user = new User("testUser", "password123", Role.CUSTOMER);
		Assertions.assertEquals("testUser", user.getUsername());
		Assertions.assertTrue(SecurityEncoder.checkPassword("password123", user.getPassword()));
		Assertions.assertEquals(Role.CUSTOMER, user.getRole());
	}

	/**
	 * This method tests the User setters by creating a new User instance, setting
	 * the values using the setters, and then asserting that they are returned
	 * correctly by the getters.
	 */
	@Test
	void testUserSetters() {
		final User user = new User();
		user.setUsername("testUser");
		user.setPassword("password123");
		user.setRole(Role.STAFF);

		Assertions.assertEquals("testUser", user.getUsername());
		Assertions.assertTrue(SecurityEncoder.checkPassword("password123", user.getPassword()));
		Assertions.assertEquals(Role.STAFF, user.getRole());
	}

	/**
	 * This method tests the User equals and hashCode methods by creating multiple
	 * User instances with different values, and then asserting that they are equal
	 * or not based on their values.
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void testUserEqualsAndHashCode() {
		final User user1 = new User("testUser", "password123", Role.CUSTOMER);
		final User user2 = new User("testUser", "password1234", Role.CUSTOMER);
		final User user3 = new User("testUser1", "password1234", Role.CUSTOMER);
		final User user4 = new User("testUser", "password1234", Role.STAFF);
		final User user5 = null;
		

		Assertions.assertEquals(user1, user1);
		Assertions.assertTrue(user1.equals(user1));
		Assertions.assertFalse(user1.equals(user3));
		Assertions.assertFalse(user1.equals(user4));
		Assertions.assertFalse(user1.equals(user5));
		Assertions.assertFalse(user1.equals("name"));
		Assertions.assertNotEquals(user1, user2);

	}
}
