package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.roles.Role;
import edu.ncsu.csc.CoffeeMaker.userAuthentication.SecurityEncoder;

/**
 * User for the coffee maker. User is tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support.
 *
 * @author Harris Khan
 * @author Sanjit Verma
 * @author James Leach
 */
@Entity
public class User extends DomainObject {

    /** User id */
    @Id
    @GeneratedValue
    private Long   id;

    /** username */
    private String username;

    /**
     * Password for the user that will be used for authentication
     */
    private String password;

    /**
     * The role of the user (either CUSTOMER or STAFF)
     */
    @Enumerated ( EnumType.STRING )
    private Role   role;

    /**
     * Constructor for Hibernate
     */
    public User () {
        this.username = "";
        this.password = "";
        this.role = null;
    };

    /**
     * Constructs a new User object give the username, password, and role
     *
     * @param username
     *            the username for the new User
     * @param password
     *            the password for the new User
     * @param role
     *            the role of the new User
     */
    public User ( final String username, final String password, final Role role ) {
        setUsername( username );
        setPassword( password );
        setRole( role );
    }

    /**
     * This gets the ID
     *
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * This sets the ID
     *
     * @param id
     *            the id to set (used by hibernate)
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * This gets the username
     *
     * @return the username
     */
    public String getUsername () {
        return username;
    }

    /**
     * This sets the username
     *
     * @param username
     *            the username to set
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * This gets the password
     *
     * @return the password
     */
    public String getPassword () {
        return password;
    }

    /**
     * This sets the password
     *
     * @param password
     *            the password to set after encryption
     */
    public void setPassword ( final String password ) {
        this.password = SecurityEncoder.encryptPassword( password );

    }

    /**
     * This method checks if the password from the user and encoded passwords
     * are similar
     *
     * @param plaintext
     *            the plaintext passcode that needs to be checked against the
     *            encryped password
     *
     * @return true if the passwords match, false if not
     */
    public boolean checkPassword ( final String plaintext ) {
        return SecurityEncoder.checkPassword( plaintext, getPassword() );
    }

    /**
     * This gets the role
     *
     * @return the role
     */
    public Role getRole () {
        return role;
    }

    /**
     * This sets the role of the user
     *
     * @param role
     *            the role to set
     */
    public void setRole ( final Role role ) {
        this.role = role;
    }


    @Override
    public int hashCode () {
        return Objects.hash( id, password, role, username );
    }

    @Override
    public boolean equals ( final Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final User other = (User) obj;
        return Objects.equals( id, other.id ) && Objects.equals( password, other.password ) && role == other.role
                && Objects.equals( username, other.username );
    }

}
