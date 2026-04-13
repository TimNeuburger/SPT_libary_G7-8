package Components;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;

/**
 * <b>-------------------MotorEV3---------------</b><br>
 * Port has to be the hardware port "MotorPort.A" with the letter.
 * Needs "import lejos.hardware.port.MotorPort;"
 */
public class MotorEV3 {

    private EV3LargeRegulatedMotor motor;

    public MotorEV3(Port port) {
        System.out.println("create Motor " + port);
        motor = new EV3LargeRegulatedMotor(port);
        motor.brake(); // default stop mode
    }

    public void setSpeed(int speed) {
        motor.setSpeed(speed);
    }

    public void move(int speed) {
        motor.setSpeed(speed);
        motor.forward();
    }

    public void stop() {
        motor.stop();
    }
}