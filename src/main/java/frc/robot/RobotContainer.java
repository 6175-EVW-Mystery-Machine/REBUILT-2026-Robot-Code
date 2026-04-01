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
import frc.robot.commands.AutoIntakeCommand;
import frc.robot.commands.AutoShootCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.commands.ShootCommand;
import frc.robot.commands.StopAutoIntakeCommand;
import frc.robot.commands.StopAutoShootCommand;
import frc.robot.commands.UnJamCommand;
import frc.robot.commands.FuelMailCommand;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CTRE_CANdle;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import frc.robot.subsystems.Feeder;
import frc.robot.subsystems.Indexer;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.TurretRing;
import frc.robot.subsystems.TurretFlywheel;
import frc.robot.subsystems.TurretMeasurements;

public class RobotContainer {

    //SUBSYSTEM SETUP
    private final Feeder Feeder = new Feeder();
    private final Indexer Indexer = new Indexer();
    private final Intake Intake = new Intake();
    private final CTRE_CANdle CANdle = new CTRE_CANdle();
    private final TurretRing TurretRing = new TurretRing();
    private final TurretFlywheel TurretWheel = new TurretFlywheel();
    private final TurretMeasurements TurretMeasurements = new TurretMeasurements();


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

    public RobotContainer() {

        NamedCommands.registerCommand("Target & Shoot", new AutoShootCommand(TurretWheel, TurretRing, CANdle, Indexer, Feeder, Intake));
        NamedCommands.registerCommand("Stop Target & Shoot", new StopAutoShootCommand(TurretWheel, TurretRing, CANdle, Indexer, Feeder, Intake));
        NamedCommands.registerCommand("Intake", new AutoIntakeCommand(Intake, CANdle));
        NamedCommands.registerCommand("Stop Intake", new StopAutoIntakeCommand(Intake, CANdle));
        autoChooser = AutoBuilder.buildAutoChooser();
        SmartDashboard.putData("Auto Chooser", autoChooser);

        configureBindings();

        CommandScheduler.getInstance().schedule(FollowPathCommand.warmupCommand());

        TurretMeasurements.setDefaultCommand(new RunCommand(() -> TurretMeasurements.getTurretData(drivetrain.getState().Pose, drivetrain.getState().Speeds), TurretMeasurements));
    }




    private void configureBindings() {
        // Note that X is defined as forward according to WPILib convention,
        // and Y is defined as to the left according to WPILib convention.
        drivetrain.setDefaultCommand(
            // Drivetrain will execute this command periodically
            drivetrain.applyRequest(() ->
                drive.withVelocityX((-driverController.getLeftY() * MaxSpeed) * .75) // Drive forward with negative Y (forward)
                    .withVelocityY((-driverController.getLeftX() * MaxSpeed) * .75) // Drive left with negative X (left)
                    .withRotationalRate(-driverController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative X (left)
            )
        );


        // Idle while the robot is disabled. This ensures the configured
        // neutral mode is applied to the drive motors while disabled.
        final var idle = new SwerveRequest.Idle();
        RobotModeTriggers.disabled().whileTrue(
            drivetrain.applyRequest(() -> idle).ignoringDisable(true)
        );


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
        .whileTrue(new ShootCommand(TurretRing, TurretWheel, Indexer, Feeder, Intake, CANdle, driverController));


        //TURRET MANUAL CONTROLS
        driverController.povRight()
        .whileTrue(new InstantCommand(() -> TurretRing.v_runTurret(500)))
        .onFalse(new InstantCommand(() -> TurretRing.v_stopMotor()));
        driverController.povLeft()
        .whileTrue(new InstantCommand(() -> TurretRing.v_runTurret(-500)))
        .onFalse(new InstantCommand(() -> TurretRing.v_stopMotor()));

        driverController.a()
        .whileTrue(new FuelMailCommand(Intake, CANdle, driverController));

        driverController.y()
        .whileTrue(new UnJamCommand(Indexer, CANdle, driverController));
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected();
    }
}
