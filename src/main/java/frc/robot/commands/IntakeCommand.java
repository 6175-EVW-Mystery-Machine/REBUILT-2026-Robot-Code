package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Intake;

public class IntakeCommand extends Command {
  private final Intake intake;
  private final CTRE_CANdle CANdle;

  public IntakeCommand(Intake intake, CTRE_CANdle CANdle) {
    this.intake = intake;
    this.CANdle = CANdle;
    addRequirements(intake, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    CANdle.v_intakeLights();
    intake.v_runWheels(775);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CANdle.v_clearIntake();
    intake.v_stopMotor();
  }
}