package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.commands.UpdateVehicleCommand;
import com.cargasafe.fleet.domain.model.valueobjects.Capability;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import com.cargasafe.fleet.interfaces.rest.resources.UpdateVehicleResource;

import java.util.stream.Collectors;

public final class UpdateVehicleCommandFromResourceAssembler {
    public static UpdateVehicleCommand toCommandFromResource(Long id, UpdateVehicleResource resource) {
        return new UpdateVehicleCommand(
                id,
                resource.type() != null ? VehicleType.valueOf(resource.type()) : null,
                resource.capabilities() != null
                        ? resource.capabilities().stream().map(Capability::valueOf).collect(Collectors.toSet())
                        : null,
                resource.status() != null ? VehicleStatus.valueOf(resource.status()) : null,
                resource.odometerKm());
    }
}
