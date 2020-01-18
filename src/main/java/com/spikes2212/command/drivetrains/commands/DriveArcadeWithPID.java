package com.spikes2212.command.drivetrains.commands;

import com.spikes2212.command.drivetrains.TankDrivetrain;
import com.spikes2212.control.PIDLoop;
import edu.wpi.first.wpilibj2.command.CommandBase;

import java.util.function.Supplier;


public class DriveArcadeWithPID extends CommandBase {

    private final TankDrivetrain drivetrain;
    private final PIDLoop movementPIDLoop;
    private Supplier<Double> setpoint;

    /**
     * @param drivetrain      is the {@link TankDrivetrain} the command moves.
     * @param movementPIDLoop is the {@link PIDLoop} that calculates and sets the speed to the drivetrain.
     */

    public DriveArcadeWithPID(TankDrivetrain drivetrain, PIDLoop movementPIDLoop, Supplier<Double> setpoint) {
        this.addRequirements(drivetrain);
        this.drivetrain = drivetrain;
        this.setpoint = setpoint;
        this.movementPIDLoop = movementPIDLoop;
        this.movementPIDLoop.setSetpoint(setpoint.get());
    }

    public DriveArcadeWithPID(TankDrivetrain drivetrain, PIDLoop movementPIDLoop, double setpoint){
        this(drivetrain, movementPIDLoop, () -> setpoint);
    }

    /**
     * starts the given PIDLoop.
     */
    @Override
    public void initialize() {
        movementPIDLoop.enable();
    }

    /**
     * updates the PIDLoop's setpoint.
     */
    @Override
    public void execute() {
        movementPIDLoop.setSetpoint(setpoint.get());
        movementPIDLoop.update();
    }

    @Override
    public void end(boolean interrupted) {
        movementPIDLoop.disable();
        drivetrain.stop();
    }

    @Override
    public boolean isFinished() {
        return movementPIDLoop.onTarget();
    }

}
