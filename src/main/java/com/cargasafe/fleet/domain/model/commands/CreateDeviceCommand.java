package com.cargasafe.fleet.domain.model.commands;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record CreateDeviceCommand(String imei, String firmware, Boolean online) {
    public CreateDeviceCommand {
        imei = Preconditions.requireNonBlank(imei, "IMEI");
        firmware = Preconditions.requireNonBlank(firmware, "Firmware");
    }
}
