package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.Intake;

public class AutoIntakeCommand extends InstantCommand {
  private final Intake intake;
  private final CTRE_CANdle CANdle;
  
  public AutoIntakeCommand(Intake intake, CTRE_CANdle CANdle) {
    this.intake = intake;
    this.CANdle = CANdle;
    addRequirements(intake, CANdle);
  }

  @Override
  public void execute() {
    CANdle.v_intakeLights();
    intake.v_runWheels(775);
  }
}
