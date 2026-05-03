package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.TurretFlywheel;
import frc.robot.subsystems.TurretRing;

import static frc.robot.subsystems.TurretRing.VaildTurretPosition;
import static frc.robot.subsystems.TurretFlywheel.ShooterAtSpeed;

public class ShootCommand extends Command {
  private final TurretRing turretRing;
  private final TurretFlywheel turretFlywheel;
  private final Indexer indexer;
  private final Feeder feeder;
  private final Intake intake;
  private final CTRE_CANdle CANdle;

  public ShootCommand(TurretRing turretRing,
  TurretFlywheel turretFlywheel,
  Indexer indexer,
  Feeder feeder,
  Intake intake,
  CTRE_CANdle CANdle) {
    this.turretRing = turretRing;
    this.turretFlywheel = turretFlywheel;
    this.indexer = indexer;
    this.feeder = feeder;
    this.intake = intake;
    this.CANdle = CANdle;
    addRequirements(turretRing, turretFlywheel, indexer, feeder, intake, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
        intake.v_runWheels(775);
    if (VaildTurretPosition == true) {
      turretFlywheel.v_runWheel();
      turretRing.v_positionTurret();
    if (ShooterAtSpeed == true) {
    CANdle.v_runAll();
      feeder.v_runWheels(4400);
        indexer.v_runWheels(2250);
    } else {
      CANdle.v_notTargeted();
      feeder.v_stopMotor();
      indexer.v_stopMotor();
    }
  } else {
  CANdle.v_notTargeted();
    turretRing.v_stopMotor();
      turretFlywheel.v_stopMotors();
        indexer.v_stopMotor();
          feeder.v_stopMotor();
  }
}

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CANdle.v_stopAll();
    turretRing.v_stopMotor();
      turretFlywheel.v_stopMotors();
        indexer.v_stopMotor();
          feeder.v_stopMotor();
            intake.v_stopMotor();
  }
}
