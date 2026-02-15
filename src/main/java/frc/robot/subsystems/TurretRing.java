package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import static frc.robot.Constants.TurretConstants.RingGearConfig;
import static frc.robot.Constants.TurretConstants.RingGearFeedbackConfig;
import static frc.robot.Constants.TurretConstants.RingGearMotionMagicConfig;
import static frc.robot.Constants.TurretConstants.PositionRequest;
import static frc.robot.Constants.TurretConstants.RingGearID;
import static frc.robot.Constants.CANIVORE;


import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretRing extends SubsystemBase {

  private final TalonFX m_krakenX44 = new TalonFX(RingGearID, CANIVORE);

  public TurretRing() {
    ConfigureMotor();
  }

  private void ConfigureMotor() {
    TalonFXConfiguration m_config = new TalonFXConfiguration()
      .withSlot0(Slot0Configs.from(RingGearConfig))
      .withFeedback(RingGearFeedbackConfig)
      .withMotionMagic(RingGearMotionMagicConfig);

    m_krakenX44.getConfigurator().apply(m_config);
  }

  public void v_runTurret(double position) {
    m_krakenX44.setControl(PositionRequest.withPosition(position));
  }

  @Override
  public void periodic() {
      SmartDashboard.putNumber("Ring Temp", m_krakenX44.getDeviceTemp().getValueAsDouble());
      SmartDashboard.putNumber("Ring Current", Math.round(m_krakenX44.getStatorCurrent().getValueAsDouble() * 10) / 10);
      SmartDashboard.putNumber("Ring ID", m_krakenX44.getDeviceID());
      SmartDashboard.putNumber("Ring RPM", Math.round(m_krakenX44.getVelocity().getValueAsDouble() * 10) / 10);
  }

}
