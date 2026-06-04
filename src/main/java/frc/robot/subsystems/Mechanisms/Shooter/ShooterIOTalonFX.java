package frc.robot.subsystems.Mechanisms.Shooter;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.controls.VoltageOut;
import com.ctre.phoenix6.configs.TalonFXConfiguration;

public class ShooterIOTalonFX implements ShooterIO {

    private final TalonFX left = new TalonFX(35);
    private final TalonFX right = new TalonFX(25);

    private final VelocityVoltage velocityRequest = new VelocityVoltage(0).withSlot(0);
    private final VoltageOut voltageRequest = new VoltageOut(0);

    public ShooterIOTalonFX() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.kP = 0.5;
        config.Slot0.kI = 0.0;
        config.Slot0.kD = 0.0;
        config.Slot0.kV = 0.13;
        config.Slot0.kS = 0.4;

        config.Feedback.SensorToMechanismRatio = 1;
        config.MotorOutput.NeutralMode = NeutralModeValue.Coast;

        // left.getConfigurator().apply(config);
        // right.getConfigurator().apply(config);

        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        right.getConfigurator().apply(config);

        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;
        left.getConfigurator().apply(config);

        left.setControl(new Follower(25, MotorAlignmentValue.Opposed));
        right.optimizeBusUtilization();
    }

    @Override
    public void updateInputs(ShooterIOInputs inputs) {
        inputs.leftVelocityRPM = left.getVelocity().getValueAsDouble() * 60.0;
        inputs.rightVelocityRPM = right.getVelocity().getValueAsDouble() * 60.0;

        inputs.leftAppliedVolts = left.getMotorVoltage().getValueAsDouble();
        inputs.rightAppliedVolts = right.getMotorVoltage().getValueAsDouble();

        inputs.leftCurrentAmps = left.getSupplyCurrent().getValueAsDouble();
        inputs.rightCurrentAmps = right.getSupplyCurrent().getValueAsDouble();

        inputs.leftMotorConnected = left.isConnected();
        inputs.rightMotorConnected = right.isConnected();
    }

    @Override
    public void setVelocityRPM(double rpm) {
        double rps = rpm / 60.0;
        left.setControl(velocityRequest.withVelocity(rps));
        right.setControl(velocityRequest.withVelocity(-rps));
    }

    @Override
    public void setVoltage(double volts) {
        left.setControl(voltageRequest.withOutput(volts));
        right.setControl(voltageRequest.withOutput(volts));
    }

    @Override
    public void configPID(double kP, double kI, double kD, double kV, double kS) {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.Slot0.kP = kP;
        config.Slot0.kI = kI;
        config.Slot0.kD = kD;
        config.Slot0.kV = kV;
        config.Slot0.kS = kS;

        left.getConfigurator().apply(config);
        right.getConfigurator().apply(config);
    }
}