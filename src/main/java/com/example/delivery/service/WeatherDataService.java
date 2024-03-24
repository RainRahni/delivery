package com.example.delivery.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Service
public interface WeatherDataService {
    void requestWeatherData() throws IOException, SAXException, ParserConfigurationException;
    void saveWeatherData(Document document);
}
