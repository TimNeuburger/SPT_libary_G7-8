import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.KeyListener;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class MotorCalibration {

    static MotorEV3 leftMotor;
    static MotorEV3 rightMotor;
    static IRSensor irSensor;
    static DifferentialDrive motors;
    static EV3Key Enter;
    static EV3Key Left;
    static EV3Key Right;
    static KeyListener enterKey;
    static KeyListener leftKey;
    static KeyListener rightKey;
    static ColorSensorEV3 leftColor;
    static ColorSensorEV3 rightColor;
    static long timestamp;

    public static Logger LOGGER = LoggerFactory.getLogger(MotorCalibration.class);

    static int CALIBRATION_SPEED = 100;
    public static void main(final String[] args) throws InterruptedException {

        setUp();

        while(true) {

            System.out.println("Program Start");
            Enter.waitForPress();

            CALIBRATION_SPEED += 50;

            motors.move(-CALIBRATION_SPEED, 0);
            TimeUnit.MILLISECONDS.sleep(200);

            motors.move(CALIBRATION_SPEED, 0);
            TimeUnit.MILLISECONDS.sleep(200);

            motors.move(0, -CALIBRATION_SPEED);
            TimeUnit.MILLISECONDS.sleep(200);

            motors.move(0, CALIBRATION_SPEED);
            TimeUnit.MILLISECONDS.sleep(200);

            motors.stop();

            System.out.println(CALIBRATION_SPEED);
        }

    }

    static void setUp() {
        System.out.println("Creating Motor A & B");
        leftMotor = new MotorEV3(MotorPort.A);
        rightMotor = new MotorEV3(MotorPort.B);
        irSensor = new IRSensor(SensorPort.S3);
        motors = new DifferentialDrive(leftMotor, rightMotor);
        Enter = new EV3Key(28);

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motors.stop();
            }
        }));
    }

}
