package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.domain.model.valueobjects.Capability;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

import java.util.Set;

public record UpdateVehicleCommand(Long id, VehicleType type, Set<Capability> capabilities,
                                   VehicleStatus status, Integer odometerKm) {
    public UpdateVehicleCommand {
        Preconditions.requireNonNull(id, "ID");
    }
}
