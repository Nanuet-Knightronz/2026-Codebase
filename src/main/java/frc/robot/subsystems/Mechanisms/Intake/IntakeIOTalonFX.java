package frc.robot.subsystems.Mechanisms.Intake;

import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue; 
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.NeutralModeValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.controls.VoltageOut;

public class IntakeIOTalonFX implements IntakeIO{
    
    private final TalonFX left = new TalonFX(47);
    private final TalonFX right = new TalonFX(48);

    private final VoltageOut voltageRequest = new VoltageOut(0.0);
    public final Follower followerRequest = new Follower(47, MotorAlignmentValue.Opposed);

    public IntakeIOTalonFX() {
        TalonFXConfiguration config = new TalonFXConfiguration();

        config.MotorOutput.NeutralMode = NeutralModeValue.Brake;
        config.MotorOutput.Inverted = InvertedValue.CounterClockwise_Positive;

        left.getConfigurator().apply(config);
        right.getConfigurator().apply(config);

        left.setControl(voltageRequest);
        right.setControl(followerRequest);

        left.optimizeBusUtilization();
        right.optimizeBusUtilization();
    }

    @Override
    public void setRollerVoltage(double volts) {
        left.setControl(voltageRequest.withOutput(volts));
    }
}
