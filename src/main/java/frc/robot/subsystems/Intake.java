package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.CANIVORE;
import static frc.robot.Constants.TurretConstants.VelocityRequest;
import static frc.robot.Constants.IntakeConstants.IntakeID;
import static frc.robot.Constants.IntakeConstants.IntakeConfig;
import static frc.robot.Constants.IntakeConstants.IntakeFeedbackConfig;
import static frc.robot.Constants.IntakeConstants.IntakeOutputConfig;

public class Intake extends SubsystemBase {

  private final TalonFX m_krakenX44 = new TalonFX(IntakeID, CANIVORE);
  public boolean intaking = false;

  public Intake() {
    ConfigureMotor();
  }

  public void ConfigureMotor() {
    TalonFXConfiguration m_config = new TalonFXConfiguration()
    .withSlot0(Slot0Configs.from(IntakeConfig))
    .withFeedback(IntakeFeedbackConfig)
    .withMotorOutput(IntakeOutputConfig);

    m_krakenX44.getConfigurator().apply(m_config);
  }

  public void v_runWheels(double RPM) {
    m_krakenX44.setControl(VelocityRequest.withVelocity(RPM / 60).withAcceleration(0.1));
    intaking = true;
  }
  
  public void v_stopMotor() {
    m_krakenX44.stopMotor();
    intaking = false;
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Intake Temp", m_krakenX44.getDeviceTemp().getValueAsDouble());
    SmartDashboard.putNumber("Intake Current", Math.round(m_krakenX44.getStatorCurrent().getValueAsDouble() * 10) / 10);
    SmartDashboard.putNumber("Intake CAN ID", m_krakenX44.getDeviceID());
    SmartDashboard.putNumber("Intake RPM", Math.round(m_krakenX44.getRotorVelocity().getValueAsDouble() * 60));
    SmartDashboard.putBoolean("Intaking Fuel?", intaking);
  }
}
