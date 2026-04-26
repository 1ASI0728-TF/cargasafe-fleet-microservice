package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.commands.UpdateVehicleStatusCommand;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.interfaces.rest.resources.UpdateVehicleStatusResource;

public final class UpdateVehicleStatusCommandFromResourceAssembler {
    private UpdateVehicleStatusCommandFromResourceAssembler() {
    }

    public static UpdateVehicleStatusCommand toCommandFromResource(Long vehicleId, UpdateVehicleStatusResource resource) {
        return new UpdateVehicleStatusCommand(vehicleId, VehicleStatus.valueOf(resource.status()));
    }
}
