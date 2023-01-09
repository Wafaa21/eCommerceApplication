package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
//import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo=mock(UserRepository.class);

    private CartRepository cartRepo=mock(CartRepository.class);
    private BCryptPasswordEncoder encoder=mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp(){
        userController =new UserController();
        TestUtils.injectObjects(userController,"userRepository",userRepo);
        TestUtils.injectObjects(userController,"cartRepository",cartRepo);
        TestUtils.injectObjects(userController,"bCryptPasswordEncoder",encoder);

    }

    @Test
    public void create_user_happy_path() throws Exception{
        when(encoder.encode("123098test")).thenReturn("thisIsHashed");
        CreateUserRequest r=new CreateUserRequest();
        r.setUsername("wafaa");
        r.setPassword("123098test");
        r.setConfirmPassword("123098test");

        final ResponseEntity<User> response =userController.createUser(r);
        assertNotNull(response);
        assertEquals(200,response.getStatusCodeValue());

        User u =response.getBody();
       assertNotNull(u);
        assertEquals(0,u.getId());
        assertEquals("wafaa",u.getUsername());
        assertEquals("thisIsHashed",u.getPassword());

    }

    @Test
    public void find_user_by_username(){

        User user = new User();
        user.setUsername("wafaa");
        when(userRepo.findByUsername("wafaa")).thenReturn(user);

        final ResponseEntity<User> response = userController.findByUserName("wafaa");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("wafaa",response.getBody().getUsername());


    }

    @Test
    public void find_user_by_username_error(){
        final ResponseEntity<User> response = userController.findByUserName("Norah");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void find_user_by_id(){
        User user = new User();
        user.setId(0L);
        user.setUsername("wafaa");
        when(userRepo.findById(0L)).thenReturn(Optional.of(user));

        final ResponseEntity<User> response = userController.findById(0L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0L,response.getBody().getId());
    }
    @Test
    public void find_user_by_id_error(){
        final ResponseEntity<User> response = userController.findById(1L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }




}
