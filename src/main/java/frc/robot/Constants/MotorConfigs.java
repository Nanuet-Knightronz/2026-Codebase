// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Constants;

import com.revrobotics.spark.SparkMax.*;
import com.revrobotics.spark.*;
import com.revrobotics.spark.config.*;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.thethriftybot.devices.ThriftyNova;
import com.thethriftybot.devices.ThriftyEncoder;
import com.thethriftybot.devices.ThriftyNova.ThriftyNovaConfig;

/** Motor Configurations */
public interface MotorConfigs {

    public interface Climber {
        SparkBaseConfig MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kBrake);
    }

    public interface Indexer {
        SparkBaseConfig MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kCoast);
    }

    public interface Intake {
        SparkBaseConfig TOP_ROLLER_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kCoast);
    
        SparkBaseConfig BOTTOM_ROLLER_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kCoast)
            .follow(Constants.IntakeConstants.topSparkMaxID);

        // SparkBaseConfig PIVOT_MOTOR_CONFIG = new SparkMaxConfig()
        //     .smartCurrentLimit(40)
        //     .idleMode(IdleMode.kBrake);
    }

    public interface Shooter {
        SparkBaseConfig FLYWHEEL_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(60)
            .idleMode(IdleMode.kCoast);

        SparkBaseConfig HOOD_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(20)
            .idleMode(IdleMode.kBrake);
    }

    public interface Turret {
        SparkBaseConfig TURRET_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kBrake);
    }
}
