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
 * Test class for APIUserController
 *
 * @author harriskhan
 *
 */
@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
class APIUserTest {

    @Autowired
    private MockMvc     mvc;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp () throws Exception {
        userService.deleteAll();
    }

    @Test
    @Transactional
    void testCreateUser () throws Exception {
        mvc.perform( get( "/api/v1/users/testuser" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( "testuser" ) ) ).andExpect( status().isNotFound() );

        final User newUser = new User( "testuser", "password", Role.CUSTOMER );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( newUser ) ) ).andExpect( status().isCreated() );

        mvc.perform( get( "/api/v1/users/testuser" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( "testuser" ) ) ).andExpect( status().isOk() );
    }

    @Test
    @Transactional
    void testCreateExistingUser () throws Exception {
        final User newUser = new User( "existinguser", "password", Role.STAFF );
        userService.save( newUser );

        mvc.perform( post( "/api/v1/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( newUser ) ) ).andExpect( status().isConflict() );
    }

    @Test
    @Transactional
    void testGetAllUsers () throws Exception {
        mvc.perform( get( "/api/v1/users" ) ).andExpect( status().isNotFound() );

        final User newUser1 = new User( "user1", "password", Role.CUSTOMER );
        final User newUser2 = new User( "user2", "password", Role.STAFF );
        userService.save( newUser1 );
        userService.save( newUser2 );

        mvc.perform( get( "/api/v1/users" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$[0].username" ).value( "user1" ) )
                .andExpect( jsonPath( "$[0].role" ).value( "CUSTOMER" ) )
                .andExpect( jsonPath( "$[1].username" ).value( "user2" ) )
                .andExpect( jsonPath( "$[1].role" ).value( "STAFF" ) );
    }

    @Test
    @Transactional
    void testDeleteUser () throws Exception {
        mvc.perform( delete( "/api/v1/users/user1" ) ).andExpect( status().isConflict() );

        final User newUser = new User( "user1", "password", Role.CUSTOMER );
        userService.save( newUser );

        mvc.perform( delete( "/api/v1/users/user1" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( "user1 successfully deleted" ) );

        mvc.perform( get( "/api/v1/users/user1" ) ).andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    void testUpdatePassword () throws Exception {
        mvc.perform( patch( "/api/v1/users/testuser/randpass" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( "testuser" ) ) ).andExpect( status().isNotFound() );

        final User newUser = new User( "user1", "password", Role.CUSTOMER );
        userService.save( newUser );

        final String newPassword = "newpassword";
        mvc.perform( patch( String.format( "/api/v1/users/%s/%s", newUser.getUsername(), newPassword ) ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.message" ).value( "user1 password updated" ) );

        final User updatedUser = userService.findByUsername( "user1" );
        assertTrue( SecurityEncoder.checkPassword( newPassword, updatedUser.getPassword() ) );

    }

    @Test
    @Transactional
    void testLogin () throws Exception {
        final User newUser = new User( "user1", "password", Role.CUSTOMER );
        userService.save( newUser );

        // Test case 1: valid username and password
        mvc.perform( get( "/api/v1/users/user1/password/CUSTOMER" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( "Correct login details!" ) );

        // Test case 2: invalid username
        mvc.perform( get( "/api/v1/users/nonexistentuser/password/CUSTOMER" ) ).andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "User with the username nonexistentuser not found" ) );

        // Test case 3: invalid password
        mvc.perform( get( "/api/v1/users/user1/notpassword/CUSTOMER" ) ).andExpect( status().isConflict() )
                .andExpect( jsonPath( "$.message" ).value( "Invalid LogIn Credentials" ) );

        // Test case 3: invalid role
        mvc.perform( get( "/api/v1/users/user1/notpassword/STAFF" ) ).andExpect( status().isConflict() )
                .andExpect( jsonPath( "$.message" ).value( "Invalid LogIn Credentials" ) );
    }

}
