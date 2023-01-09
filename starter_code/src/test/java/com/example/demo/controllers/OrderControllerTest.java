package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private UserRepository userRepo=mock(UserRepository.class);;
    private OrderRepository orderRepo=mock(OrderRepository.class);;
    @Before
    public void setUp(){
        orderController =new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepo);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepo);

    }

    @Test
    public void submit_by_username(){
        User user = new User();
        user.setUsername("wafaa");

        List items=new ArrayList();
        Item item = new Item();
        item.setId(1L);
        item.setName("Cup");
        item.setPrice(new BigDecimal(5));
        item.setDescription("Blue cup.....");
        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Pen");
        item2.setPrice(new BigDecimal(3));
        item2.setDescription("Black pen....");

        items.add(item);
        items.add(item2);


        Cart cart = new Cart();
        cart.setId(1l);
        cart.setUser(user);
        cart.setItems(items);
        cart.setTotal(new BigDecimal(8));

        user.setCart(cart);

        when(userRepo.findByUsername("wafaa")).thenReturn(user);
        UserOrder userOrder = UserOrder.createFromCart(user.getCart());
        when(orderRepo.save(userOrder)).thenReturn(userOrder);


        final ResponseEntity response = orderController.submit("wafaa");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new BigDecimal(8), userOrder.getTotal());
    }

    @Test
    public void get_by_user_negative(){
        final ResponseEntity response = orderController.getOrdersForUser("wafaa");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

}
