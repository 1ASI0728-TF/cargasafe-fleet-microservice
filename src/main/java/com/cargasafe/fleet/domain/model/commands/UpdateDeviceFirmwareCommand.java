package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record UpdateDeviceFirmwareCommand(Long id, String firmware) {
    public UpdateDeviceFirmwareCommand {
        Preconditions.requireNonNull(id, "ID");
        firmware = Preconditions.requireNonBlank(firmware, "Firmware");
    }
}
