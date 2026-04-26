package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record DeleteVehicleCommand(Long id) {
    public DeleteVehicleCommand {
        Preconditions.requireNonNull(id, "ID");
    }
}
