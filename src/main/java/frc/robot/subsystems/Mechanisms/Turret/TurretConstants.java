// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.Mechanisms.Turret;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.util.Units;

/** Turret Constants */
public class TurretConstants {
    public static final int BIG_ENCODER_TEETH_COUNT = 19;
    public static final int SMALL_ENCODER_TEETH_COUNT = 18;
    public static final int TURRET_TEETH_COUNT = 200;
    public static final int MOTOR_TEETH_COUNT = 19;

    public static final double TOTAL_GEAR_RATIO = 800.0 / 19.0;

    public static final double kP = 0.0;
    public static final double kI = 0.0;
    public static final double kD = 0.0;

    public static final int TURRET_MOTOR_ID = 37;
    public static final int BIG_ENCODER_ID = 1;
    public static final int SMALL_ENCODER_ID = 0;

    public static final double POSITION_ERROR_DEGREES = 5;

    public static final double BIG_ENCODER_OFFSET = 0.0; 
    public static final double SMALL_ENCODER_OFFSET = 0.0; 

    public static final double TURRET_RPM = 120; 
    public static final double TURRET_RPM_S = 300;

    public static Transform2d ROBOT_TO_TURRET = 
        new Transform2d(Units.inchesToMeters(-6.25), -0.5, Rotation2d.kZero);
    public static Transform3d ROBOT_TO_TURRET_3D 
        = new Transform3d(Units.inchesToMeters(-6.25), -0.5, Units.inchesToMeters(11), Rotation3d.kZero);
}
