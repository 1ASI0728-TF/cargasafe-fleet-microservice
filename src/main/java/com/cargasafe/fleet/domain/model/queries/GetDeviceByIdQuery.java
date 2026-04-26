package com.cargasafe.fleet.domain.model.queries;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record GetDeviceByIdQuery(Long id) {
    public GetDeviceByIdQuery {
        Preconditions.requireNonNull(id, "ID");
    }
}
