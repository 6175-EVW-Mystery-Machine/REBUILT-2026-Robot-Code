package frc.robot.commands;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.swerve.SwerveDrivetrain;
import com.ctre.phoenix6.swerve.SwerveModule;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveControlParameters;
import com.ctre.phoenix6.swerve.SwerveDrivetrain.SwerveDriveState;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.TurretFlywheel;
import frc.robot.subsystems.TurretRing;

public class ShootCommand extends Command {
  private final TurretRing turretRing;
  private final TurretFlywheel turretFlywheel;
  private final Indexer indexer;
  private final Feeder feeder;
  private final Intake intake;
  private final CTRE_CANdle CANdle;
  private final CommandXboxController controller;

  public ShootCommand(TurretRing turretRing,
  TurretFlywheel turretFlywheel,
  Indexer indexer,
  Feeder feeder,
  Intake intake,
  CTRE_CANdle CANdle,
  CommandXboxController controller) {
    this.turretRing = turretRing;
    this.turretFlywheel = turretFlywheel;
    this.indexer = indexer;
    this.feeder = feeder;
    this.intake = intake;
    this.CANdle = CANdle;
    this.controller = controller;
    addRequirements(turretRing, turretFlywheel, indexer, feeder, intake, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    CANdle.v_runAll();
    turretRing.v_positionTurret();
    controller.setRumble(RumbleType.kRightRumble, 1);
    turretFlywheel.v_runWheel();
      feeder.v_runWheels(4400);
        indexer.v_runWheels(1500);
          intake.v_runWheels(1100);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    controller.setRumble(RumbleType.kRightRumble, 0);
    CANdle.v_stopAll();
    turretRing.v_stopMotor();
      turretFlywheel.v_stopMotors();
        indexer.v_stopMotor();
          feeder.v_stopMotor();
            intake.v_stopMotor();
  }
}
