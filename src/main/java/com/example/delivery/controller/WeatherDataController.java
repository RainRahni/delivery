package com.example.delivery.controller;

import com.example.delivery.service.WeatherDataServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@RestController
@RequestMapping("weather")
@RequiredArgsConstructor
public class WeatherDataController {
    private final WeatherDataServiceImpl weatherService;
    @GetMapping("/DATA")
    public void getData() throws IOException, ParserConfigurationException, SAXException {
        weatherService.requestWeatherData();
    }
}
