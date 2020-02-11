/**
 * File: JerryBotProtoCode2-9
 * By: Baylee Carpenter
 * FRC team 2197
 * Date: 02-09-2020
 * 
 * Description: This is prototype code for several features on Jerry's Bot:
 * Arcade drive
 * Shuffleboard implementation
 * CAN monitoring of PDP
 * USB Camera
 * Manual intake and shooting
 * Automated Wheel of Fortune
 */

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;


public final class Main {
  private Main() {
  }

  /**
   * Main initialization function. Do not perform any initialization here.
   *
   * <p>If you change your main robot class, change the parameter type.
   */
  public static void main(String... args) {
    RobotBase.startRobot(Robot::new);
  }
}
