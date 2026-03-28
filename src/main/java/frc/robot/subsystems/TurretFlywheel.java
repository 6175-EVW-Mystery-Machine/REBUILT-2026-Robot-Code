package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.Follower;
import com.ctre.phoenix6.hardware.TalonFX;

import static com.ctre.phoenix6.signals.MotorAlignmentValue.Opposed;
import static frc.robot.Constants.TurretConstants.FlywheelConfig;
import static frc.robot.Constants.TurretConstants.FlywheelFeedback;
import static frc.robot.Constants.TurretConstants.FlywheelMotionMagicConfig;
import static frc.robot.Constants.TurretConstants.FlywheelLeaderID;
import static frc.robot.Constants.TurretConstants.FlywheelFollowerID;
import static frc.robot.Constants.TurretConstants.VelocityRequest;
import static frc.robot.subsystems.TurretMeasurements.distanceToTarget;
import static frc.robot.Constants.ManipulatorCanivore;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretFlywheel extends SubsystemBase {

  private final TalonFX m_flywheel = new TalonFX(FlywheelLeaderID, ManipulatorCanivore);
  private final TalonFX m_flywheelFollower = new TalonFX(FlywheelFollowerID, ManipulatorCanivore);
  private boolean shooting = false;
  public static boolean passing = false;
  public static boolean ShooterAtSpeed;
  private double PredictedRPM;

  public TurretFlywheel() {
    ConfigureMotors();
  }

  private void ConfigureMotors() {
    TalonFXConfiguration m_config = new TalonFXConfiguration()
    .withSlot0(Slot0Configs.from(FlywheelConfig))
    .withMotionMagic(FlywheelMotionMagicConfig)
    .withFeedback(FlywheelFeedback);

    m_flywheel.getConfigurator().apply(m_config);
    m_flywheelFollower.getConfigurator().apply(m_config);
    m_flywheelFollower.setControl(new Follower(FlywheelLeaderID, Opposed));
  }

  public void v_runWheel() {
    if (distanceToTarget < 100) {
      m_flywheel.setControl(VelocityRequest.withVelocity(MathUtil.interpolate(
        720 / 60,
        1150 / 60,
        distanceToTarget / 220)));

      //RPM CHECK
      PredictedRPM = MathUtil.interpolate(
        720 / 60,
        1150 / 60,
        distanceToTarget / 220);
    } else if (distanceToTarget > 100) {
      m_flywheel.setControl(VelocityRequest.withVelocity(MathUtil.interpolate(
        700 / 60,
        1350 / 60,
        distanceToTarget / 220)));

      //RPM CHECK
      PredictedRPM = MathUtil.interpolate(
        700 / 60,
        1350 / 60,
        distanceToTarget / 220);
    }
    shooting = true;
  }

  public void v_stopMotors() {
    m_flywheel.stopMotor();
    shooting = false;
  }

  @Override
  public void periodic() {
      SmartDashboard.putNumber("Turret Temp", m_flywheel.getDeviceTemp().getValueAsDouble());
      SmartDashboard.putNumber("Turret Current", Math.round(m_flywheel.getStatorCurrent().getValueAsDouble() * 10) / 10);
      SmartDashboard.putNumber("Turret ID", m_flywheel.getDeviceID());
      SmartDashboard.putNumber("Turret RPM", Math.round(m_flywheel.getVelocity().getValueAsDouble() * 60));

      SmartDashboard.putBoolean("Shooting Fuel?", shooting);
      SmartDashboard.putNumber("Target RPM", MathUtil.interpolate(500, 1250, distanceToTarget / 190));

      ShooterAtSpeed = (m_flywheel.getVelocity().getValueAsDouble() * 60) > ((PredictedRPM * 60) * 0.65) ? true : false;
  }
}
