package com.alertaApagao.monitoringService.integration;

import com.alertaApagao.monitoringService.model.WeatherResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WeatherApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public WeatherResponse getWeather(double lat, double lon){
        String url = "https://api.weatherapi.com/v1/current.json?q=" + lat + " " + lon + "&key=ee071585ce4840c08ba213958251106";
        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, WeatherResponse.class);

    }
}
