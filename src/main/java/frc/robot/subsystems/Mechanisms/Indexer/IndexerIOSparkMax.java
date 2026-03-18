// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Indexer;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import frc.robot.subsystems.Mechanisms.Indexer.IndexerIO.IndexerIOInputs;;

/** Add your docs here. */
public class IndexerIOSparkMax {

    private final SparkMax cake = new SparkMax(37, MotorType.kBrushless);
    private final SparkMax feeder = new SparkMax(38, MotorType.kBrushless);

    private final SparkClosedLoopController cakeController;
    private final SparkClosedLoopController feederController;

    public IndexerIOSparkMax() {
            SparkMaxConfig config = new SparkMaxConfig();
    
            config.idleMode(IdleMode.kCoast);
    
            config.smartCurrentLimit(40);
            config.voltageCompensation(12);
            config.closedLoop.outputRange(-1, 1);
            
            config.closedLoop.p(.12);
            config.closedLoop.i(0.0);
            config.closedLoop.d(0.0);

        cakeController = cake.getClosedLoopController();
        feederController = feeder.getClosedLoopController();

        cake.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        feeder.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void updateInputs(IndexerIOInputs inputs) {
        inputs.cakeRPM = cake.getAppliedOutput() * 5676;
        inputs.feederRPM = feeder.getAppliedOutput() * 5676;

        // inputs.cakeAppliedVolts  = cake.get();
        // inputs.feederAppliedVolts  = feeder.getOutputCurrent();

        inputs.cakeCurrentAmps = cake.getOutputCurrent();
        inputs.feederCurrentAmps = feeder.getOutputCurrent();
    }

    public void cakePower(double speed) {
        cake.set(speed);
    }

    public void feederPower(double speed) {
        feeder.set(speed);
    }
}
