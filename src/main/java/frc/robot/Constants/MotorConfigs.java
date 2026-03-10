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

        SparkBaseConfig RIGHT_FLYWHEEL_MOTOR_CONFIG = createRightFlywheelConfig();

        private static SparkBaseConfig createRightFlywheelConfig() {
            SparkBaseConfig config = new SparkMaxConfig()
                .smartCurrentLimit(60)
                .idleMode(IdleMode.kCoast)
                .voltageCompensation(12);

            // configure PID here
            config.closedLoop
                .feedbackSensor(FeedbackSensor.kAlternateOrExternalEncoder)
                .pid(0.00025, 0.0, 0.0)
                .velocityFF(0.00018)
                .outputRange(-1, 1);

            return config;
        }

        SparkBaseConfig LEFT_FLYWHEEL_MOTOR_CONFIG = createLeftFlywheelConfig();

        private static SparkBaseConfig createLeftFlywheelConfig() {
            SparkBaseConfig config = new SparkMaxConfig()
                .smartCurrentLimit(60)
                .idleMode(IdleMode.kCoast)
                .voltageCompensation(12)
                .follow(25, true);

            config.closedLoop
                .feedbackSensor(FeedbackSensor.kAlternateOrExternalEncoder)
                .pid(0.00025, 0.0, 0.0)
                .velocityFF(0.00018)
                .outputRange(-1, 1);

            return config;
        }

        SparkBaseConfig HOOD_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(20)
            .idleMode(IdleMode.kBrake);

        EncoderConfig FLYWHEEL_ENCODER_CONFIG = new EncoderConfig()
            .countsPerRevolution(8192);
    }

    public interface Turret {
        SparkBaseConfig TURRET_MOTOR_CONFIG = new SparkMaxConfig()
            .smartCurrentLimit(40)
            .idleMode(IdleMode.kBrake);
    }
}
