package com.cargasafe.fleet.domain.exceptions;

public class DeviceImeiAlreadyExistsException extends RuntimeException {
    public DeviceImeiAlreadyExistsException(String imei) {
        super("Device with IMEI " + imei + " already exists");
    }
}
