package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Indexer;

public class UnJamCommand extends Command {
  private final Indexer indexer;
  private final CTRE_CANdle CANdle;
 
  public UnJamCommand(Indexer indexer, CTRE_CANdle CANdle) {
    this.indexer = indexer;
    this.CANdle = CANdle;
    addRequirements(indexer, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    CANdle.v_unJam();
    indexer.v_runWheels(-2000);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CANdle.v_clearIndexer();
    indexer.v_stopMotor();
  }
}
