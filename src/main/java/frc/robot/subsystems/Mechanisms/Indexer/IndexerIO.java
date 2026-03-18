// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Indexer;

import org.littletonrobotics.junction.AutoLog;

/** Creates IndexerIO */
public interface IndexerIO {

    @AutoLog
    public static class IndexerIOInputs {
        public double cakeRPM = 0.0;
        public double feederRPM = 0.0;

        public double cakeAppliedVolts = 0.0;
        public double feederAppliedVolts = 0.0;

        public double cakeCurrentAmps = 0.0;
        public double feederCurrentAmps =0.0;

        public boolean cakeMotorConnected = true;
        public boolean feederMotorConnected = true;
    }

    default void updateInputs(IndexerIOInputs inputs) {}

    default void cakePower(double speed) {}

    default void feederPower(double speed) {}

}
