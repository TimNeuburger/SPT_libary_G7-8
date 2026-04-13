package Components;

/**
 * <b>-------------------DifferentialDrive---------------</b><br>
 * needs 2 MotorEV3 to be created
 */
public class DifferentialDrive {

    private MotorEV3 leftMotor;
    private MotorEV3 rightMotor;

    public DifferentialDrive(MotorEV3 leftMotor, MotorEV3 rightMotor) {
        System.out.println("Connect Motor A & B");
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
    }

    public void setSpeed(int speed) {
        leftMotor.setSpeed(speed);
        rightMotor.setSpeed(speed);
    }

    public void move(int speed_left, int speed_right) {
        leftMotor.move(speed_left);
        rightMotor.move(speed_right);
    }

    public void stop() {
        leftMotor.stop();
        rightMotor.stop();
    }
}