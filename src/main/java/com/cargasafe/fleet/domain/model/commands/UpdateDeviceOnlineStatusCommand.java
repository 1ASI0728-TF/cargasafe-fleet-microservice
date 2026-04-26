package com.cargasafe.fleet.domain.model.commands;

public record UpdateDeviceOnlineStatusCommand(Long deviceId, Boolean online) {
}
