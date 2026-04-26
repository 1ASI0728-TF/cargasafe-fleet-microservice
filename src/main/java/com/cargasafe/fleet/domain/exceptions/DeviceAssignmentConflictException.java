package com.cargasafe.fleet.domain.exceptions;

public class DeviceAssignmentConflictException extends RuntimeException {
    public DeviceAssignmentConflictException(String message) {
        super(message);
    }
}
