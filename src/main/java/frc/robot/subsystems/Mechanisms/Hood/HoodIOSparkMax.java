package frc.robot.subsystems.Mechanisms.Hood;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.util.Units;

public class HoodIOSparkMax implements HoodIO {

  private final SparkMax motor;
  private final RelativeEncoder encoder;

  private final double gearRatio = 20.0; // total reduction

  public HoodIOSparkMax() {
    motor = new SparkMax(15, SparkMax.MotorType.kBrushless);
    encoder = motor.getEncoder();

    SparkMaxConfig config = new SparkMaxConfig();

    config.encoder.positionConversionFactor(1.0 / gearRatio);
    config.encoder.velocityConversionFactor(1.0 / gearRatio);

    config.smartCurrentLimit(20);
    config.voltageCompensation(12.0);

    motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void setPosition(Rotation2d position) {
    motor.getClosedLoopController().setReference(
        position.getRotations(),
        ControlType.kPosition
    );
  }

  @Override
  public void setVoltage(double volts) {
    motor.setVoltage(volts);
  }

  @Override
  public Rotation2d getPosition() {
    return Rotation2d.fromRotations(encoder.getPosition());
  }

  @Override
  public void zeroEncoder() {
    encoder.setPosition(0.0);
  }
}