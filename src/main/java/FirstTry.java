import Components.ColorSensorEV3;
import Components.ColorMode;
import Components.DifferentialDrive;
import Components.MotorEV3;
import Components.IRSensor;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;

public class FirstTry {

    public static void main(final String[] args){

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

        System.out.println("Start program");
        while(true) {

            Enter.waitForPress();
            leftColor.setLight(Color.BLUE);
            rightColor.setLight(Color.BLUE);
            while (true) {
                System.out.println(leftColor.getValue1());
                System.out.println(rightColor.getValue1());
            }
        }
    }
}
