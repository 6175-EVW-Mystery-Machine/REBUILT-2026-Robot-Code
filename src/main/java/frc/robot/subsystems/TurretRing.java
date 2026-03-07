package frc.robot.subsystems;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;

import static frc.robot.Constants.TurretConstants.RingGearFeedbackConfig;
import static frc.robot.Constants.TurretConstants.RingGearMotionMagicConfig;
import static frc.robot.Constants.TurretConstants.RingGearMotorOutput;
import static frc.robot.Constants.TurretConstants.VelocityRequest;
import static frc.robot.Constants.TurretConstants.PositionRequest;
import static frc.robot.Constants.TurretConstants.RingGear0Config;
import static frc.robot.Constants.TurretConstants.RingGear1Config;
import static frc.robot.Constants.TurretConstants.RingGearID;
import static frc.robot.Constants.TurretConstants.RingGearLimits;
import static frc.robot.Constants.CANIVORE;

import static frc.robot.subsystems.TurretMeasurements.m_robotRelativeAngle;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretRing extends SubsystemBase {

  private final TalonFX m_krakenX44 = new TalonFX(RingGearID, CANIVORE);
  private final CANcoder m_encoder = new CANcoder(17, CANIVORE);
  public boolean TargetRumble;

  public TurretRing() {
    ConfigureMotor();
    TargetRumble = false;
  }

  private void ConfigureMotor() {
    TalonFXConfiguration m_config = new TalonFXConfiguration()
      .withSlot0(RingGear0Config)
      .withSlot1(RingGear1Config)
      .withFeedback(RingGearFeedbackConfig)
      .withMotionMagic(RingGearMotionMagicConfig)
      .withSoftwareLimitSwitch(RingGearLimits)
      .withMotorOutput(RingGearMotorOutput);

    m_krakenX44.getConfigurator().apply(m_config);
  }

  public void v_positionTurret() {
    m_krakenX44.setControl(PositionRequest.withPosition(m_robotRelativeAngle));
  }

  public void v_runTurret(double RPM) {
    m_krakenX44.setControl(VelocityRequest.withVelocity(RPM/360));
  }

  public void v_stopMotor() {
    m_krakenX44.stopMotor();
  }

  public void v_resetEncoder() {
    m_encoder.setPosition(0);
  }

  @Override
  public void periodic() {
      double MotorRPM = m_krakenX44.getVelocity().getValueAsDouble() * 60;
      double EncoderRead = m_encoder.getAbsolutePosition().getValueAsDouble() * 360;
      SmartDashboard.putNumber("Ring Temp", m_krakenX44.getDeviceTemp().getValueAsDouble());
      SmartDashboard.putNumber("Ring Current", Math.round(m_krakenX44.getStatorCurrent().getValueAsDouble() * 10) / 10);
      SmartDashboard.putNumber("Ring ID", m_krakenX44.getDeviceID());
      SmartDashboard.putNumber("Ring RPM", Math.round(MotorRPM * 10) / 10);
      SmartDashboard.putNumber("Turret Position", m_krakenX44.getPosition().getValueAsDouble() * 360);

      SmartDashboard.putNumber("Turret Encoder Reading", EncoderRead * 100 / 100);
  }

}