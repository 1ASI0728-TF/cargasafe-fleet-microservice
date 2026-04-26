package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.commands.CreateVehicleCommand;
import com.cargasafe.fleet.domain.model.valueobjects.Capability;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import com.cargasafe.fleet.interfaces.rest.resources.CreateVehicleResource;

import java.util.stream.Collectors;

public final class CreateVehicleCommandFromResourceAssembler {
    public static CreateVehicleCommand toCommandFromResource(CreateVehicleResource resource) {
        return new CreateVehicleCommand(
                resource.plate(),
                resource.type() != null ? VehicleType.valueOf(resource.type()) : null,
                resource.capabilities() != null
                        ? resource.capabilities().stream().map(Capability::valueOf).collect(Collectors.toSet())
                        : null,
                resource.status() != null ? VehicleStatus.valueOf(resource.status()) : null,
                resource.odometerKm());
    }
}
