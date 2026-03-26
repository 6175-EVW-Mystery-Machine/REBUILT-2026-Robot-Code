package frc.robot.subsystems;

import static edu.wpi.first.math.util.Units.metersToInches;
import static edu.wpi.first.units.Units.Inches;
import static frc.robot.Constants.target;
import static frc.robot.subsystems.TurretFlywheel.passing;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class TurretMeasurements extends SubsystemBase {

  public static double turretAngle;
  public static double m_fieldRelativeAngle;
  public static double distanceToTarget;
  private Translation2d vRobot;

  private Translation2d robotToTurret = new Translation2d(Inches.of(3.033), Inches.of(-8.505));

  public TurretMeasurements() {
  }

  public void getGearPosition(Pose2d robotPose, ChassisSpeeds robotSpeedSupplier) {

    //ROBOT SPEED READINGS
    var robotSpeeds = robotSpeedSupplier;

    if (robotPose.getRotation().getDegrees() < 0) {
      vRobot = passing == false ? new Translation2d(robotSpeeds.vyMetersPerSecond, -robotSpeeds.vxMetersPerSecond) : 
      new Translation2d(robotSpeeds.vyMetersPerSecond, -robotSpeeds.vxMetersPerSecond);
    } else if (robotPose.getRotation().getDegrees() > 0) {
      vRobot = passing == false ? new Translation2d(robotSpeeds.vyMetersPerSecond, robotSpeeds.vxMetersPerSecond) : 
      new Translation2d(-robotSpeeds.vyMetersPerSecond, robotSpeeds.vxMetersPerSecond);
    }

    var omega = robotSpeeds.omegaRadiansPerSecond;

    //SHOOTER TRANSLATION SETUP
    Translation2d turretPose = robotPose.getTranslation().plus(robotToTurret.rotateBy(robotPose.getRotation()));
    var robotToTurret = turretPose.minus(robotPose.getTranslation());
    var vTan = new Translation2d(-omega * robotToTurret.getY(), omega * robotToTurret.getX());

      Translation2d predictedTargetTranslation = target.getTranslation().minus(vRobot.plus(vTan));

    double tY = predictedTargetTranslation.getY() - robotPose.getY();
    double tX = predictedTargetTranslation.getX() - robotPose.getX();


    //SETTING SHOOTER TARGET
    Rotation2d fieldRelativeAngle = Rotation2d.fromRadians(Math.atan2(tY, tX));
    Rotation2d robotRelativeAngle = fieldRelativeAngle.minus(robotPose.getRotation());

    turretAngle = robotRelativeAngle.getDegrees() / 360;
    m_fieldRelativeAngle = fieldRelativeAngle.getDegrees() / 360;

    SmartDashboard.putNumber("Shooting Offset X", vRobot.getX());
    SmartDashboard.putNumber("Shooting Offset Y", vRobot.getY());
    SmartDashboard.putNumber("Robot Rotation", robotPose.getRotation().getDegrees());

    distanceToTarget = metersToInches(turretPose.getDistance(predictedTargetTranslation));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Distance to Target", distanceToTarget);
    SmartDashboard.putNumber("Turret Angle", turretAngle * 360);
  }
}
