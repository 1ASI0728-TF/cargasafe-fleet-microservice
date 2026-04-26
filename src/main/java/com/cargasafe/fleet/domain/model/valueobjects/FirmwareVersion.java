package com.cargasafe.fleet.domain.model.valueobjects;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;
import jakarta.persistence.Embeddable;

@Embeddable
public record FirmwareVersion(String value) {
    public FirmwareVersion {
        value = Preconditions.requireNonBlank(value, "FirmwareVersion");
        value = value.trim();
        if (!value.matches("v\\d+\\.\\d+\\.\\d+")) {
            throw new IllegalArgumentException("FirmwareVersion must match pattern vMAJOR.MINOR.PATCH (e.g., v1.8.2)");
        }
    }
}
