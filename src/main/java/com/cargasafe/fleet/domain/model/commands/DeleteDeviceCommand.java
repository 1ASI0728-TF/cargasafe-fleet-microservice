package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record DeleteDeviceCommand(Long id) {
    public DeleteDeviceCommand {
        Preconditions.requireNonNull(id, "ID");
    }
}
