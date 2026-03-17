package frc.robot.subsystems.Mechanisms.Shooter;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.LinearFilter;
import edu.wpi.first.wpilibj.Alert;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.*;

import java.util.function.DoubleSupplier;

import org.littletonrobotics.junction.*;

import frc.robot.Constants.Constants;
import frc.robot.subsystems.Mechanisms.Shooter.ShooterIO.ShooterIOInputs;
import frc.robot.util.LoggedTracer;
import frc.robot.util.LoggedTunableNumber;

public class ShooterSubsystem extends SubsystemBase {

    private static boolean hasInstance = false;

    private final LoggedTunableNumber kP = new LoggedTunableNumber("Shooter/kP", 0.12);
    private final LoggedTunableNumber kI = new LoggedTunableNumber("Shooter/kI", 0.0);
    private final LoggedTunableNumber kD = new LoggedTunableNumber("Shooter/kD", 0.0);
    private final LoggedTunableNumber kS = new LoggedTunableNumber("Shooter/kS", 0.0);
    private final LoggedTunableNumber kV = new LoggedTunableNumber("Shooter/kV", 0.12);

    private final ShooterIO io;
    private final ShooterIOInputs inputs = new ShooterIOInputs();

    private final LinearFilter filter = LinearFilter.movingAverage(5);

    @AutoLogOutput(key = "Shooter/FilteredRPM")
    private double filteredRPM = 0.0;

    @AutoLogOutput(key = "Shooter/SetpointRPM")
    private double setpointRPM = 0.0;

    private final Alert leftDisconnected =
        new Alert("Shooter left motor disconnected", Alert.AlertType.kError);
    private final Alert rightDisconnected =
        new Alert("Shooter right motor disconnected", Alert.AlertType.kError);

    public ShooterSubsystem(ShooterIO io) {
        if (hasInstance) throw new IllegalStateException("Shooter already exists");
        hasInstance = true;
        this.io = io;
    }

    @Override
    public void periodic() {
        LoggedTracer.reset();

        io.updateInputs(inputs);

        filteredRPM = filter.calculate(getVelocityRPM());

        if (DriverStation.isDisabled()) {
            stop();
        }

        LoggedTunableNumber.ifChanged(
            hashCode(),
            () -> io.configPID(kP.get(), kI.get(), kD.get(), kV.get(), kS.get()),
            kP, kI, kD, kV, kS
        );

        leftDisconnected.set(!inputs.leftMotorConnected);
        rightDisconnected.set(!inputs.rightMotorConnected);

        Command active = CommandScheduler.getInstance().requiring(this);
        Logger.recordOutput("Shooter/ActiveCommand",
            active != null ? active.getName() : "None");

        LoggedTracer.record("Shooter");
    }

    public void runVelocity(double rpm) {
        setpointRPM = rpm;
        io.setVelocityRPM(rpm);
    }

    public void setVoltage(double volts) {
        io.setVoltage(MathUtil.clamp(volts, -12, 12));
    }

    public void stop() {
        setVoltage(0);
        setpointRPM = 0;
    }

    public double getVelocityRPM() {
        return (inputs.leftVelocityRPM + inputs.rightVelocityRPM) / 2.0;
    }

    @AutoLogOutput(key = "Shooter/AtSetpoint")
    public boolean atSetpoint() {
        return MathUtil.isNear(setpointRPM, filteredRPM, 100);
    }

    public Command commandVelocity(DoubleSupplier rpmSupplier) {
        return runEnd(
            () -> runVelocity(rpmSupplier.getAsDouble()),
            this::stop
        ).withName("ShooterVelocity");
    }

    public Command stopCommand() {
        return runOnce(this::stop);
    };
}