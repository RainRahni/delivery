package com.example.delivery.service;

import com.example.delivery.repository.WeatherDataRepository;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static org.mockito.BDDMockito.then;

class WeatherDataServiceImplTest {

    @InjectMocks
    private WeatherDataServiceImpl weatherDataService;

    @Mock
    private WeatherDataRepository weatherDataRepository;

    private WireMockServer wireMockServer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterEach
    public void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void requestWeatherDataTest() throws IOException, ParserConfigurationException, SAXException {
        String xmlResponse = new String(Files.readAllBytes(Paths.get("src/test/resources/mockResponse.xml")));

        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/ilma_andmed/xml/observations.php"))
                .willReturn(aResponse().withBody(xmlResponse)));

        weatherDataService.requestWeatherData();

        then(weatherDataRepository).should().save(ArgumentMatchers.any());
    }

}
