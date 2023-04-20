package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;
import edu.ncsu.csc.CoffeeMaker.userAuthentication.SecurityEncoder;

/**
 * 
 * The APIUserTest class contains unit tests for the APIUserController class. It
 * includes tests for creating, getting, updating, and deleting users via REST
 * API endpoints. The tests are implemented using JUnit 5 framework and mock
 * HTTP requests using MockMvc. The SpringExtension class is used to integrate
 * with the Spring framework. The test cases in this class cover the following
 * scenarios: Creating a new user via POST request and checking if it exists via
 * GET request. Attempting to create a user with an existing username, and
 * ensuring the operation returns a conflict status code. Retrieving all
 * existing users via GET request and verifying their usernames and roles.
 * Deleting an existing user via DELETE request and checking if it was deleted
 * via GET request. Updating an existing user's password via PATCH request and
 * verifying the new password via UserService. Authenticating a user by their
 * username, password, and role via GET request and ensuring the response
 * matches expected.
 * 
 * @author harriskhan
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class APIUserTest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	@Autowired
	private MockMvc mvc;
	
	/**
	 * User service to interact with the database
	 */
	@Autowired
	private UserService userService;

	/**
	 * This method is executed before each test method to delete all existing users.
	 */
	@BeforeEach
	void setUp() throws Exception {
		userService.deleteAll();
	}

	/**
	 * This method tests creating a new user via POST request and checking if it
	 * exists via GET request. It first sends a GET request for the username, and
	 * expects a "not found" status code. Then, it creates a new User instance,
	 * sends a POST request to the "/api/v1/users" endpoint with the JSON
	 * representation of the new user, and expects a "created" status code. Finally,
	 * it sends a GET request for the username again, and expects an "OK" status
	 * code.
	 */
	@Test
	@Transactional
	void testCreateUser() throws Exception {
		mvc.perform(get("/api/v1/users/testuser").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString("testuser"))).andExpect(status().isNotFound());

		final User newUser = new User("testuser", "password", Role.CUSTOMER);

		mvc.perform(
				post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(newUser)))
				.andExpect(status().isCreated());

		mvc.perform(get("/api/v1/users/testuser").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString("testuser"))).andExpect(status().isOk());
	}

	/**
	 * This method tests attempting to create a user with an existing username, and
	 * ensuring the operation returns a conflict status code. It first creates a new
	 * User instance and saves it to the UserService. Then, it sends a POST request
	 * to the "/api/v1/users" endpoint with the JSON representation of the existing
	 * user, and expects a "conflict" status code.
	 */
	@Test
	@Transactional
	void testCreateExistingUser() throws Exception {
		final User newUser = new User("existinguser", "password", Role.STAFF);
		userService.save(newUser);

		mvc.perform(
				post("/api/v1/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(newUser)))
				.andExpect(status().isConflict());
	}

	/**
	 * This method tests retrieving all existing users via GET request and verifying
	 * their usernames and roles. It first sends a GET request to the
	 * "/api/v1/users" endpoint and expects a "not found" status code. Then, it
	 * creates two new User instances, saves them to the UserService, and sends
	 * another GET request to the "/api/v1/users" endpoint. Finally, it verifies the
	 * response JSON matches the expected format for both users.
	 */
	@Test
	@Transactional
	void testGetAllUsers() throws Exception {
		mvc.perform(get("/api/v1/users")).andExpect(status().isNotFound());

		final User newUser1 = new User("user1", "password", Role.CUSTOMER);
		final User newUser2 = new User("user2", "password", Role.STAFF);
		userService.save(newUser1);
		userService.save(newUser2);

		mvc.perform(get("/api/v1/users")).andExpect(status().isOk()).andExpect(jsonPath("$[0].username").value("user1"))
				.andExpect(jsonPath("$[0].role").value("CUSTOMER")).andExpect(jsonPath("$[1].username").value("user2"))
				.andExpect(jsonPath("$[1].role").value("STAFF"));
	}

	/**
	 * This method tests deleting an existing user via DELETE request and checking
	 * if it was deleted via GET request. It first sends a DELETE request to the
	 * "/api/v1/users/user1" endpoint and expects a "conflict" status code. Then, it
	 * creates a new User instance, saves it to the UserService, sends a DELETE
	 * request to the "/api/v1/users/user1" endpoint, and expects an "OK" status
	 * code. Finally, it sends a GET request to the "/api/v1/users/user1" endpoint
	 * and expects a "not found" status code.
	 */
	@Test
	@Transactional
	void testDeleteUser() throws Exception {
		mvc.perform(delete("/api/v1/users/user1")).andExpect(status().isConflict());

		final User newUser = new User("user1", "password", Role.CUSTOMER);
		userService.save(newUser);

		mvc.perform(delete("/api/v1/users/user1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("user1 successfully deleted"));

		mvc.perform(get("/api/v1/users/user1")).andExpect(status().isNotFound());
	}

	/**
	 * This method tests updating an existing user's password via PATCH request and
	 * verifying that the change was successful. It first sends a PATCH request to
	 * the "/api/v1/users/testuser/randpass" endpoint and expects a "not found"
	 * status code. Then, it creates a new User instance, saves it to the
	 * UserService, and sends a PATCH request to the
	 * "/api/v1/users/user1/newpassword" endpoint. Finally, it verifies that the
	 * password was updated successfully by fetching the User instance from the
	 * UserService and checking the password hash.
	 */
	@Test
	@Transactional
	void testUpdatePassword() throws Exception {
		mvc.perform(patch("/api/v1/users/testuser/randpass").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.asJsonString("testuser"))).andExpect(status().isNotFound());

		final User newUser = new User("user1", "password", Role.CUSTOMER);
		userService.save(newUser);

		final String newPassword = "newpassword";
		mvc.perform(patch(String.format("/api/v1/users/%s/%s", newUser.getUsername(), newPassword)))
				.andExpect(status().isOk()).andExpect(jsonPath("$.message").value("user1 password updated"));

		final User updatedUser = userService.findByUsername("user1");
		assertTrue(SecurityEncoder.checkPassword(newPassword, updatedUser.getPassword()));

	}

	/**
	 * This method tests the login functionality by sending GET requests to the
	 * "/api/v1/users/{username}/{password}/{role}" endpoint. It first creates a new
	 * User instance and saves it to the UserService. Then, it sends four GET
	 * requests with different combinations of valid/invalid usernames, passwords,
	 * and roles, and expects the appropriate status codes and response messages.
	 */
	@Test
	@Transactional
	void testLogin() throws Exception {
		final User newUser = new User("user1", "password", Role.CUSTOMER);
		userService.save(newUser);

		// Test case 1: valid username and password
		mvc.perform(get("/api/v1/users/user1/password/CUSTOMER")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Correct login details!"));

		// Test case 2: invalid username
		mvc.perform(get("/api/v1/users/nonexistentuser/password/CUSTOMER")).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.message").value("User with the username nonexistentuser not found"));

		// Test case 3: invalid password
		mvc.perform(get("/api/v1/users/user1/notpassword/CUSTOMER")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Invalid LogIn Credentials"));

		// Test case 3: invalid role
		mvc.perform(get("/api/v1/users/user1/notpassword/STAFF")).andExpect(status().isConflict())
				.andExpect(jsonPath("$.message").value("Invalid LogIn Credentials"));
	}

}
