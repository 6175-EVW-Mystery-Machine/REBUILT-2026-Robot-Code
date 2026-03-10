package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.TurretFlywheel;
import frc.robot.subsystems.TurretRing;

public class StopAutoShootCommand extends InstantCommand {
  private final TurretFlywheel turretFlywheel;
  private final TurretRing turretRing;
  private final CTRE_CANdle CANdle;
  private final Indexer indexer;
  private final Feeder feeder;
  private final Intake intake;

  public StopAutoShootCommand(TurretFlywheel turretFlywheel, TurretRing turretRing, CTRE_CANdle CANdle, Indexer indexer, Feeder feeder, Intake intake) {
    this.turretFlywheel = turretFlywheel;
    this.turretRing = turretRing;
    this.CANdle = CANdle;
    this.indexer = indexer;
    this.feeder = feeder;
    this.intake = intake;
    addRequirements(turretFlywheel, turretRing, CANdle, indexer, feeder, intake);
  }

  @Override
  public void execute() {
    CANdle.v_stopAll();
    turretRing.v_stopMotor();
    turretFlywheel.v_stopMotors();
      feeder.v_stopMotor();
        indexer.v_stopMotor();
          intake.v_stopMotor();
  }
}
