package com.example.delivery.controller;

import com.example.delivery.model.Rule;
import com.example.delivery.model.type.City;
import com.example.delivery.model.type.FeeType;
import com.example.delivery.model.type.Vehicle;
import com.example.delivery.service.RuleServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RuleController.class)

class RuleControllerTest {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private RuleServiceImpl ruleService;
    private ObjectMapper objectMapper;
    private Rule ruleOne;
    private Rule ruleTwo;
    @BeforeEach
    public void setup() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        ruleOne = new Rule();
        ruleTwo = Rule.builder()
                .id(1L)
                .build();
        objectMapper = new ObjectMapper();
    }
    @Test
    void Given_createURI_When_RuleCorrect_Then_CreatingSuccessful() throws Exception {
        doNothing().when(ruleService).createRule(any(Rule.class));

        mockMvc.perform(post("/rules/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ruleOne)))
                .andExpect(status().isOk());

        then(ruleService).should().createRule(any(Rule.class));
    }
    @Test
    void Given_readURI_When_IdCorrect_Then_ReadingSuccessful() throws Exception {
        given(ruleService.readRule(1L)).willReturn(ruleTwo);

        mockMvc.perform(get("/rules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleTwo)))
                .andExpect(status().isOk());

        then(ruleService).should().readRule(1L);
    }
    @Test
    void Given_updateURI_When_IdCorrect_Then_UpdatingSuccessful() throws Exception {
        doNothing().when(ruleService).updateRule(1L, ruleTwo);

        mockMvc.perform(put("/rules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleTwo)))
                .andExpect(status().isOk());
    }
    @Test
    void Given_deleteURI_When_IdCorrect_Then_DeletingSuccessful() throws Exception {
        doNothing().when(ruleService).deleteRule(1L);

        mockMvc.perform(delete("/rules/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ruleTwo)))
                .andExpect(status().isOk());
        then(ruleService).should().deleteRule(1L);
    }
}