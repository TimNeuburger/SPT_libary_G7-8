import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;

import java.util.concurrent.TimeUnit;

public class FinalVersion {

    static MotorEV3 leftMotor;
    static MotorEV3 rightMotor;
    static IRSensor irSensor;
    static DifferentialDrive motors;
    static EV3Key Enter;
    static ColorSensorEV3 leftColor;
    static ColorSensorEV3 rightColor;

    static final int MAX_SPEED_STRAIGHT = 400; //400
    static final int MAX_SPEED_STRAIGHT_Fast = 700; //400
    static final int MAX_SPEED_TURNNING = 400; //300
    static int CALIBRATION_FACTOR = 100;
    static final int GROUND_LEVEL = 20;
    static final int LEFT_SENSOR_VALUE = 368;
    static final int RIGHT_SENSOR_VALUE = 8;
    static DriveMode driveMode = DriveMode.STOP;
    static DriveMode lastDriveMode = DriveMode.STOP;
    static boolean blackRight;
    static boolean blackLeft;
    static boolean fastMode;
    static boolean RightOverTurn;
    static boolean LeftOverTurn;

    public static void main(final String[] args) throws InterruptedException {

        setUp();

        while(true) {

            Enter.waitForPress();

            driveMode = DriveMode.STRAIGHT;
            blackLeft = leftColor.getValue1() < LEFT_SENSOR_VALUE;
            blackRight = rightColor.getValue1() < RIGHT_SENSOR_VALUE;
            while (!blackLeft || !blackRight) {
                if(!blackRight && !blackLeft && driveMode == DriveMode.STOP) {
                    driveMode = DriveMode.STRAIGHT;
                }

                checkLeftSensor();

                checkRightSensor();

                blackLeft = leftColor.getValue1() < LEFT_SENSOR_VALUE;
                blackRight = rightColor.getValue1() < RIGHT_SENSOR_VALUE;

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
                if(fastMode) {
                    motors.move(0, MAX_SPEED_STRAIGHT_Fast/2);
                    motors.move(MAX_SPEED_STRAIGHT_Fast * CALIBRATION_FACTOR / 100, MAX_SPEED_STRAIGHT_Fast);
                } else {
                    motors.move(MAX_SPEED_STRAIGHT * CALIBRATION_FACTOR / 100, MAX_SPEED_STRAIGHT);
                }
                break;
            case LEFT_WHITE:
                motors.move(MAX_SPEED_TURNNING / 3, -MAX_SPEED_TURNNING);
                break;
            case LEFT_BLACK:
                motors.move(MAX_SPEED_TURNNING / 3, -MAX_SPEED_TURNNING);
                break;
            case RIGHT_WHITE:
                motors.move(-MAX_SPEED_TURNNING, MAX_SPEED_TURNNING / 3);
                break;
            case RIGHT_BLACK:
                motors.move(-MAX_SPEED_TURNNING, MAX_SPEED_TURNNING / 3);
                break;
        }
    }

    private static void checkRightSensor() throws InterruptedException {
        if(blackLeft && driveMode == DriveMode.RIGHT_WHITE) {
            motors.move(MAX_SPEED_TURNNING, -MAX_SPEED_TURNNING / 3);
            RightOverTurn = true;
        }
        if(blackRight && ((driveMode != DriveMode.RIGHT_WHITE && driveMode != DriveMode.RIGHT_BLACK && driveMode != DriveMode.LEFT_WHITE && driveMode != DriveMode.LEFT_BLACK) || RightOverTurn)) {
            rightMotor.stop();
            leftMotor.stop();
            lastDriveMode = DriveMode.STOP;
            TimeUnit.MILLISECONDS.sleep(150);
            driveMode = DriveMode.RIGHT_WHITE;
            blackRight = rightColor.getValue1() < RIGHT_SENSOR_VALUE;
            fastMode = blackRight;
            RightOverTurn = false;
        }
        if (blackRight && driveMode == DriveMode.RIGHT_WHITE) {
            driveMode = DriveMode.RIGHT_BLACK;
        } else if (!blackRight && driveMode == DriveMode.RIGHT_BLACK) {
            TimeUnit.MILLISECONDS.sleep(40);
            motors.stop();
            driveMode = DriveMode.STOP;
            TimeUnit.MILLISECONDS.sleep(75);
        }
    }

    private static void checkLeftSensor() throws InterruptedException {
        if(blackRight && driveMode == DriveMode.LEFT_WHITE) {
            motors.move(-MAX_SPEED_TURNNING / 3, MAX_SPEED_TURNNING);
            LeftOverTurn = true;
        }
        if(blackLeft && ((driveMode != DriveMode.LEFT_WHITE && driveMode != DriveMode.LEFT_BLACK && driveMode != DriveMode.RIGHT_WHITE && driveMode != DriveMode.RIGHT_BLACK) || LeftOverTurn)) {
            leftMotor.stop();
            rightMotor.stop();
            lastDriveMode = DriveMode.STOP;
            TimeUnit.MILLISECONDS.sleep(150);
            driveMode = DriveMode.LEFT_WHITE;
            blackLeft = leftColor.getValue1() < LEFT_SENSOR_VALUE;
            fastMode = blackLeft;
            LeftOverTurn = false;
        }
        if (blackLeft && driveMode == DriveMode.LEFT_WHITE) {
            driveMode = DriveMode.LEFT_BLACK;
        } else if (!blackLeft && driveMode == DriveMode.LEFT_BLACK) {
            TimeUnit.MILLISECONDS.sleep(40);
            motors.stop();
            driveMode = DriveMode.STOP;
            TimeUnit.MILLISECONDS.sleep(75);
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
        rightColor = new ColorSensorEV3(SensorPort.S4, ColorMode.RED);
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
