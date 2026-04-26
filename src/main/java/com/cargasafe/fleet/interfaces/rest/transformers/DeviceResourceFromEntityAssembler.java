package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.aggregates.Device;
import com.cargasafe.fleet.interfaces.rest.resources.DeviceResource;

public final class DeviceResourceFromEntityAssembler {
    public static DeviceResource toResourceFromEntity(Device entity) {
        return new DeviceResource(
                entity.getId(),
                entity.getImei().value(),
                entity.getFirmware().value(),
                entity.isOnline(),
                entity.getVehiclePlate() != null ? entity.getVehiclePlate().value() : null
        );
    }
}
