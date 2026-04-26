package com.cargasafe.fleet.application.internal.commandservices;

import com.cargasafe.fleet.domain.exceptions.*;
import com.cargasafe.fleet.domain.model.aggregates.Vehicle;
import com.cargasafe.fleet.domain.model.commands.*;
import com.cargasafe.fleet.domain.model.valueobjects.Imei;
import com.cargasafe.fleet.domain.model.valueobjects.VehicleStatus;
import com.cargasafe.fleet.domain.services.VehicleCommandService;
import com.cargasafe.fleet.infrastructure.persistence.jpa.DeviceRepository;
import com.cargasafe.fleet.infrastructure.persistence.jpa.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class VehicleCommandServiceImpl implements VehicleCommandService {

    private final VehicleRepository vehicleRepository;
    private final DeviceRepository deviceRepository;

    public VehicleCommandServiceImpl(VehicleRepository vehicleRepository, DeviceRepository deviceRepository) {
        this.vehicleRepository = vehicleRepository;
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    @Override
    public Optional<Vehicle> handle(CreateVehicleCommand command) {
        var plate = new com.cargasafe.fleet.domain.model.valueobjects.Plate(command.plate());
        if (vehicleRepository.existsByPlate(plate)) {
            throw new VehiclePlateAlreadyExistsException(command.plate());
        }
        return Optional.of(vehicleRepository.save(new Vehicle(command)));
    }

    @Transactional
    @Override
    public Optional<Vehicle> handle(UpdateVehicleCommand command) {
        var vehicle = vehicleRepository.findById(command.id())
                .orElseThrow(() -> new VehicleNotFoundException(command.id()));
        if (command.type() != null) vehicle.updateType(command.type());
        if (command.capabilities() != null && !command.capabilities().isEmpty()) vehicle.updateCapabilities(command.capabilities());
        if (command.status() != null) vehicle.updateStatus(command.status());
        if (command.odometerKm() != null) vehicle.updateOdometer(command.odometerKm());
        return Optional.of(vehicleRepository.save(vehicle));
    }

    @Transactional
    @Override
    public void handle(DeleteVehicleCommand command) {
        var vehicle = vehicleRepository.findById(command.id())
                .orElseThrow(() -> new VehicleNotFoundException(command.id()));
        if (vehicle.getStatus() != VehicleStatus.RETIRED) {
            throw new IllegalStateException("Vehicle must be in RETIRED status before deletion");
        }
        if (vehicle.hasAnyDevice()) {
            throw new DeviceAssignmentConflictException("Cannot delete vehicle with assigned devices. Unassign devices first.");
        }
        vehicleRepository.delete(vehicle);
    }

    @Transactional
    @Override
    public Optional<Vehicle> handle(AssignDeviceToVehicleCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));
        Imei deviceImei = new Imei(command.deviceImei());
        var device = deviceRepository.findByImei(deviceImei)
                .orElseThrow(() -> new DeviceNotFoundException(command.deviceImei()));
        if (vehicle.getStatus() == VehicleStatus.RETIRED) {
            throw new IllegalStateException("Cannot assign device to RETIRED vehicle");
        }
        if (device.isAssigned() && !device.getVehiclePlate().equals(vehicle.getPlate())) {
            throw new DeviceAlreadyAssignedException(command.deviceImei());
        }
        vehicle.assignDevice(deviceImei);
        device.assignToVehicle(vehicle.getPlate());
        vehicleRepository.save(vehicle);
        deviceRepository.save(device);
        return Optional.of(vehicle);
    }

    @Transactional
    @Override
    public Optional<Vehicle> handle(UnassignDeviceFromVehicleCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));
        Imei deviceImei = new Imei(command.deviceImei());
        var device = deviceRepository.findByImei(deviceImei)
                .orElseThrow(() -> new DeviceNotFoundException(command.deviceImei()));
        if (!device.isAssigned() || !device.getVehiclePlate().equals(vehicle.getPlate())) {
            throw new DeviceAssignmentConflictException("Device not assigned to this vehicle");
        }
        vehicle.unassignDevice(deviceImei);
        device.unassignFromVehicle();
        vehicleRepository.save(vehicle);
        deviceRepository.save(device);
        return Optional.of(vehicle);
    }

    @Transactional
    @Override
    public Optional<Vehicle> handle(UpdateVehicleStatusCommand command) {
        var vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new VehicleNotFoundException(command.vehicleId()));
        vehicle.updateStatus(command.status());
        return Optional.of(vehicleRepository.save(vehicle));
    }
}
