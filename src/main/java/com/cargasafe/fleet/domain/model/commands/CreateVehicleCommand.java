package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.domain.model.valueobjects.Capability;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

import java.util.Set;

public record CreateVehicleCommand(String plate, VehicleType type, Set<Capability> capabilities,
                                   VehicleStatus status, Integer odometerKm) {
    public CreateVehicleCommand {
        plate = Preconditions.requireNonBlank(plate, "Plate");
        Preconditions.requireNonNull(type, "VehicleType");
        Preconditions.requireNonNull(capabilities, "Capabilities");
        Preconditions.requireNonNull(odometerKm, "OdometerKm");
        if (capabilities.isEmpty()) throw new IllegalArgumentException("Capabilities must not be empty");
        if (odometerKm < 0) throw new IllegalArgumentException("OdometerKm must be >= 0");
    }
}
