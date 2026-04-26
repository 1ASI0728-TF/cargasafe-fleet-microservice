package com.cargasafe.fleet.infrastructure.persistence.jpa;

import com.cargasafe.fleet.domain.model.aggregates.Vehicle;
import com.cargasafe.fleet.domain.model.valueobjects.Imei;
import com.cargasafe.fleet.domain.model.valueobjects.Plate;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate(Plate plate);
    boolean existsByPlate(Plate plate);
    List<Vehicle> findAllByStatus(VehicleStatus status);
    List<Vehicle> findAllByType(VehicleType type);
    Optional<Vehicle> findByDeviceImeis(Imei deviceImeis);
}
