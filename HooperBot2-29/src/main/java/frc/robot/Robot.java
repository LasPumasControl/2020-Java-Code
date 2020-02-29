/**
 * robot class that controls the robot
 */

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
// import com.revrobotics.ColorMatch;
// import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 11;
  private static final int kRearLeftChannel = 10;
  private static final int kFrontRightChannel = 9;
  private static final int kRearRightChannel = 8;

  private static final int kIntakeChannel = 1;

  private static final int kConveyerChannel = 2;
  private static final int kElevatorChannel = 3;
  private static final int kElevatorHelperChannel = 4;

  private static final int kClimberChannel = 6;
  private static final int kTransverseChannel = 7;

  private static final int kShooterChannel = 5;

  // private static final int kWheelOfFortuneChannel = 5;
  // private static final int kWheelOfFortune_Up_Down_Channel = 6;


  private static final int kJoystickChannel = 0;

  WPI_TalonFX frontLeft;
  WPI_TalonFX rearLeft;
  WPI_TalonFX frontRight;
  WPI_TalonFX rearRight;

  CANSparkMax intakeMotor;

  CANSparkMax conveyerMotor;
  CANSparkMax elevatorMotor;
  CANSparkMax elevatorHelperMotor;

  CANSparkMax climberMotor;
  CANSparkMax transverseMotor;

  CANSparkMax shooterMotor;

  // CANSparkMax wheelOfFortuneMotor;
  // CANSparkMax wheelOfFortuneUpDownMotor;

  NetworkTableEntry mode;
  NetworkTableEntry shooterShuffle;
  // NetworkTableEntry WoFReadyShuffle;
  NetworkTableEntry conveyerShuffle;
  NetworkTableEntry driveShuffle;
  NetworkTableEntry elevatorShuffle;
  NetworkTableEntry transverseShuffle;
  NetworkTableEntry intakeShuffle;
  NetworkTableEntry climberShuffle;
  NetworkTableEntry elevatorHelperShuffle;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;

  DigitalInput intakeLimit;
  DigitalInput shooterLimit;

  Elevator elevator;
  Shooter shooter;

  // WheelOfFortune WoF;

  //define i2c port for color sensor

  // private final I2C.Port i2cPort = I2C.Port.kOnboard;

  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a 
   * parameter. The device will be automatically initialized with default 
   * parameters.
   */
  // private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  /**
   * A Rev Color Match object is used to register and detect known colors. This can 
   * be calibrated ahead of time or during operation.
   * 
   * This object uses a simple euclidian distance to estimate the closest match
   * with given confidence range.
   */
  // private final ColorMatch m_colorMatcher = new ColorMatch();

  @Override
  public void robotInit() {

    // initalize 12 robot motors
    frontLeft = new WPI_TalonFX(kFrontLeftChannel);
    rearLeft = new WPI_TalonFX(kRearLeftChannel);
    frontRight = new WPI_TalonFX(kFrontRightChannel);
    rearRight = new WPI_TalonFX(kRearRightChannel);

    intakeMotor = new CANSparkMax(kIntakeChannel, MotorType.kBrushed);

    conveyerMotor = new CANSparkMax(kConveyerChannel, MotorType.kBrushed);
    elevatorMotor = new CANSparkMax(kElevatorChannel, MotorType.kBrushed);
    elevatorHelperMotor = new CANSparkMax(kElevatorHelperChannel, MotorType.kBrushed);

    climberMotor = new CANSparkMax(kClimberChannel, MotorType.kBrushless);
    transverseMotor = new CANSparkMax(kTransverseChannel, MotorType.kBrushed);

    shooterMotor = new CANSparkMax(kShooterChannel, MotorType.kBrushed);

    // wheelOfFortuneMotor = new CANSparkMax(kWheelOfFortuneChannel, MotorType.kBrushless);
    // wheelOfFortuneUpDownMotor = new CANSparkMax(kWheelOfFortune_Up_Down_Channel, MotorType.kBrushless);

    // init shuffleboard tabs
    // WoFReadyShuffle = Shuffleboard.getTab("Driving").add("WoF Ready?", false).getEntry();
    mode = Shuffleboard.getTab("Driving").add("Manual Mode?", false).getEntry();
    shooterShuffle = Shuffleboard.getTab("LiveWindow").add("Shooter Speed", 1).getEntry();
    conveyerShuffle = Shuffleboard.getTab("Driving").add("Conveyer Speed", 1).getEntry();
    driveShuffle = Shuffleboard.getTab("Driving").add("Drive Speed", 1).getEntry();
    elevatorShuffle = Shuffleboard.getTab("Driving").add("Elevator Speed", 1).getEntry();
    intakeShuffle = Shuffleboard.getTab("Driving").add("Intake Speed", 1).getEntry();
    transverseShuffle = Shuffleboard.getTab("Driving").add("Transverse Speed", 1).getEntry();
    climberShuffle = Shuffleboard.getTab("Driving").add("Climber Speed", 1).getEntry();
    elevatorHelperShuffle = Shuffleboard.getTab("Driving").add("Elevator Helper Speed", 1).getEntry();


    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kJoystickChannel);

    // WoF = new WheelOfFortune(wheelOfFortuneMotor, m_colorMatcher, m_colorSensor, 0.4, m_stick);

    intakeLimit = new DigitalInput(0);
    shooterLimit = new DigitalInput(1);

    elevator = new Elevator(intakeLimit, shooterLimit, conveyerMotor, elevatorMotor, elevatorHelperMotor);
    shooter = new Shooter(shooterMotor, elevator);
  }

  @Override
  public void robotPeriodic() {

    // boolean WoFReady = WoFReadyShuffle.getBoolean(false);
    /* 
    if(WoFReady == true) {
      if (m_stick.getRawButton(2)) {
        WoF.rotateThreeTimes();
      }

      if (m_stick.getRawButton(3)) {
        WoF.autoWoF();
      }
    }
    


    if (m_stick.getRawButton(4)){
      wheelOfFortuneUpDownMotor.set(1);
    } else {
      wheelOfFortuneUpDownMotor.set(0);
    }

    
    if (m_stick.getRawButton(5)){
      wheelOfFortuneUpDownMotor.set(-1);
    } else {
      wheelOfFortuneUpDownMotor.set(0);
    }

    */

    boolean manualMode = mode.getBoolean(false);

    double conveyerSpeed = conveyerShuffle.getDouble(1);
    double shooterSpeed = shooterShuffle.getDouble(1);
    double elevatorSpeed = elevatorShuffle.getDouble(1);
    double intakeSpeed = intakeShuffle.getDouble(1);
    double transverseSpeed = transverseShuffle.getDouble(1);
    double climberSpeed = climberShuffle.getDouble(1);
    double elevatorHelperSpeed = elevatorHelperShuffle.getDouble(1);


    if (manualMode) {
      if (m_stick.getRawButton(1)) {
        shooterMotor.set(shooterSpeed);
      } else {
        shooterMotor.set(0);
      }
  
      if(m_stick.getRawButton(3)) {
        conveyerMotor.set(conveyerSpeed);
      } else {
        conveyerMotor.set(0);
      }

      if(m_stick.getRawButton(4)) {
        elevatorMotor.set(elevatorSpeed);
      } else {
        elevatorMotor.set(0);
      }

      if(m_stick.getRawButton(5)) {
        elevatorHelperMotor.set(elevatorHelperSpeed);
      } else {
        elevatorHelperMotor.set(0);
      }
    } else {

      if(m_stick.getRawButton(1)) {
        shooter.shoot(shooterSpeed, conveyerSpeed, elevatorSpeed, elevatorHelperSpeed);
      }

      elevator.runElevator(conveyerSpeed, elevatorSpeed, elevatorHelperSpeed);
    }
    

    if(m_stick.getRawButton(2)) {
      intakeMotor.set(intakeSpeed);
    } else {
      intakeMotor.set(0);
    }

    if(m_stick.getRawButton(7)) {
      transverseMotor.set(transverseSpeed);
    } else {
      transverseMotor.set(0);
    }

    if(m_stick.getRawButton(8)) {
      climberMotor.set(climberSpeed);
    } else {
      climberMotor.set(0);
    }
    }


  @Override
  public void teleopPeriodic() {

    double driveSpeed = driveShuffle.getDouble(1);

    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(m_stick.getX() * driveSpeed, m_stick.getY() * driveSpeed, m_stick.getZ() *driveSpeed, 0.0);
  }
}
