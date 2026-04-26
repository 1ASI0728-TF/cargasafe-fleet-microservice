package com.cargasafe.fleet.interfaces.rest.resources;

import java.util.List;
import java.util.Set;

public record VehicleResource(Long id, String plate, String type, Set<String> capabilities,
                               String status, Integer odometerKm, List<String> deviceImeis) {
}
