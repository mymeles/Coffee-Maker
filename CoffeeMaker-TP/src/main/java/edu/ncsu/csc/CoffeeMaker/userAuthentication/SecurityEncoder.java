package edu.ncsu.csc.CoffeeMaker.userAuthentication;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This class creates a security encoder that all the classes can reference the
 * same one
 *
 * @author sanjitverma
 *
 */

public class SecurityEncoder {
    static PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    /**
     * This method encrypts the user password
     *
     * @param password
     *            the password to encrypt
     * @return
     */
    public static String encryptPassword ( final String password ) {
        return passwordEncoder.encode( password );
    }

    /**
     * This method checks if the encoded password matches the entered password
     *
     * @param password
     *            the password passed in
     * @param encodedPassword
     *            the encoded password passed in
     * @return true if the passwords match
     */
    public static boolean checkPassword ( final String password, final String encodedPassword ) {
        return passwordEncoder.matches( password, encodedPassword );
    }

}
