// the Robot class is where the logic that controls the robot is
// this class is then used in the Main class

// this is the package that the robot class is located in
package frc.robot;

// imports needed classes from several vendor provided libraries
// these libraries are located in vendordeps
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.buttons.*;
import edu.wpi.first.cameraserver.CameraServer;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ColorMatch;

public class Robot extends TimedRobot {

  private static final int kPDPId = 0;
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private Joystick m_stick;
  private VictorSP conveyer;
  private VictorSP shooterLeft;
  private VictorSP shooterRight;
  private final VictorSP m_leftMotorBack = new VictorSP(0);
  private final VictorSP m_rightMotorBack = new VictorSP(2);
  private final DifferentialDrive m_robotDriveBack = new DifferentialDrive(m_leftMotorBack, m_rightMotorBack);
  private final VictorSP m_leftMotorFront = new VictorSP(1);
  private final VictorSP m_rightMotorFront = new VictorSP(3);
  private final DifferentialDrive m_robotDriveFront = new DifferentialDrive(m_leftMotorFront, m_rightMotorFront);

  // this creates an object m_pdp (power distrubution panel) that will
  // be used to monitor voltages, currents, and the temperature of the pdp
  private final PowerDistributionPanel m_pdp = new PowerDistributionPanel(kPDPId);

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


  private final VictorSP WoF = new VictorSP(9);

  private final Joystick m_Joystick = new Joystick(0);
  private final JoystickButton m_WoFButton = new JoystickButton(m_Joystick, 5);
  
  // default speed of WoF motor
  double defaultWoF = 0.4;

  // create new object of class WoF (used to control wheel of fortune)
  WoF wheelOfFortune;


  /**
   * This function is run when the robot is first started up 
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    conveyer = new VictorSP(5);
    shooterLeft = new VictorSP(6);
    shooterRight = new VictorSP(7);
    m_stick = new Joystick(0);

    // pass the motor, default speed, and color sensor data to the wheelOfFortune object
    wheelOfFortune = new WoF(WoF, m_colorMatcher, m_colorSensor, defaultWoF);
    
    CameraServer.getInstance().startAutomaticCapture();
  }


  @Override
  public void robotPeriodic() {
    /*
     * Get the current going through channel 7, in Amperes. The PDP returns the
     * current in increments of 0.125A. At low currents
     * the current readings tend to be less accurate.
     */
    SmartDashboard.putNumber("Current Channel 7", m_pdp.getCurrent(7));

    /*
     * Get the voltage going into the PDP, in Volts.
     * The PDP returns the voltage in increments of 0.05 Volts.
     */
    SmartDashboard.putNumber("Voltage", m_pdp.getVoltage());

    /*
     * Retrieves the temperature of the PDP, in degrees Celsius.
     */
    SmartDashboard.putNumber("Temperature", m_pdp.getTemperature());
  }

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

  @Override
  public void teleopInit() {
    
    super.teleopInit();
    SmartDashboard.putNumber("WoF Speed", defaultWoF);
  }
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {

    // this bot has four drive motors, so two arcade drives are needed
    m_robotDriveBack.arcadeDrive(m_stick.getY(), m_stick.getX());
    m_robotDriveFront.arcadeDrive(m_stick.getY(), m_stick.getX());

    // Trigger button will turn shooter motors on
    if(m_stick.getRawButtonPressed(1)){
      shooterLeft.setSpeed(1);  // Set to max speed
      shooterRight.setSpeed(1);
    }
    else{
      shooterLeft.setSpeed(0);  // Set back to 0 when not pressed
      shooterRight.setSpeed(0);
    }
    
    // Button 2 will turn the conveyer forwards
    if(m_stick.getRawButtonPressed(2)){
      conveyer.setSpeed(1);  // Set conveyer to max forwards speed
    }
    else{
      conveyer.setSpeed(0);;   // Set conveyer back to 0 when not pressed
    }

    // Button 3 will turn the conveyer backwards
    if(m_stick.getRawButtonPressed(3)){
      conveyer.setSpeed(-1);  // Set conveyer to max backwards speed
    }
    else{
      conveyer.setSpeed(0);;   // Set conveyer back to 0 when not pressed
    }

    // if button 4 is pressed, then rotate the WoF three times
    if(m_stick.getRawButton(4) == true) {
      wheelOfFortune.rotateThreeTimes();
    }

    // if button 5 is pressed then call the autoWoF function to find the correct color
    if (m_WoFButton.get() == true) {
      wheelOfFortune.autoWoF();
    }


  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
  
}
