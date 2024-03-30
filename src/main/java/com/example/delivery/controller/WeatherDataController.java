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

    /**
     * Requests data from Estonia's weather service. Data is
     * in XML.
     * @throws IOException if an I/O error occurs.
     * @throws ParserConfigurationException if a configuration error is encountered during the parsing process.
     * @throws SAXException if a SAX error is encountered during the XML parsing.
     */
    @GetMapping("/data")
    public void getData() throws IOException, ParserConfigurationException, SAXException {
        weatherService.requestWeatherData();
    }
}
