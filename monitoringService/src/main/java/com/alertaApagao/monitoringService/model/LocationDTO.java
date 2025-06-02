package com.alertaApagao.monitoringService.model;

public record LocationDTO(String id, String userId, double latitude, double longitude, String state,
                      String city, String district, String cep, String zone, String status, String updatedAt) {
}
