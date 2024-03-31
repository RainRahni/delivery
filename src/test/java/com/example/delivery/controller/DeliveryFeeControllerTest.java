package com.example.delivery.controller;

import com.example.delivery.model.type.City;
import com.example.delivery.model.type.Vehicle;
import com.example.delivery.service.DeliveryFreeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryFeeController.class)
class DeliveryFeeControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private DeliveryFreeServiceImpl deliveryFreeService;
    private City city;
    private Vehicle vehicle;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        city = City.TALLINN;
        vehicle = Vehicle.CAR;

    }
    @Test
    void Given_calculatingURI_When_ParamsCorrect_Then_CalculationSuccessful() throws Exception {
        given(deliveryFreeService.getDeliveryFee(city.name(), vehicle.name())).willReturn(BigDecimal.ONE);

        mockMvc.perform(get("/deliveryFee/totalFee/TALLINN/CAR"))
                .andExpect(status().isOk());

        then(deliveryFreeService).should().getDeliveryFee(city.name(), vehicle.name());
    }

}