package app;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.utility.Delay;

public class ColorSensor extends Thread{
	private DataExchange de;
	
	
	public ColorSensor (DataExchange de) {
		this.de = de;
	}
	
	
	public void run() {
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4); // setting up the sensor on the port
		
		
		// Initialize sampleFetcher
		float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];
		
		
		// loop for getting the current values of color intensity
		while (de.getCommand() != Command.KILLSWITCH) {

			redMode.fetchSample(redSample, 0);
			de.setColorIntensity(redSample[0]);
			
			
			// small delay to avoid infinite loop and freezing of computer or crashing
			Delay.msDelay(50);
		}
		
		System.out.println("END OF COLOR SENSOR...");
		
		
		
	}

}
