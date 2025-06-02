package com.alertaApagao.monitoringService.service;

import com.alertaApagao.monitoringService.model.LocationDTO;
import com.alertaApagao.monitoringService.model.UserDTO;
import com.alertaApagao.monitoringService.model.ZoneStatusUpdateDTO;
import com.alertaApagao.monitoringService.stub_classes.AlertService;
import com.alertaApagao.monitoringService.stub_classes.AlertServiceImplementationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class BlackoutService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseLocationUrl = "http://localhost:8081/locations";
    private final String userServiceUrl = "http://localhost:8082/auth";
    private final AlertService alertService;

    public BlackoutService(){
        this.alertService = new AlertServiceImplementationService().getAlertServiceImplementationPort();
    }

    public void updateStatusofZoneAndSendMessage(String zone){
        updateZoneStatus(zone, "POWERED_OFF");
        LocationDTO[] locations = restTemplate.getForObject(baseLocationUrl + "/zones/" + zone, LocationDTO[].class);
        for(LocationDTO location : locations){
            UserDTO user = restTemplate.getForObject(userServiceUrl + "/users/" + location.userId(), UserDTO.class);
            alertService.sendSMS(blackoutMessage(location.cep()),"+55" + user.phoneNumber());
        }
    }

    public String blackoutMessage(String cep){
        return "⚫ Alerta de Apagão ⚫\n"
                + "🚫 Queda de energia detectada em uma de suas localizações.\n"
                + "📍 CEP: " + cep + "\n"
                + "📡 Aguardando atualização do status.";
    }

    private void updateZoneStatus(String zone, String status) {
        ZoneStatusUpdateDTO updateDTO = new ZoneStatusUpdateDTO(zone, status);
        restTemplate.put(baseLocationUrl + "/zones/status", updateDTO);
    }
}
