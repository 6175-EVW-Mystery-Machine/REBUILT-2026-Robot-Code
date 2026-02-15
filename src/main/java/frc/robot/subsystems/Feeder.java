package frc.robot.subsystems;

import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import static com.revrobotics.spark.SparkBase.ControlType.kMAXMotionVelocityControl;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Feeder extends SubsystemBase {

  private final SparkMax m_neo2 = new SparkMax(14, MotorType.kBrushless);
  private final SparkMaxConfig m_config = new SparkMaxConfig();

  public Feeder() {
    m_config
    .smartCurrentLimit(40)
    .inverted(true)
    .idleMode(IdleMode.kBrake);
    m_config.closedLoop
    .pid(0, 0, 0)
    .feedForward.sva(0, 0.12, 0);
    m_config.closedLoop.maxMotion
    .cruiseVelocity(100)
    .maxAcceleration(75);

    m_neo2.configure(m_config,
    ResetMode.kResetSafeParameters,
    PersistMode.kPersistParameters);
  }

  public void v_runWheels(double RPM) {
    m_neo2.getClosedLoopController().setSetpoint(RPM/12, kMAXMotionVelocityControl);
  }

  public void v_stopMotor() {
    m_neo2.stopMotor();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Feeder Temp", m_neo2.getMotorTemperature());
    SmartDashboard.putNumber("Feeder Current", Math.round(m_neo2.getOutputCurrent() * 10) / 10);
    SmartDashboard.putNumber("Feeder CAN ID", m_neo2.getDeviceId());
    SmartDashboard.putNumber("Feeder RPM", Math.round(m_neo2.getEncoder().getVelocity() * 10) / 10);
  }
}
