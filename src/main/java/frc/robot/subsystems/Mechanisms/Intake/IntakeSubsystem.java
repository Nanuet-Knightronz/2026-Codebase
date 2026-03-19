package frc.robot.subsystems.Mechanisms.Intake;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.*;
import org.littletonrobotics.junction.AutoLog;
import org.littletonrobotics.junction.AutoLogOutput;

import frc.robot.util.LoggedTunableNumber;

/** Intake Arm Subsystem using NEO with Spark MAX */
public class IntakeSubsystem extends SubsystemBase {

    private static boolean hasInstance = false;

    // Tunable PID gains for Spark MAX position control
    private final LoggedTunableNumber kP = new LoggedTunableNumber("Intake/kP", 0.1);
    private final LoggedTunableNumber kI = new LoggedTunableNumber("Intake/kI", 0.0);
    private final LoggedTunableNumber kD = new LoggedTunableNumber("Intake/kD", 0.0);
    private final LoggedTunableNumber kS = new LoggedTunableNumber("Intake/kS", 0.0);
    private final LoggedTunableNumber kV = new LoggedTunableNumber("Intake/kV", 0.1);

    private final IntakeIO io;
    private final IntakeIO.IntakeIOInputs inputs = new IntakeIO.IntakeIOInputs();

    @AutoLogOutput(key = "Intake/FilteredPosition")
    private double filteredPosition = 0.0;

    @AutoLogOutput(key = "Intake/Setpoint")
    private double setpoint = 0.0;

    /** Alerts for motor connection */
    // Optional if you want to monitor connection
    // private final Alert motorDisconnected = new Alert("Intake motor disconnected", Alert.AlertType.kError);

    /** Singleton pattern */
    public IntakeSubsystem(IntakeIO io) {
        if (hasInstance) throw new IllegalStateException("IntakeSubsystem already exists");
        hasInstance = true;
        this.io = io;
    }

    @Override
    public void periodic() {
        io.updateInputs(inputs);

        // Optional: simple moving average filter
        filteredPosition = inputs.position; // or add a LinearFilter if needed

        // Update PID gains if tunable
        io.configPID(kP.get(), kI.get(), kD.get(), kV.get(), kS.get());

        // Optionally check connection
        // motorDisconnected.set(!inputs.motorConnected);
        SmartDashboard.putNumber("Arm Position", filteredPosition);
        SmartDashboard.putNumber("Arm Setpoint", setpoint);
    }

    private boolean isDown = false;

    public boolean isDown() {
      return isDown;
    }

    /** Move arm to a position (degrees) */
    public void moveToPosition(double degrees) {
        setpoint = degrees;
        io.setPosition(degrees);
    }

    /** Simple command for up position */
    public Command moveUpCommand() {
        return runOnce(() -> moveToPosition(100)); // adjust your up angle
    }

    /** Simple command for down position */
    public Command moveDownCommand() {
        return runOnce(() -> moveToPosition(0)); // parallel to ground
    }

    public Command intakeInCommand() {
    return runEnd(
        () -> io.setRollerVoltage(-6),
        () -> io.setRollerVoltage(0)
    );
}


    /** Stop motor (optional) */
    public void stop() {
        io.setVoltage(0);
    }

    /** Create a command that holds the current setpoint */
    public Command holdPositionCommand() {
        return runEnd(() -> moveToPosition(setpoint), this::stop);
    }

    public Command toggleCommand() {
    return runOnce(() -> {
        if (isDown) {
            moveToPosition(-100);
        } else {
            moveToPosition(-45);
        }
        isDown = !isDown;
    });
}
}