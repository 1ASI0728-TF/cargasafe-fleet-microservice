package com.cargasafe.fleet.application.internal.queryservices;

import com.cargasafe.fleet.domain.model.aggregates.Device;
import com.cargasafe.fleet.domain.model.queries.*;
import com.cargasafe.fleet.domain.model.valueobjects.Imei;
import com.cargasafe.fleet.domain.services.DeviceQueryService;
import com.cargasafe.fleet.infrastructure.persistence.jpa.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> handle(GetAllDevicesQuery query) {
        return deviceRepository.findAll();
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.id());
    }

    @Override
    public Optional<Device> handle(GetDeviceByImeiQuery query) {
        return deviceRepository.findByImei(new Imei(query.imei()));
    }

    @Override
    public List<Device> handle(GetDevicesByOnlineQuery query) {
        return deviceRepository.findAllByOnline(query.online());
    }
}
