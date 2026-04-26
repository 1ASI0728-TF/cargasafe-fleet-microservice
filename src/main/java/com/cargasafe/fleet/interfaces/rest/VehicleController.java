package com.cargasafe.fleet.interfaces.rest;

import com.cargasafe.fleet.domain.exceptions.VehicleNotFoundException;
import com.cargasafe.fleet.domain.model.commands.AssignDeviceToVehicleCommand;
import com.cargasafe.fleet.domain.model.commands.DeleteVehicleCommand;
import com.cargasafe.fleet.domain.model.commands.UnassignDeviceFromVehicleCommand;
import com.cargasafe.fleet.domain.model.queries.*;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleType;
import com.cargasafe.fleet.domain.services.VehicleCommandService;
import com.cargasafe.fleet.domain.services.VehicleQueryService;
import com.cargasafe.fleet.interfaces.rest.resources.*;
import com.cargasafe.fleet.interfaces.rest.transformers.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@Slf4j
@RestController
@RequestMapping("/api/v1/fleet/vehicles")
@Tag(name = "Vehicles", description = "Endpoints for managing vehicles in fleet management")
public class VehicleController {

    private final VehicleCommandService vehicleCommandService;
    private final VehicleQueryService vehicleQueryService;

    public VehicleController(VehicleCommandService vehicleCommandService, VehicleQueryService vehicleQueryService) {
        this.vehicleCommandService = vehicleCommandService;
        this.vehicleQueryService = vehicleQueryService;
    }

    @Operation(summary = "Get all vehicles")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Vehicles returned"))
    @GetMapping
    public ResponseEntity<List<VehicleResource>> getAllVehicles() {
        var resources = vehicleQueryService.handle(new GetAllVehiclesQuery()).stream()
                .map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get vehicle by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vehicle returned"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")})
    @GetMapping("/{id}")
    public ResponseEntity<VehicleResource> getVehicleById(@PathVariable Long id) {
        var vehicle = vehicleQueryService.handle(new GetVehicleByIdQuery(id))
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @Operation(summary = "Get vehicle by plate")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vehicle returned"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")})
    @GetMapping("/by-plate/{plate}")
    public ResponseEntity<VehicleResource> getVehicleByPlate(@PathVariable String plate) {
        var vehicle = vehicleQueryService.handle(new GetVehicleByPlateQuery(plate))
                .orElseThrow(() -> new VehicleNotFoundException(plate));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @Operation(summary = "Get vehicles by status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Vehicles returned"))
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<VehicleResource>> getVehiclesByStatus(@PathVariable String status) {
        var resources = vehicleQueryService.handle(new GetVehiclesByStatusQuery(VehicleStatus.valueOf(status)))
                .stream().map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get vehicles by type")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Vehicles returned"))
    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<VehicleResource>> getVehiclesByType(@PathVariable String type) {
        var resources = vehicleQueryService.handle(new GetVehiclesByTypeQuery(VehicleType.valueOf(type)))
                .stream().map(VehicleResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Create vehicle")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Vehicle created"),
            @ApiResponse(responseCode = "409", description = "Plate already exists")})
    @PostMapping
    public ResponseEntity<VehicleResource> createVehicle(@RequestBody CreateVehicleResource resource) {
        var command = CreateVehicleCommandFromResourceAssembler.toCommandFromResource(resource);
        var vehicle = vehicleCommandService.handle(command)
                .orElseThrow(() -> new RuntimeException("Failed to create vehicle"));
        return new ResponseEntity<>(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle), CREATED);
    }

    @Operation(summary = "Update vehicle")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Vehicle updated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")})
    @PutMapping("/{id}")
    public ResponseEntity<VehicleResource> updateVehicle(@PathVariable Long id,
                                                          @RequestBody UpdateVehicleResource resource) {
        var command = UpdateVehicleCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var vehicle = vehicleCommandService.handle(command)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @Operation(summary = "Delete vehicle")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Vehicle deleted"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "409", description = "Cannot delete — not RETIRED or has devices")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleCommandService.handle(new DeleteVehicleCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign device to vehicle")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Device assigned"),
            @ApiResponse(responseCode = "404", description = "Vehicle or device not found"),
            @ApiResponse(responseCode = "409", description = "Assignment conflict")})
    @PostMapping("/{id}/assign-device/{imei}")
    public ResponseEntity<VehicleResource> assignDeviceToVehicle(@PathVariable Long id,
                                                                   @PathVariable String imei) {
        var vehicle = vehicleCommandService.handle(new AssignDeviceToVehicleCommand(id, imei))
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @Operation(summary = "Unassign device from vehicle")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Device unassigned"),
            @ApiResponse(responseCode = "409", description = "Assignment conflict")})
    @PostMapping("/{id}/unassign-device/{imei}")
    public ResponseEntity<VehicleResource> unassignDevice(@PathVariable Long id, @PathVariable String imei) {
        var vehicle = vehicleCommandService.handle(new UnassignDeviceFromVehicleCommand(id, imei))
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }

    @Operation(summary = "Change vehicle status")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Status updated"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found"),
            @ApiResponse(responseCode = "409", description = "Status change not allowed")})
    @PatchMapping("/{id}/status")
    public ResponseEntity<VehicleResource> updateStatus(@PathVariable Long id,
                                                         @RequestBody UpdateVehicleStatusResource resource) {
        var command = UpdateVehicleStatusCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var vehicle = vehicleCommandService.handle(command)
                .orElseThrow(() -> new VehicleNotFoundException(id));
        return ResponseEntity.ok(VehicleResourceFromEntityAssembler.toResourceFromEntity(vehicle));
    }
}
