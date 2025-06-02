package com.AlertaApagao.Location_service.integration;

import com.AlertaApagao.Location_service.model.CepResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CepApiClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public CepResponse requestExternalAPI(String cep){
        String url = "https://cep.awesomeapi.com.br/json/" + cep;
        return restTemplate.getForObject(url, CepResponse.class);
    }
}
