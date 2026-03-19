package frc.robot.subsystems.Mechanisms.Intake;

import org.littletonrobotics.junction.AutoLog;

public interface IntakeIO {

    @AutoLog
    class IntakeIOInputs {
        public double position = 0.0;
        public double appliedVolts = 0.0;
        public double currentAmps = 0.0;
        public boolean motorConnected = true;
    }

    default void updateInputs(IntakeIOInputs inputs) {}
    default void setPosition(double degrees) {}
    default void setVoltage(double volts) {}
    default void configPID(double kP, double kI, double kD, double kV, double kS) {}
}