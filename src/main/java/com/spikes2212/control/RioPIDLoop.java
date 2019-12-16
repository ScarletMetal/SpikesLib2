package com.spikes2212.control;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.PIDController;

import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A PID loop that runs on the Roborio in a separated thread.
 *
 * @author Yuval Levy
 */

public class RioPIDLoop implements PIDLoop {

    /**
     * An enum that represents the frequency the PID loop runs in.
     */
    private enum Frequency {
        LOW(0.05),
        DEFAULT(0.02),
        HIGH(0.01);

        private double period;

        Frequency(double period) {
            this.period = period;
        }
    }

    /**
     * The notifier creates a separated thread for the PID loop.
     */
    private Notifier notifier;

    /**
     * The PIDController calculates the wanted speed to reach target.
     */
    private PIDController controller;

    /**
     * Contains the setting for the pid loop: Kp, Ki, Kd, tolerance and waitTime.
     */
    private PIDSettings pidSettings;

    /**
     * The setpoint for the loop.
     */
    private Supplier<Double> setpoint;

    /**
     * The frequency the PID loop runs in.
     */
    private Frequency frequency;

    /**
     * Returns my current location.
     */
    private Supplier<Double> source;

    /**
     * The last time the subsystem didn't reached target.
     */
    private double lastTimeNotOnTarget;

    /**
     * The output of RioPIDLoop.
     */
    private Consumer<Double> output;

    /**
     * A lock that synchronizes the different threads.
     */
    private ReentrantLock lock;

    public RioPIDLoop(PIDSettings pidSettings, Supplier<Double> setpoint, Supplier<Double> source, Consumer<Double> output, Frequency frequency) {
        this.pidSettings = pidSettings;
        this.setpoint = setpoint;
        this.frequency = frequency;
        this.source = source;
        this.lastTimeNotOnTarget = Timer.getFPGATimestamp();
        this.output = output;
        notifier = new Notifier(this::periodic);
        lock = new ReentrantLock();
    }

    public RioPIDLoop(PIDSettings pidSettings, Supplier<Double> setpoint, Supplier<Double> source, Consumer<Double> output) {
        this(pidSettings, setpoint, source, output, Frequency.DEFAULT);
    }

    public RioPIDLoop(PIDSettings pidSettings, double setpoint, Supplier<Double> source, Consumer<Double> output, Frequency frequency) {
        this(pidSettings, () -> setpoint, source, output, frequency);
    }

    public RioPIDLoop(PIDSettings pidSettings, double setpoint, Supplier<Double> source, Consumer<Double> output) {
        this(pidSettings, () -> setpoint, source, output, Frequency.DEFAULT);
    }

    @Override
    public void enable() {
        controller = new PIDController(pidSettings.getkP(), pidSettings.getkI(), pidSettings.getkD(), frequency.period);
        notifier.startPeriodic(frequency.period);
    }

    private void periodic() {
        lock.lock();
        output.accept(controller.calculate(source.get()));
        lock.unlock();
    }

    @Override
    public void disable() {
        notifier.stop();
    }


    public void enableContinuousInput(double minimumValue, double maximumValue) {
        this.controller.enableContinuousInput(minimumValue, maximumValue);
    }

    public void disableContinuousInput() {
        this.controller.disableContinuousInput();
    }

    @Override
    public void update() {
        lock.lock();
        try {
            controller.setSetpoint(setpoint.get());
        } finally {
            lock.unlock();
        }
    }


    @Override
    public boolean onTarget() {
        if (source.get() - setpoint.get() > pidSettings.getTolerance()) {
            lastTimeNotOnTarget = Timer.getFPGATimestamp();
        }
        return Timer.getFPGATimestamp() - lastTimeNotOnTarget >= pidSettings.getWaitTime();
    }
}
