package com.cargasafe.fleet.domain.services;

import com.cargasafe.fleet.domain.model.aggregates.Vehicle;
import com.cargasafe.fleet.domain.model.commands.*;

import java.util.Optional;

public interface VehicleCommandService {
    Optional<Vehicle> handle(CreateVehicleCommand command);
    Optional<Vehicle> handle(UpdateVehicleCommand command);
    void handle(DeleteVehicleCommand command);
    Optional<Vehicle> handle(AssignDeviceToVehicleCommand command);
    Optional<Vehicle> handle(UnassignDeviceFromVehicleCommand command);
    Optional<Vehicle> handle(UpdateVehicleStatusCommand command);
}
