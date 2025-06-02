package com.AlertaApagao.Location_service.service;

import com.AlertaApagao.Location_service.exception.LocationAlreadyExistsException;
import com.AlertaApagao.Location_service.exception.MaxUserLocationsReachedException;
import com.AlertaApagao.Location_service.integration.CepApiClient;
import com.AlertaApagao.Location_service.model.CepResponse;
import com.AlertaApagao.Location_service.model.LocationCreationDTO;
import com.AlertaApagao.Location_service.model.LocationStatus;
import com.AlertaApagao.Location_service.model.UserLocation;
import com.AlertaApagao.Location_service.repository.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LocationService {

    private final LocationRepository repository;
    private final CepApiClient cepApiClient;

    public LocationService(LocationRepository repository, CepApiClient cepApiClient) {
        this.repository = repository;
        this.cepApiClient = cepApiClient;
    }


    public CepResponse searchLocationByCep(String cep) {
        return cepApiClient.requestExternalAPI(cep);
    }


    private UserLocation buildLocationFromCep(CepResponse cepResponse, UUID userId) {
        String zone = cepResponse.getState() + "." +
                cepResponse.getCity() + "." +
                cepResponse.getDistrict().replace(" ", "");

        return new UserLocation(
                userId,
                Double.parseDouble(cepResponse.getLat()),
                Double.parseDouble(cepResponse.getLng()),
                cepResponse.getState(),
                cepResponse.getCity(),
                cepResponse.getDistrict(),
                cepResponse.getCep(),
                zone
        );
    }


    @Transactional
    public void saveLocation(LocationCreationDTO dto) {
        if(repository.existsUserLocationByCepAndUserId(dto.cep(), UUID.fromString(dto.userId()))){
            throw new LocationAlreadyExistsException("Este endereço já está cadastrado para o usuário.");
        }

        UUID userId = UUID.fromString(dto.userId());

        int count = repository.countUserLocationsByUserId(userId);
        if (count >= 3) throw new MaxUserLocationsReachedException("O usuário não pode ter mais de 3 localizações cadastradas.");

        CepResponse cepResponse = searchLocationByCep(dto.cep());
        UserLocation newLocation = buildLocationFromCep(cepResponse, userId);
        repository.save(newLocation);
    }


    @Transactional
    public void deleteLocation(String locationId) {
        UUID id = UUID.fromString(locationId);
        repository.findById(id).ifPresent(repository::delete);
    }


    public List<UserLocation> getAllLocationsByUser(String userId) {
        return repository.findUserLocationByUserId(UUID.fromString(userId));
    }


    public List<UserLocation> getAllLocationsByZone(String zone) {
        return repository.findUserLocationsByZone(zone);
    }


    public List<UserLocation> fetchOneLocationPerZoneRandomly() {
        return repository.findDistinctRandomByZone();
    }


    @Transactional
    public void updateStatusByZone(String zone, LocationStatus status) {
        repository.updateStatusByZone(zone, status, LocalDateTime.now());
    }

    public UserLocation getLocationById(String id){
        Optional<UserLocation> location = repository.findById(UUID.fromString(id));
        if(location.isPresent()){
            return location.get();
        }
        return null;
    }


}
