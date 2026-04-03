package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkFlex;

import static com.revrobotics.spark.SparkBase.ControlType.kMAXMotionVelocityControl;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkFlexConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Indexer extends SubsystemBase {

  private final SparkFlex m_vortex = new SparkFlex(14, MotorType.kBrushless);
  private final SparkFlexConfig m_config = new SparkFlexConfig();
  private boolean indexing = false;

  public Indexer() {
    m_config
    .smartCurrentLimit(80)
    .idleMode(IdleMode.kBrake)
    .inverted(true);
    m_config.closedLoop
    .pid(0, 0, 0)
    .feedForward.sva(0, 0.12, 0);
    m_config.closedLoop.maxMotion
    .cruiseVelocity(750)
    .maxAcceleration(600);


    m_vortex.configure(m_config,
    ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
  }

  public void v_runWheels(double RPM) {
    m_vortex.getClosedLoopController().setSetpoint(RPM/66.2, kMAXMotionVelocityControl);
    indexing = true;
  }

  public void v_stopMotor() {
    m_vortex.stopMotor();
    indexing = false;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Indexer Temp", m_vortex.getMotorTemperature());
    SmartDashboard.putNumber("Indexer Current", Math.round(m_vortex.getOutputCurrent() * 10) / 10);
    SmartDashboard.putNumber("Indexer CAN ID", m_vortex.getDeviceId());
    SmartDashboard.putNumber("Indexer RPM", Math.round(m_vortex.getEncoder().getVelocity() * 10) / 10);

    SmartDashboard.putBoolean("Indexing Fuel?", indexing);
  }
}
