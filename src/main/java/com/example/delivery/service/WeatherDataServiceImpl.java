package com.example.delivery.service;

import com.example.delivery.handler.WeatherDataHandler;
import com.example.delivery.model.Weather;
import com.example.delivery.model.WeatherData;
import com.example.delivery.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherDataServiceImpl implements WeatherDataService {
    private final WeatherDataRepository weatherDataRepository;
    private static final String WEATHER_DATA_URL
            = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    /**
     *
     * @throws IOException if an input or output exception occurred.
     * @throws SAXException if any parse errors occur.
     * @throws ParserConfigurationException if a configuration error occurred.
     */
    @Override
    @Scheduled(cron = "${weather.cron.expression:0 15 * * * *}")
    public void requestWeatherData() throws IOException, SAXException, ParserConfigurationException {
        log.info("Scheduled method triggered");
        URL url = new URL(WEATHER_DATA_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        WeatherDataHandler weatherDataHandler = new WeatherDataHandler(weatherDataRepository);

        saxParser.parse(new InputSource(url.openStream()), weatherDataHandler);
    }
    /**
     * Get latest weather report in the given city.
     * @param city of which weather report to get.
     * @return latest weather report.
     */
    public Weather getLatestWeatherReport(String city) {
        WeatherData latest = weatherDataRepository.findLatestByCity(city);
        return latest.getWeather();
    }
}
