package app;

import java.io.File;
import lejos.hardware.*;
import lejos.hardware.Sound;
import lejos.utility.Delay;

public class Sounds extends Thread{
	private DataExchange de;
	public Sounds(DataExchange de) {
		this.de = de;
	}
	
	public void run () {
		while (de.getCommand() != Command.KILLSWITCH) {
			
			if (de.isObstacleDetected()) {
				Sound.playSample(new File("epix_sax.wav"));
			} 
			
		Delay.msDelay(50);
			
		}
		System.out.println("End of sounds thread...");
	}
}
