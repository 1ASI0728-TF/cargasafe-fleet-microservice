package com.cargasafe.fleet.shared.interfaces.rest;

import com.cargasafe.fleet.domain.exceptions.*;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        var errors = new ArrayList<Map<String, String>>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errors.add(Map.of("field", fe.getField(), "message", fe.getDefaultMessage()))
        );
        var body = new LinkedHashMap<String, Object>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Bad Request");
        body.put("messages", errors);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleVehicleNotFound(VehicleNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDeviceNotFound(DeviceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(VehiclePlateAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handlePlateConflict(VehiclePlateAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DeviceImeiAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleImeiConflict(DeviceImeiAlreadyExistsException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DeviceAlreadyAssignedException.class)
    public ResponseEntity<Map<String, Object>> handleDeviceAlreadyAssigned(DeviceAlreadyAssignedException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DeviceAssignmentConflictException.class)
    public ResponseEntity<Map<String, Object>> handleAssignmentConflict(DeviceAssignmentConflictException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(VehicleAlreadyHasDeviceException.class)
    public ResponseEntity<Map<String, Object>> handleVehicleHasDevice(VehicleAlreadyHasDeviceException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildError(HttpStatus status, String message) {
        var body = new LinkedHashMap<String, Object>();
        body.put("timestamp", Instant.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
