package com.cargasafe.fleet.domain.model.valueobjects;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;
import jakarta.persistence.Embeddable;

@Embeddable
public record OdometerKm(Integer value) {
    public OdometerKm {
        Preconditions.requireNonNull(value, "OdometerKm");
        if (value < 0) {
            throw new IllegalArgumentException("OdometerKm must be greater than or equal to 0");
        }
    }
}
