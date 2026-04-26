package com.cargasafe.fleet.interfaces.rest.resources;

import java.util.Set;

public record CreateVehicleResource(String plate, String type, Set<String> capabilities,
                                    String status, Integer odometerKm) {
}
