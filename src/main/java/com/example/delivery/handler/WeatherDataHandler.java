package com.example.delivery.handler;

import com.example.delivery.model.Station;
import com.example.delivery.model.Weather;
import com.example.delivery.model.WeatherData;
import com.example.delivery.repository.WeatherDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.math.BigDecimal;
import java.util.Date;
@Slf4j
@RequiredArgsConstructor
@Component
public class WeatherDataHandler extends DefaultHandler {
    private final WeatherDataRepository weatherDataRepository;
    private String tempVal;
    private Station tempStation;
    private Weather tempWeather;
    private Date tempTimeStamp;
    private boolean isValidStation = true;
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        tempVal = "";
        if (qName.equalsIgnoreCase("observations")) {
            String time = attributes.getValue("timestamp");
            tempTimeStamp = new Date(Long.parseLong(time) * 1000L);
        } else if (qName.equalsIgnoreCase("station")) {
            tempStation = new Station();
            tempWeather = new Weather();
        }
    }
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        tempVal = new String(ch, start, length);
    }
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException{
            switch (qName) {
                case "name":
                    isValidStation = tempVal.equals("Tallinn-Harku")
                            || tempVal.equals("Tartu-Tõravere")
                            || tempVal.equals("Pärnu");
                    tempStation.setName(tempVal);
                    break;
                case "wmocode":
                    tempStation.setWmoCode(tempVal);
                    break;
                case "airTemperature":
                    if (tempVal != null && !tempVal.isEmpty()) {
                        tempWeather.setAirTemperature(new BigDecimal(tempVal));
                    } else  {
                        tempWeather.setAirTemperature(null);
                    }
                    break;
                case "windspeed":
                    if (tempVal != null && !tempVal.isEmpty()) {
                        tempWeather.setWindSpeed(new BigDecimal(tempVal));
                    } else  {
                        tempWeather.setWindSpeed(null);
                    }
                    break;
                case "phenomenon":
                    tempWeather.setPhenomenon(tempVal);
                    break;
                case "station":
                    if (isValidStation) {
                        tempWeather.setDate(tempTimeStamp);
                        WeatherData data = WeatherData.builder()
                                .weather(tempWeather)
                                .station(tempStation)
                                .build();
                        weatherDataRepository.save(data);
                    }
        }
    }
}
