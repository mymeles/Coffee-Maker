package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller class for the URL mappings for CoffeeMaker. The controller returns
 * the approprate HTML page in the /src/main/resources/templates folder. For a
 * larger application, this should be split across multiple controllers.
 *
 * @author Kai Presler-Marshall
 */
@Controller
public class MappingController {

    /**
     * On a GET request to /index, the IndexController will return
     * /src/main/resources/templates/index.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/index", "/" } )
    public String index ( final Model model ) {
        return "index";
    }

    /**
     * On a GET request to /index, the Controller will return
     * /src/main/resources/templates/homePage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/homePage", "/homePage.html" } )
    public String home ( final Model model ) {
        return "homePage";
    }

    /**
     * On a GET request to /index, the Controller will return
     * /src/main/resources/templates/homePage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/createUser", "/createUser.html" } )
    public String createUser ( final Model model ) {
        return "createUser";
    }

    /**
     * On a GET request to /index, the Controller will return
     * /src/main/resources/templates/homePage.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/loginUser", "/loginUser.html" } )
    public String loginUser ( final Model model ) {
        return "loginUser";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/recipe", "/recipe.html" } )
    public String addRecipePage ( final Model model ) {
        return "recipe";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/addInventory", "/addInventory.html" } )
    public String addInventory ( final Model model ) {
        return "addInventory";
    }

    /**
     * On a GET request to /recipe, the RecipeController will return
     * /src/main/resources/templates/recipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipePage", "/editrecipePage.html" } )
    public String editRecipePage ( final Model model ) {
        return "editrecipePage";
    }

    /**
     * On a GET request to /edit_unit, the EditIngredientUnitController will
     * return /src/main/resources/templates/edit_unit.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editUnits", "/edit_unit.html" } )
    public String editIngredientUnitPage ( final Model model ) {
        return "edit_unit";
    }

    /**
     * On a GET request to /edit_unit, the RemoveIngredientController will
     * return /src/main/resources/templates/remove_ingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/removeIngredient", "/remove_ingredient.html" } )
    public String removeIngredientPage ( final Model model ) {
        return "remove_ingredient";
    }

    /**
     * On a GET request to /ingredient, the AddIngredientController will return
     * /src/main/resources/templates/ingredient.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/ingredient", "/ingredient.html" } )
    public String addIngredientPage ( final Model model ) {
        return "ingredient";
    }

    /**
     * On a GET request to /deleterecipe, the DeleteRecipeController will return
     * /src/main/resources/templates/deleterecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/deleterecipe", "/deleterecipe.html" } )
    public String deleteRecipeForm ( final Model model ) {
        return "deleterecipe";
    }

    /**
     * On a GET request to /editrecipe, the EditRecipeController will return
     * /src/main/resources/templates/editrecipe.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/editrecipe", "/editrecipe.html" } )
    public String editRecipeForm ( final Model model ) {
        return "editrecipe";
    }

    /**
     * Handles a GET request for inventory. The GET request provides a view to
     * the client that includes the list of the current ingredients in the
     * inventory and a form where the client can enter more ingredients to add
     * to the inventory.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/inventory", "/inventory.html" } )
    public String inventoryForm ( final Model model ) {
        return "inventory";
    }

    /**
     * On a GET request to /makecoffee, the MakeCoffeeController will return
     * /src/main/resources/templates/makecoffee.html.
     *
     * @param model
     *            underlying UI model
     * @return contents of the page
     */
    @GetMapping ( { "/makecoffee", "/makecoffee.html" } )
    public String makeCoffeeForm ( final Model model ) {
        return "makecoffee";
    }

    @GetMapping ( { "/staffMenu", "/staffMenu.html" } )
    public String staffManuForm ( final Model model ) {
        return "staffMenu";
    }

    @GetMapping ( { "/customerMenu", "/customerMenu.html" } )
    public String staffMenuForm ( final Model model ) {
        return "customerMenu";
    }

}
