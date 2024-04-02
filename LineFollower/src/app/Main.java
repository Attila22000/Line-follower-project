package app;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.*;
import lejos.hardware.port.*;
import lejos.utility.Delay;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting");
		DataExchange de = new DataExchange();
		
		Button.LEDPattern(4);     // flash green led and
        Sound.beepSequenceUp();   // make sound when ready.
        Button.waitForAnyPress();
		
		
		ObstacleDetector od = new ObstacleDetector(de);
		LineFollower line = new LineFollower(de);
		od.start();
		line.start();
		while (!de.isKilled()) {
		Button.waitForAnyPress();
		de.setCommand(2);
		}
		
	}

}
