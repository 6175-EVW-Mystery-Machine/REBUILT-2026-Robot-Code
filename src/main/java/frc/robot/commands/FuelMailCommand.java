package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Intake;

public class FuelMailCommand extends Command {
  private final Intake intake;
  private final CTRE_CANdle CANdle;

  public FuelMailCommand(Intake intake, CTRE_CANdle CANdle) {
    this.intake = intake;
    this.CANdle = CANdle;
    addRequirements(intake, CANdle);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    CANdle.v_fuelMail();
    intake.v_runWheels(-1500);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    CANdle.v_clearIntake();
    intake.v_stopMotor();
  }
}