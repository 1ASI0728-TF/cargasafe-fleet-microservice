package com.cargasafe.fleet.domain.model.queries;

import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record GetVehiclesByTypeQuery(VehicleType type) {
    public GetVehiclesByTypeQuery {
        Preconditions.requireNonNull(type, "VehicleType");
    }
}
