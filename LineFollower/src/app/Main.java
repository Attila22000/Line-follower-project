package app;

import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("PROGRAM STARTED...");
		DataExchange de = new DataExchange();

		Button.LEDPattern(4); // flash green led and
		Sound.beepSequenceUp(); // make sound when ready.
		Button.waitForAnyPress();

		ColorSensor cs = new ColorSensor(de);
		ObstacleDetector od = new ObstacleDetector(de);
		Motors motors = new Motors(de);
		Sounds song = new Sounds(de);

		// starting all the threads
		cs.start();
		od.start();
//		song.start();
		de.start();
		motors.start();

				

	}

}
