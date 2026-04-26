package com.cargasafe.fleet.domain.model.queries;

import com.cargasafe.fleet.shared.domain.model.validation.Preconditions;

public record GetDeviceByImeiQuery(String imei) {
    public GetDeviceByImeiQuery {
        imei = Preconditions.requireNonBlank(imei, "IMEI");
    }
}
