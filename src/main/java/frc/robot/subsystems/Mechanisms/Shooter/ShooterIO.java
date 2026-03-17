// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Shooter;

import org.littletonrobotics.junction.AutoLog;

/** Creates ShooterIO */

public interface ShooterIO {

    @AutoLog
    public static class ShooterIOInputs {
        public double leftVelocityRPM = 0.0;
        public double rightVelocityRPM = 0.0;

        public double leftAppliedVolts = 0.0;
        public double rightAppliedVolts = 0.0;

        public double leftCurrentAmps = 0.0;
        public double rightCurrentAmps = 0.0;

        public boolean leftMotorConnected = true;
        public boolean rightMotorConnected = true;
    }

    default void updateInputs(ShooterIOInputs inputs) {}

    default void setVelocityRPM(double rpm) {}

    default void setVoltage(double volts) {}

    default void configPID(double kP, double kI, double kD, double kV, double kS) {}
}