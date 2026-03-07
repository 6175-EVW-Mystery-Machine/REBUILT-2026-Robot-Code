package frc.robot.subsystems;

import static edu.wpi.first.math.util.Units.degreesToRadians;
import static edu.wpi.first.math.util.Units.metersToInches;
import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.Rotation;
import static edu.wpi.first.units.Units.Rotations;
import static frc.robot.Constants.blueHubLocation;
import static frc.robot.Constants.hubLocation;
import static frc.robot.Constants.redHubLocation;

import java.util.function.Supplier;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.units.measure.MutAngle;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.subsystems.TurretFlywheel.DesiredMotorRPM;

public class TurretMeasurements extends SubsystemBase {

  public static double m_robotRelativeAngle;
  public static double m_fieldRelativeAngle;
  public static double distanceToTarget;

  private Translation2d robotToTurret = new Translation2d(Inches.of(3.125), Inches.of(-8.375));

  public TurretMeasurements() {
  }

  public void getGearPosition(Pose2d robotPose, ChassisSpeeds robotSpeedSupplier) {
    
    //HUB LOCATION CHOOSING
    if (DriverStation.getAlliance().isEmpty()) {
      return;
    }
      hubLocation = DriverStation.getAlliance().get() == Alliance.Blue ? blueHubLocation : redHubLocation;


    //ROBOT SPEED READINGS
    var robotSpeeds = robotSpeedSupplier;
      var vRobot = new Translation2d(robotSpeeds.vxMetersPerSecond, robotSpeeds.vyMetersPerSecond);
      var omega = robotSpeeds.omegaRadiansPerSecond;


    //SHOOTER TRANSLATION SETUP
    Translation2d turretPose = robotPose.getTranslation().plus(robotToTurret.rotateBy(robotPose.getRotation()));
    var robotToShooterTrans = turretPose.minus(robotPose.getTranslation());
      var vTan = new Translation2d(omega * robotToShooterTrans.getX(), -omega * robotToShooterTrans.getY());
    var effectiveShooterVelocity = vRobot.plus(vTan);


    //MORE THINGS THAT I DONT KNOW HOW TO DESCRIBE
      var targetDist = turretPose.getDistance(hubLocation.getTranslation());
      var timeUntilScored = 0.0;
        double fuelExitVelocity = 0.6 * DesiredMotorRPM;
      
      if (DesiredMotorRPM > 0) {
      timeUntilScored = targetDist / fuelExitVelocity * Math.cos(degreesToRadians(35));
      }

      Translation2d predictedTargetTranslation = hubLocation.getTranslation().minus(effectiveShooterVelocity);

    double tY = hubLocation.getY() - robotPose.getY();
    double tX = hubLocation.getX() - robotPose.getX();

    //SETTING SHOOTER TARGET
    Rotation2d fieldRelativeAngle = Rotation2d.fromRadians(Math.atan2(tY, tX));
    Rotation2d robotRelativeAngle = fieldRelativeAngle.minus(robotPose.getRotation());

    m_robotRelativeAngle = robotRelativeAngle.getDegrees() / 360;
    m_fieldRelativeAngle = fieldRelativeAngle.getDegrees() / 360;

    SmartDashboard.putNumber("RobotPose", m_robotRelativeAngle);
    SmartDashboard.putNumber("Shooting Offset X", effectiveShooterVelocity.getX());
    SmartDashboard.putNumber("Shooting Offset Y", effectiveShooterVelocity.getY());
    SmartDashboard.putNumber("Robot Strafe Speed", robotSpeeds.vxMetersPerSecond);

    distanceToTarget = metersToInches(turretPose.getDistance(hubLocation.getTranslation()));
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Distance to Target", distanceToTarget);
    SmartDashboard.putNumber("Turret Angle", m_robotRelativeAngle * 360);
  }
}
