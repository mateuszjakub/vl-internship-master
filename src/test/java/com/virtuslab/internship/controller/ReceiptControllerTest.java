package com.virtuslab.internship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.virtuslab.internship.basket.Basket;
import com.virtuslab.internship.product.ProductDb;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class ReceiptControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldGenerateReceiptWithBothDiscountsWhenThreeGrainsInBasketAndPriceIsAbove50() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        Basket basket = new Basket();
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        var steak = productDb.getProduct("Steak");
        basket.addProduct(bread);
        basket.addProduct(cereals);
        basket.addProduct(bread);
        basket.addProduct(steak);
        basket.addProduct(steak);

        //when
        mockMvc.perform(
                        post("/receipts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(basket)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(BigDecimal.valueOf(90.27)));
    }

    @Test
    public void shouldGenerateReceiptWithFifteenPercentDiscountWhenThreeGrainsInBasket() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        Basket basket = new Basket();
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        basket.addProduct(bread);
        basket.addProduct(cereals);
        basket.addProduct(bread);

        //when
        mockMvc.perform(
                        post("/receipts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(basket)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(BigDecimal.valueOf(15.3)));
    }

    @Test
    public void shouldGenerateReceiptWithTenPercentDiscountWhenPriceIsAbove50() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        Basket basket = new Basket();
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        var steak = productDb.getProduct("Steak");
        basket.addProduct(bread);
        basket.addProduct(cereals);
        basket.addProduct(steak);

        //when
        mockMvc.perform(
                        post("/receipts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(basket)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(BigDecimal.valueOf(56.7)));
    }

    @Test
    public void shouldGenerateReceiptWithNoDiscountWhenPriceIsBelow50AndNotThreeGrainsInBasket() throws Exception {
        //given
        ObjectMapper objectMapper = new ObjectMapper();
        Basket basket = new Basket();
        var productDb = new ProductDb();
        var bread = productDb.getProduct("Bread");
        var cereals = productDb.getProduct("Cereals");
        basket.addProduct(bread);
        basket.addProduct(cereals);


        //when
        mockMvc.perform(
                        post("/receipts")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(basket)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalPrice").value(BigDecimal.valueOf(13)));
    }
}