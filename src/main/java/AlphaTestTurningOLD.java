import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;

import java.util.concurrent.TimeUnit;

public class AlphaTestTurningOLD {

    public static void main(final String[] args) throws InterruptedException {

        System.out.println("Creating Motor A & B");
        MotorEV3 leftMotor = new MotorEV3(MotorPort.A);
        MotorEV3 rightMotor = new MotorEV3(MotorPort.B);
        IRSensor irSensor = new IRSensor(SensorPort.S3);
        DifferentialDrive motors = new DifferentialDrive(leftMotor, rightMotor);
        EV3Key Enter = new EV3Key(28);

        System.out.println("Creating Sensors");
        ColorSensorEV3 leftColor = new ColorSensorEV3(SensorPort.S1, ColorMode.RED);
        ColorSensorEV3 rightColor = new ColorSensorEV3(SensorPort.S2, ColorMode.RED);

        //To Stop the motor in case of pkill java for example
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motors.stop();
            }
        }));

        irSensor.getDistance();
        leftColor.getValue1();
        rightColor.getValue1();
        System.out.println("Start program");
        while(true) {

            Enter.waitForPress();
            leftColor.setLight(Color.RED);
            rightColor.setLight(Color.RED);
            motors.move(415,400);
            while (irSensor.getDistance() < 20) {
                if(leftColor.getValue1() < 368) {
                    motors.move(300, -200);
                    TimeUnit.MILLISECONDS.sleep(150);
                    motors.move(300,300);
                }
                if(rightColor.getValue1() < 8) {
                    motors.move(-200, 300);
                    TimeUnit.MILLISECONDS.sleep(150);
                    motors.move(300,300);
                }
            }
            motors.stop();
        }
    }
}
