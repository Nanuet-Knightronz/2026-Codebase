// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Turret;
import frc.robot.Constants.MotorConfigs.Turret;
import frc.robot.subsystems.Mechanisms.Turret.TurretConstants;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkMaxAlternateEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.config.MAXMotionConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig;
import com.revrobotics.spark.config.MAXMotionConfig.MAXMotionPositionMode;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.units.Units;
import edu.wpi.first.wpilibj.DutyCycleEncoder;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.math.geometry.Rotation2d;

import com.revrobotics.spark.SparkClosedLoopController;
/** Add your docs here. */
public class TurretIO {

    private static final MAXMotionPositionMode positionControl = MAXMotionPositionMode.kMAXMotionTrapezoidal;
    private static final MAXMotionConfig motionConfig = new MAXMotionConfig();
    private final SparkClosedLoopController turretController;

    private SparkMax turretMotor = new SparkMax(TurretConstants.TURRET_MOTOR_ID, SparkMax.MotorType.kBrushless);
    private final DutyCycleEncoder bigEncoder = new DutyCycleEncoder(TurretConstants.BIG_ENCODER_ID);
    private final AbsoluteEncoder smallEncoder = turretMotor.getAbsoluteEncoder();
    private final RelativeEncoder relativeEncoder = turretMotor.getEncoder();

    private SparkMaxConfig config;

    public TurretIO() {
        
        config = new SparkMaxConfig();

        config.idleMode(IdleMode.kBrake);
        config.softLimit.forwardSoftLimit(0);
        config.softLimit.reverseSoftLimit(0);
        config.encoder.positionConversionFactor(1 / TurretConstants.TOTAL_GEAR_RATIO);
        config.encoder.velocityConversionFactor(1 / TurretConstants.TOTAL_GEAR_RATIO);

        config.smartCurrentLimit(40);
        config.voltageCompensation(12);
        config.closedLoop.outputRange(-1, 1, null);
        
        config.closedLoop.p(0);
        config.closedLoop.i(0);
        config.closedLoop.d(0);

        config.closedLoop.feedForward
            .kS(0)
            .kV(0)
            .kA(0);
        
        config.closedLoop.maxMotion.cruiseVelocity(TurretConstants.TURRET_RPM);
        config.closedLoop.maxMotion.maxAcceleration(TurretConstants.TURRET_RPM_S);

        turretMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        turretController = turretMotor.getClosedLoopController();
    }

    public void setVoltage(double volts) {
        turretMotor.setVoltage(volts);
    }

    public void setSetpoint(Rotation2d position) {
        turretController.setSetpoint(position.getRotations(), ControlType.kMAXMotionPositionControl);
    }

    public void seedMotorPosition(Rotation2d position) {
        relativeEncoder.setPosition(position.getRotations());
    }

    public void turretData() {
        double motorTemp = turretMotor.getMotorTemperature();
        double motorCurrent = turretMotor.getOutputCurrent();
    }

    public Rotation2d getSmallEncoderPosition() {
    return Rotation2d.fromRotations(
        smallEncoder.getPosition() - TurretConstants.SMALL_ENCODER_OFFSET
    );
}

public Rotation2d getBigEncoderPosition() {
    return Rotation2d.fromRotations(
        bigEncoder.get() - TurretConstants.BIG_ENCODER_OFFSET
    );
}

public Rotation2d getRelativeEncoderPosition() {
    return Rotation2d.fromRotations(relativeEncoder.getPosition());
}
}
