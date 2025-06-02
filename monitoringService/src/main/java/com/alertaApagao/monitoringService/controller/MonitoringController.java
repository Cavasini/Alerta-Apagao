package com.alertaApagao.monitoringService.controller;

import com.alertaApagao.monitoringService.service.BlackoutService;
import com.alertaApagao.monitoringService.service.MonitoringService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/monitoring")
public class MonitoringController {

    private final MonitoringService monitoringService;
    private final BlackoutService blackoutService;

    public MonitoringController(MonitoringService monitoringService, BlackoutService blackoutService){
        this.monitoringService = monitoringService;
        this.blackoutService = blackoutService;
    }


    @PostMapping
    public ResponseEntity executeMonitoring(){
        monitoringService.monitorBlackoutRisk();
        return ResponseEntity.ok("Monitoramento executado!");
    }


    @PostMapping("/{locationId}")
    public ResponseEntity executeMonitoringOfZone(@PathVariable String locationId){
        monitoringService.monitoringSingleLocation(locationId);
        return ResponseEntity.ok("Monitoramento executado!");
    }

    @PostMapping("/blackout/{zone}")
    public ResponseEntity registerBlackout(@PathVariable String zone){
        blackoutService.updateStatusofZoneAndSendMessage(zone);
        return ResponseEntity.ok("Apagão reportado!");
    }



}
