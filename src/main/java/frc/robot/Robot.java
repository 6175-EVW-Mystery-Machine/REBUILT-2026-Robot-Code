package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.LimelightHelpers.PoseEstimate;

import static edu.wpi.first.math.util.Units.inchesToMeters;
import static frc.robot.Constants.CANStatus;

import com.revrobotics.util.StatusLogger;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;
  private Pose2d robotPose;
  private Field2d REBUILTField = new Field2d();

  public Robot() {
    m_robotContainer = new RobotContainer();

    LimelightHelpers.setCameraPose_RobotSpace("limelight-left",
    inchesToMeters(-10.5),
    inchesToMeters(-11.5),
    inchesToMeters(15.5),
    0,
    20,
    90);
    LimelightHelpers.setCameraPose_RobotSpace("limelight-front",
    inchesToMeters(12.75),
    inchesToMeters(10.5),
    inchesToMeters(13.5),
    0,
    0,
    0);
  }

  @Override
  public void robotInit() {
    StatusLogger.disableAutoLogging();
    StatusLogger.stop();
  }

  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run(); 

    float busUtil = CANStatus.BusUtilization; 
    if (busUtil > 0.8) {
      System.out.print("CANBus in heavy use!");
    }
    REBUILTField.setRobotPose(m_robotContainer.drivetrain.getState().Pose);
    
    SmartDashboard.putData("Field", REBUILTField);
    SmartDashboard.putNumber("Match Time", DriverStation.getMatchTime());
    SmartDashboard.putNumber("Battery Voltage", RobotController.getBatteryVoltage());


    var driveState = m_robotContainer.drivetrain.getState();
    double headingDeg = driveState.Pose.getRotation().getDegrees();
    double angVelocity = Units.radiansToRotations(driveState.Speeds.omegaRadiansPerSecond);

    robotPose = m_robotContainer.drivetrain.getState().Pose;


      PoseEstimate leftPoseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight-left");
      PoseEstimate frontPoseEstimate = LimelightHelpers.getBotPoseEstimate_wpiBlue("limelight-front");


      if (leftPoseEstimate != null && leftPoseEstimate.tagCount > 0 && angVelocity < 2.0) {
        m_robotContainer.drivetrain.addVisionMeasurement(leftPoseEstimate.pose, leftPoseEstimate.timestampSeconds);
    }
      if (frontPoseEstimate != null && frontPoseEstimate.tagCount > 0 && angVelocity < 2.0) {
        m_robotContainer.drivetrain.addVisionMeasurement(frontPoseEstimate.pose, frontPoseEstimate.timestampSeconds);
      }
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {}

  public void enabledPeriodic() {
  }

  @Override
  public void disabledExit() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

  @Override
  public void testInit() {
    CommandScheduler.getInstance().cancelAll();
  }

  @Override
  public void testPeriodic() {}

  @Override
  public void testExit() {}

  @Override
  public void simulationPeriodic() {}
}
