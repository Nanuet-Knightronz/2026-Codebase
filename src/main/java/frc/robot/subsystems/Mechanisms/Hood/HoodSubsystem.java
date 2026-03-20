package frc.robot.subsystems.Mechanisms.Hood;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.util.function.Supplier;

public class HoodSubsystem extends SubsystemBase {

  private final HoodIO io;

  private Rotation2d setpoint = Rotation2d.kZero;

  public HoodSubsystem(HoodIO io) {
    this.io = io;
  }

  public Rotation2d getPosition() {
    return io.getPosition();
  }

  public void stop() {
    io.setVoltage(0);
  }

  public void setTarget(Rotation2d target) {
    setpoint = target;
    io.setPosition(target);
  }

  public Command setAngle(Supplier<Rotation2d> target) {
    return run(() -> setTarget(target.get()));
  }

  public Command stopCommand() {
    return runOnce(this::stop);
  }

  public Command zero() {
    return runOnce(() -> {
      io.zeroEncoder();
      setpoint = Rotation2d.kZero;
    });
  }
}