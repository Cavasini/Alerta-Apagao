package com.alertaApagao.monitoringService.scheduler;

import com.alertaApagao.monitoringService.service.MonitoringService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BlackoutRiskScheduler {

    private final MonitoringService service;

    public BlackoutRiskScheduler(MonitoringService service){
        this.service = service;
    }

    @Scheduled(fixedRate = 3600000) // Executa a cada 1 hora (em milissegundos)
    public void checkBlackoutRiskHourly() {
//        service.monitorBlackoutRisk();
    }
}
