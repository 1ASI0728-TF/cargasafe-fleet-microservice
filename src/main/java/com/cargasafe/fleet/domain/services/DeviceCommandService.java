package com.cargasafe.fleet.domain.services;

import com.cargasafe.fleet.domain.model.aggregates.Device;
import com.cargasafe.fleet.domain.model.commands.*;

import java.util.Optional;

public interface DeviceCommandService {
    Optional<Device> handle(CreateDeviceCommand command);
    Optional<Device> handle(UpdateDeviceCommand command);
    void handle(DeleteDeviceCommand command);
    Optional<Device> handle(UpdateDeviceFirmwareCommand command);
    Optional<Device> handle(UpdateDeviceOnlineStatusCommand command);
}
