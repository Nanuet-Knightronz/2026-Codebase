package frc.robot.subsystems.Mechanisms.Hood;

import edu.wpi.first.math.geometry.Rotation2d;

public interface HoodIO {

public class HoodIOInputs {
    public double motorPositionRotations = 0.0;
    public double motorVelocityRPM = 0.0;
    public Rotation2d hoodPosition = Rotation2d.kZero;
    public double appliedVolts = 0.0;
}

  default void updateInputs(HoodIOInputs inputs) {}

  default void setPosition(Rotation2d position) {}

  default void setVoltage(double volts) {}

  default Rotation2d getPosition() {
    return Rotation2d.kZero;
  }

  default void zeroEncoder() {}
}