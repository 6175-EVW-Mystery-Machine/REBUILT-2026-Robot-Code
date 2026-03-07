package frc.robot.subsystems;

import com.ctre.phoenix6.configs.CANdleConfiguration;
import com.ctre.phoenix6.controls.ColorFlowAnimation;
import com.ctre.phoenix6.controls.EmptyAnimation;
import com.ctre.phoenix6.controls.FireAnimation;
import com.ctre.phoenix6.controls.LarsonAnimation;
import com.ctre.phoenix6.controls.RainbowAnimation;
import com.ctre.phoenix6.controls.SingleFadeAnimation;
import com.ctre.phoenix6.controls.SolidColor;
import com.ctre.phoenix6.controls.StrobeAnimation;
import com.ctre.phoenix6.controls.TwinkleAnimation;
import com.ctre.phoenix6.signals.AnimationDirectionValue;
import com.ctre.phoenix6.signals.LarsonBounceValue;
import com.ctre.phoenix6.signals.RGBWColor;
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
import frc.robot.LimelightHelpers;

public class CTRE_CANdle extends SubsystemBase {

  private CANdle m_CANdle = new CANdle(20, "rio");
  private CANdleConfiguration m_config = new CANdleConfiguration();

  /*
  LeftIntake: 204-230
  RightIntake: 231-257
  Indexer: 97-203
  LeftTurretSupport: 8-53
  RightTurretSupport: 54-96
  CandleLights: 0-7
  */

  public CTRE_CANdle() {
    m_config.LED.BrightnessScalar = 0.5;
    m_config.LED.StripType = StripTypeValue.GRB;
    m_CANdle.getConfigurator().apply(m_config);

    v_stopAll();
    v_startAnim();
  }

  public void v_stopAll() {
    m_CANdle.setControl(new EmptyAnimation(kLeftIntake));
    m_CANdle.setControl(new EmptyAnimation(kRightIntake));
    m_CANdle.setControl(new EmptyAnimation(kIndexer));
    m_CANdle.setControl(new EmptyAnimation(kLeftTurretSupport));
    m_CANdle.setControl(new EmptyAnimation(kRightTurretSupport));
    m_CANdle.setControl(new EmptyAnimation(5));
    m_CANdle.setControl(new EmptyAnimation(6));
    m_CANdle.setControl(new EmptyAnimation(7));
  }

  public void v_startAnim() {
    m_CANdle.setControl(new SingleFadeAnimation(8, 53).withColor(kCyan).withSlot(kLeftTurretSupport).withFrameRate(30));
    m_CANdle.setControl(new SingleFadeAnimation(54, 96).withColor(kCyan).withSlot(kRightTurretSupport).withFrameRate(30));
      m_CANdle.setControl(new LarsonAnimation(97, 203).withColor(kGreen).withFrameRate(120).withSize(10).withSlot(kIndexer).withBounceMode(LarsonBounceValue.Back));
        m_CANdle.setControl(new ColorFlowAnimation(204, 230).withSlot(kLeftIntake).withFrameRate(45).withColor(kPurple));
        m_CANdle.setControl(new ColorFlowAnimation(231, 257).withSlot(kRightIntake).withFrameRate(45).withColor(kPurple));
  }

  public void v_intakeLights() {
    m_CANdle.setControl(new ColorFlowAnimation(204, 230).withColor(kLime).withSlot(kLeftIntake).withFrameRate(240).withDirection(AnimationDirectionValue.Backward));
    m_CANdle.setControl(new ColorFlowAnimation(231, 257).withColor(kLime).withSlot(kRightIntake).withFrameRate(240).withDirection(AnimationDirectionValue.Backward));
  }

  public void v_indexerLights() {
    m_CANdle.setControl(new ColorFlowAnimation(97, 203).withColor(kBlue).withFrameRate(480).withSlot(kIndexer));
  }

  public void v_turretShoot() {
    m_CANdle.setControl(new FireAnimation(8, 53).withBrightness(1).withSparking(0.15).withCooling(0).withSlot(kLeftTurretSupport).withFrameRate(150).withDirection(AnimationDirectionValue.Backward));
    m_CANdle.setControl(new FireAnimation(54, 96).withBrightness(1).withSparking(0.15).withCooling(0).withSlot(kRightTurretSupport).withFrameRate(150));
    v_indexerLights();
    v_intakeLights();
  }

  public void v_turretAim() {
    m_CANdle.setControl(new StrobeAnimation(0, 7).withColor(kYellow).withFrameRate(15).withSlot(5));
  }

  public void v_snowblowTurret() {
    v_intakeLights();
    v_indexerLights();
    v_turretShoot();
    v_turretAim();
  }

  public void v_clearTurretRails() {
    m_CANdle.setControl(new EmptyAnimation(kLeftTurretSupport));
    m_CANdle.setControl(new EmptyAnimation(kRightTurretSupport));
  }

  public void v_clearIntake() {
    m_CANdle.setControl(new EmptyAnimation(kLeftIntake));
    m_CANdle.setControl(new EmptyAnimation(kRightIntake));
  }
  
  public void v_clearIndexer() {
    m_CANdle.setControl(new EmptyAnimation(kIndexer));
  }

  @Override
  public void periodic() {
  }
}
