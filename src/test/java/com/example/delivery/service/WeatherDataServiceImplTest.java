package com.example.delivery.service;

import com.example.delivery.model.Station;
import com.example.delivery.model.Weather;
import com.example.delivery.model.WeatherData;
import com.example.delivery.repository.WeatherDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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

    @Test
    public void testGetLatestWeatherReport() {
        Weather weather = Weather.builder().airTemperature(2).build();
        String city = "Pärnu";
        Station station = Station.builder().name(city).build();
        WeatherData data = WeatherData.builder().weather(weather).station(station).build();
        given(weatherDataRepository.findLatestByCity(city)).willReturn(data);

        var actual = weatherDataService.getLatestWeatherReport(city);
        double expected = 2;

        assertEquals(expected, actual.getAirTemperature());
    }
}