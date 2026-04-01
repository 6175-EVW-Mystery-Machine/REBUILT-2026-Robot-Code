// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class UnJamCommand extends Command {
  private final Indexer indexer;
  private final CTRE_CANdle CANdle;
  private final CommandXboxController driverController;
 
  public UnJamCommand(Indexer indexer, CTRE_CANdle CANdle, CommandXboxController driverController) {
    this.indexer = indexer;
    this.CANdle = CANdle;
    this.driverController = driverController;
    addRequirements(indexer, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    CANdle.v_unJam();
    indexer.v_runWheels(-2000);
    driverController.setRumble(RumbleType.kRightRumble, 1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CANdle.v_clearIndexer();
    indexer.v_stopMotor();
    driverController.setRumble(RumbleType.kLeftRumble, 0);
  }
}
