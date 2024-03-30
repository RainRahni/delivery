package com.example.delivery.service;

import com.example.delivery.repository.WeatherDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class WeatherDataServiceImplTest {

    @Mock
    private WeatherDataRepository weatherDataRepository;
    @InjectMocks
    private WeatherDataServiceImpl weatherDataService;
    @Test
    public void testRequestWeatherData() {
        assertDoesNotThrow(() -> weatherDataService.requestWeatherData());
    }
    @Test
    public void testSaveWeatherData() {
        assertDoesNotThrow(() -> weatherDataService.requestWeatherData());

    }


}