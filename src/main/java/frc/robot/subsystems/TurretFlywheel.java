package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;

import static com.ctre.phoenix6.signals.MotorAlignmentValue.Opposed;

import static frc.robot.Constants.TurretConstants.FlywheelConfig;
import static frc.robot.Constants.TurretConstants.FlywheelMotionMagicConfig;
import static frc.robot.Constants.TurretConstants.FlywheelLeaderID;
import static frc.robot.Constants.TurretConstants.FlywheelFollowerID;
import static frc.robot.Constants.TurretConstants.VelocityRequest;
import static frc.robot.Constants.CANIVORE;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretFlywheel extends SubsystemBase {

  private final TalonFX m_flywheel = new TalonFX(FlywheelLeaderID, CANIVORE);
  private final TalonFX m_flywheelFollower = new TalonFX(FlywheelFollowerID, CANIVORE);

  public TurretFlywheel() {
    ConfigureMotors();
  }

  private void ConfigureMotors() {
    TalonFXConfiguration m_config = new TalonFXConfiguration()
    .withSlot0(Slot0Configs.from(FlywheelConfig))
    .withMotionMagic(FlywheelMotionMagicConfig);
    m_config.TorqueCurrent.TorqueNeutralDeadband = 16;

    m_flywheel.getConfigurator().apply(m_config);
    m_flywheelFollower.getConfigurator().apply(m_config);
    m_flywheelFollower.setControl(new Follower(FlywheelLeaderID, Opposed));
  }

  public void v_runWheel(double RPM) {
    m_flywheel.setControl(VelocityRequest.withVelocity(RPM/60));
  }

  public void v_stopMotors() {
    m_flywheel.stopMotor();
  }

  @Override
  public void periodic() {
      SmartDashboard.putNumber("Turret Temp", m_flywheel.getDeviceTemp().getValueAsDouble());
      SmartDashboard.putNumber("Turret Current", Math.round(m_flywheel.getStatorCurrent().getValueAsDouble() * 10) / 10);
      SmartDashboard.putNumber("Turret ID", m_flywheel.getDeviceID());
      SmartDashboard.putNumber("Turret RPM", Math.round(m_flywheel.getVelocity().getValueAsDouble() * 10) / 10);
  }
}
