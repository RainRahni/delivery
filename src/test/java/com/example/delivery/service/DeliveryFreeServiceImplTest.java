package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Rule;
import com.example.delivery.model.Weather;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
@ExtendWith(MockitoExtension.class)
class DeliveryFreeServiceImplTest {
    @Mock
    private WeatherDataServiceImpl weatherDataService;
    @Mock
    private RuleServiceImpl ruleService;
    @InjectMocks
    private DeliveryFreeServiceImpl deliveryFreeService;
    private Rule ruleAtef;
    private Rule ruleWpef;
    private Rule ruleRBF;

    @BeforeEach
    void setUp() {

        ruleAtef = Rule.builder()
                .feeType(FeeType.ATEF)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .lowerBound(BigDecimal.valueOf(-10))
                .upperBound(BigDecimal.ZERO)
                .fee(BigDecimal.valueOf(0.5))
                .build();

        ruleWpef = Rule.builder()
                .feeType(FeeType.WPEF)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .phenomenon("snow")
                .fee(BigDecimal.ONE)
                .build();

        ruleRBF = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .fee(BigDecimal.valueOf(2.5))
                .phenomenon("snow")
                .build();
    }
    @Test
    void Given_CityAndVehicle_When_ParametersWrong_Then_ThrowError() {
        String city = "PAIDE";
        String vehicleType = "BOAT";
        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));
    }
    @Test
    void Given_CityAndVehicle_When_AllExtraFeesApply_Then_CalculationSuccessful() {
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .airTemperature(BigDecimal.valueOf(-2.1))
                .windSpeed(BigDecimal.valueOf(4.7))
                .phenomenon("Light snow shower")
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(ruleRBF);
        given(ruleService.readRuleWithParams(FeeType.ATEF, City.TARTU, Vehicle.BIKE, report.getAirTemperature(),null))
                .willReturn(ruleAtef);
        given(ruleService.readRuleWithParams(FeeType.WSEF, City.TARTU, Vehicle.BIKE, report.getWindSpeed(),null))
                .willReturn(null);
        given(ruleService.readRuleWithParams(FeeType.WPEF, City.TARTU, Vehicle.BIKE, null,"Light snow shower"))
                .willReturn(ruleWpef);

        BigDecimal actual = deliveryFreeService.getDeliveryFee(city, vehicleType);

        BigDecimal expected = BigDecimal.valueOf(4.0);
        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyAtefApplies_Then_CalculationSuccessful() {
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .airTemperature(BigDecimal.valueOf(-11))
                .build();
        Rule onlyAtef = Rule.builder()
                .feeType(FeeType.ATEF)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .lowerBound(BigDecimal.valueOf(-10))
                .fee(BigDecimal.valueOf(1))
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(ruleRBF);
        given(ruleService.readRuleWithParams(FeeType.ATEF, City.TARTU, Vehicle.BIKE,
                report.getAirTemperature(),null))
                .willReturn(onlyAtef);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = BigDecimal.valueOf(3.5);

        assertEquals(expected, actual);
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWsefApplies_Then_CalculationSuccessful() {
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .windSpeed(BigDecimal.valueOf(30))
                .build();

        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(ruleRBF);

        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));
    }

    @Test
    void Given_CityAndVehicle_When_OnlyWpefApplies_Then_CalculationSuccessful() {
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .phenomenon("snow")
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(ruleRBF);
        given(ruleService.readRuleWithParams(FeeType.ATEF, City.TARTU, Vehicle.BIKE, null,null))
                .willReturn(null);
        given(ruleService.readRuleWithParams(FeeType.WPEF, City.TARTU, Vehicle.BIKE, null,"snow"))
                .willReturn(ruleWpef);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = BigDecimal.valueOf(3.5);

        assertEquals(expected, actual);
    }
    @Test
    void Given_CityAndVehicle_When_OnlyRain_Then_CalculationSuccessful() {
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather report = Weather.builder()
                .phenomenon("rain")
                .build();
        Rule rainWpef = Rule.builder()
                .feeType(FeeType.WPEF)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .phenomenon("rain")
                .fee(BigDecimal.valueOf(0.5))
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(ruleRBF);
        given(ruleService.readRuleWithParams(FeeType.ATEF, City.TARTU, Vehicle.BIKE, null,null))
                .willReturn(null);
        given(ruleService.readRuleWithParams(FeeType.WPEF, City.TARTU, Vehicle.BIKE, null,"rain"))
                .willReturn(rainWpef);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = BigDecimal.valueOf(3.0);

        assertEquals(expected, actual);
    }
    @Test
    void Given_CityAndVehicle_When_GlazeHailThunder_Then_CalculationSuccessful() {
        String city = "TARTU";
        String vehicleType = "BIKE";
        Weather reportGlaze = Weather.builder()
                .phenomenon("glaze")
                .build();
        Weather reportHail = Weather.builder()
                .phenomenon("hail")
                .build();
        Weather reportThunder = Weather.builder()
                .phenomenon("thunder")
                .build();

        given(weatherDataService.getLatestWeatherReport(city)).willReturn(reportGlaze);
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(ruleRBF);
        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));

        given(weatherDataService.getLatestWeatherReport(city)).willReturn(reportHail);
        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));

        given(weatherDataService.getLatestWeatherReport(city)).willReturn(reportThunder);
        assertThrows(BadRequestException.class, () -> deliveryFreeService.getDeliveryFee(city, vehicleType));
    }

    @Test
    void Given_TallinnCar_When_OnlyRBF_Then_CalculationSuccessful() {
        Rule tallinnCar = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TALLINN)
                .vehicle(Vehicle.CAR)
                .fee(BigDecimal.valueOf(4))
                .build();
        String city = "TALLINN";
        String vehicleType = "CAR";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TALLINN, Vehicle.CAR, null, null))
                .willReturn(tallinnCar);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = BigDecimal.valueOf(4);

        assertEquals(expected, actual);
    }
    @Test
    void Given_TallinnScooter_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(3.5);
        Rule tallinnScooter = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TALLINN)
                .vehicle(Vehicle.SCOOTER)
                .fee(fee)
                .build();
        String city = "TALLINN";
        String vehicleType = "SCOOTER";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TALLINN, Vehicle.SCOOTER, null, null))
                .willReturn(tallinnScooter);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_TallinnBike_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(3);
        Rule tallinnBike = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TALLINN)
                .vehicle(Vehicle.BIKE)
                .fee(fee)
                .build();
        String city = "TALLINN";
        String vehicleType = "BIKE";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TALLINN, Vehicle.BIKE, null, null))
                .willReturn(tallinnBike);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_TartuCar_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(3.5);
        Rule tartuCar = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TARTU)
                .vehicle(Vehicle.CAR)
                .fee(fee)
                .build();
        String city = "TARTU";
        String vehicleType = "CAR";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.CAR, null, null))
                .willReturn(tartuCar);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_TartuScooter_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(3);
        Rule tartuScooter = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TARTU)
                .vehicle(Vehicle.SCOOTER)
                .fee(fee)
                .build();
        String city = "TARTU";
        String vehicleType = "SCOOTER";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.SCOOTER, null, null))
                .willReturn(tartuScooter);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_TartuBike_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(2.5);
        Rule tartuBike = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .fee(fee)
                .build();
        String city = "TARTU";
        String vehicleType = "BIKE";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, null, null))
                .willReturn(tartuBike);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_ParnuCar_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(3);
        Rule parnuCar = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.PÄRNU)
                .vehicle(Vehicle.CAR)
                .fee(fee)
                .build();
        String city = "PÄRNU";
        String vehicleType = "CAR";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.PÄRNU, Vehicle.CAR, null, null))
                .willReturn(parnuCar);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_ParnuScooter_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(2.5);
        Rule parnuScooter = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.PÄRNU)
                .vehicle(Vehicle.SCOOTER)
                .fee(fee)
                .build();
        String city = "PÄRNU";
        String vehicleType = "SCOOTER";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.PÄRNU, Vehicle.SCOOTER, null, null))
                .willReturn(parnuScooter);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
    @Test
    void Given_ParnuBike_When_OnlyRBF_Then_CalculationSuccessful() {
        BigDecimal fee = BigDecimal.valueOf(2);
        Rule parnuBike = Rule.builder()
                .feeType(FeeType.RBF)
                .city(City.PÄRNU)
                .vehicle(Vehicle.BIKE)
                .fee(fee)
                .build();
        String city = "PÄRNU";
        String vehicleType = "BIKE";
        given(ruleService.readRuleWithParams(FeeType.RBF, City.PÄRNU, Vehicle.BIKE, null, null))
                .willReturn(parnuBike);
        Weather report = Weather.builder()
                .build();
        given(weatherDataService.getLatestWeatherReport(city)).willReturn(report);

        var actual = deliveryFreeService.getDeliveryFee(city, vehicleType);
        BigDecimal expected = fee;

        assertEquals(expected, actual);
    }
}
