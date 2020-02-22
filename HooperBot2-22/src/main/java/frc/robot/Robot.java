/**
 * robot class that controls the robot
 */

package frc.robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;

import com.revrobotics.CANSparkMax;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

/**
 * This is a demo program showing how to use Mecanum control with the RobotDrive
 * class.
 */
public class Robot extends TimedRobot {
  private static final int kFrontLeftChannel = 12;
  private static final int kRearLeftChannel = 11;
  private static final int kFrontRightChannel = 10;
  private static final int kRearRightChannel = 9;

  private static final int kIntakeChannel = 1;

  private static final int kConveyerChannel = 2;
  private static final int kElevatorChannel = 3;

  private static final int kClimberChannel = 7;
  private static final int kTransverseChannel = 8;

  private static final int kShooterChannel = 4;

  private static final int kWheelOfFortuneChannel = 5;
  private static final int kWheelOfFortune_Up_Down_Channel = 6;


  private static final int kJoystickChannel = 0;

  WPI_TalonFX frontLeft;
  WPI_TalonFX rearLeft;
  WPI_TalonFX frontRight;
  WPI_TalonFX rearRight;

  CANSparkMax intakeMotor;

  CANSparkMax conveyerMotor;
  CANSparkMax elevatorMotor;

  CANSparkMax climberMotor;
  CANSparkMax transverseMotor;

  CANSparkMax shooterMotor;

  CANSparkMax wheelOfFortuneMotor;
  CANSparkMax wheelOfFortuneUpDownMotor;

  NetworkTableEntry mode;
  NetworkTableEntry shooterSpeed;
  NetworkTableEntry WoFReadyShuffle;

  private MecanumDrive m_robotDrive;
  private Joystick m_stick;

  WheelOfFortune WoF;

  //define i2c port for color sensor

  private final I2C.Port i2cPort = I2C.Port.kOnboard;

  /**
   * A Rev Color Sensor V3 object is constructed with an I2C port as a 
   * parameter. The device will be automatically initialized with default 
   * parameters.
   */
  private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

  /**
   * A Rev Color Match object is used to register and detect known colors. This can 
   * be calibrated ahead of time or during operation.
   * 
   * This object uses a simple euclidian distance to estimate the closest match
   * with given confidence range.
   */
  private final ColorMatch m_colorMatcher = new ColorMatch();

  @Override
  public void robotInit() {

    // initalize 12 robot motors
    frontLeft = new WPI_TalonFX(kFrontLeftChannel);
    rearLeft = new WPI_TalonFX(kRearLeftChannel);
    frontRight = new WPI_TalonFX(kFrontRightChannel);
    rearRight = new WPI_TalonFX(kRearRightChannel);

    intakeMotor = new CANSparkMax(kIntakeChannel, MotorType.kBrushless);

    conveyerMotor = new CANSparkMax(kConveyerChannel, MotorType.kBrushless);
    elevatorMotor = new CANSparkMax(kElevatorChannel, MotorType.kBrushless);

    climberMotor = new CANSparkMax(kClimberChannel, MotorType.kBrushed);
    transverseMotor = new CANSparkMax(kTransverseChannel, MotorType.kBrushless);

    shooterMotor = new CANSparkMax(kShooterChannel, MotorType.kBrushless);

    wheelOfFortuneMotor = new CANSparkMax(kWheelOfFortuneChannel, MotorType.kBrushless);
    wheelOfFortuneUpDownMotor = new CANSparkMax(kWheelOfFortune_Up_Down_Channel, MotorType.kBrushless);

    // init shuffleboard tabs
    WoFReadyShuffle = Shuffleboard.getTab("Driving").add("WoF Ready?", false).getEntry();
    mode = Shuffleboard.getTab("Driving").add("Manual Mode?", false).getEntry();
    shooterSpeed = Shuffleboard.getTab("LiveWindow").add("Shooter Speed", 0).getEntry();  

    // Invert the left side motors.
    // You may need to change or remove this to match your robot.
    frontLeft.setInverted(true);
    rearLeft.setInverted(true);

    m_robotDrive = new MecanumDrive(frontLeft, rearLeft, frontRight, rearRight);

    m_stick = new Joystick(kJoystickChannel);

    WoF = new WheelOfFortune(wheelOfFortuneMotor, m_colorMatcher, m_colorSensor, 0.4, m_stick);
  }

  @Override
  public void robotPeriodic() {

    boolean WoFReady = WoFReadyShuffle.getBoolean(false);

    if(WoFReady == true) {
      if (m_stick.getRawButton(5)) {
        WoF.rotateThreeTimes();
      }

      if (m_stick.getRawButton(6)) {
        WoF.autoWoF();
      }
    }


    if (m_stick.getRawButton(8)){
      wheelOfFortuneUpDownMotor.set(1);
    } else {
      wheelOfFortuneUpDownMotor.set(0);
    }

    
    if (m_stick.getRawButton(9)){
      wheelOfFortuneUpDownMotor.set(-1);
    } else {
      wheelOfFortuneUpDownMotor.set(0);
    }

    boolean manualMode = mode.getBoolean(false);

    if (manualMode) {
      if (m_stick.getRawButton(1)) {
        shooterMotor.set(1);
      } else {
        shooterMotor.set(0);
      }

      if (m_stick.getRawButton(3)) {
        conveyerMotor.set(-1);
      } else {
        conveyerMotor.set(0);
      }
  
      if(m_stick.getRawButton(4)) {
        conveyerMotor.set(1);
      } else {
        conveyerMotor.set(0);
      }
    }
  }

  @Override
  public void teleopPeriodic() {

    // Use the joystick X axis for lateral movement, Y axis for forward
    // movement, and Z axis for rotation.
    m_robotDrive.driveCartesian(m_stick.getX(), m_stick.getY(), m_stick.getZ(), 0.0);
  }
}
