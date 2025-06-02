package com.AlertaApagao.Location_service.repository;

import com.AlertaApagao.Location_service.model.LocationStatus;
import com.AlertaApagao.Location_service.model.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<UserLocation, UUID> {

    List<UserLocation> findUserLocationByUserId(UUID userId);
    int countUserLocationsByUserId(UUID userId);
    List<UserLocation> findUserLocationsByZone(String zone);
    boolean existsUserLocationByCepAndUserId(String cep, UUID userId);

    @Modifying
    @Query("UPDATE UserLocation l SET l.status = :status, l.updatedAt = :updatedAt WHERE l.zone = :zone")
    void updateStatusByZone(
            @Param("zone") String zone,
            @Param("status") LocationStatus status,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    @Query(value = """
    SELECT DISTINCT ON (zone) *
    FROM user_locations
    ORDER BY zone, RANDOM()
    """, nativeQuery = true)
    List<UserLocation> findDistinctRandomByZone();
}
