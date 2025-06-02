package com.alertaApagao.monitoringService.service;

import com.alertaApagao.monitoringService.evaluation.RiskEvaluator;
import com.alertaApagao.monitoringService.model.LocationDTO;
import com.alertaApagao.monitoringService.model.SimulationRequestDTO;
import com.alertaApagao.monitoringService.model.UserDTO;
import com.alertaApagao.monitoringService.model.WeatherResponse;
import com.alertaApagao.monitoringService.stub_classes.AlertService;
import com.alertaApagao.monitoringService.stub_classes.AlertServiceImplementationService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SimulationService {

    private MonitoringService monitoringService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String baseLocationUrl = "http://localhost:8081/locations";
    private final String userServiceUrl = "http://localhost:8082/auth";
    private final AlertService alertService;
    private final BlackoutService blackoutService;

    public SimulationService(MonitoringService monitoringService, BlackoutService blackoutService){
        this.monitoringService = monitoringService;
        this.alertService = new AlertServiceImplementationService().getAlertServiceImplementationPort();
        this.blackoutService = blackoutService;
    }

    public void simulateExtremeWeather(SimulationRequestDTO request){
        LocationDTO location = restTemplate.getForObject(baseLocationUrl + "/" + request.locationId(), LocationDTO.class);
        String riskMessage = monitoringService.checkLocationWeatherAndUpdateStatus(location, true);
        if(riskMessage != null){
            UserDTO user = restTemplate.getForObject(userServiceUrl + "/users/" + location.userId(), UserDTO.class);
            alertService.sendSMS(riskMessage,"+55" + user.phoneNumber());
        }
    }

    public void simulateBlackout(SimulationRequestDTO request){
        LocationDTO location = restTemplate.getForObject(baseLocationUrl + "/" + request.locationId(), LocationDTO.class);
        blackoutService.updateStatusofZoneAndSendMessage(location.zone());
    }

    public String simulatePowerRestoration(SimulationRequestDTO request){
        LocationDTO location = restTemplate.getForObject(baseLocationUrl + "/" + request.locationId(), LocationDTO.class);
        String status = location.status();
        if(status.equals("POWERED_ON")){
            return "Essa Localização já esta com energia!";
        }

        String riskMessage = monitoringService.checkLocationWeatherAndUpdateStatus(location, false);
        UserDTO user = restTemplate.getForObject(userServiceUrl + "/users/" + location.userId(), UserDTO.class);
        String message;
        if(riskMessage != null && status.equals("AT_RISK")){
            message = "⚠️ Risco de Apagão ⚠️\n"
                    + "🌩️ Detectamos condições climáticas que indicam risco de queda de energia.\n"
                    + "📍 Local: " + location.cep() + "\n"
                    + "🔋 Recomendamos se preparar preventivamente.";
        }
        else if(riskMessage != null){
            message = "⚡ Energia Restaurada ⚠️\n"
                    + "A energia voltou em sua localização, mas o risco de nova queda ainda existe.\n"
                    + "📍 Local: " + location.cep() + "\n"
                    + "🔔 Continue atento aos próximos alertas.";
        }
        else if (status.equals("AT_RISK")) {
            message = "✅ Risco Encerrado ✅\n"
                    + "☀️ As condições climáticas voltaram ao normal. Sem risco de apagão no momento.\n"
                    + "📍 Local: " + location.cep() + "\n"
                    + "😌 Tudo está estável por enquanto.";
        }else {
            message = "✅ Energia Restaurada ✅\n"
                    + "⚡ A energia elétrica foi normalizada em uma de suas localizações monitoradas.\n"
                    + "📍 CEP: " + location.cep() + "\n"
                    + "🙏 Obrigado por acompanhar nossos alertas.";
        }

        alertService.sendSMS(message, "+55" + user.phoneNumber());
        return "Simulação Iniciada: Normalização!";
    }


}
