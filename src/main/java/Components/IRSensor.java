package Components;

import ev3dev.sensors.ev3.EV3IRSensor;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

/**
 * <b>-------------------IRSensor---------------</b><br>
 * Port has to be the hardware port "SensorPort.S1" with the right number.
 * Needs "import lejos.hardware.port.SensorPort;"
 */
public class IRSensor {

    private EV3IRSensor sensor;
    private SampleProvider distanceMode;

    public IRSensor(Port port) {
        System.out.println("create IRSensor " + port);
        sensor = new EV3IRSensor(port);
        distanceMode = sensor.getDistanceMode();
    }

    public int getDistance() {
        float[] sample = new float[distanceMode.sampleSize()];
        distanceMode.fetchSample(sample, 0);
        return (int) sample[0];
    }
}