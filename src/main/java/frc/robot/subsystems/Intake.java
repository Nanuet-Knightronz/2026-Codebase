// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
import frc.robot.Constants;

public class Intake extends SubsystemBase {
  /** Creates a new Intake. */

  private final Spark leftArmSpark;
  private final Spark rightArmSpark;

  private final SparkMax topRollerSparkMax;
  private final SparkMax bottomRollerSparkMax;

  public Intake() {

    //SPARK SETUP
    leftArmSpark = new Spark(Constants.IntakeConstant.leftArmSparkID);
    rightArmSpark = new Spark(Constants.IntakeConstant.rightArmSparkID);

    //SPARKMAX SETUP
    SparkMaxConfig globalConfig = new SparkMaxConfig();
      globalConfig.smartCurrentLimit(40)
      .idleMode(IdleMode.kCoast);

    topRollerSparkMax = new SparkMax(48, MotorType.kBrushless);
    bottomRollerSparkMax = new SparkMax(47, MotorType.kBrushless);

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
