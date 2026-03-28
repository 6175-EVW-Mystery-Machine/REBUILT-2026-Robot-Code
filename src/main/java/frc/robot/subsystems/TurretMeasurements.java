package frc.robot.subsystems;

import static edu.wpi.first.math.util.Units.metersToInches;
import static edu.wpi.first.units.Units.Inches;
import static frc.robot.Constants.blueHubLocation;
import static frc.robot.Constants.blueLeftPassing;
import static frc.robot.Constants.blueRightPassing;
import static frc.robot.Constants.redHubLocation;
import static frc.robot.Constants.redLeftPassing;
import static frc.robot.Constants.redRightPassing;
import static frc.robot.Constants.target;
import static frc.robot.subsystems.TurretFlywheel.passing;
import static edu.wpi.first.math.util.Units.inchesToMeters;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretMeasurements extends SubsystemBase {

  public static double turretAngle;
  public static double m_fieldRelativeAngle;
  public static double distanceToTarget;
  private Translation2d vRobot;

  private Translation2d robotToTurret = new Translation2d(Inches.of(-8.505), Inches.of(3.033));

  public TurretMeasurements() {
  }

  public void getTurretData(Pose2d robotPose, ChassisSpeeds robotSpeedSupplier) {

  //SHOOTING TARGET CHECK
          if (DriverStation.getAlliance().get() == Alliance.Blue) {
        if (robotPose.getX() < inchesToMeters(162)) {
          target = blueHubLocation;
          passing = false;
        } else if (robotPose.getX() > inchesToMeters(162)) {
          passing = true;
          if (robotPose.getY() < inchesToMeters(158.84)) {
            target = blueRightPassing;
          } else if (robotPose.getY() > inchesToMeters(158.84)) {
            target = blueLeftPassing;
          }
        }
      } else if (DriverStation.getAlliance().get() == Alliance.Red) {
        if (robotPose.getX() > inchesToMeters(490)) {
          target = redHubLocation;
          passing = false;
        } else if (robotPose.getX() < inchesToMeters(490)) {
          passing = true;
          if (robotPose.getY() < inchesToMeters(158.84)) {
            target = redLeftPassing;
          } else if (robotPose.getY() > inchesToMeters(158.84)) {
            target = redRightPassing;
          }
        } 
      } else if (DriverStation.getAlliance().isEmpty()) {
        return;
      }
    //ROBOT SPEED READINGS
    var robotSpeeds = robotSpeedSupplier;

      vRobot = new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond).rotateBy(robotPose.getRotation());

    var omega = robotSpeeds.omegaRadiansPerSecond;

    //SHOOTER TRANSLATION SETUP
    Translation2d turretPose = robotPose.getTranslation().plus(robotToTurret.rotateBy(robotPose.getRotation()));
    // var robotToTurret = turretPose.minus(robotPose.getTranslation());
    // var vTan = new Translation2d(-omega * robotToTurret.getY(), omega * robotToTurret.getX());

      Translation2d predictedTargetTranslation = target.getTranslation().minus(vRobot);

    double tY = predictedTargetTranslation.getY() - robotPose.getY();
    double tX = predictedTargetTranslation.getX() - robotPose.getX();


    //SETTING SHOOTER TARGET
    Rotation2d fieldRelativeAngle = Rotation2d.fromRadians(Math.atan2(tY, tX));
    Rotation2d robotRelativeAngle = fieldRelativeAngle.minus(robotPose.getRotation());

    turretAngle = robotRelativeAngle.getDegrees() / 360;
    m_fieldRelativeAngle = fieldRelativeAngle.getDegrees() / 360;

    SmartDashboard.putNumber("Robot X", metersToInches(robotPose.getX()));
    SmartDashboard.putNumber("Robot Y", metersToInches(robotPose.getY()));
    SmartDashboard.putNumber("Turret X", metersToInches(turretPose.getX()));
    SmartDashboard.putNumber("Turret Y", metersToInches(turretPose.getY()));
    SmartDashboard.putNumber("Diff X", metersToInches(turretPose.getX() - robotPose.getX()));
    SmartDashboard.putNumber("Diff Y", metersToInches(turretPose.getY() - robotPose.getY()));

    distanceToTarget = metersToInches(predictedTargetTranslation.getDistance(turretPose));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Distance to Target", distanceToTarget);
    SmartDashboard.putNumber("Turret Angle", turretAngle * 360);
  }
}
