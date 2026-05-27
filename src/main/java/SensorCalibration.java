import Components.*;
import ev3dev.sensors.EV3Key;
import lejos.hardware.port.SensorPort;
import lejos.robotics.Color;

import java.util.concurrent.TimeUnit;

import static java.lang.Integer.min;
import static java.lang.Integer.max;

public class SensorCalibration {
    public static void main(final String[] args) throws InterruptedException {

        EV3Key Enter = new EV3Key(28);

        System.out.println("Creating Sensors");
        ColorSensorEV3 leftColor = new ColorSensorEV3(SensorPort.S1, ColorMode.RED);
        ColorSensorEV3 rightColor = new ColorSensorEV3(SensorPort.S2, ColorMode.RED);
        leftColor.setLight(Color.RED);
        rightColor.setLight(Color.RED);

        while (true) {
            System.out.println("Left sensor on table [press button to scan]");
            Enter.waitForPress();
            int leftSensorTable = leftColor.getValue1();
            System.out.println(leftSensorTable);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            System.out.println("Right sensor on table [press button to scan]");
            Enter.waitForPress();
            int rightSensorTable = rightColor.getValue1();
            System.out.println(rightSensorTable);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);


            System.out.println("Left sensor on tracklimit [press button to scan]");
            Enter.waitForPress();
            int leftSensorTracklimit = leftColor.getValue1();
            System.out.println(leftSensorTracklimit);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            System.out.println("Right sensor on tracklimit [press button to scan]");
            Enter.waitForPress();
            int rightSensorTracklimit = rightColor.getValue1();
            System.out.println(rightSensorTracklimit);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            // ------------------------------------------------------------------
            //                        Under Bridge
            // ------------------------------------------------------------------
            System.out.println("Left sensor on table under bridge [press button to scan]");
            Enter.waitForPress();
            int leftSensorTableUB = leftColor.getValue1();
            System.out.println(leftSensorTableUB);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            System.out.println("Right sensor on table under bridge [press button to scan]");
            Enter.waitForPress();
            int rightSensorTableUB = rightColor.getValue1();
            System.out.println(rightSensorTableUB);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);


            System.out.println("Left sensor on tracklimit under bridge [press button to scan]");
            Enter.waitForPress();
            int leftSensorTracklimitUB = leftColor.getValue1();
            System.out.println(leftSensorTracklimitUB);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            System.out.println("Right sensor on tracklimit under bridge [press button to scan]");
            Enter.waitForPress();
            int rightSensorTracklimitUB = rightColor.getValue1();
            System.out.println(rightSensorTracklimitUB);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            // ------------------------------------------------------------------
            //                         On Bridge
            // ------------------------------------------------------------------
            System.out.println("Left sensor on bridge [press button to scan]");
            Enter.waitForPress();
            int leftSensorBridge = leftColor.getValue1();
            System.out.println(leftSensorBridge);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            System.out.println("Right sensor on bridge [press button to scan]");
            Enter.waitForPress();
            int rightSensorBridge = rightColor.getValue1();
            System.out.println(rightSensorBridge);
            System.out.println();
            TimeUnit.SECONDS.sleep(2);

            // ------------------------------------------------------------------
            //                Calculate Brightness Check Value
            // ------------------------------------------------------------------
            int min = min(leftSensorTable, leftSensorTableUB);
            min = min(min, leftSensorBridge);
            int max = max(leftSensorTracklimit, leftSensorTracklimitUB);
            int leftCalculatedValue = (min + max) / 2;
            System.out.println("min: " + min + "  max: " + max);

            min = min(rightSensorTable, rightSensorTableUB);
            min = min(min, rightSensorBridge);
            max = max(rightSensorTracklimit, rightSensorTracklimitUB);
            int rightCalculatedValue = (min + max) / 2;
            System.out.println("min: " + min + "  max: " + max);

            System.out.println("Left sensor check value: " + leftCalculatedValue);
            System.out.println("Right sensor check value: " + rightCalculatedValue);
            System.out.println("Press Button to redo");
            Enter.waitForPress();
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
