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

  private final SparkFlex m_vortex = new SparkFlex(15, MotorType.kBrushless);
  private final SparkFlexConfig m_config = new SparkFlexConfig();

  public Indexer() {
    m_config
    .smartCurrentLimit(40)
    .idleMode(IdleMode.kCoast)
    .inverted(true);
    m_config.closedLoop
    .pid(0, 0, 0)
    .feedForward.sva(0, 0.12, 0);
    m_config.closedLoop.maxMotion
    .cruiseVelocity(100)
    .maxAcceleration(75);


    m_vortex.configure(m_config,
    ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
  }

  public void v_runWheels(double RPM) {
    m_vortex.getClosedLoopController().setSetpoint(RPM/12, kMAXMotionVelocityControl);
  }

  public void v_stopMotor() {
    m_vortex.stopMotor();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Indexer Temp", m_vortex.getMotorTemperature());
    SmartDashboard.putNumber("Indexer Current", Math.round(m_vortex.getOutputCurrent() * 10) / 10);
    SmartDashboard.putNumber("Indexer CAN ID", m_vortex.getDeviceId());
    SmartDashboard.putNumber("Indexer RPM", Math.round(m_vortex.getEncoder().getVelocity() * 10) / 10);
  }
}
