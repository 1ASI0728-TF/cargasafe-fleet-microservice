package com.cargasafe.fleet.domain.model.aggregates;

import com.cargasafe.fleet.domain.model.commands.CreateVehicleCommand;
import com.cargasafe.fleet.domain.model.valueobjects.*;
import com.cargasafe.fleet.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
public class Vehicle extends AuditableAbstractAggregateRoot<Vehicle> {

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "plate", unique = true, nullable = false))
    private final Plate plate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_capabilities", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "capability")
    private Set<Capability> capabilities;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleStatus status;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "odometer_km", nullable = false))
    private OdometerKm odometerKm;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "vehicle_device_imeis", joinColumns = @JoinColumn(name = "vehicle_id"))
    @AttributeOverride(name = "value", column = @Column(name = "device_imei", nullable = false))
    private Set<Imei> deviceImeis;

    public Vehicle() {
        this.plate = null;
        this.type = null;
        this.capabilities = new HashSet<>();
        this.status = VehicleStatus.IN_SERVICE;
        this.odometerKm = null;
        this.deviceImeis = new HashSet<>();
    }

    public Vehicle(CreateVehicleCommand command) {
        this.plate = new Plate(command.plate());
        this.type = command.type();
        this.capabilities = new HashSet<>(command.capabilities());
        this.status = command.status() != null ? command.status() : VehicleStatus.IN_SERVICE;
        this.odometerKm = new OdometerKm(command.odometerKm());
        this.deviceImeis = new HashSet<>();
    }

    public void updateType(VehicleType type) {
        this.type = type;
    }

    public void updateCapabilities(Set<Capability> capabilities) {
        if (capabilities != null && !capabilities.isEmpty()) {
            this.capabilities = new HashSet<>(capabilities);
        }
    }

    public void updateStatus(VehicleStatus status) {
        if (this.status == VehicleStatus.RETIRED && status != VehicleStatus.RETIRED) {
            throw new IllegalStateException("Cannot change status from RETIRED to another state");
        }
        this.status = status;
    }

    public void updateOdometer(Integer newOdometerKm) {
        OdometerKm newOdometer = new OdometerKm(newOdometerKm);
        if (this.odometerKm != null && newOdometer.value() < this.odometerKm.value()) {
            throw new IllegalArgumentException("Odometer value cannot decrease. Current: " + this.odometerKm.value() + ", New: " + newOdometer.value());
        }
        this.odometerKm = newOdometer;
    }

    public void assignDevice(Imei deviceImei) {
        if (this.status == VehicleStatus.RETIRED) {
            throw new IllegalStateException("Cannot assign device to RETIRED vehicle");
        }
        if (this.deviceImeis.contains(deviceImei)) {
            throw new IllegalStateException("Device already assigned to this vehicle");
        }
        this.deviceImeis.add(deviceImei);
    }

    public void unassignDevice(Imei deviceImei) {
        this.deviceImeis.remove(deviceImei);
    }

    public boolean hasAnyDevice() {
        return !this.deviceImeis.isEmpty();
    }

    public boolean hasDevice(Imei deviceImei) {
        return this.deviceImeis.contains(deviceImei);
    }
}
