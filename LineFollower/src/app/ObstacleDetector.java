package app;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class ObstacleDetector extends Thread {

	private static EV3UltrasonicSensor us1 = new EV3UltrasonicSensor(SensorPort.S2);
	private DataExchange de;
	
	public ObstacleDetector (DataExchange de) {
		this.de = de;
	}
	
	
	@Override
	public void run() {

		final SampleProvider sp = us1.getDistanceMode();
		int distanceValue = 0;

		while (true) {
			if (de.getCommand() == 2) {
				break;
			}
			
			float[] sample = new float[sp.sampleSize()];
			sp.fetchSample(sample, 0);
			distanceValue = (int) (sample[0] * 100);
			if (de.getCommand() == 1) {	
			
			System.out.println("Distance: " + distanceValue);
			}
			Delay.msDelay(50);
			
			if (distanceValue <= 20) {
				de.setCommand(0);
				System.out.println("Obstactle detected!");
			}
			
			
		}

	}
}
