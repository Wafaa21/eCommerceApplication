package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepo=mock(UserRepository.class);
    private CartRepository cartRepo=mock(CartRepository.class);
    private ItemRepository itemRepo=mock(ItemRepository.class);

    @Before
    public void setUp(){
        cartController =new CartController();
        TestUtils.injectObjects(cartController,"userRepository",userRepo);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepo);
        TestUtils.injectObjects(cartController,"itemRepository",itemRepo);

    }

    @Test
    public void add_to_cart(){
        User user = new User();
        user.setUsername("wafaa");

        Cart cart = new Cart();
        cart.setId(1l);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepo.findByUsername("wafaa")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("Pen");
        item.setPrice(new BigDecimal(10));
        item.setDescription("Blue Pen.....");

        when(itemRepo.findById(1l)).thenReturn(java.util.Optional.of(item));
        when(cartRepo.save(cart)).thenReturn(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1l);
        modifyCartRequest.setUsername("wafaa");

        final ResponseEntity response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new BigDecimal(10), ((Cart) response.getBody()).getTotal());
    }

    @Test
    public void remove_from_cart(){
        User user = new User();
        user.setUsername("wafaa");

        Cart cart = new Cart();
        cart.setId(1l);
        cart.setUser(user);

        user.setCart(cart);

        when(userRepo.findByUsername("wafaa")).thenReturn(user);

        Item fakeItem = new Item();
        fakeItem.setId(1L);
        fakeItem.setName("Pen");
        fakeItem.setPrice(new BigDecimal(10));
        fakeItem.setDescription("Blue Pen.....");

        when(itemRepo.findById(1l)).thenReturn(java.util.Optional.of(fakeItem));
        when(cartRepo.save(cart)).thenReturn(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1l);
        modifyCartRequest.setUsername("wafaa");

        final ResponseEntity response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new BigDecimal(-10), ((Cart) response.getBody()).getTotal());
    }
}

