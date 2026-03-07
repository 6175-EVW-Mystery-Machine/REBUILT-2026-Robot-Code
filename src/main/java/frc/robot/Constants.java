package frc.robot;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.CANBus.CANBusStatus;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.SlotConfigs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.controls.PositionVoltage;
import com.ctre.phoenix6.controls.VelocityVoltage;
import com.ctre.phoenix6.signals.FeedbackSensorSourceValue;
import com.ctre.phoenix6.signals.RGBWColor;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;

import static com.ctre.phoenix6.signals.InvertedValue.Clockwise_Positive;
import static com.ctre.phoenix6.signals.InvertedValue.CounterClockwise_Positive;
import static com.ctre.phoenix6.signals.NeutralModeValue.Coast;
import static com.ctre.phoenix6.signals.NeutralModeValue.Brake;
import static com.ctre.phoenix6.signals.FeedbackSensorSourceValue.FusedCANcoder;
import static com.ctre.phoenix6.signals.FeedbackSensorSourceValue.RotorSensor;
import static edu.wpi.first.math.util.Units.inchesToMeters;
import static edu.wpi.first.units.Units.Rotations;

public final class Constants{

  public static final Pose2d blueHubLocation = new Pose2d(inchesToMeters(182.11), inchesToMeters(158.84), new Rotation2d());
  public static final Pose2d redHubLocation = new Pose2d(inchesToMeters(469.11), inchesToMeters(158.85), new Rotation2d());

  public static Pose2d hubLocation = new Pose2d();

  private Constants() {
  }

  public static final CANBus CANIVORE = new CANBus("canivore");
  public static final CANBusStatus CANStatus = new CANBusStatus();



  public static class OdometryConstants {
    public static final Matrix<N3, N1> StateSTDDevs = VecBuilder.fill(
      0.1, //X: 10cm error per meter (Trust Wheels)
      0.1, //Y: 10cm error per meter
      0.05); //Theta: 0.05 radians (Trust Pigeon)
  }

  public static class TurretConstants {
    //TURRET FLYWHEEL
    public static int FlywheelLeaderID = 18;
    public static int FlywheelFollowerID = 19; 
    public static VelocityVoltage VelocityRequest = new VelocityVoltage(0).withEnableFOC(false);
      public static final SlotConfigs FlywheelConfig = new SlotConfigs()
      .withKP(1)
      .withKD(0)
      .withKS(0)
      .withKV(0)
      .withKA(0);
      public static final FeedbackConfigs FlywheelFeedback = new FeedbackConfigs()
      .withFeedbackSensorSource(FeedbackSensorSourceValue.RotorSensor)
      .withSensorToMechanismRatio(2)
      .withRotorToSensorRatio(1);
      public static final MotionMagicConfigs FlywheelMotionMagicConfig = new MotionMagicConfigs()
      .withMotionMagicAcceleration(1000)
      .withMotionMagicCruiseVelocity(1500)
      .withMotionMagicJerk(0);

    //TURRET GEAR
    public static int RingGearID = 16;
    public static PositionVoltage PositionRequest = new PositionVoltage(0).withSlot(1);
      public static final Slot0Configs RingGear0Config = new Slot0Configs()
      .withKP(5)
      .withKD(0)
      .withKS(0)
      .withKV(.12)
      .withKA(0);
      public static final Slot1Configs RingGear1Config = new Slot1Configs()
      .withKP(50)
      .withKD(0.4)
      .withKS(0.1)
      .withKV(1)
      .withKA(0.05);
      public static final FeedbackConfigs RingGearFeedbackConfig = new FeedbackConfigs()
      .withFeedbackSensorSource(FusedCANcoder)
      .withFeedbackRemoteSensorID(17)
      .withRotorToSensorRatio(36)
      .withSensorToMechanismRatio(1);
      public static final MotionMagicConfigs RingGearMotionMagicConfig = new MotionMagicConfigs()
      .withMotionMagicAcceleration(3500)
      .withMotionMagicCruiseVelocity(4000)
      .withMotionMagicJerk(2500);
      public static final SoftwareLimitSwitchConfigs RingGearLimits = new SoftwareLimitSwitchConfigs()
      .withForwardSoftLimitThreshold(Rotations.of(.375))
      .withReverseSoftLimitThreshold(Rotations.of(-.45))
      .withForwardSoftLimitEnable(true)
      .withReverseSoftLimitEnable(true);
      public static final MotorOutputConfigs RingGearMotorOutput = new MotorOutputConfigs()
      .withInverted(Clockwise_Positive)
      .withNeutralMode(Brake);
  }

  public static class IntakeConstants {
      public static int IntakeID = 13;
      public static final SlotConfigs IntakeConfig = new SlotConfigs()
      .withKP(1)
      .withKD(0)
      .withKS(0)
      .withKV(.06)
      .withKA(0);
      public static final FeedbackConfigs IntakeFeedbackConfig = new FeedbackConfigs()
      .withFeedbackSensorSource(RotorSensor)
      .withRotorToSensorRatio(1)
      .withSensorToMechanismRatio(2);
      public static final MotorOutputConfigs IntakeOutputConfig = new MotorOutputConfigs()
      .withInverted(CounterClockwise_Positive)
      .withNeutralMode(Coast);
  }

  public static class CANdle {
      //SLOTS
      public static int kRightIntake = 0;
      public static int kLeftIntake = 1;
      public static int kIndexer = 2;
      public static int kLeftTurretSupport = 3;
      public static int kRightTurretSupport = 4;

      //COLORS
      public static RGBWColor kYellow = new RGBWColor(255, 125, 0);
      public static RGBWColor kOrange = new RGBWColor(255, 50, 0);
      public static RGBWColor kRed = new RGBWColor(225, 0, 0);
      public static RGBWColor kBlue = new RGBWColor(0, 100, 255);
      public static RGBWColor kGreen = new RGBWColor(0, 200, 0);
      public static RGBWColor kPurple = new RGBWColor(150, 0, 200);
      public static RGBWColor kLime = new RGBWColor(125, 255, 0);
      public static RGBWColor kPink = new RGBWColor(255, 0, 150);
      public static RGBWColor kCyan = new RGBWColor(0, 255, 200);
  }

}