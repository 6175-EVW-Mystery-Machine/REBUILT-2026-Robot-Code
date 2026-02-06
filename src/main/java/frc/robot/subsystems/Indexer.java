package frc.robot.subsystems;

import com.revrobotics.spark.SparkFlex;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {

  private final SparkFlex m_vortex = new SparkFlex(0, MotorType.kBrushless);
  private final SparkFlexConfig m_config = new SparkFlexConfig();

  public Indexer() {
    m_config
    .smartCurrentLimit(40)
    .idleMode(IdleMode.kCoast)
    .inverted(false);

    m_vortex.configure(m_config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  public void v_runWheels(double speed) {
    m_vortex.set(speed);
  }

  @Override
  public void periodic() {
  }
}
