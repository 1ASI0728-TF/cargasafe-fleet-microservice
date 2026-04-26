package com.cargasafe.fleet.interfaces.rest.resources;

public record DeviceResource(Long id, String imei, String firmware, boolean online, String vehiclePlate) {
}
