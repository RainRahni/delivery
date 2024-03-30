package com.example.delivery.service;

import com.example.delivery.model.Station;
import com.example.delivery.model.Weather;
import com.example.delivery.model.WeatherData;
import com.example.delivery.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataServiceImpl implements WeatherDataService {
    private final WeatherDataRepository weatherDataRepository;
    private static final String WEATHER_DATA_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";


    @Override
    @Scheduled(cron = "${weather.cron.expression:0 15 * * * *}")
    public void requestWeatherData() throws IOException, SAXException, ParserConfigurationException {
        log.info("Scheduled method triggered");
        URL url = new URL(WEATHER_DATA_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        InputStream inputStream = connection.getInputStream();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(inputStream);

        saveWeatherData(document);
        inputStream.close();
        connection.disconnect();
    }
    @Override
    public void saveWeatherData(Document document) {
        String timestamp = document.getDocumentElement().getAttribute("timestamp");
        Date date = new Date(Long.parseLong(timestamp) * 1000L);
        NodeList stationNodes = document.getElementsByTagName("station");
        for (int i = 0; i < stationNodes.getLength(); i++) {
            Element stationElement = (Element) stationNodes.item(i);
            String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();
            if (stationName.equals("Tallinn-Harku") || stationName.equals("Tartu-Tõravere")
                    || stationName.equals("Pärnu")) {
                BigDecimal airTemperature = new BigDecimal(stationElement.getElementsByTagName("airtemperature")
                        .item(0).getTextContent());
                String wmoCode = stationElement.getElementsByTagName("wmocode")
                        .item(0).getTextContent();
                BigDecimal windSpeed = new BigDecimal(stationElement.getElementsByTagName("windspeed")
                        .item(0).getTextContent());
                String phenomenon = stationElement.getElementsByTagName("phenomenon")
                        .item(0).getTextContent();
                Station station = new Station(wmoCode, stationName);
                Weather weather = new Weather(airTemperature, windSpeed, phenomenon, date);
                WeatherData observation = WeatherData.builder().weather(weather).station(station).build();
                weatherDataRepository.save(observation);
            }
        }
    }
    public Weather getLatestWeatherReport(String city) {
        WeatherData latest = weatherDataRepository.findLatestByCity(city);
        return latest.getWeather();
    }

}
