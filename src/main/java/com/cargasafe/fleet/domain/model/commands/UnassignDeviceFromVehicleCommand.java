package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record UnassignDeviceFromVehicleCommand(Long vehicleId, String deviceImei) {
    public UnassignDeviceFromVehicleCommand {
        Preconditions.requireNonNull(vehicleId, "VehicleId");
        Preconditions.requireNonNull(deviceImei, "DeviceImei");
    }
}
