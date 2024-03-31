package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Rule;
import com.example.delivery.model.Weather;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class DeliveryFreeServiceImpl implements DeliveryFeeService {
    private final WeatherDataServiceImpl weatherDataService;
    private final RuleServiceImpl ruleService;
    private static final String INVALID_INPUT = "Invalid input!";
    private static final String VEHICLE_USAGE_FORBIDDEN = "Usage of selected vehicle type is forbidden!";
    private static final List<String> VALID_CITIES = List.of(
            City.TALLINN.name(),
            City.TARTU.name(),
            City.PÄRNU.name());
    private static final List<String> VALID_VEHICLES = List.of(
            Vehicle.CAR.name(),
            Vehicle.SCOOTER.name(),
            Vehicle.BIKE.name());
    private static final List<String> FORBIDDEN_PHENOMENONS = List.of(
            "GLAZE",
            "HAIL",
            "THUNDER"
    );
    /**
     * Calculate total delivery fee by adding
     * regional base fee and extra fee on business rules.
     * @param city which city.
     * @param vehicle which type is the vehicle.
     * @return total delivery fee.
     */
    public BigDecimal getDeliveryFee(String city, String vehicle) {
        validateParameters(city, vehicle);
        City givenCity = City.valueOf(city.toUpperCase());
        Vehicle givenVehicle = Vehicle.valueOf(vehicle.toUpperCase());

        BigDecimal regionalBaseFee = calculateRegionalBaseFee(givenCity, givenVehicle);
        BigDecimal extraFee = calculateExtraFee(city, givenVehicle);
        return regionalBaseFee.add(extraFee);
    }
    private void validateParameters(String city, String vehicle) {
        if (!VALID_CITIES.contains((city.toUpperCase()))
                || !VALID_VEHICLES.contains(vehicle.toUpperCase())) {
            throw new BadRequestException(INVALID_INPUT);
        }
    }
    private BigDecimal calculateRegionalBaseFee(City city, Vehicle vehicle) {
        Rule specificRule = ruleService
                .readRuleWithParams(FeeType.RBF, city, vehicle, null, null);
        return specificRule.getFee();
    }
    private BigDecimal calculateExtraFee(String city, Vehicle vehicle) {
        Weather weather = weatherDataService.getLatestWeatherReport(city);
        City givenCity = City.valueOf(city);
        BigDecimal atef = getAtef(weather.getAirTemperature(), givenCity, vehicle);
        BigDecimal wsef = getWsef(weather.getWindSpeed(), givenCity, vehicle);
        BigDecimal wpef = getWpef(weather.getPhenomenon(), givenCity, vehicle);
        return atef.add(wpef).add(wsef);
    }
    private BigDecimal getAtef(BigDecimal airTemp, City city, Vehicle vehicle) {
        Rule rule = ruleService
                .readRuleWithParams(FeeType.ATEF, city, vehicle, airTemp, null);
        return rule == null ? BigDecimal.ZERO : rule.getFee();
    }
    private BigDecimal getWsef(BigDecimal windSpeed, City city, Vehicle vehicle) {
        if (windSpeed == null) {
            return BigDecimal.ZERO;
        }
        if (windSpeed.compareTo(BigDecimal.valueOf(20)) > 0) {
            throw new BadRequestException(VEHICLE_USAGE_FORBIDDEN);
        }
        Rule specificRule = ruleService
                .readRuleWithParams(FeeType.WSEF, city, vehicle, windSpeed, null);
        return specificRule == null ? BigDecimal.ZERO : specificRule.getFee();
    }
    private BigDecimal getWpef(String phenomenon, City city, Vehicle vehicle) {
        if (phenomenon == null || phenomenon.isEmpty()) {
            return BigDecimal.ZERO;
        }
        if (FORBIDDEN_PHENOMENONS.contains(phenomenon.toUpperCase())) {
            throw new BadRequestException(VEHICLE_USAGE_FORBIDDEN);
        }
        Rule rule = ruleService.readRuleWithParams(FeeType.WPEF, city, vehicle, null, phenomenon);
        return rule == null ? BigDecimal.ZERO : rule.getFee();
    }
}
