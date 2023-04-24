package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the controller that holds the REST endpoints that handle add and
 * update, delete and get operations for the User.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Harris Khan
 * @author sanjitverma
 * @author James Leach
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController {
    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService userService;

    /**
     * REST API method to provide GET access to all Users in the system.
     *
     * @return a ResponseEntity containing a List of all Users in the system, or
     *         an error if none exist
     */
    @GetMapping ( BASE_PATH + "/users" )
    public ResponseEntity<List<User>> getAllUsers () {
        final List<User> users = userService.getAllUsers();
        if ( users.isEmpty() ) {
            return new ResponseEntity( errorResponse( "No users found in the system" ), HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( users, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to the Users in the system.
     *
     * @param username
     *            the username to search by
     * @return a user, if the user exists
     */
    @GetMapping ( BASE_PATH + "/users/{username}" )
    public ResponseEntity getUserByUsername ( @PathVariable final String username ) {
        final User user = userService.findByUsername( username );
        if ( user == null ) {
            return new ResponseEntity( errorResponse( "User with the username " + username + " not found" ),
                    HttpStatus.NOT_FOUND );
        }
        return new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * This method can pull the role of a user
     *
     * @param username
     *            the name of the user in the system
     * @return the role of the user if the user exists
     */
    @GetMapping ( BASE_PATH + "/users/role/{username}" )
    public ResponseEntity<String> getRoleByUsername ( @PathVariable final String username ) {
        // find the user
        final User user = userService.findByUsername( username );

        // ensure the user is not null
        if ( user == null ) {
            return new ResponseEntity( errorResponse( "User with username " + username + " does not exist" ),
                    HttpStatus.NOT_FOUND );
        }

        // if the user does exist, return the role of the user as a JSON string
        final String response = "{ \"role\": \"" + user.getRole().toString() + "\" }";
        return new ResponseEntity<>( response, HttpStatus.OK );
    }

    /**
     * Updates a user password
     *
     * @param username
     *            the username to change the password of
     * @param password
     *            the new password
     * @return a success or error response
     */
    @PatchMapping ( BASE_PATH + "/users/{username}/{password}" )
    public ResponseEntity updatePassword ( @PathVariable final String username, @PathVariable final String password ) {
        final User user = userService.findByUsername( username );
        if ( user == null ) {
            return new ResponseEntity( errorResponse( "User with the username " + username + " not found" ),
                    HttpStatus.NOT_FOUND );
        }

        user.setPassword( password );
        userService.save( user );

        return new ResponseEntity( successResponse( username + " password updated" ), HttpStatus.OK );
    }

    /**
     * Logs into a user
     *
     * @param username
     *            the username to log into
     * @param password
     *            the password of the user to log in to
     *
     * @return a success or error response
     */
    @GetMapping ( BASE_PATH + "/users/{username}/{password}" )
    public ResponseEntity login ( @PathVariable final String username, @PathVariable final String password ) {
        final User user = userService.findByUsername( username );
        if ( user == null ) {
            return new ResponseEntity( errorResponse( "User with the username " + username + " not found" ),
                    HttpStatus.NOT_FOUND );
        }

        if ( !user.checkPassword( password ) ) {
            return new ResponseEntity( errorResponse( "Invalid LogIn Credentials" ), HttpStatus.CONFLICT );
        }
        else if ( user.getRole().toString().equals( "GUEST" ) ) {
            return new ResponseEntity( errorResponse( "Cannot login with a Guest account!" ), HttpStatus.CONFLICT );

        }
        else {
            return new ResponseEntity( successResponse( "Correct login details!", user.getRole() ), HttpStatus.OK );
        }
    }

    /**
     * This creates a success response message to send to the API Caller
     * specifically meant for the login api call
     *
     * @param message
     *            the message to be sent
     * @param role
     *            the role of the user being called
     * @return a ResponseEntity with either the error or the user role and
     *         success message
     */
    private Map<String, Object> successResponse ( final String message, final Role role ) {
        final Map<String, Object> response = new HashMap<>();
        response.put( "message", message );
        response.put( "role", role.toString() );
        response.put( "success", true );
        return response;
    }

    /**
     * REST API method to provide POST access to the User model. This is used to
     * create a new User by automatically converting the JSON RequestBody
     * provided to a User object. Invalid JSON will fail.
     *
     * @param user
     *            The valid User to be saved.
     * @return ResponseEntity indicating success if the User could be saved to
     *         the inventory, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/users" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        if ( userService.findByUsername( user.getUsername() ) != null ) {
            return new ResponseEntity(
                    errorResponse( "User with the username " + user.getUsername() + " already exists" ),
                    HttpStatus.CONFLICT );
        }

        userService.save( user );

        return new ResponseEntity( successResponse( user.getUsername() + " successfully created" ),
                HttpStatus.CREATED );
    }

    /**
     * REST API method to provide DELETE access to the User model. This is used
     * to delete a User by automatically converting the JSON RequestBody
     * provided to a User object. Invalid JSON will fail.
     *
     * @param username
     *            The valid User to be deleted.
     *
     * @return ResponseEntity indicating success if the User could be deleted
     */
    @DeleteMapping ( BASE_PATH + "/users/{username}" )
    public ResponseEntity deleteUser ( @PathVariable final String username ) {
        final User tempUser = userService.findByUsername( username );
        if ( tempUser == null ) {
            return new ResponseEntity( errorResponse( "User with the username " + username + " not found" ),
                    HttpStatus.CONFLICT );
        }

        userService.delete( tempUser );

        return new ResponseEntity( successResponse( username + " successfully deleted" ), HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the User model. This is used to
     * create a new Guest User
     *
     * @return ResponseEntity with the username of the guest user that was
     *         created
     */
    @GetMapping ( BASE_PATH + "/users/createGuest" )
    public ResponseEntity makeGuest () {
        User tempUser;
        String username;

        // generate the username
        do {
            // use rand to generate a random username
            username = "guest" + (int) ( Math.random() * 1000000 );
            // check if the guest already exists
            tempUser = userService.findByUsername( username );

        }
        while ( tempUser != null );

        // create with the new username
        tempUser = new User( username, "", Role.GUEST );
        userService.save( tempUser );

        // create the return response to the user
        final String response = "{ \"username\": \"" + tempUser.getUsername() + "\" }";

        // return the response
        return new ResponseEntity( response, HttpStatus.OK );

    }

}
