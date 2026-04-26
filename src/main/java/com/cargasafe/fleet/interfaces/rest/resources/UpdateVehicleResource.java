package com.cargasafe.fleet.interfaces.rest.resources;

import java.util.Set;

public record UpdateVehicleResource(String type, Set<String> capabilities, String status, Integer odometerKm) {
}
