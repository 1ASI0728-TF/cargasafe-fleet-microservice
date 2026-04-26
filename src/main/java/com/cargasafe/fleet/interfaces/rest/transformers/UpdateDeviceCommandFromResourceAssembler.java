package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.commands.UpdateDeviceCommand;
import com.cargasafe.fleet.interfaces.rest.resources.UpdateDeviceResource;

public final class UpdateDeviceCommandFromResourceAssembler {
    public static UpdateDeviceCommand toCommandFromResource(Long id, UpdateDeviceResource resource) {
        return new UpdateDeviceCommand(id, resource.firmware(), resource.online());
    }
}
