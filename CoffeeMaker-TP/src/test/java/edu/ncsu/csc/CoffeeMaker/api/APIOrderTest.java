package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

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

import edu.ncsu.csc.CoffeeMaker.models.CustomerOrder;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.models.status.Status;
import edu.ncsu.csc.CoffeeMaker.services.CustomerOrderService;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * 
 * The APIOrderTest class tests the order endpoints of the CoffeeMaker API. It
 * tests that the placeOrder, completeOrder, pickupOrder, and getOrder endpoints
 * work correctly, and that errors are thrown as appropriate. This class uses
 * the Spring Testing framework and JUnit to generate http requests and test
 * their results.
 * 
 * @author Meles Meles
 *
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIOrderTest {

	/**
	 * MockMvc uses Spring's testing framework to handle requests to the REST API
	 */
	@Autowired
	private MockMvc mvc;

	/** recipe service object for db **/
	@Autowired
	private RecipeService rService;

	/** User service object for db **/
	@Autowired
	private UserService userService;

	/** CustomerOrderService service object for db **/
	@Autowired
	private CustomerOrderService orderService;

	/** inventory service object for db **/
	@Autowired
	private InventoryService inventoryService;

	/**
	 * The first customer order created for testing purposes.
	 */
	CustomerOrder ord1;

	/**
	 * The second customer order created for testing purposes.
	 */
	CustomerOrder ord2;

	/**
	 * The third customer order created for testing purposes.
	 */
	CustomerOrder ord3;

	/**
	 * The first user created for testing purposes.
	 */
	User user1;

	/**
	 * The second user created for testing purposes.
	 */
	User user2;

	/**
	 * The third user created for testing purposes.
	 */
	User user3;

	/**
	 * This method sets up the required variables for each test case. It deletes all
	 * the entities stored in the database, creates the required inventories and
	 * recipes to create orders and users.
	 */
	@BeforeEach
	public void setup() {
		rService.deleteAll();
		userService.deleteAll();

		final Inventory ivt = inventoryService.getInventory();
		final Ingredient coffeIngredient = new Ingredient("Coffee", 100);
		final Ingredient milkIngredient = new Ingredient("milk", 100);
		final Ingredient sugarIngredient = new Ingredient("sugar", 100);
		final Ingredient caramelIngredient = new Ingredient("chocolate", 100);

		final ArrayList<Ingredient> lists = new ArrayList<Ingredient>();
		lists.add(coffeIngredient);
		lists.add(milkIngredient);
		lists.add(sugarIngredient);
		lists.add(caramelIngredient);

		ivt.setIngredients(lists);
		inventoryService.save(ivt);

		// set-up recipes to add to orders
		final Recipe r1 = new Recipe();
		r1.setName("Black Coffee");
		r1.setPrice(1);
		r1.addIngredient(new Ingredient("Coffee", 5));
		r1.addIngredient(new Ingredient("milk", 5));
		r1.addIngredient(new Ingredient("sugar", 5));
		r1.addIngredient(new Ingredient("chocolate", 5));
		rService.save(r1);

		final Recipe r2 = new Recipe();
		r2.setName("Mocha");
		r2.setPrice(1);
		r2.addIngredient(new Ingredient("Coffee", 1));
		r2.addIngredient(new Ingredient("milk", 1));
		r2.addIngredient(new Ingredient("sugar", 1));
		r2.addIngredient(new Ingredient("chocolate", 1));
		rService.save(r2);

		final Recipe r3 = new Recipe();
		r3.setName("MilkShake");
		r3.setPrice(1);
		r3.addIngredient(new Ingredient("Coffee", 1));
		r3.addIngredient(new Ingredient("milk", 1));
		r3.addIngredient(new Ingredient("sugar", 1));
		r3.addIngredient(new Ingredient("chocolate", 1));
		rService.save(r3);

		final List<Recipe> recipes = rService.findAll();
		assertEquals(3, recipes.size());

		// setup orders to add to users

		ord1 = new CustomerOrder(recipes.get(0).getName(), Status.Order_Placed, "test");
		ord2 = new CustomerOrder(recipes.get(1).getName(), Status.Order_Placed, "test");
		ord3 = new CustomerOrder(recipes.get(2).getName(), Status.Order_Placed, "test");

		// setup users
		user1 = new User();
		user1.setUsername("usr1");
		user1.setPassword("password123");
		user1.setRole(Role.CUSTOMER);
		userService.save(user1);

		user2 = new User();
		user2.setUsername("usr2");
		user2.setPassword("password123");
		user2.setRole(Role.CUSTOMER);
		userService.save(user2);

		user3 = new User();
		user3.setUsername("usr3");
		user3.setPassword("password123");
		user3.setRole(Role.CUSTOMER);
		userService.save(user3);
	}

	/**
	 * 
	 * This method tests the functionality of placing orders using the API. It
	 * verifies that the order was successfully placed and the payment required
	 * error check. It also checks for invalid user names and recipe names.
	 * 
	 * @throws Exception if there is an error performing the request or assertion
	 *                   failure.
	 */
	@Test
	@Transactional
	public void testApiPlaceOrders() throws Exception {
		final List<User> ursList = userService.getAllUsers();
		final List<Recipe> recipeList = rService.findAll();
		assertEquals(recipeList.size(), 3);
		// paymemt required error check
		mvc.perform(post("/api/v1/orders/" + ursList.get(0).getUsername() + "/" + 0 + "/" + recipeList.get(0).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isPaymentRequired());

		mvc.perform(post("/api/v1/orders/" + ursList.get(0).getUsername() + "/" + 5 + "/" + recipeList.get(0).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(post("/api/v1/orders/" + ursList.get(1).getUsername() + "/" + 5 + "/" + recipeList.get(1).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(post("/api/v1/orders/" + ursList.get(2).getUsername() + "/" + 5 + "/" + recipeList.get(2).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		assertEquals(3, orderService.count());

		// check with invalid user names
		mvc.perform(post("/api/v1/orders/" + "fake" + "/" + 5 + "/" + recipeList.get(2).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

		mvc.perform(post("/api/v1/orders/" + ursList.get(2).getUsername() + "/" + 5 + "/" + "recipe")
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	/**
	 * 
	 * This test method tests the functionality of placing orders through the API.
	 * It first retrieves all users and recipes from the database, asserts that the
	 * recipe list size is 3, and then tests the payment required error by making a
	 * POST request to the API with an invalid payment amount. It then makes
	 * successful POST requests to the API for each user with a valid payment amount
	 * and valid recipe name. After the successful POST requests, the method asserts
	 * that the number of orders in the database is equal to 3. Finally, the method
	 * tests the error handling for invalid user names and recipe names by making
	 * POST requests to the API with invalid user names and recipe names, and
	 * asserting that the status code is '404 Not Found'.
	 * 
	 * @throws Exception if an error occurs during the test
	 */
	@Test
	@Transactional
	public void testApiGetOrders() throws Exception {
		final List<User> ursList = userService.getAllUsers();
		final List<Recipe> recipeList = rService.findAll();
		assertEquals(recipeList.size(), 3);
		for (int i = 0; i < ursList.size(); i++) {
			System.out.println(ursList.get(i).getId());
		}

		mvc.perform(post("/api/v1/orders/" + ursList.get(0).getUsername() + "/" + 5 + "/" + recipeList.get(0).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(post("/api/v1/orders/" + ursList.get(1).getUsername() + "/" + 5 + "/" + recipeList.get(1).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(post("/api/v1/orders/" + ursList.get(2).getUsername() + "/" + 5 + "/" + recipeList.get(2).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		assertEquals(3, orderService.count());

		final String lst = mvc.perform(get("/api/v1/orders")).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertTrue(lst.contains("Mocha"));
		assertTrue(lst.contains("Black Coffee"));
		assertTrue(lst.contains("MilkShake"));
	}

	/**
	 * 
	 * Test case for completing an order via API. Creates three orders and fulfills
	 * the first one. Checks that the inventory has been updated accordingly. Checks
	 * that attempting to fulfill an order with an invalid ID results in a 404
	 * status code. Checks that attempting to fulfill an order with insufficient
	 * inventory results in a 409 status code.
	 * 
	 * @throws Exception if there's an error performing the test.
	 */
	@Test
	@Transactional
	public void testApiCompleteOrder() throws Exception {
		final List<User> ursList = userService.getAllUsers();
		final List<Recipe> recipeList = rService.findAll();
		assertEquals(recipeList.size(), 3);
		for (int i = 0; i < ursList.size(); i++) {
			System.out.println(ursList.get(i).getId());
		}

		mvc.perform(post("/api/v1/orders/" + ursList.get(0).getUsername() + "/" + 5 + "/" + recipeList.get(0).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// Now all the orders are in for the users
		// It is time to make the order
		final Long id = orderService.findAll().get(0).getId();
		mvc.perform(put("/api/v1/orders/fulfill/" + id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		final List<CustomerOrder> ordList = orderService.findAll();
		assertEquals(ordList.get(0).getOrderStatus(), Status.Order_Fulfilled);

		final Inventory inv = inventoryService.getInventory();

		for (int i = 0; i < inv.getIngredients().size(); i++) {
			assertEquals((int) inv.getIngredients().get(i).getAmount(), (95));
		}

		// invalid id for the order
		mvc.perform(put("/api/v1/orders/fulfill/" + 34901234).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		// check for insufficent value fr invetory to compelte an order.
		for (int i = 0; i < 19; i++) {
			inv.useIngredients(rService.findByName(ord1.getRecipe()));
		}
		inventoryService.save(inv);

		mvc.perform(put("/api/v1/orders/fulfill/" + id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}

	/**
	 * 
	 * Tests the API endpoint for completing an order.
	 * 
	 * The test creates orders for three users and then fulfills an order using the
	 * {@code /api/v1/orders/fulfill/{id}} endpoint. The test then checks whether
	 * the inventory amount is reduced by the correct amount and whether the order
	 * is marked as fulfilled.
	 * 
	 * Additionally, the test also checks for the case where the order ID provided
	 * is invalid and where the inventory does not have enough ingredients to
	 * complete the order.
	 * 
	 * @throws Exception if an error occurs while performing the test
	 */
	@Test
	@Transactional
	public void testApiPickUpOrder() throws Exception {
		final List<User> ursList = userService.getAllUsers();
		final List<Recipe> recipeList = rService.findAll();
		assertEquals(recipeList.size(), 3);
		// order is placed
		mvc.perform(post("/api/v1/orders/" + ursList.get(0).getUsername() + "/" + 5 + "/" + recipeList.get(0).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// Now all the orders are in for the users
		// It is time to make the order
		final Long id = orderService.findAll().get(0).getId();
		mvc.perform(put("/api/v1/orders/pickup/" + id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		final List<CustomerOrder> ordList = orderService.findAll();
		assertEquals(ordList.get(0).getOrderStatus(), Status.Order_Completed);
	}

	/**
	 * This method tests the API to get a specific order by its ID. It creates
	 * orders for three different users and verifies that the orders were
	 * successfully created. Then, it gets the order by ID and verifies that the
	 * expected order is returned. Additionally, it tests the scenario where an
	 * invalid order ID is provided.
	 * 
	 * @throws Exception if there is an error while performing the API calls
	 */
	@Test
	@Transactional
	public void testApiGetOrder() throws Exception {
		final List<User> ursList = userService.getAllUsers();
		final List<Recipe> recipeList = rService.findAll();
		assertEquals(recipeList.size(), 3);
		for (int i = 0; i < ursList.size(); i++) {
			System.out.println(ursList.get(i).getId());
		}

		mvc.perform(post("/api/v1/orders/" + ursList.get(0).getUsername() + "/" + 5 + "/" + recipeList.get(0).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(post("/api/v1/orders/" + ursList.get(1).getUsername() + "/" + 5 + "/" + recipeList.get(1).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		mvc.perform(post("/api/v1/orders/" + ursList.get(2).getUsername() + "/" + 5 + "/" + recipeList.get(2).getName())
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		assertEquals(3, orderService.count());

		final List<CustomerOrder> ordList = orderService.findAll();

		// checking the getorder method so get order by id
		final String black_coffee = mvc.perform(get("/api/v1/orders/" + ordList.get(0).getId())).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		final String mocha = mvc.perform(get("/api/v1/orders/" + ordList.get(1).getId())).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		final String milk_shake = mvc.perform(get("/api/v1/orders/" + ordList.get(2).getId())).andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertTrue(mocha.contains("Mocha"));
		assertTrue(black_coffee.contains("Black Coffee"));
		assertTrue(milk_shake.contains("MilkShake"));

		// Trying to get an order with invalid ID
		mvc.perform(get("/api/v1/orders/" + 0)).andDo(print()).andExpect(status().isNotFound()).andReturn()
				.getResponse().getContentAsString();
	}
}
