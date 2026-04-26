package com.cargasafe.fleet.domain.model.queries;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record GetVehicleByIdQuery(Long id) {
    public GetVehicleByIdQuery {
        Preconditions.requireNonNull(id, "ID");
    }
}
