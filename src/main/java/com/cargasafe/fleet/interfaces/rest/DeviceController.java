package com.cargasafe.fleet.interfaces.rest;

import com.cargasafe.fleet.domain.exceptions.DeviceNotFoundException;
import com.cargasafe.fleet.domain.model.commands.DeleteDeviceCommand;
import com.cargasafe.fleet.domain.model.commands.UpdateDeviceFirmwareCommand;
import com.cargasafe.fleet.domain.model.queries.*;
import com.cargasafe.fleet.domain.services.DeviceCommandService;
import com.cargasafe.fleet.domain.services.DeviceQueryService;
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
@RequestMapping("/api/v1/fleet/devices")
@Tag(name = "Devices", description = "Endpoints for managing IoT devices in fleet management")
public class DeviceController {

    private final DeviceCommandService deviceCommandService;
    private final DeviceQueryService deviceQueryService;

    public DeviceController(DeviceCommandService deviceCommandService, DeviceQueryService deviceQueryService) {
        this.deviceCommandService = deviceCommandService;
        this.deviceQueryService = deviceQueryService;
    }

    @Operation(summary = "Get all devices")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Devices returned"))
    @GetMapping
    public ResponseEntity<List<DeviceResource>> getAllDevices() {
        var resources = deviceQueryService.handle(new GetAllDevicesQuery()).stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Get device by ID")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Device returned"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResource> getDeviceById(@PathVariable Long id) {
        var device = deviceQueryService.handle(new GetDeviceByIdQuery(id))
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Get device by IMEI")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Device returned"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    @GetMapping("/by-imei/{imei}")
    public ResponseEntity<DeviceResource> getDeviceByImei(@PathVariable String imei) {
        var device = deviceQueryService.handle(new GetDeviceByImeiQuery(imei))
                .orElseThrow(() -> new DeviceNotFoundException(imei));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Get devices by online status")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Devices returned"))
    @GetMapping("/by-online/{online}")
    public ResponseEntity<List<DeviceResource>> getDevicesByOnline(@PathVariable boolean online) {
        var resources = deviceQueryService.handle(new GetDevicesByOnlineQuery(online)).stream()
                .map(DeviceResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }

    @Operation(summary = "Create device")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Device created"),
            @ApiResponse(responseCode = "409", description = "IMEI already exists")})
    @PostMapping
    public ResponseEntity<DeviceResource> createDevice(@RequestBody CreateDeviceResource resource) {
        var command = CreateDeviceCommandFromResourceAssembler.toCommandFromResource(resource);
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new RuntimeException("Failed to create device"));
        return new ResponseEntity<>(DeviceResourceFromEntityAssembler.toResourceFromEntity(device), CREATED);
    }

    @Operation(summary = "Update device")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Device updated"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResource> updateDevice(@PathVariable Long id,
                                                        @RequestBody UpdateDeviceResource resource) {
        var command = UpdateDeviceCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Delete device")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Device deleted"),
            @ApiResponse(responseCode = "404", description = "Device not found"),
            @ApiResponse(responseCode = "409", description = "Device is assigned to a vehicle")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceCommandService.handle(new DeleteDeviceCommand(id));
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Update device firmware")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Firmware updated"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    @PostMapping("/{id}/firmware")
    public ResponseEntity<DeviceResource> updateDeviceFirmware(@PathVariable Long id,
                                                                @RequestParam String firmware) {
        var device = deviceCommandService.handle(new UpdateDeviceFirmwareCommand(id, firmware))
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }

    @Operation(summary = "Change device online status")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Online status updated"),
            @ApiResponse(responseCode = "404", description = "Device not found")})
    @PatchMapping("/{id}/online")
    public ResponseEntity<DeviceResource> updateOnlineStatus(@PathVariable Long id,
                                                              @RequestBody UpdateDeviceOnlineStatusResource resource) {
        var command = UpdateDeviceOnlineStatusCommandFromResourceAssembler.toCommandFromResource(id, resource);
        var device = deviceCommandService.handle(command)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return ResponseEntity.ok(DeviceResourceFromEntityAssembler.toResourceFromEntity(device));
    }
}
