package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record UpdateDeviceCommand(Long id, String firmware, Boolean online) {
    public UpdateDeviceCommand {
        Preconditions.requireNonNull(id, "ID");
    }
}
