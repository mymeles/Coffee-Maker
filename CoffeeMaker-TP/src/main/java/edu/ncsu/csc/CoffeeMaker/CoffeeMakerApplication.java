package edu.ncsu.csc.CoffeeMaker;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Entrypoint to the CoffeeMaker Application. Allows running as Java
 * application.
 *
 * @author Kai Presler-Marshall (kpresle@ncsu.edu)
 *
 */
@SpringBootApplication ( scanBasePackages = { "edu.ncsu.csc.CoffeeMaker" } )
public class CoffeeMakerApplication {

    /**
     * Used to save the manager at initialization
     */
    @Autowired
    private UserService userService;

    /**
     * Main method
     *
     * @param args
     *            Command-line args
     */
    public static void main ( final String[] args ) {
        SpringApplication.run( CoffeeMakerApplication.class, args );
    }

    /**
     * Method to create the default manager user
     */
    @PostConstruct
    public void createDefaultManager () {
        if ( userService.findByUsername( "manager" ) == null ) {
            final User manager = new User( "manager", "manager", Role.MANAGER );
            userService.save( manager );
        }
    }
}
