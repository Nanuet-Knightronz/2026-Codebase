// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Intake;

import org.littletonrobotics.junction.AutoLog;

/** Create IntakeIO */
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

