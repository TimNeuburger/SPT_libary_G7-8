package Tests;

import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;

import java.util.concurrent.TimeUnit;

public class TestReactionTime {
    static MotorEV3 leftMotor;
    static MotorEV3 rightMotor;
    static IRSensor irSensor;
    static DifferentialDrive motors;
    static EV3Key Enter;
    static ColorSensorEV3 leftColor;
    static ColorSensorEV3 rightColor;

    static final int MAX_SPEED_STRAIGHT = 400;
    static final int MAX_SPEED_TURNNING = 300;
    static int CALIBRATION_FACTOR = 100;
    static final int GROUND_LEVEL = 20;
    static final int LEFT_SENSOR_VALUE = 368;
    static final int RIGHT_SENSOR_VALUE = 8;
    static DriveMode driveMode = DriveMode.STOP;
    static DriveMode lastDriveMode = DriveMode.STOP;

    public static void main(final String[] args) throws InterruptedException {

        setUp();

        while(true) {

            Enter.waitForPress();

            driveMode = DriveMode.STRAIGHT;
            while (irSensor.getDistance() < GROUND_LEVEL
                    || driveMode != DriveMode.STRAIGHT) {
                checkLeftSensor();

                checkRightSensor();

                if (driveMode != lastDriveMode) {
                    handleDriveModeChange();
                }
            }
            driveMode = DriveMode.STOP;
            lastDriveMode = driveMode;
            motors.stop();
        }
    }

    private static void handleDriveModeChange() {
        lastDriveMode = driveMode;
        switch (driveMode) {
            case STOP:
                motors.stop();
                break;
            case STRAIGHT:
                motors.move(MAX_SPEED_STRAIGHT * CALIBRATION_FACTOR / 100, MAX_SPEED_STRAIGHT);
                break;
            case LEFT_WHITE:
                motors.move(MAX_SPEED_TURNNING / 2, -MAX_SPEED_TURNNING);
                break;
            case LEFT_BLACK:
                motors.move(MAX_SPEED_TURNNING / 5, -MAX_SPEED_TURNNING);
                break;
            case RIGHT_WHITE:
                motors.move(-MAX_SPEED_TURNNING, MAX_SPEED_TURNNING / 2);
                break;
            case RIGHT_BLACK:
                motors.move(-MAX_SPEED_TURNNING, MAX_SPEED_TURNNING / 5);
                break;
        }
    }

    private static void checkRightSensor() throws InterruptedException {
        if(rightColor.getValue1() < RIGHT_SENSOR_VALUE && driveMode != DriveMode.RIGHT_WHITE && driveMode != DriveMode.RIGHT_BLACK) {
            motors.stop();
            TimeUnit.MILLISECONDS.sleep(150);
            driveMode = DriveMode.RIGHT_WHITE;
        } else if (rightColor.getValue1() < RIGHT_SENSOR_VALUE && driveMode == DriveMode.RIGHT_WHITE) {
            driveMode = DriveMode.RIGHT_BLACK;
        } else if (rightColor.getValue1() > RIGHT_SENSOR_VALUE && driveMode == DriveMode.RIGHT_BLACK) {
            driveMode = DriveMode.STRAIGHT;
        }
    }

    private static void checkLeftSensor() throws InterruptedException {
        if(leftColor.getValue1() < LEFT_SENSOR_VALUE && driveMode != DriveMode.LEFT_WHITE && driveMode != DriveMode.LEFT_BLACK) {
            motors.stop();
            TimeUnit.MILLISECONDS.sleep(150);
            driveMode = DriveMode.LEFT_WHITE;
        } else if (leftColor.getValue1() < LEFT_SENSOR_VALUE && driveMode == DriveMode.LEFT_WHITE) {
            driveMode = DriveMode.LEFT_BLACK;
        } else if (leftColor.getValue1() > LEFT_SENSOR_VALUE && driveMode == DriveMode.LEFT_BLACK) {
            driveMode = DriveMode.STRAIGHT;
        }
    }

    static void setUp() {
        System.out.println("Creating Motor A & B");
        leftMotor = new MotorEV3(MotorPort.A);
        rightMotor = new MotorEV3(MotorPort.B);
        irSensor = new IRSensor(SensorPort.S3);
        motors = new DifferentialDrive(leftMotor, rightMotor);
        Enter = new EV3Key(28);

        System.out.println("Creating Sensors");
        leftColor = new ColorSensorEV3(SensorPort.S1, ColorMode.RED);
        rightColor = new ColorSensorEV3(SensorPort.S2, ColorMode.RED);
        leftColor.setLight(Color.RED);
        rightColor.setLight(Color.RED);

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motors.stop();
            }
        }));
    }

}
