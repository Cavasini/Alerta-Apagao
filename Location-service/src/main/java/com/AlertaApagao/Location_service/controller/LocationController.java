package com.AlertaApagao.Location_service.controller;

import com.AlertaApagao.Location_service.model.CepResponse;
import com.AlertaApagao.Location_service.model.LocationCreationDTO;
import com.AlertaApagao.Location_service.model.UserLocation;
import com.AlertaApagao.Location_service.model.ZoneStatusUpdateRequest;
import com.AlertaApagao.Location_service.service.LocationService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService service){
        this.locationService = service;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<UserLocation>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(locationService.getAllLocationsByUser(userId));
    }

    @GetMapping("/zones/{zone}")
    public ResponseEntity<List<UserLocation>> getByZone(@PathVariable String zone) {
        return ResponseEntity.ok(locationService.getAllLocationsByZone(zone));
    }

    @GetMapping("/cep/{cep}")
    public ResponseEntity<CepResponse> getByCep(@PathVariable String cep) {
        return ResponseEntity.ok(locationService.searchLocationByCep(cep));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody LocationCreationDTO dto) {
        locationService.saveLocation(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Void> delete(@PathVariable String locationId) {
        locationService.deleteLocation(locationId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/zones")
    public ResponseEntity<List<UserLocation>> fetchOnePerZone() {
        return ResponseEntity.ok(locationService.fetchOneLocationPerZoneRandomly());
    }

    @PutMapping("/zones/status")
    public ResponseEntity<Void> updateStatus(@RequestBody ZoneStatusUpdateRequest request) {
        locationService.updateStatusByZone(request.zone(), request.status());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{locationId}")
    public ResponseEntity getLocation(@PathVariable String locationId){
        UserLocation location = locationService.getLocationById(locationId);
        return ResponseEntity.ok(location);
    }
}

