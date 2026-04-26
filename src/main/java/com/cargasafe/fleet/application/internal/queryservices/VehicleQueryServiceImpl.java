package com.cargasafe.fleet.application.internal.queryservices;

import com.cargasafe.fleet.domain.model.aggregates.Vehicle;
import com.cargasafe.fleet.domain.model.queries.*;
import com.cargasafe.fleet.domain.model.valueobjects.Plate;
import com.cargasafe.fleet.domain.services.VehicleQueryService;
import com.cargasafe.fleet.infrastructure.persistence.jpa.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleQueryServiceImpl implements VehicleQueryService {

    private final VehicleRepository vehicleRepository;

    public VehicleQueryServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> handle(GetAllVehiclesQuery query) {
        return vehicleRepository.findAll();
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByIdQuery query) {
        return vehicleRepository.findById(query.id());
    }

    @Override
    public Optional<Vehicle> handle(GetVehicleByPlateQuery query) {
        return vehicleRepository.findByPlate(new Plate(query.plate()));
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByStatusQuery query) {
        return vehicleRepository.findAllByStatus(query.status());
    }

    @Override
    public List<Vehicle> handle(GetVehiclesByTypeQuery query) {
        return vehicleRepository.findAllByType(query.type());
    }
}
