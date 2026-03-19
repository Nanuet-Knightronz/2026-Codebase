package frc.robot.subsystems.Mechanisms.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {

    @AutoLog
    public static class IntakeIOInputs {
    public double position = 0.0;
    public double appliedVolts = 0.0;
    public double currentAmps = 0.0;

    public double topRollerVelocityRPM = 0.0;
    public double bottomRollerVelocityRPM = 0.0;
    public double topRollerVolts = 0.0;
    public double bottomRollerVolts = 0.0;
}

    default void updateInputs(IntakeIOInputs inputs) {}
    default void setPosition(double degrees) {}
    default void setVoltage(double volts) {}
    default void configPID(double kP, double kI, double kD, double kV, double kS) {}

    default void setRollerVoltage(double volts) {}
}