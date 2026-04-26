package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.aggregates.Vehicle;
import com.cargasafe.fleet.domain.model.valueobjects.Imei;
import com.cargasafe.fleet.interfaces.rest.resources.VehicleResource;

import java.util.stream.Collectors;

public final class VehicleResourceFromEntityAssembler {
    public static VehicleResource toResourceFromEntity(Vehicle entity) {
        return new VehicleResource(
                entity.getId(),
                entity.getPlate().value(),
                entity.getType().name(),
                entity.getCapabilities().stream().map(Enum::name).collect(Collectors.toSet()),
                entity.getStatus().name(),
                entity.getOdometerKm().value(),
                entity.getDeviceImeis().stream().map(Imei::value).toList()
        );
    }
}
