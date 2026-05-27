import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class AlphaTestTurningV2 {

    static MotorEV3 leftMotor;
    static MotorEV3 rightMotor;
    static IRSensor irSensor;
    static DifferentialDrive motors;
    static EV3Key Enter;
    static ColorSensorEV3 leftColor;
    static ColorSensorEV3 rightColor;
    static long timestamp;

    public static Logger LOGGER = LoggerFactory.getLogger(AlphaTestTurningV2.class);

    static final int MAX_SPEED_STRAIGHT = 500;
    static final int MAX_SPEED_TURNNING = 300;
    static int CALIBRATION_FACTOR = 100;
    static final int GROUND_LEVEL = 20;
    static final int LEFT_SENSOR_VALUE = 368;
    static final int RIGHT_SENSOR_VALUE = 8;
    static final int TURNING_TIME = 300;
    static final long BIG_TURN_TIME = 1000;      //in ms TODO
    static final int SMALL_TURNING_TIME = 200;          //TODO test value
    public static void main(final String[] args) throws InterruptedException {

        setUp();

        irSensor.getDistance();
        leftColor.getValue1();
        rightColor.getValue1();
        System.out.println("Start program");
        timestamp = System.currentTimeMillis();
        while(true) {

            Enter.waitForPress();

            motors.move(MAX_SPEED_STRAIGHT * CALIBRATION_FACTOR/100, MAX_SPEED_STRAIGHT);
            while (irSensor.getDistance() < GROUND_LEVEL) {
                if(leftColor.getValue1() < LEFT_SENSOR_VALUE) {
                    motors.stop();
                    TimeUnit.MILLISECONDS.sleep(200);
                    while(leftColor.getValue1() > LEFT_SENSOR_VALUE) {motors.move(MAX_SPEED_TURNNING/2, -MAX_SPEED_TURNNING);}
                    while(leftColor.getValue1() < LEFT_SENSOR_VALUE) {motors.move(MAX_SPEED_TURNNING/2, -MAX_SPEED_TURNNING);}
//                    if (System.currentTimeMillis() - timestamp < BIG_TURN_TIME){
//                        TimeUnit.MILLISECONDS.sleep(TURNING_TIME);
//                    } else {
//                        TimeUnit.MILLISECONDS.sleep(SMALL_TURNING_TIME);
//                    }
//                    timestamp = System.currentTimeMillis();
                    motors.move(MAX_SPEED_STRAIGHT * CALIBRATION_FACTOR/100,MAX_SPEED_STRAIGHT);
                }
                if(rightColor.getValue1() < RIGHT_SENSOR_VALUE) {
                    motors.stop();
                    TimeUnit.MILLISECONDS.sleep(200);
                    while(rightColor.getValue1() > RIGHT_SENSOR_VALUE) {motors.move(-MAX_SPEED_TURNNING, MAX_SPEED_TURNNING/2);}
                    while(rightColor.getValue1() < RIGHT_SENSOR_VALUE) {motors.move(-MAX_SPEED_TURNNING, MAX_SPEED_TURNNING/2);}
//                    if (System.currentTimeMillis() - timestamp < BIG_TURN_TIME){
//                        TimeUnit.MILLISECONDS.sleep(TURNING_TIME);
//                    } else {
//                        TimeUnit.MILLISECONDS.sleep(SMALL_TURNING_TIME);
//                    }
//                    timestamp = System.currentTimeMillis();
                    motors.move(MAX_SPEED_STRAIGHT * CALIBRATION_FACTOR/100,MAX_SPEED_STRAIGHT);
                }
            }
            motors.stop();
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
