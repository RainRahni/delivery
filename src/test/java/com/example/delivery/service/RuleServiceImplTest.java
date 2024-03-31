package com.example.delivery.service;

import com.example.delivery.exception.BadRequestException;
import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import com.example.delivery.repository.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class RuleServiceImplTest {
    @Mock
    private RuleRepository ruleRepository;
    @InjectMocks
    private RuleServiceImpl ruleService;
    private Rule ruleOne;
    private Rule ruleTwo;
    private final Long ruleId = 1L;
    @Test
    void Given_NeededVariables_When_CreatingRule_Then_CreatingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .fee(BigDecimal.valueOf(2.5))
                .vehicle(Vehicle.BIKE)
                .feeType(FeeType.RBF)
                .build();

        ruleService.createRule(ruleOne);

        then(ruleRepository).should().save(ruleOne);
    }
    @Test
    void Given_NoVariables_When_CreatingRule_Then_ThrowError() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .build();

        assertThrows(BadRequestException.class, () -> ruleService.createRule(ruleOne));
    }
    @Test
    void Given_CorrectId_When_UpdatingRule_Then_UpdateSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .city(City.TALLINN)
                .build();
        ruleTwo = Rule.builder()
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .city(City.TARTU)
                .build();

        given(ruleRepository.findById(ruleId)).willReturn(Optional.of(ruleOne));

        ruleService.updateRule(ruleId, ruleTwo);

        var actual = ruleOne.getCity();
        City expected = City.TARTU;

        then(ruleRepository).should().save(ruleOne);

        assertEquals(expected, actual);
    }
    @Test
    void Given_CorrectId_When_DeletingRule_Then_DeletionSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .build();

        given(ruleRepository.findById(ruleId)).willReturn(Optional.of(ruleOne));

        ruleService.deleteRule(ruleId);

        then(ruleRepository).should().delete(ruleOne);
    }
    @Test
    void Given_InCorrectId_When_DeletingRule_Then_ThrowsError() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .build();
        assertThrows(BadRequestException.class, () -> ruleService.deleteRule(2L));
    }
    @Test
    void Given_CorrectId_When_ReadingRule_Then_ReadingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .build();
        given(ruleRepository.findById(ruleId)).willReturn(Optional.of(ruleOne));

        var actual = ruleService.readRule(ruleId);
        Rule expected = ruleOne;

        assertEquals(expected, actual);
    }
    @Test
    void Given_AllParams_When_ReadingRule_Then_ReadingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .lowerBound(BigDecimal.ZERO)
                .upperBound(BigDecimal.TEN)
                .phenomenon("snow")
                .build();
        BigDecimal value = BigDecimal.ONE;
        given(ruleRepository.findRule(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, "snow"))
                .willReturn(ruleOne);

        var actual = ruleService
                .readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, "snow");
        Rule expected = ruleOne;

        then(ruleRepository)
                .should()
                .findRule(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, "snow");
        assertEquals(expected, actual);
    }
    @Test
    void Given_NoPhenomenon_When_ReadingRule_Then_ReadingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .lowerBound(BigDecimal.ZERO)
                .upperBound(BigDecimal.TEN)
                .build();
        BigDecimal value = BigDecimal.ONE;
        given(ruleRepository.findRule(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, null))
                .willReturn(ruleOne);

        var actual = ruleService
                .readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, null);
        Rule expected = ruleOne;

        then(ruleRepository)
                .should()
                .findRule(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, null);
        assertEquals(expected, actual);
    }
    @Test
    void Given_NoBounds_When_ReadingRule_Then_ReadingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .city(City.TARTU)
                .vehicle(Vehicle.BIKE)
                .build();
        BigDecimal value = BigDecimal.ONE;
        given(ruleRepository.findRule(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, null))
                .willReturn(ruleOne);

        var actual = ruleService
                .readRuleWithParams(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, null);
        Rule expected = ruleOne;

        then(ruleRepository)
                .should()
                .findRule(FeeType.RBF, City.TARTU, Vehicle.BIKE, value, null);
        assertEquals(expected, actual);
    }
    @Test
    void Given_NoCity_When_ReadingRule_Then_ReadingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .vehicle(Vehicle.BIKE)
                .build();
        BigDecimal value = BigDecimal.ONE;
        given(ruleRepository.findRule(FeeType.RBF, null, Vehicle.BIKE, value, null))
                .willReturn(ruleOne);

        var actual = ruleService
                .readRuleWithParams(FeeType.RBF, null, Vehicle.BIKE, value, null);
        Rule expected = ruleOne;

        then(ruleRepository)
                .should()
                .findRule(FeeType.RBF, null, Vehicle.BIKE, value, null);
        assertEquals(expected, actual);
    }
    @Test
    void Given_NoVehicle_When_ReadingRule_Then_ReadingSuccessful() {
        ruleOne = Rule.builder()
                .id(ruleId)
                .feeType(FeeType.RBF)
                .fee(BigDecimal.ONE)
                .build();
        BigDecimal value = BigDecimal.ONE;
        given(ruleRepository.findRule(FeeType.RBF, null, null, value, null))
                .willReturn(ruleOne);

        var actual = ruleService
                .readRuleWithParams(FeeType.RBF, null, null, value, null);
        Rule expected = ruleOne;

        then(ruleRepository)
                .should()
                .findRule(FeeType.RBF, null, null, value, null);
        assertEquals(expected, actual);
    }
}