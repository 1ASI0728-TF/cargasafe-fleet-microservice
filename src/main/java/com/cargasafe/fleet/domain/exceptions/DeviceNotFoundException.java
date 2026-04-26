package com.cargasafe.fleet.domain.exceptions;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
        super("Device with id " + id + " not found");
    }
    public DeviceNotFoundException(String imei) {
        super("Device with IMEI " + imei + " not found");
    }
}
