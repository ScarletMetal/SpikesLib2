package com.spikes2212.command.genericsubsystem.commands;

import com.spikes2212.command.genericsubsystem.GenericSubsystem;
import edu.wpi.first.wpilibj.Timer;

import java.util.function.Supplier;

/**
 * This command accelerates the speed of a {@link GenericSubsystem} linearly so that it will
 * reach a wanted speed after a given time.
 *
 * @author Yuval Levy
 */
public class AccelerateGenericSubsystem extends MoveGenericSubsystem {
    /**
     * The time this subsystem should take to accelerate up to the required speed.
     */
    protected final double time;

    /**
     * The acceleration this command will apply to get to the required speed in the required time.
     */
    private double acceleration;

    /**
     * The current speed of the subsystem.
     */
    private double currentSpeed;

    /**
     * The time this command started running.
     */
    private double startTime;

    /**
     * This constructs a new {@link AccelerateGenericSubsystem} command using the
     * {@link GenericSubsystem} this command operates on and a supplier supplying the
     * wanted speed the {@link GenericSubsystem} should move with after the given
     * time.
     *
     * @param subsystem   the {@link GenericSubsystem} this command should move.
     * @param wantedSpeed the speed the subsystem should move at the end of the command.
     * @param time        the time it takes for the subsystem to get to the speed.
     */
    public AccelerateGenericSubsystem(GenericSubsystem subsystem, Supplier<Double> wantedSpeed, double time) {
        super(subsystem, wantedSpeed);
        if (time <= 1) {
            time = 1;
        }
        this.time = time;
    }

    /**
     * This constructs a new {@link AccelerateGenericSubsystem} command using the
     * {@link GenericSubsystem} this command operates on and a supplier supplying the
     * wanted speed the {@link GenericSubsystem} should move with after the given
     * time.
     *
     * @param subsystem   the {@link GenericSubsystem} this command should move.
     * @param wantedSpeed the speed the subsystem should move at the end of the command.
     * @param time        the time it takes for the subsystem to get to the speed.
     */
    public AccelerateGenericSubsystem(GenericSubsystem subsystem, double wantedSpeed, double time) {
        this(subsystem, () -> wantedSpeed, time);
    }

    /**
     * Reset the timer.
     */
    @Override
    public void initialize() {
        startTime = Timer.getFPGATimestamp();
        currentSpeed = 0;
        acceleration = speedSupplier.get() / time;
    }

    /**
     * Calculate the speed and moves the subsystem.
     */
    @Override
    public void execute() {
        currentSpeed = (Timer.getFPGATimestamp() - startTime) * acceleration;
        if (Math.abs(currentSpeed) > Math.abs(speedSupplier.get()))
            currentSpeed = speedSupplier.get();

        subsystem.move(currentSpeed);
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || currentSpeed == speedSupplier.get();
    }
}
