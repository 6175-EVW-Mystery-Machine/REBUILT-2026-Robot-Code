package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANdiConfiguration;
import com.ctre.phoenix6.configs.CANdleConfiguration;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CANdle extends SubsystemBase {

  private final CANdle m_CANdle = new CANdle();
  private final CANdleConfiguration m_config = new CANdleConfiguration();

  public CANdle() {
    
  }

  @Override
  public void periodic() {
  }
}
