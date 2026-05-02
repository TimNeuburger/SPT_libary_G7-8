import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class AlphaTestTurning {

    static MotorEV3 leftMotor;
    static MotorEV3 rightMotor;
    static IRSensor irSensor;
    static DifferentialDrive motors;
    static EV3Key Enter;
    static ColorSensorEV3 leftColor;
    static ColorSensorEV3 rightColor;
    static long timestamp;
    static final long BIG_TURN_TIME = 500;      //in ms TODO

    public static Logger LOGGER = LoggerFactory.getLogger(AlphaTestTurning.class);

    static final int MAX_SPEED = 400;
    static int CALIBRATION_FACTOR = 1;
    static final int GROUND_LEVEL = 20;
    static final int LEFT_SENSOR_VALUE = 368;
    static final int RIGHT_SENSOR_VALUE = 8;
    static final int TURNING_TIME = 150;
    static final int SMALL_TURNING_TIME = 110;          //TODO test value
    static final double DISTANCE_PER_ROTATION_DEGREE = 1.308;
    public static void main(final String[] args) throws InterruptedException {

        setUp();

        irSensor.getDistance();
        leftColor.getValue1();
        rightColor.getValue1();
        System.out.println("Start program");
        timestamp = System.currentTimeMillis();
        while(true) {

            Enter.waitForPress();

            motors.move(MAX_SPEED * CALIBRATION_FACTOR, MAX_SPEED + 5);
            while (irSensor.getDistance() < GROUND_LEVEL) {
                if(leftColor.getValue1() < LEFT_SENSOR_VALUE) {
                    motors.move(MAX_SPEED, -MAX_SPEED/2);
                    if (System.currentTimeMillis() - timestamp < BIG_TURN_TIME){
                        TimeUnit.MILLISECONDS.sleep(TURNING_TIME);
                    } else {
                        TimeUnit.MILLISECONDS.sleep(SMALL_TURNING_TIME);
                    }
                    timestamp = System.currentTimeMillis();
                    motors.move(MAX_SPEED * CALIBRATION_FACTOR,MAX_SPEED);
                }
                if(rightColor.getValue1() < RIGHT_SENSOR_VALUE) {

                    motors.move(-MAX_SPEED/2, MAX_SPEED);
                    if (System.currentTimeMillis() - timestamp < BIG_TURN_TIME){
                        TimeUnit.MILLISECONDS.sleep(TURNING_TIME);
                    } else {
                        TimeUnit.MILLISECONDS.sleep(SMALL_TURNING_TIME);
                    }
                    timestamp = System.currentTimeMillis();
                    motors.move(MAX_SPEED * CALIBRATION_FACTOR,MAX_SPEED);
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
