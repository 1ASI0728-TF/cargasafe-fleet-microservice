package com.cargasafe.fleet.domain.services;

import com.cargasafe.fleet.domain.model.aggregates.Vehicle;
import com.cargasafe.fleet.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface VehicleQueryService {
    List<Vehicle> handle(GetAllVehiclesQuery query);
    Optional<Vehicle> handle(GetVehicleByIdQuery query);
    Optional<Vehicle> handle(GetVehicleByPlateQuery query);
    List<Vehicle> handle(GetVehiclesByStatusQuery query);
    List<Vehicle> handle(GetVehiclesByTypeQuery query);
}
