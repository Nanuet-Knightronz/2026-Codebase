// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Indexer;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import java.util.function.DoubleSupplier;
import org.littletonrobotics.junction.Logger;

public class IndexerSubsystem extends SubsystemBase {
  /** Creates a new IndexerSubsystem. */

  private final IndexerIOSparkMax io;
  private final IndexerIO.IndexerIOInputs inputs = new IndexerIO.IndexerIOInputs();

  public IndexerSubsystem(IndexerIOSparkMax io) {
    this.io = io;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

  public void setMotorSpeeds(double cakePercent, double feederPercent) {
    io.cakePower(cakePercent);
    io.feederPower(feederPercent);
  }

  public void stop() {
        setMotorSpeeds(0.0, 0.0);
  }

  public Command runCommand(DoubleSupplier spinPercent, DoubleSupplier feederPercent) {
        return runEnd(() -> setMotorSpeeds(spinPercent.getAsDouble(), feederPercent.getAsDouble()), this::stop)
                .withName("SpindexerRunCommand");
    }
}
