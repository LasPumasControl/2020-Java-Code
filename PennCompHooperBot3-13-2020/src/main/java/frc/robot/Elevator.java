/**
 * This class controls the elevator
 */

package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;

public class Elevator {
    int ballCount = 0;
    DigitalInput intakeLimit;
    DigitalInput shooterLimit;
    CANSparkMax elevator;
    CANSparkMax elevatorHelper;
    CANSparkMax conveyer;

    public Elevator(DigitalInput intakeLimit, DigitalInput shooterLimit, CANSparkMax conveyer, CANSparkMax elevator, CANSparkMax elevatorHelper) {
        this.intakeLimit = intakeLimit;
        this.shooterLimit = shooterLimit;
        this.elevator = elevator;
        this.conveyer = conveyer;
        this.elevatorHelper = elevatorHelper;
    }
    

    public void runElevator(double conveyerSpeed, double elevatorSpeed, double elevatorHelperSpeed) {

        int prevBallCount = ballCount;

        if(intakeLimit.get()) {
            ballCount++;

            runConveyer(conveyerSpeed);
        }

        if(ballCount == 1 && (prevBallCount < ballCount)) {

            runElevatorOneBall(elevatorSpeed, elevatorHelperSpeed);

        }
        
        if(ballCount == 3 && (prevBallCount < ballCount)) {
            runElevatorUp(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed);
        }

    }

    public void runConveyer(double conveyerSpeed) {
        conveyer.set(conveyerSpeed);

        Timer.waitMilli(2000);

        conveyer.set(0);
    }

    public void runElevatorOneBall(double elevatorSpeed, double elevatorHelperSpeed) {
        elevator.set(elevatorSpeed);
        elevatorHelper.set(elevatorHelperSpeed);

        Timer.waitMilli(2000);

        elevator.set(0);
        elevatorHelper.set(0);
    }

    public void runElevatorUp (double elevatorSpeed, double elevatorHelperSpeed, double conveyerSpeed) {
        boolean prevLimit = shooterLimit.get();
        boolean stop = false;
        while(!stop) {
            elevator.set(elevatorSpeed);
            elevatorHelper.set(elevatorHelperSpeed);
            conveyer.set(conveyerSpeed);
            if(shooterLimit.get() && !prevLimit) {
                stop = true;
            }
            prevLimit = shooterLimit.get();
        }

        elevator.set(0);
        elevatorHelper.set(0);
        conveyer.set(0);

    }

    public void shoot (double elevatorSpeed, double elevatorHelperSpeed, double conveyerSpeed) {
        runElevatorUp(elevatorSpeed, elevatorHelperSpeed, conveyerSpeed);
        ballCount--;
    }

 }
