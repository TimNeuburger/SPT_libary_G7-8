package Components;

import ev3dev.sensors.ev3.EV3ColorSensor;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

/**
 * <b>-------------------ColorSensor---------------</b><br>
 * Port has to be the hardware port "SensorPort.S1" with the right number
 * <b>setLight</b><br>
 * Use "import lejos.robotics.Color;" for selection.
 * <p>
 * Mode is one of three available modes:
 * <b>ColorMode.COLOR_ID</b><br>
 * The Value1 is an ID from (0-7) of the detected color. Can be compared with color from "import lejos.robotics.Color;"
 * <p>
 * <b>ColorMode.RED</b><br>
 * The Value1 is the intensity level (Normalized between 0 and 1) of reflected light.
 * <p>
 * <b>ColorMode.RGB</b><br>
 * The Value1 is the intensity level (Normalized between 0 and 1) of reflected RED light.
 * The Value2 is the intensity level (Normalized between 0 and 1) of reflected GREEN light.
 * The Value3 is the intensity level (Normalized between 0 and 1) of reflected BLUE light.
 * <p>
 * <b>ColorMode.AMBIENT</b><br>
 * The Value1 is the intensity level (Normalized between 0 and 1) of ambient light.
 */
public class ColorSensorEV3 {

    private EV3ColorSensor sensor;
    private SampleProvider values;

    public ColorSensorEV3(Port port, ColorMode mode) {
        System.out.println("create ColorSensor " + port);
        sensor = new EV3ColorSensor(port);

        switch (mode) {
            case COLOR_ID:
                values = sensor.getColorIDMode();
                break;
            case RED:
                values = sensor.getRedMode();
                break;
            case RGB:
                values = sensor.getRGBMode();
                break;
            case AMBIENT:
                values = sensor.getAmbientMode();
                break;
        }
    }

    public void setLight(int color) {
        sensor.setFloodlight(color);
    }

    public float[] getSample() {
        float[] sample = new float[values.sampleSize()];
        values.fetchSample(sample, 0);
        return sample;
    }

    public int getValue1() {
        float[] sample = new float[values.sampleSize()];
        values.fetchSample(sample, 0);
        return (int) sample[0];
    }

    public int getValue2() {
        float[] sample = new float[values.sampleSize()];
        values.fetchSample(sample, 0);
        return (int) sample[0];
    }

    public int getValue3() {
        float[] sample = new float[values.sampleSize()];
        values.fetchSample(sample, 0);
        return (int) sample[0];
    }
}