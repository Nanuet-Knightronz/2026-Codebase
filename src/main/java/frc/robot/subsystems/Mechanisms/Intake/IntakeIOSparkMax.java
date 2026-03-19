package frc.robot.subsystems.Mechanisms.Intake;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.*;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;

public class IntakeIOSparkMax implements IntakeIO {

    private final SparkMax armMotor = new SparkMax(14, MotorType.kBrushless);
    private final SparkClosedLoopController controller;

    public IntakeIOSparkMax() {
        SparkMaxConfig config = new SparkMaxConfig();

        config.idleMode(SparkBaseConfig.IdleMode.kBrake);
        config.smartCurrentLimit(40);
        config.voltageCompensation(12);

        config.closedLoop.outputRange(-1, 1);
        config.closedLoop.p(0.1);
        config.closedLoop.i(0.0);
        config.closedLoop.d(0.0);

        // VERY IMPORTANT: position conversion (rotations → degrees)
        config.encoder.positionConversionFactor(360.0 / 60.0); 
        // 60:1 gearbox → motor rotates 60x per arm rotation

        armMotor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        controller = armMotor.getClosedLoopController();
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
        inputs.position = armMotor.getEncoder().getPosition();
        inputs.appliedVolts = armMotor.getAppliedOutput() * 12.0;
        inputs.currentAmps = armMotor.getOutputCurrent();
    }

    @Override
    public void setPosition(double degrees) {
        controller.setReference(degrees, com.revrobotics.spark.SparkBase.ControlType.kPosition);
    }

    @Override
    public void setVoltage(double volts) {
        armMotor.setVoltage(volts);
    }

    public double getPosition() {
        return armMotor.getEncoder().getPosition();
    }
}