package frc.robot.subsystems.Mechanisms.Turret;

import java.util.function.Supplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static edu.wpi.first.units.Units.Rotations;
import yams.units.EasyCRT;
import yams.units.EasyCRTConfig;

public class TurretSubsystem extends SubsystemBase {

  private final TurretIO io;

  private boolean initialized = false;
  private double enableTimer = 0.0;
  private final EasyCRTConfig easyCrtConfig;
  private final EasyCRT crt;

  private Rotation2d zeroOffset = Rotation2d.kZero;

  public TurretSubsystem(TurretIO io) {
    this.io = io;
    this.easyCrtConfig = buildEasyCrtConfig();
    this.crt = new EasyCRT(easyCrtConfig);
  }

  private EasyCRTConfig buildEasyCrtConfig() {
    return new EasyCRTConfig(
      () -> Rotations.of(io.getSmallEncoderPosition().getRotations()),
      () -> Rotations.of(io.getBigEncoderPosition().getRotations())
    )
    .withCommonDriveGear(
        1.0,   
        200,   
        19,    
        21     
    )
    .withAbsoluteEncoderOffsets(
       Rotations.of(0.0),
       Rotations.of(0.0)
    )
    .withAbsoluteEncoderInversions(
        true,
        true
    );
  }

  private double normalizeRotations(double rotations) {
    rotations %= 1.0;
    return rotations < 0 ? rotations + 1.0 : rotations;
  }

  private Rotation2d getAbsoluteAngle() {
    return crt.getAngleOptional()
      .map(angle -> 
        Rotation2d.fromRotations(
          angle.in(Rotations)
        )
      )
      .orElse(Rotation2d.kZero);
  }

  public Rotation2d getPosition() {
    return io.getRelativeEncoderPosition();
  }

  private void seedRelativeEncoder() {
    io.seedMotorPosition(getAbsoluteAngle());
  }

  public void stop() {
    io.setVoltage(0.0);
  }

  public void setVoltage(double volts) {
    io.setVoltage(MathUtil.clamp(volts, -12.0, 12.0));
  }

  private Rotation2d unwrapTarget(Rotation2d target) {
    double targetRotations = target.getRotations();
    double currentRotations = getPosition().getRotations();

    double bestCandidate = targetRotations;
    double smallestError = Double.POSITIVE_INFINITY;

    for (int offset = -2; offset <= 2; offset++) {

      double candidate = targetRotations + offset;

      boolean withinLimits =
          candidate >= TurretConstants.MIN_ANGLE.getRotations() &&
          candidate <= TurretConstants.MAX_ANGLE.getRotations();

      if (!withinLimits) continue;

      double error = Math.abs(candidate - currentRotations);

      if (error < smallestError) {
        smallestError = error;
        bestCandidate = candidate;
      }
    }

    return Rotation2d.fromRotations(bestCandidate);
  }

  public void setTarget(Rotation2d target) {
    Rotation2d correctedTarget = target.plus(zeroOffset);
    io.setSetpoint(unwrapTarget(correctedTarget));
  }

  public Command zero() {
    return runOnce(() -> {
      zeroOffset = getAbsoluteAngle();
      seedRelativeEncoder();
    });
  }

  public Command zeroCommand() {
    return runOnce(() -> zeroOffset = getAbsoluteAngle());
  }

  public Command aimCommand(
      Supplier<Rotation2d> targetSupplier,
      boolean fieldRelative,
      Supplier<Rotation2d> robotHeadingSupplier
  ) {
    return runEnd(
        () -> {
          Rotation2d target = targetSupplier.get();

          if (fieldRelative) {
            target = target.minus(robotHeadingSupplier.get());
          }

          setTarget(target);
        },
        this::stop
    );
  }

  @Override
  public void periodic() {
    if (RobotState.isEnabled()) {
      enableTimer += 0.02;

      if (!initialized && enableTimer > 0.25) {
        seedRelativeEncoder();
        initialized = true;
      }
    } else {
      enableTimer = 0.0;
      initialized = false;
      stop();
    }
  }
}