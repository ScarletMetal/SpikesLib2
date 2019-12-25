package com.spikes2212.command.drivetrains.commands;

import java.util.function.Supplier;

import com.spikes2212.command.drivetrains.TankDrivetrain;

import edu.wpi.first.wpilibj2.command.CommandBase;

/**
 * This command moves a {@link TankDrivetrain} using speeds supplied to the left and the right sides independently.
 */
public class DriveTank extends CommandBase {

    protected final TankDrivetrain tankDrivetrain;
    protected final Supplier<Double> leftSpeedSuplier;
    protected final Supplier<Double> rightSpeedSuplier;
    Supplier<Boolean> isFinished;

    /**
     * This constructs a new {@link DriveTank} command that moves the given
     * {@link TankDrivetrain} according to constant left side and right side speeds.<br>
     * Positive values move forwards.
     *
     * @param drivetrain the tank drivetrain this command operates on.
     * @param leftSpeed  the speed to move the left side with.
     * @param rightSpeed the speed to move the right side with.
     */


    /**
     * This constructs a new {@link DriveTank} command that moves the given
     * {@link TankDrivetrain} acording to speed values from Double
     * {@link Supplier}s for left and right sides.<br>
     * Positive values move forwards.
     *
     * @param drivetrain         the drivetrain this command requires and moves.
     * @param leftSpeedSupplier  the double {@link Supplier} supplying the speed to move in the
     *                           left side with.
     * @param rightSpeedSupplier the double {@link Supplier} supplying the speed to move in the
     *                           right side with.
     */
    public DriveTank(TankDrivetrain drivetrain, Supplier<Double> leftSpeedSupplier,
                     Supplier<Double> rightSpeedSupplier, Supplier<Boolean> isFinished) {
        super();
        this.tankDrivetrain = drivetrain;
        this.leftSpeedSuplier = leftSpeedSupplier;
        this.rightSpeedSuplier = rightSpeedSupplier;
        this.isFinished = isFinished;
        this.addRequirements(drivetrain);
    }

    public DriveTank(TankDrivetrain drivetrain, Supplier<Double> leftSpeedSupplier,
                     Supplier<Double> rightSpeedSupplier) {
        this(drivetrain, leftSpeedSupplier, rightSpeedSupplier, () -> false);
    }

    public DriveTank(TankDrivetrain drivetrain, double leftSpeed, double rightSpeed, boolean isFinished) {
        this(drivetrain, () -> leftSpeed, () -> rightSpeed, () -> isFinished);
    }

    public DriveTank(TankDrivetrain drivetrain, double leftSpeed, double rightSpeed) {
        this(drivetrain, () -> leftSpeed, () -> rightSpeed, () -> false);
    }


    @Override
    public void initialize() {
    }

    /**
     * sets the values for both the leftSpeedSupplier and the rightSpeedSupplier.
     */
    @Override
    public void execute() {
        tankDrivetrain.tankDrive(leftSpeedSuplier.get(), rightSpeedSuplier.get());
    }

    @Override
    public boolean isFinished() {
        return this.isFinished.get();
    }

    @Override
    public void end(boolean interrupted) {
        tankDrivetrain.stop();
    }

}