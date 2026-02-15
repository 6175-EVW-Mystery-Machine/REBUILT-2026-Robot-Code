package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.signals.StripTypeValue;
import com.ctre.phoenix6.hardware.CANdle;

import static frc.robot.Constants.CANdle.kLeftIntake;
import static frc.robot.Constants.CANdle.kRightIntake;
import static frc.robot.Constants.CANdle.kIndexer;
import static frc.robot.Constants.CANdle.kLeftTurretSupport;
import static frc.robot.Constants.CANdle.kRightTurretSupport;
import static frc.robot.Constants.CANdle.kBlue;
import static frc.robot.Constants.CANdle.kCyan;
import static frc.robot.Constants.CANdle.kGreen;
import static frc.robot.Constants.CANdle.kLime;
import static frc.robot.Constants.CANdle.kOrange;
import static frc.robot.Constants.CANdle.kPink;
import static frc.robot.Constants.CANdle.kPurple;
import static frc.robot.Constants.CANdle.kRed;
import static frc.robot.Constants.CANdle.kYellow;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CTRE_CANdle extends SubsystemBase {

  private CANdle m_CANdle = new CANdle(23, "rio");
  private CANdleConfiguration m_config = new CANdleConfiguration();



  public CTRE_CANdle() {
    m_config.LED.BrightnessScalar = 0.5;
    m_config.LED.StripType = StripTypeValue.GRB;
    m_CANdle.getConfigurator().apply(m_config);
  }

  private void v_stopAll() {
    m_CANdle.setControl(new EmptyAnimation(0));
    m_CANdle.setControl(new EmptyAnimation(1));
    m_CANdle.setControl(new EmptyAnimation(2));
    m_CANdle.setControl(new EmptyAnimation(3));
    m_CANdle.setControl(new EmptyAnimation(4));
    m_CANdle.setControl(new EmptyAnimation(5));
    m_CANdle.setControl(new EmptyAnimation(6));
    m_CANdle.setControl(new EmptyAnimation(7));
  }

  public void v_startAnim() {
    v_stopAll();
    m_CANdle.setControl(new TwinkleAnimation(0, 0).withColor(kYellow).withSlot(kLeftIntake).withFrameRate(60));
    m_CANdle.setControl(new TwinkleAnimation(0, 0).withColor(kYellow).withSlot(kRightIntake).withFrameRate(60));
    m_CANdle.setControl(new RainbowAnimation(0, 0).withFrameRate(30).withSlot(kIndexer));
    m_CANdle.setControl(new TwinkleAnimation(0, 0).withColor(kPurple).withFrameRate(30).withSlot(kLeftTurretSupport));
    m_CANdle.setControl(new TwinkleAnimation(0, 0).withColor(kPurple).withFrameRate(30).withSlot(kRightTurretSupport));
    
  }

  public void v_intakeLights() {
    m_CANdle.setControl(new ColorFlowAnimation(0, 0).withColor(kGreen).withSlot(kLeftIntake).withFrameRate(700));
    m_CANdle.setControl(new ColorFlowAnimation(0, 0).withColor(kGreen).withSlot(kRightIntake).withFrameRate(700));
  }

  public void v_indexerLights() {
    m_CANdle.setControl(new ColorFlowAnimation(0, 0).withColor(kOrange).withFrameRate(120).withSlot(kIndexer));
  }

  public void v_turretShoot() {
    v_stopAll();
    m_CANdle.setControl(new FireAnimation(0, 0).withBrightness(0.5).withSparking(0.2).withCooling(0).withSlot(kLeftTurretSupport));
    m_CANdle.setControl(new FireAnimation(0, 0).withBrightness(0.5).withSparking(0.2).withCooling(0).withSlot(kRightTurretSupport));
  }

  public void v_turretLock() {
    v_stopAll();
    m_CANdle.setControl(new StrobeAnimation(0, 0).withColor(kCyan).withFrameRate(30).withSlot(kLeftTurretSupport));
    m_CANdle.setControl(new StrobeAnimation(0, 0).withColor(kCyan).withFrameRate(30).withSlot(kRightTurretSupport));
  }

  public void v_snowblowTurret() {
    v_stopAll();
    m_CANdle.setControl(new ColorFlowAnimation(0, 0).withColor(kGreen).withSlot(kLeftIntake).withFrameRate(700));
    m_CANdle.setControl(new ColorFlowAnimation(0, 0).withColor(kGreen).withSlot(kRightIntake).withFrameRate(700));
    m_CANdle.setControl(new ColorFlowAnimation(0, 0).withColor(kOrange).withFrameRate(120).withSlot(kIndexer));
    m_CANdle.setControl(new FireAnimation(0, 0).withBrightness(0.5).withSparking(0.2).withCooling(0).withSlot(kLeftTurretSupport));
    m_CANdle.setControl(new FireAnimation(0, 0).withBrightness(0.5).withSparking(0.2).withCooling(0).withSlot(kRightTurretSupport));
  }

  @Override
  public void periodic() {
  }
}
