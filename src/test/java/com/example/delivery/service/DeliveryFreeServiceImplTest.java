package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Weather;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class DeliveryFreeServiceImplTest {
    @Mock
    private WeatherDataServiceImpl weatherDataService;
    @InjectMocks
    private DeliveryFreeServiceImpl deliveryFreeService;
    @Test
    void Given_CityAndVehicle_When_ParametersWrong_Then_ThrowError() {
        //given
        String city = "PAIDE";
        String vehicleType = "BOAT";
        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));
    }
    @Test
    void Given_CityAndVehicle_When_AllExtraFeesApply_Then_CalculationSuccessful() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .airTemperature(-2.1)
                .windSpeed(4.7)
                .phenomenon("Light snow shower")
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 4;
        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyAtefFeeAppliesAndTempBetweenMinusTenAndZero_Then_CalculationSuccessful() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .airTemperature(-2.1)
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.0;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyAtefFeeAppliesAndTempBelowMinusTen_Then_CalculationSuccessful() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .airTemperature(-20)
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.5;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyAtefFeeAppliesAndWrongVehicleType_Then_CalculationSuccessful() {
        //given
        String city = "TARTU";
        String vehicleType = "CAR";
        Weather report = Weather.builder()
                .airTemperature(-2.1)
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.5;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWsefFeeAppliesWindBetweenTenAndTwenty_Then_CalculationSuccessful() {
        //given
        String city = "TALLINN";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .windSpeed(15)
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.5;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWsefFeeAppliesWindGreaterThanTwenty_Then_ThrowError() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .windSpeed(30)
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWpefFeeAppliesPhenomenonSnow_Then_CalculationSuccessful() {
        //given
        String city = "PÄRNU";
        String vehicleType = "SCOOTER";
        Weather report = Weather.builder()
                .phenomenon("Snowfall")
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.5;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWpefFeeAppliesPhenomenonSleet_Then_CalculationSuccessful() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .phenomenon("Moderate Sleet")
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.5;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWpefFeeAppliesPhenomenonRain_Then_CalculationSuccessful() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .phenomenon("Heavy Rain")
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        //when
        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        double expected = 3.0;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWpefFeeAppliesPhenomenonGlazeHailThunder_Then_ThrowError() {
        //given
        String city = "TARTU";
        String vehicleType = "BIKE";
        String[] phenomenons = new String[]{"Glaze", "Hail", "Thunder"};
        for (String phenom: phenomenons) {
            Weather report = Weather.builder()
                    .phenomenon(phenom)
                    .build();
            given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
            assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));
        }

    }
}
