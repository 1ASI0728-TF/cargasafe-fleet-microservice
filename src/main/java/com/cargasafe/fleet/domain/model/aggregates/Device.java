package com.cargasafe.fleet.domain.model.aggregates;

import com.cargasafe.fleet.domain.model.commands.CreateDeviceCommand;
import com.cargasafe.fleet.domain.model.valueobjects.*;
import com.cargasafe.fleet.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Device extends AuditableAbstractAggregateRoot<Device> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "imei", unique = true, nullable = false))
    private final Imei imei;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "firmware", nullable = false))
    private FirmwareVersion firmware;

    @Column(nullable = false)
    private boolean online;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "vehicle_plate"))
    private Plate vehiclePlate;

    public Device() {
        this.imei = null;
        this.firmware = null;
        this.online = false;
        this.vehiclePlate = null;
    }

    public Device(CreateDeviceCommand command) {
        this.imei = new Imei(command.imei());
        this.firmware = new FirmwareVersion(command.firmware());
        this.online = command.online() != null ? command.online() : false;
        this.vehiclePlate = null;
    }

    public void updateFirmware(FirmwareVersion firmware) {
        this.firmware = firmware;
    }

    public void updateOnline(boolean online) {
        this.online = online;
    }

    public void assignToVehicle(Plate vehiclePlate) {
        if (this.vehiclePlate != null) {
            throw new IllegalStateException("Device is already assigned to a vehicle");
        }
        this.vehiclePlate = vehiclePlate;
    }

    public void unassignFromVehicle() {
        this.vehiclePlate = null;
    }

    public boolean isAssigned() {
        return this.vehiclePlate != null;
    }
}
