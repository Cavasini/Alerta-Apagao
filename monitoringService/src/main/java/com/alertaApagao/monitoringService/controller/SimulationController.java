package com.alertaApagao.monitoringService.controller;

import com.alertaApagao.monitoringService.model.SimulationRequestDTO;
import com.alertaApagao.monitoringService.service.SimulationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private SimulationService service;

    public SimulationController(SimulationService service){
        this.service = service;
    }

    @PostMapping("/climaExtremo")
    public ResponseEntity simulateExtremeWeather(@RequestBody SimulationRequestDTO request){
        service.simulateExtremeWeather(request);
        return ResponseEntity.ok("Simulação Iniciada: Clima Extremo!");
    }

    @PostMapping("/apagao")
    public ResponseEntity simulateBlackout(@RequestBody SimulationRequestDTO request){
        service.simulateBlackout(request);
        return ResponseEntity.ok("Simulação Iniciada: Apagão!");
    }

    @PostMapping("/normalizacao")
    public ResponseEntity simulatePowerRestoration(@RequestBody SimulationRequestDTO request){
        String result = service.simulatePowerRestoration(request);
        return ResponseEntity.ok(result);
    }
}
