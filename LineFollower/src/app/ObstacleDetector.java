package app;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ObstacleDetector extends Thread {

	private static EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2); // setting up the sensor on the ports
	private DataExchange de;
	
	public ObstacleDetector (DataExchange de) {
		this.de = de;
	}
	
	
	@Override
	public void run() {

		final SampleProvider sp = us1.getDistanceMode(); // adjusting the ultrasonic sensor to the distance mode
		float[] sample = new float[sp.sampleSize()];
		int distanceValue = 0;
		

		
		// loop to get current distance every 50 ms
		while (de.getCommand() != Command.KILLSWITCH) {
			
			sp.fetchSample(sample, 0);
			distanceValue = (int) (sample[0] * 100);
			
			de.setObstacleDistance(distanceValue); // sending the data to data exchange
			
			
			// Small delay to avoid infinite loop hell
			Delay.msDelay(50);
			
		}
		System.out.println("END OF OBSTACLE DETECTOR...");
	}
}
