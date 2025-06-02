package com.alertaApagao.monitoringService.service;

import com.alertaApagao.monitoringService.evaluation.RiskEvaluator;
import com.alertaApagao.monitoringService.integration.WeatherApiClient;
import com.alertaApagao.monitoringService.model.UserDTO;
import com.alertaApagao.monitoringService.model.WeatherResponse;
import com.alertaApagao.monitoringService.model.LocationDTO;
import com.alertaApagao.monitoringService.model.ZoneStatusUpdateDTO;
import com.alertaApagao.monitoringService.stub_classes.AlertService;
import com.alertaApagao.monitoringService.stub_classes.AlertServiceImplementationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.HashMap;

@Service
public class MonitoringService {

    private final WeatherApiClient weatherApiClient;
    private final AlertService service;
    private final RestTemplate restTemplate = new RestTemplate();

    private final String baseLocationUrl = "http://localhost:8081/locations";
    private final String userServiceUrl = "http://localhost:8082/auth";

    public MonitoringService(WeatherApiClient weatherApiClient) {
        this.weatherApiClient = weatherApiClient;
        this.service = new AlertServiceImplementationService().getAlertServiceImplementationPort();
    }

    public void monitorBlackoutRisk() {
        LocationDTO[] zones = restTemplate.getForObject(baseLocationUrl + "/zones", LocationDTO[].class);

        for (LocationDTO zone : zones) {
            String riskMessage = checkLocationWeatherAndUpdateStatus(zone, false);

            if (riskMessage != null) {
                LocationDTO[] locationsOfZone = restTemplate.getForObject(baseLocationUrl + "/zones/" + zone.zone(), LocationDTO[].class);

                for (LocationDTO location : locationsOfZone) {
                    UserDTO user = restTemplate.getForObject(userServiceUrl + "/users/" + location.userId(), UserDTO.class);
                    sendSMSAlert(user.phoneNumber(), riskMessage);
                }
            }
        }
    }

    public void monitoringSingleLocation(String id){
        LocationDTO location = restTemplate.getForObject(baseLocationUrl + "/" + id, LocationDTO.class);
        String riskMessage = checkLocationWeatherAndUpdateStatus(location, false);
        if(riskMessage != null){
            UserDTO user = restTemplate.getForObject(userServiceUrl + "/users/" + location.userId(), UserDTO.class);
            sendSMSAlert(user.phoneNumber(), riskMessage);
        }
    }

    public String checkLocationWeatherAndUpdateStatus(LocationDTO location, boolean simulateBadWeather) {
        WeatherResponse weatherResponse = weatherApiClient.getWeather(location.latitude(), location.longitude());
        String riskPossibles = RiskEvaluator.evaluateRisk(weatherResponse, simulateBadWeather, location.cep());

        String newStatus = riskPossibles.matches("✅ Sem risco de apagão detectado.")
                ? "POWERED_ON"
                : "AT_RISK";

        if (!location.status().equalsIgnoreCase(newStatus)) {
            updateZoneStatus(location.zone(), newStatus);
        }

        return newStatus.equals("AT_RISK") ? riskPossibles : null;
    }

    private void updateZoneStatus(String zone, String status) {
        ZoneStatusUpdateDTO updateDTO = new ZoneStatusUpdateDTO(zone, status);
        restTemplate.put(baseLocationUrl + "/zones/status", updateDTO);
    }

    private void sendSMSAlert(String sms, String message){
        sms = "+55" + sms;
        service.sendSMS(message, sms);
    }

}
