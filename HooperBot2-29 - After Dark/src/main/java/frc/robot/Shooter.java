/**
 * This is the class that controls the shooter
 */

 package frc.robot;

import com.revrobotics.CANSparkMax;

public class Shooter {
     CANSparkMax shooter;
     Elevator elevator;

     public Shooter(CANSparkMax shooter, Elevator elevator) {
         this.shooter = shooter;
         this.elevator = elevator;
     }

     public void shoot(double shooterSpeed, double conveyerSpeed, double elevatorSpeed, double elevatorHelperSpeed) {
        shooter.set(shooterSpeed);

        Timer.waitMilli(1000);

        elevator.shoot(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed); 
        
        shooter.set(0);
     }
 }