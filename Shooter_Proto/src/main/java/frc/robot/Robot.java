/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;


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


  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
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


    m_robotDriveBack.arcadeDrive(m_stick.getY(), m_stick.getX());
    m_robotDriveFront.arcadeDrive(m_stick.getY(), m_stick.getX());
    // Trigger button?
    if(m_stick.getRawButtonPressed(1)){
      shooterLeft.setSpeed(1);  // Set to max speed
      shooterRight.setSpeed(1);
    }
    else{
      shooterLeft.setSpeed(0);  // Set back to 0 when not pressed
      shooterRight.setSpeed(0);
    }
    
    // Where is button 1?
    if(m_stick.getRawButtonPressed(2)){
      conveyer.setSpeed(1);  // Set conveyer to max speed
    }
    else{
      conveyer.setSpeed(0);;   // Set conveyer back to 0 when not pressed
    }

  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
