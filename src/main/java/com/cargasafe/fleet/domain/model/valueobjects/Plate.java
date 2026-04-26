package com.cargasafe.fleet.domain.model.valueobjects;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;
import jakarta.persistence.Embeddable;

import java.util.Locale;

@Embeddable
public record Plate(String value) {
    public Plate {
        value = Preconditions.requireNonBlank(value, "Plate");
        value = value.trim().toUpperCase(Locale.ROOT);
        if (!value.matches("[A-Z0-9-]{4,12}")) {
            throw new IllegalArgumentException("Plate must match pattern [A-Z0-9-]{4,12}");
        }
    }
}
