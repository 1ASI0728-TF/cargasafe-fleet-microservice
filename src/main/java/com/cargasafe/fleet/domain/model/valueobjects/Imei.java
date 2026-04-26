package com.cargasafe.fleet.domain.model.valueobjects;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;
import jakarta.persistence.Embeddable;

@Embeddable
public record Imei(String value) {
    public Imei {
        value = Preconditions.requireNonBlank(value, "IMEI");
        value = value.trim();
        if (!value.matches("IMEI-[0-9]{7,15}")) {
            throw new IllegalArgumentException("IMEI must match pattern IMEI-[0-9]{7,15}");
        }
    }
}
