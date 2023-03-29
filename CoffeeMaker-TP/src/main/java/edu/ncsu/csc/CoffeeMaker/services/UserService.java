package edu.ncsu.csc.CoffeeMaker.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.repositories.UserRepository;

/**
 * Provides db services for User
 *
 * @author Harris Khan
 *
 */
@Component
@Transactional
public class UserService extends Service<User, Long> {

    /**
     * UserRepository, to be autowired in by Spring and provide CRUD operations
     * on User model.
     */
    @Autowired
    private UserRepository userRepository;

    @Override
    protected JpaRepository<User, Long> getRepository () {
        return userRepository;
    }

    /**
     * Returns a list of all users in the user repository
     *
     * @return a list of all users in the repository
     */
    public List<User> getAllUsers () {
        return userRepository.findAll();
    }

    /**
     * Uses the username to find a user in the user repository
     *
     * @param username
     *            the username of the user being searched for
     * @return the user object or null if the user is not found
     */
    public User findByUsername ( final String username ) {
        return userRepository.findByUsername( username );
    }

}
