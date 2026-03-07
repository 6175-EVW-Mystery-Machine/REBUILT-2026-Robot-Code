// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.RobotContainer;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Intake;
import frc.robot.generated.TunerConstants;

/* You should consider using the more terse Command factories API instead https://docs.wpilib.org/en/stable/docs/software/commandbased/organizing-command-based.html#defining-commands */
public class IntakeCommand extends Command {
  private final Intake intake;
  private final CTRE_CANdle CANdle;
  private final CommandXboxController driverController;

  public IntakeCommand(Intake intake, CTRE_CANdle CANdle, CommandXboxController driverController) {
    this.intake = intake;
    this.CANdle = CANdle;
    this.driverController = driverController;
    addRequirements(intake, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    CANdle.v_intakeLights();
    intake.v_runWheels(2400);
    driverController.setRumble(RumbleType.kLeftRumble, 0.5);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CANdle.v_clearIntake();
    intake.v_stopMotor();
    driverController.setRumble(RumbleType.kLeftRumble, 0);
  }
}
