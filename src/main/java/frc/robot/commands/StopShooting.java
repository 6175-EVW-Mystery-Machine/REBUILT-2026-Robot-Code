package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.TurretFlywheel;

public class StopShooting extends SequentialCommandGroup {

  public StopShooting(TurretFlywheel m_flywheel, Feeder m_feeder, Indexer m_indexer, Intake m_intake) {
    addCommands(
      new InstantCommand(() -> m_flywheel.v_stopMotors())
        .alongWith(new InstantCommand(() -> m_feeder.v_stopMotor()))
          .alongWith(new InstantCommand(() -> m_indexer.v_stopMotor()))
            .alongWith(new InstantCommand(() -> m_intake.v_stopMotor()))
    );
  }
}
