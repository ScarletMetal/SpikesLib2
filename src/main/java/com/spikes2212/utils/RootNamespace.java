package com.spikes2212.utils;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.smartdashboard.SendableRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class RootNamespace implements Namespace {
    private static final Map<String, Sendable> TABLES_TO_DATA = new HashMap<>();

    protected String name;
    private NetworkTable table;

    public RootNamespace(String name) {
        this.name = name;
        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        this.table = inst.getTable(this.name);
    }

    @Override
    public Supplier<Double> addConstantDouble(String name, double value) {
        NetworkTableEntry entry = this.table.getEntry(name);
        entry.setPersistent();
        entry.setDouble(value);
        return () -> entry.getDouble(value);
    }

    @Override
    public Supplier<Integer> addConstantInt(String name, int value) {
        NetworkTableEntry entry = this.table.getEntry(name);
        entry.setPersistent();
        entry.setNumber(value);
        return () -> (Integer) entry.getNumber(value);
    }

    @Override
    public Supplier<String> addConstantString(String name, String value) {
        NetworkTableEntry entry = this.table.getEntry(name);
        entry.setPersistent();
        entry.setString(value);
        entry.setString(value);
        return () -> entry.getString(value);
    }

    @Override
    public Namespace addChild(String name) {

        return new ChildNamespace(name, this);
    }

    @Override
    public void putData(String key, Sendable value) {
        Sendable sddata = TABLES_TO_DATA.get(key);
        if (sddata == null || sddata != value) {
            TABLES_TO_DATA.put(key, value);
            NetworkTable dataTable = table.getSubTable(key);
            SendableRegistry.publish(value, dataTable);
            dataTable.getEntry(".name").setString(key);
        }
    }

    @Override
    public Sendable getSendable(String key) {
        NetworkTableEntry entry = this.table.getEntry(key);
        NetworkTableValue value = entry.getValue();
        return (Sendable) value.getValue();
    }

    @Override
    public void putString(String key, String value) {
        NetworkTableEntry entry = this.table.getEntry(key);
        entry.setString(value);
    }

    @Override
    public String getString(String key) {
        NetworkTableEntry entry = this.table.getEntry(key);
        NetworkTableValue value = entry.getValue();
        return value.getString();
    }

    @Override
    public void putNumber(String key, double value) {
        NetworkTableEntry entry = this.table.getEntry(key);
        entry.setDouble(value);
    }

    @Override
    public double getNumber(String key) {
        NetworkTableEntry entry = this.table.getEntry(key);
        NetworkTableValue value = entry.getValue();
        return value.getDouble();
    }

    @Override
    public void putBoolean(String key, boolean value) {
        NetworkTableEntry entry = this.table.getEntry(key);
        entry.setBoolean(value);
    }

    @Override
    public boolean getBoolean(String key) {
        NetworkTableEntry entry = this.table.getEntry(key);
        NetworkTableValue value = entry.getValue();
        return value.getBoolean();
    }
}
