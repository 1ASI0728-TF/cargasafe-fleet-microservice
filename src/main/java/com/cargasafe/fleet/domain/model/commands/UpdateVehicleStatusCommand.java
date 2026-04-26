package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;

public record UpdateVehicleStatusCommand(Long vehicleId, VehicleStatus status) {
    public UpdateVehicleStatusCommand {
        if (vehicleId == null) throw new IllegalArgumentException("vehicleId is required");
        if (status == null) throw new IllegalArgumentException("status is required");
    }
}
