package frc.robot.subsystems.Mechanisms.Intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import org.littletonrobotics.junction.AutoLogOutput;

import frc.robot.util.LoggedTunableNumber;

public class IntakeSubsystem extends SubsystemBase {

    private static boolean hasInstance = false;

    // Tunable PID gains (ONLY used when changed)
    private final LoggedTunableNumber kP = new LoggedTunableNumber("Intake/kP", 0.1);
    private final LoggedTunableNumber kI = new LoggedTunableNumber("Intake/kI", 0.0);
    private final LoggedTunableNumber kD = new LoggedTunableNumber("Intake/kD", 0.0);
    private final LoggedTunableNumber kS = new LoggedTunableNumber("Intake/kS", 0.0);
    private final LoggedTunableNumber kV = new LoggedTunableNumber("Intake/kV", 0.1);

    private final IntakeIO io;
    private final IntakeIO.IntakeIOInputs inputs = new IntakeIO.IntakeIOInputs();

    @AutoLogOutput(key = "Intake/Position")
    private double position = 0.0;

    @AutoLogOutput(key = "Intake/Setpoint")
    private double setpoint = 0.0;

    private boolean isDown = false;

    // Limits (adjust these!)
    private static final double MIN_ANGLE = 0;
    private static final double MAX_ANGLE = 110;

    public IntakeSubsystem(IntakeIO io) {
        if (hasInstance) throw new IllegalStateException("IntakeSubsystem already exists");
        hasInstance = true;
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);

        position = inputs.position;

        // Only update PID if changed
        LoggedTunableNumber.ifChanged(
            hashCode(),
            () -> io.configPID(kP.get(), kI.get(), kD.get(), kV.get(), kS.get()),
            kP, kI, kD, kV, kS
        );

        SmartDashboard.putNumber("Intake/Position", position);
        SmartDashboard.putNumber("Intake/Setpoint", setpoint);
    }

    public boolean isDown() {
        return isDown;
    }

    /** Move arm safely */
    public void moveToPosition(double degrees) {
        double clamped = MathUtil.clamp(degrees, MIN_ANGLE, MAX_ANGLE);
        setpoint = clamped;
        io.setPosition(clamped);
    }

    /** Move up */
    public Command moveUpCommand() {
        return runOnce(() -> {
            moveToPosition(82.57156372070312);
            isDown = false;
        }).withName("IntakeUp");
    }

    /** Move down */
    public Command moveDownCommand() {
        return runOnce(() -> {
            moveToPosition(0);
            isDown = true;
        }).withName("IntakeDown");
    }

    /** Toggle between up/down */
    public Command toggleCommand() {
        return Commands.runOnce(() -> {
            if (isDown) {
                moveToPosition(70);
            } else {
                moveToPosition(0);
            }
            isDown = !isDown;
        }, this).withName("IntakeToggle");
    }

    /** Run rollers inward */
    public Command intakeInCommand() {
    return runEnd(
        () -> io.setRollerVoltage(-6),
        () -> io.setRollerVoltage(0)
    );
}

    /** Stop arm motor */
    public void stop() {
        io.setVoltage(0);
    }

    public void zeroPosition() {
        io.zeroEncoder();   // delegate to IO layer
        setpoint = 0;       // keep control stable
    }

    public Command zeroCommand() {
      return runOnce(this::zeroPosition).withName("IntakeZero");
    }

    /** Hold last position (IMPORTANT: set as default command) */
    public Command holdPositionCommand() {
        return runEnd(() -> moveToPosition(setpoint), this::stop);
    }
}
