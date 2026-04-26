package com.cargasafe.fleet.infrastructure.persistence.jpa;

import com.cargasafe.fleet.domain.model.aggregates.Device;
import com.cargasafe.fleet.domain.model.valueobjects.Imei;
import com.cargasafe.fleet.domain.model.valueobjects.Plate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByImei(Imei imei);
    boolean existsByImei(Imei imei);
    List<Device> findAllByOnline(boolean online);
    List<Device> findAllByVehiclePlate(Plate vehiclePlate);
}
