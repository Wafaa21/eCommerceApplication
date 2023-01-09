package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {
    private ItemController itemController;
    private ItemRepository itemRepo=mock(ItemRepository.class);


    @Before
    public void setUp(){
        itemController =new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepo);

    }

    @Test
    public void find_item_by_id(){
        Item item = new Item();
        item.setId(2L);
        item.setName("Cup");
        item.setPrice(new BigDecimal(5.6));
        item.setDescription("Cup is small");
        when(itemRepo.findById(2L)).thenReturn(Optional.of((item)));
        final ResponseEntity<Item> response = itemController.getItemById(2L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Cup",response.getBody().getName());

    }

    @Test
    public void find_items_by_name(){
        List items=new ArrayList();
        Item item = new Item();
        item.setId(2L);
        item.setName("Cup");
        item.setPrice(new BigDecimal(5.6));
        item.setDescription("Cup is small");
        Item item2 = new Item();
        item2.setId(3L);
        item2.setName("Cup");
        item2.setPrice(new BigDecimal(5.6));
        item2.setDescription("Cup is small");

        items.add(item);
        items.add(item2);

        when(itemRepo.findByName("Cup")).thenReturn(items);
        final ResponseEntity <List<Item>> response = itemController.getItemsByName("Cup");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }


}
