package app;

import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.SensorMode;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class LineFollower extends Thread {
	private DataExchange de;

	private boolean isStopped = false;
	private final int maxMotorSpeed = 300;

	public boolean isStopped() {
		return isStopped;
	}

	public void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}

	public LineFollower(DataExchange de) {
		this.de = de;

	}

	@Override
	public void run() {
		// Initialize motors, sensors and screen
		System.out.println("Line follower");
		RegulatedMotor rightMotor = Motor.A;
		RegulatedMotor leftMotor = Motor.B;
		EV3ColorSensor colorSensor = new EV3ColorSensor(SensorPort.S4);
		EV3 ev3 = (EV3) BrickFinder.getLocal();
		TextLCD lcd = ev3.getTextLCD();

		// Initialize sampleFetcher
		float redSample[];
		SensorMode redMode = colorSensor.getRedMode();
		redSample = new float[redMode.sampleSize()];

		// Hard-coded values

		rightMotor.setSpeed(maxMotorSpeed);
		leftMotor.setSpeed(maxMotorSpeed);
		float lowerColorTreshold = 0.12f;
		float upperColorThreshold = 0.17f;

		// Start moving the robot
		rightMotor.forward();
		leftMotor.forward();

		double turningSpeedPercentage = 0.3;

		while (true) {
			if (de.getCommand() == 2) {
				break;
			}

			// Output sample data

			if (de.getCommand() == 1) {
				redMode.fetchSample(redSample, 0);
				System.out.println("Sample: " + redSample[0]);
				leftMotor.forward();
				rightMotor.forward();


				if (redSample[0] < lowerColorTreshold) {

					leftMotor.setSpeed(maxMotorSpeed);
					rightMotor.setSpeed((int) Math.floor(maxMotorSpeed * turningSpeedPercentage));

				} else if (redSample[0] > upperColorThreshold) {

					rightMotor.setSpeed(maxMotorSpeed);
					leftMotor.setSpeed((int) Math.floor(maxMotorSpeed * turningSpeedPercentage));
				} else {
					rightMotor.setSpeed(maxMotorSpeed);
					leftMotor.setSpeed(maxMotorSpeed);
				}
			} else if (de.getCommand() == 0) {

				// Move robot left for 1,8sec
				leftMotor.setSpeed(150);
				rightMotor.setSpeed(300);
				leftMotor.forward();
				rightMotor.forward();

				Delay.msDelay(1800);
				
				
				// move robot forward
				leftMotor.setSpeed(200);
				rightMotor.setSpeed(200);
				leftMotor.forward();
				rightMotor.forward();

				Delay.msDelay(100);
				

				// Move robot right for 2sec
				leftMotor.setSpeed(300);
				rightMotor.setSpeed(140);
				leftMotor.forward();
				rightMotor.forward();

				Delay.msDelay(3000);

					
				
				boolean isOnLine = false;
				leftMotor.setSpeed(200);
				rightMotor.setSpeed(200);
				leftMotor.forward();
				rightMotor.forward();
				while (!isOnLine) {
					redMode.fetchSample(redSample, 0);
					if (redSample[0] < lowerColorTreshold) {
						isOnLine = true;
					}
				}
				Delay.msDelay(100);
				while (isOnLine) {
					redMode.fetchSample(redSample, 0);
					if (redSample[0] > upperColorThreshold) {
						isOnLine = false;
					}
				}

				Delay.msDelay(100);

				leftMotor.setSpeed(150);
				rightMotor.setSpeed(300);
				leftMotor.forward();
				rightMotor.forward();
				
				Delay.msDelay(300);
				
				System.out.println("SWITCHING TO LINE FOLLOWER...");
				de.setCommand(1);
				

			} else {
				break;
			}

			// Allow for some time before self-correcting
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		System.out.println("END OF CODE...");
		rightMotor.stop();
		leftMotor.stop();
		colorSensor.close();
		de.setKilled(true);
//		leftMotor.stop();
//		rightMotor.stop();
//		colorSensor.close();
	}
}
