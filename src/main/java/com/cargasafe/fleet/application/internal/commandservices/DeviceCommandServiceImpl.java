package com.cargasafe.fleet.application.internal.commandservices;

import com.cargasafe.fleet.domain.exceptions.*;
import com.cargasafe.fleet.domain.model.aggregates.Device;
import com.cargasafe.fleet.domain.model.commands.*;
import com.cargasafe.fleet.domain.model.valueobjects.FirmwareVersion;
import com.cargasafe.fleet.domain.model.valueobjects.Imei;
import com.cargasafe.fleet.domain.services.DeviceCommandService;
import com.cargasafe.fleet.infrastructure.persistence.jpa.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    @Override
    public Optional<Device> handle(CreateDeviceCommand command) {
        var imei = new Imei(command.imei());
        if (deviceRepository.existsByImei(imei)) {
            throw new DeviceImeiAlreadyExistsException(command.imei());
        }
        return Optional.of(deviceRepository.save(new Device(command)));
    }

    @Transactional
    @Override
    public Optional<Device> handle(UpdateDeviceCommand command) {
        var device = deviceRepository.findById(command.id())
                .orElseThrow(() -> new DeviceNotFoundException(command.id()));
        if (command.firmware() != null && !command.firmware().isBlank()) {
            device.updateFirmware(new FirmwareVersion(command.firmware()));
        }
        if (command.online() != null) {
            device.updateOnline(command.online());
        }
        return Optional.of(deviceRepository.save(device));
    }

    @Transactional
    @Override
    public void handle(DeleteDeviceCommand command) {
        var device = deviceRepository.findById(command.id())
                .orElseThrow(() -> new DeviceNotFoundException(command.id()));
        if (device.isAssigned()) {
            throw new DeviceAssignmentConflictException("Cannot delete device assigned to a vehicle. Unassign device first.");
        }
        deviceRepository.delete(device);
    }

    @Transactional
    @Override
    public Optional<Device> handle(UpdateDeviceFirmwareCommand command) {
        var device = deviceRepository.findById(command.id())
                .orElseThrow(() -> new DeviceNotFoundException(command.id()));
        device.updateFirmware(new FirmwareVersion(command.firmware()));
        return Optional.of(deviceRepository.save(device));
    }

    @Override
    public Optional<Device> handle(UpdateDeviceOnlineStatusCommand command) {
        var deviceOpt = deviceRepository.findById(command.deviceId());
        if (deviceOpt.isEmpty()) return Optional.empty();
        var device = deviceOpt.get();
        device.updateOnline(command.online() != null ? command.online() : false);
        return Optional.of(deviceRepository.save(device));
    }
}
