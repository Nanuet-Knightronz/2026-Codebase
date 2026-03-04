// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.Constants.IntakeConstants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.motorcontrol.*;

import com.revrobotics.PersistMode;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.MotorConfigs;

public class IntakeSubsystem extends SubsystemBase {
  /** Creates a new Intake. */

  private final Spark leftArmSpark;
  private final Spark rightArmSpark;

  private final SparkMax topRollerSparkMax;
  private final SparkMax bottomRollerSparkMax;

  public IntakeSubsystem() {

    //SPARK SETUP
    leftArmSpark = new Spark(IntakeConstants.leftArmSparkID);
    rightArmSpark = new Spark(IntakeConstants.rightArmSparkID);

    //SPARKMAX SETUP
    SparkMaxConfig globalConfig = new SparkMaxConfig();
      globalConfig.smartCurrentLimit(40)
      .idleMode(IdleMode.kCoast);

    bottomRollerSparkMax = new SparkMax(IntakeConstants.bottomSparkMaxID, MotorType.kBrushless);
    topRollerSparkMax = new SparkMax(IntakeConstants.topSparkMaxID, MotorType.kBrushless);

    topRollerSparkMax.configure(
        globalConfig, 
          ResetMode.kResetSafeParameters, 
          PersistMode.kPersistParameters);

    bottomRollerSparkMax.configure(
        globalConfig, 
          ResetMode.kResetSafeParameters, 
          PersistMode.kPersistParameters);
  }

  public void raiseIntake(double velocity) {
    leftArmSpark.set(velocity);
    rightArmSpark.set(-velocity);
  }

  public void lowerIntake(double velocity) {
    leftArmSpark.set(-velocity);
    rightArmSpark.set(velocity);
  }

  public void runIntake(double intakeVelocity) {
    topRollerSparkMax.set(-intakeVelocity);
    bottomRollerSparkMax.set(-intakeVelocity);
  }

  public Command raiseIntakeCommand() {
    return runEnd(()-> {raiseIntake(1);}, ()-> {raiseIntake(0);});
  }

  public Command lowerIntakeCommand() {
    return runEnd(()-> {raiseIntake(-1);}, ()-> {raiseIntake(0);});
  }

  public Command runIntakeCommand() {
    return runEnd(()-> {runIntake(.35);}, ()-> {runIntake(0);});
  }
  
  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
