package com.cargasafe.fleet.domain.model.queries;

import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record GetVehiclesByStatusQuery(VehicleStatus status) {
    public GetVehiclesByStatusQuery {
        Preconditions.requireNonNull(status, "VehicleStatus");
    }
}
