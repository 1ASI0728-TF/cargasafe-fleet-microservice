package com.cargasafe.fleet.domain.exceptions;

public class VehiclePlateAlreadyExistsException extends RuntimeException {
    public VehiclePlateAlreadyExistsException(String plate) {
        super("Vehicle with plate " + plate + " already exists");
    }
}
