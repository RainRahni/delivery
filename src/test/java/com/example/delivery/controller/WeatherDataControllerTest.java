package com.example.delivery.controller;

import com.example.delivery.service.WeatherDataServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherDataController.class)
class WeatherDataControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private WeatherDataServiceImpl weatherDataService;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Test
    void Given_Nothing_When_RequestingData_Then_RequestingDataSuccessful() throws Exception {
        doNothing().when(weatherDataService).requestWeatherData();

        mockMvc.perform(get("/weather/data"))
                .andExpect(status().isOk());

        then(weatherDataService).should().requestWeatherData();
    }
}