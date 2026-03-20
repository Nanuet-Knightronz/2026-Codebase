package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;

import java.util.function.BooleanSupplier;

import org.littletonrobotics.junction.Logger;

import frc.robot.subsystems.Mechanisms.Turret.TurretSubsystem;
import frc.robot.subsystems.Mechanisms.Shooter.ShooterSubsystem;
import frc.robot.subsystems.Mechanisms.Indexer.IndexerSubsystem;

public class FeedingCommands {

    /** Core logic: should we feed? */
    public static boolean shouldFeed(
        TurretSubsystem turret,
        ShooterSubsystem shooter,
        BooleanSupplier override
    ) {
        return (
            // turret.atSetpoint() &&     // turret aligned
            shooter.atSetpoint()       // shooter at speed
        ) || override.getAsBoolean();
    }

    /** Basic feed command */
    public static Command feedCommand(
        TurretSubsystem turret,
        ShooterSubsystem shooter,
        IndexerSubsystem indexer
    ) {
        return feedCommand(turret, shooter, indexer, () -> false);
    }

    /** Feed command with override */
    public static Command feedCommand(
        TurretSubsystem turret,
        ShooterSubsystem shooter,
        IndexerSubsystem indexer,
        BooleanSupplier override
    ) {
        return indexer.runCommand(
                () -> shouldFeed(turret, shooter, override) ? 1.5 : -1.0,  // spindexer
                () -> shouldFeed(turret, shooter, override) ? -1.5 : 1.0   // feeder
            )
            .alongWith(
                Commands.run(() -> {
                    boolean feeding = shouldFeed(turret, shooter, override);

                    Logger.recordOutput("Feeding/IsFeeding", feeding);
                    Logger.recordOutput("Feeding/Override", override.getAsBoolean());
                    // Logger.recordOutput("Feeding/TurretReady", turret.atSetpoint());
                    Logger.recordOutput("Feeding/ShooterReady", shooter.atSetpoint());
                })
            )
            .withName("FeedShooterCommand");
    }
}