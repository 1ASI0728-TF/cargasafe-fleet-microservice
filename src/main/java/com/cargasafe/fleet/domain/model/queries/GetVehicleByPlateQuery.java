package com.cargasafe.fleet.domain.model.queries;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record GetVehicleByPlateQuery(String plate) {
    public GetVehicleByPlateQuery {
        plate = Preconditions.requireNonBlank(plate, "Plate");
    }
}
