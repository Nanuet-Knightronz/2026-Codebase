package frc.robot.subsystems.Mechanisms.Intake;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.config.*;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.PersistMode;

public class IntakeIOSparkMax implements IntakeIO {

    private final SparkMax armMotor = new SparkMax(14, MotorType.kBrushless);

    // private final SparkMax bottomRoller = new SparkMax(47, MotorType.kBrushless);
    // private final SparkMax topRoller = new SparkMax(48, MotorType.kBrushless);

    private final SparkClosedLoopController controller;

    public IntakeIOSparkMax() {
        SparkMaxConfig arm_config = new SparkMaxConfig();

        arm_config.idleMode(SparkBaseConfig.IdleMode.kBrake);
        arm_config.smartCurrentLimit(40);
        arm_config.voltageCompensation(12);
        arm_config.closedLoop.outputRange(-1, 1);
        arm_config.closedLoop.p(0.1);
        arm_config.closedLoop.i(0.0);
        arm_config.closedLoop.d(0.0);
        arm_config.encoder.positionConversionFactor(360.0 / 60.0); 
        // 60:1 gearbox → motor rotates 60x per arm rotation

        armMotor.configure(arm_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        controller = armMotor.getClosedLoopController();

        // SparkMaxConfig roller_config = new SparkMaxConfig();

        // roller_config.idleMode(SparkBaseConfig.IdleMode.kCoast);
        // roller_config.smartCurrentLimit(20);
        // roller_config.voltageCompensation(12);
        
        // roller_config.inverted(false); // bottom
        // bottomRoller.configure(roller_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        // roller_config.inverted(true); 
        // topRoller.configure(roller_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public void updateInputs(IntakeIOInputs inputs) {
    inputs.position = armMotor.getEncoder().getPosition();
    inputs.appliedVolts = armMotor.getAppliedOutput() * 12.0;
    inputs.currentAmps = armMotor.getOutputCurrent();

    // inputs.topRollerVelocityRPM = topRoller.getEncoder().getVelocity();
    // inputs.bottomRollerVelocityRPM = bottomRoller.getEncoder().getVelocity();
    // inputs.topRollerVolts = topRoller.getAppliedOutput() * 12.0;
    // inputs.bottomRollerVolts = bottomRoller.getAppliedOutput() * 12.0;
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

    // public void setRollerVoltage(double volts) {
    //   topRoller.setVoltage(volts);
    //   bottomRoller.setVoltage(volts);
    // }

    @Override
    public void zeroEncoder() {
        armMotor.getEncoder().setPosition(0);
    }
}