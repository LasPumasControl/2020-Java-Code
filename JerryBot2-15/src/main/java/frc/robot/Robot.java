/**
 * robot class that controls the robot
 */

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

import com.revrobotics.ColorSensorV3;

import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.I2C;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  //nine motor controllers
  private VictorSP frontLeftDrive;
  private VictorSP backLeftDrive;
  private VictorSP frontRightDrive;
  private VictorSP backRightDrive;
  private VictorSP intakeMotor;
  private VictorSP conveyer;
  private VictorSP shooterLeft;
  private VictorSP shooterRight;
  private VictorSP wheelOfFortuneMotor;
  private Joystick m_stick;

  //speed controller groups

  private DifferentialDrive driveTrain;
  private SpeedControllerGroup leftDrive;
  private SpeedControllerGroup rightDrive;
 
  //wheel of fortune object

  private WheelOfFortune WoF;

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




  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    m_stick = new Joystick(0);
    frontLeftDrive = new VictorSP(0);
    backLeftDrive = new VictorSP(1);
    frontRightDrive = new VictorSP(3);
    backRightDrive = new VictorSP(2);
    intakeMotor = new VictorSP(7);
    conveyer = new VictorSP(8);
    shooterLeft = new VictorSP(5);
    shooterRight = new VictorSP(6);
    wheelOfFortuneMotor = new VictorSP(4);
    
    leftDrive = new SpeedControllerGroup(frontLeftDrive, backLeftDrive);
    rightDrive = new SpeedControllerGroup(frontRightDrive, backRightDrive);
    driveTrain = new DifferentialDrive(leftDrive, rightDrive);
    double defaultWoF = .4;
    WoF = new WheelOfFortune(wheelOfFortuneMotor, m_colorMatcher, m_colorSensor, defaultWoF, m_stick);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
        // Trigger button?
        if (m_stick.getRawButton(1)) {
          shooterLeft.setSpeed(-1); // Set to max speed
          shooterRight.setSpeed(1);
        } else {
          shooterLeft.setSpeed(0); // Set back to 0 when not pressed
          shooterRight.setSpeed(0);
        }
        if (m_stick.getRawButton(5)){
          WoF.rotateThreeTimes();
        }
    
        if (m_stick.getRawButton(6)){
          WoF.autoWoF();
        }
    
    
        // Where is button 2?
        if (m_stick.getRawButton(3)) {
          conveyer.setSpeed(0.6); // Set conveyer to max speed fowards
        } else {
          conveyer.setSpeed(0);
          // Set conveyer back to 0 when not pressed
        }
        if (m_stick.getRawButton(4)) {
          intakeMotor.setSpeed(-1); // Set intake to max speed fowards
        } else {
          intakeMotor.setSpeed(0);
          // Set intake back to 0 when not pressed
        }
        if (m_stick.getRawButton(11)) {
          shooterLeft.setSpeed(-1); // Set intake to max speed fowards
        } else {
          shooterLeft.setSpeed(0);
          // Set intake back to 0 when not pressed
        }
        if (m_stick.getRawButton(12)) {
          shooterRight.setSpeed(1); // Set intake to max speed fowards
        } else {
          shooterRight.setSpeed(0);
          // Set intake back to 0 when not pressed
        }
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    driveTrain.arcadeDrive(-1.0*m_stick.getY(), m_stick.getX());


  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
