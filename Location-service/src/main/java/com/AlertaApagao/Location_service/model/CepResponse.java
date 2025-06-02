package com.AlertaApagao.Location_service.model;

import lombok.Data;

@Data
public class CepResponse {
    private String cep;
    private String address_type;
    private String address_name;
    private String address;
    private String state;
    private String district;
    private String lat;
    private String lng;
    private String city;
    private String ddd;
}
