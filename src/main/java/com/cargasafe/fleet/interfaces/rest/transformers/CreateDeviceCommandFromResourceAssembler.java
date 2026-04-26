package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.commands.CreateDeviceCommand;
import com.cargasafe.fleet.interfaces.rest.resources.CreateDeviceResource;

public final class CreateDeviceCommandFromResourceAssembler {
    public static CreateDeviceCommand toCommandFromResource(CreateDeviceResource resource) {
        return new CreateDeviceCommand(resource.imei(), resource.firmware(), resource.online());
    }
}
