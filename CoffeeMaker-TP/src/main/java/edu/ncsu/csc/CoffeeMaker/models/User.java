package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.roles.Role;

/**
 * User for the coffee maker. User is tied to the database using Hibernate
 * libraries. See UserRepository and UserService for the other two pieces used
 * for database support.
 *
 * @author Harris Khan
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
     * @return the id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * @param id
     *            the id to set (used by hibernate)
     */
    @SuppressWarnings ( "unused" )
    private void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername () {
        return username;
    }

    /**
     * @param username
     *            the username to set
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword () {
        return password;
    }

    /**
     * @param password
     *            the password to set
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * @return the role
     */
    public Role getRole () {
        return role;
    }

    /**
     * @param role
     *            the role to set
     */
    public void setRole ( final Role role ) {
        this.role = role;
    }

    @Override
    public String toString () {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", role=" + role + "]";
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
