// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.estimator.KalmanTypeFilter;
import edu.wpi.first.wpilibj.Encoder;

import frc.robot.Constants.Constants.ShooterConstants;
import frc.robot.Constants.MotorConfigs.Shooter;
import frc.robot.Constants.MotorConfigs;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkMaxAlternateEncoder;
import com.revrobotics.spark.SparkRelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkAbsoluteEncoder;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;

public class ShooterSubsystem extends SubsystemBase {
  /** Creates a new ShooterSubsystem. */
  private SparkMax LeftFlywheelMotor;
  private SparkMax RightFlywheelMotor;
  private SparkMax HoodMotor;

  private RelativeEncoder FlywheelEncoder;

  private final SparkPIDController FlywheelPID = RightFlywheelMotor.getPIDController();

  public ShooterSubsystem() {

    //Creates the flywheel motors
    LeftFlywheelMotor = new SparkMax(ShooterConstants.leftFlywheelMotorID, MotorType.kBrushless);
    RightFlywheelMotor = new SparkMax(ShooterConstants.rightFlywheelMotorID, MotorType.kBrushless);

    //Creates the hood motor
    HoodMotor = new SparkMax(ShooterConstants.hoodMotorID, MotorType.kBrushless);

    //Creates the flywheel encoder
    FlywheelEncoder = RightFlywheelMotor.getAlternateEncoder();

    //Creates the hood encoder
    // RelativeEncoder HoodEncoder = HoodMotor.getAlternateEncoder();

    //--------------------------------------------------------------------------------------------------------------------------
    //Apply configs to devices
    LeftFlywheelMotor.configure(Shooter.LEFT_FLYWHEEL_MOTOR_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    RightFlywheelMotor.configure(Shooter.RIGHT_FLYWHEEL_MOTOR_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    HoodMotor.configure(Shooter.HOOD_MOTOR_CONFIG, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void setShooterSpeed(double velocity) {
    RightFlywheelMotor.set(velocity);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

  }
}
