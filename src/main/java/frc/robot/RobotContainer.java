package frc.robot;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.FollowPathCommand;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.RobotModeTriggers;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShootFuelCommand;
import frc.robot.commands.SnowblowFuelCommand;
import frc.robot.commands.StopTargeting;
import frc.robot.commands.TargetTurretCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.TurretRing;
import frc.robot.subsystems.TurretFlywheel;
import frc.robot.subsystems.TurretMeasurements;
import frc.robot.Constants.OdometryConstants;
import frc.robot.Constants.TurretConstants;

public class RobotContainer {

    //SUBSYSTEM SETUP
    private final Feeder Feeder = new Feeder();
    private final Indexer Indexer = new Indexer();
    private final Intake Intake = new Intake();
    private final CTRE_CANdle CANdle = new CTRE_CANdle();
    private final TurretRing TurretRing = new TurretRing();
    private final TurretFlywheel TurretWheel = new TurretFlywheel();
    private final TurretMeasurements TurretPosition = new TurretMeasurements();


    //PRE-GENERATED CTR-E SWERVE DRIVE CODE
    private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max angular velocity

    /* Setting up bindings for necessary control of the swerve drive platform */
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
    private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

    private final Telemetry logger = new Telemetry(MaxSpeed); 

    private final CommandXboxController driverController = new CommandXboxController(0);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
    private final SendableChooser<Command> autoChooser;

    public void intakeDriveSpeed() {
        drivetrain.applyRequest(() ->
            drive.withVelocityX(-driverController.getLeftY() * MaxSpeed * 0.25) // Drive forward with negative Y (forward)
                .withVelocityY(-driverController.getLeftX() * MaxSpeed *.25) // Drive left with negative X (left)
                .withRotationalRate(-driverController.getRightX() * MaxAngularRate *.25) // Drive counterclockwise with negative X (left)
        );
    }

    public RobotContainer() {

        NamedCommands.registerCommand("Target & Shoot", new ShootFuelCommand(TurretRing, TurretWheel, Indexer, Feeder, Intake, CANdle, driverController));
        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        configureBindings();

        CommandScheduler.getInstance().schedule(FollowPathCommand.warmupCommand());

        TurretPosition.setDefaultCommand(new RunCommand(() -> TurretPosition.getGearPosition(drivetrain.getState().Pose, drivetrain.getState().Speeds), TurretPosition));
    }




    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX(-driverController.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
                    .withVelocityY(-driverController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
                    .withRotationalRate(-driverController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );

        // driverController.x()
        // .onTrue(drivetrain.applyRequest(() ->
        //     drive.withVelocityX(-driverController.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
        //         .withVelocityY(-driverController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
        //         .withRotationalRate(TurretMeasurements.m_robotRelativeAngle * 13))
        // );

        // driverController.b()
        // .onTrue(drivetrain.applyRequest(() ->
        //     drive.withVelocityX(-driverController.getLeftY() * MaxSpeed) // Drive forward with negative Y (forward)
        //         .withVelocityY(-driverController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
        //         .withRotationalRate(-driverController.getRightX() * MaxAngularRate))
        // );


        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );

        // driverController.a().whileTrue(drivetrain.applyRequest(() -> brake));
        // driverController.b().whileTrue(drivetrain.applyRequest(() ->
        //     point.withModuleDirection(new Rotation2d(-driverController.getLeftY(), -driverController.getLeftX()))
        // ));

        // Run SysId routines when holding back/start and X/Y.
        // Note that each routine should be run exactly once in a single log.
        driverController.back().and(driverController.y()).whileTrue(drivetrain.sysIdDynamic(Direction.kForward));
        driverController.back().and(driverController.x()).whileTrue(drivetrain.sysIdDynamic(Direction.kReverse));
        driverController.start().and(driverController.y()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kForward));
        driverController.start().and(driverController.x()).whileTrue(drivetrain.sysIdQuasistatic(Direction.kReverse));

        // reset the field-centric heading on left bumper press
        driverController.leftBumper().onTrue(drivetrain.runOnce(() -> drivetrain.seedFieldCentric()));

        drivetrain.registerTelemetry(logger::telemeterize);


        //START OF MANIPULATOR CONTROLS
        driverController.leftTrigger(0.5)
        .whileTrue(new IntakeCommand(Intake, CANdle, driverController));

        driverController.rightTrigger(0.5)
        .whileTrue(new ShootFuelCommand(TurretRing, TurretWheel, Indexer, Feeder, Intake, CANdle, driverController));


        //TURRET MANUAL CONTROLS
        driverController.povRight()
        .whileTrue(new InstantCommand(() -> TurretRing.v_runTurret(500)))
        .onFalse(new InstantCommand(() -> TurretRing.v_stopMotor()));
        driverController.povLeft()
        .whileTrue(new InstantCommand(() -> TurretRing.v_runTurret(-500)))
        .onFalse(new InstantCommand(() -> TurretRing.v_stopMotor()));

        driverController.a()
        .onTrue(new InstantCommand(() -> TurretRing.v_resetEncoder()));
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
