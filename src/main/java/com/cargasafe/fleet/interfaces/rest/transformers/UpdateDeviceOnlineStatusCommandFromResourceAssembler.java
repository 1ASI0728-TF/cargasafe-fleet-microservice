package com.cargasafe.fleet.interfaces.rest.transformers;

import com.cargasafe.fleet.domain.model.commands.UpdateDeviceOnlineStatusCommand;
import com.cargasafe.fleet.interfaces.rest.resources.UpdateDeviceOnlineStatusResource;

public final class UpdateDeviceOnlineStatusCommandFromResourceAssembler {
    public static UpdateDeviceOnlineStatusCommand toCommandFromResource(Long id, UpdateDeviceOnlineStatusResource resource) {
        return new UpdateDeviceOnlineStatusCommand(id, resource.online());
    }
}
