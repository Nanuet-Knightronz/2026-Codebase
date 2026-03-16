// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Turret;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.Mechanisms.Turret.TurretConstants;

public class TurretSubsystem extends SubsystemBase {
  /** Creates a new TurretSubsystem. */
  private final TurretIO io;
  private Rotation2d setpointRotation = Rotation2d.kZero;

  public TurretSubsystem(TurretIO turretIO) {
    this.io = turretIO;
  }

  public Rotation2d TurretCRT() {
    double smallEncoderPosition = io.getSmallEncoderPosition().getRotations();
    double bigEncoderPosition = io.getBigEncoderPosition().getRotations();

    double[] smallEncoderPositions = new double[12];
    double[] bigEncoderPositions = new double[12];
    double output = 0.0;
    double minimumValue = 1.0;

    //generate possible positions
    for (int i = 0; i < 12; i++) {
      smallEncoderPositions[i] = 
        (i + (smallEncoderPosition)) * ((double) TurretConstants.SMALL_ENCODER_TEETH_COUNT / TurretConstants.TURRET_TEETH_COUNT);
      bigEncoderPositions[i] = 
        (i + (bigEncoderPosition)) * ((double) TurretConstants.BIG_ENCODER_TEETH_COUNT / TurretConstants.TURRET_TEETH_COUNT);
    }

    for (int i = 0; i < 12; i++) {
      for (int z = 0; z < 12; z++) {
        if (Math.abs(smallEncoderPositions[i] - bigEncoderPositions[z]) < minimumValue) {
          output = (smallEncoderPositions[i] + bigEncoderPositions[z]) / 2.0;
          minimumValue = Math.abs(smallEncoderPositions[i] - bigEncoderPositions[z]);
        }
      }
    }

    Rotation2d lastCRTError = Rotation2d.fromRotations(minimumValue);

    Rotation2d rawPosition = Rotation2d.fromRotations(output);
    double rawPositionDegrees = rawPosition.getDegrees();

    SmartDashboard.putNumber("Turret Raw Rotations", rawPosition.getRotations());
    SmartDashboard.putNumber("Turret Raw Degrees", rawPositionDegrees);

    return Rotation2d.fromRotations(rawPosition.getRotations());
  }

  public Rotation2d unwrapTurretAngle(Rotation2d targetAngle) {
    double targetRot = targetAngle.getRotations();
    double currentRot = io.getRelativeEncoderPosition().getRotations();
    double bestRot = 0.0;
    boolean hasBestRot = false;

    for (int i = -2; i <= 2; i++) {
      double candidate = targetRot + i; // consider multiple revolutions
      if (candidate < TurretConstants.MIN_ANGLE.getRotations() || candidate > TurretConstants.MAX_ANGLE.getRotations()) {
        continue;
      }
      if (!hasBestRot || Math.abs(candidate - currentRot) < Math.abs(bestRot - currentRot)) {
        hasBestRot = true;
        bestRot = candidate;
      }
    }

    return Rotation2d.fromRotations(bestRot);
  }

  public void stop() {
        io.setVoltage(0);
    }

  public void setSetpoint(Rotation2d position) {
        setpointRotation = unwrapTurretAngle(position);
        io.setSetpoint(setpointRotation);
    }

   public Command zeroCommand() {
        return runOnce(this::stop)
                .andThen(runOnce(() -> {
                    io.seedMotorPosition(TurretCRT());
                }))
                .withName("TurretZeroCommand");
    }

    public Command commandToSetpoint(
        Supplier<Rotation2d> rotation,
        boolean isFieldRelative,
        Supplier<Rotation2d> robotHeading) {

    return runEnd(
            () -> setSetpoint(
                    rotation.get().minus(
                            isFieldRelative
                                    ? robotHeading.get()
                                    : Rotation2d.kZero)),
            this::stop)
        .withName("TurretSetpointCommand");
}

    public void setVoltage(double voltage) {
        io.setVoltage(voltage);
    }

    public Rotation2d getPosition() {
        return io.getRelativeEncoderPosition();
    }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if (DriverStation.isDisabled()) {
            stop();
            Rotation2d crtAngle = TurretCRT();   
  }
  }
}