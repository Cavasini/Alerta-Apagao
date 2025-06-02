package com.alertaApagao.monitoringService.model;

import lombok.Data;

@Data
public class WeatherResponse {

    private Location location;
    private Current current;

    @Data
    public static class Location {
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        private String localtime;
    }

    @Data
    public static class Current {
        private Condition condition;   // Estado atual (ex: "Heavy rain")
        private double wind_kph;       // Velocidade do vento
        private double gust_kph;       // Rajadas de vento
        private double precip_mm;      // Quantidade de chuva
        private double humidity;       // Umidade relativa
        private double vis_km;         // Visibilidade
        private double cloud;          // Cobertura de nuvens (em %)
        private double temp_c;         // Temperatura
    }

    @Data
    public static class Condition {
        private String text;           // Descrição textual (ex: "Thunderstorm")
        private int code;              // Código do tempo (pode ser usado para mapear condições críticas)
    }
}
