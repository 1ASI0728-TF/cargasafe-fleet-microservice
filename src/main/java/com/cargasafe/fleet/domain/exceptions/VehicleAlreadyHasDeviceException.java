package com.cargasafe.fleet.domain.exceptions;

public class VehicleAlreadyHasDeviceException extends RuntimeException {
    public VehicleAlreadyHasDeviceException(Long vehicleId) {
        super("Vehicle with id " + vehicleId + " already has a device assigned");
    }
}
