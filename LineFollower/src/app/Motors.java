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

public class Motors extends Thread {
	private DataExchange de;
	private double turningSpeedPercentage = 0.3;
	private float lowerColorTreshold = 0.12f;
	private float upperColorThreshold = 0.17f;

	private int maxMotorSpeed = 300;



	public Motors(DataExchange de) {
		this.de = de;

	}

	@Override
	public void run() {
		// Initialize motors, sensors and screen
		RegulatedMotor rightMotor = Motor.A;
		RegulatedMotor leftMotor = Motor.B;


		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// setting the initial speed
		rightMotor.setSpeed(maxMotorSpeed);
		leftMotor.setSpeed(maxMotorSpeed);


		// Start moving the robot
		rightMotor.forward();
		leftMotor.forward();

		

		while (true) {
			
			
			
			
			
			if (de.getCommand() == Command.KILLSWITCH) {
				break;
			}

			// Output sample data

			if (de.getCommand() == Command.LINE) {
				turningSpeedPercentage = de.getTurningSpeedPercentage();
				lowerColorTreshold = de.getLowerColorTreshold();
				upperColorThreshold = de.getUpperColorThreshold();
				maxMotorSpeed = de.getMaxMotorSpeed();
				
				
				
				leftMotor.forward();
				rightMotor.forward();


				if (de.getColorIntensity() < lowerColorTreshold) {

					leftMotor.setSpeed(maxMotorSpeed);
					rightMotor.setSpeed((int) Math.floor(maxMotorSpeed * turningSpeedPercentage));

				} else if (de.getColorIntensity() > upperColorThreshold) {

					rightMotor.setSpeed(maxMotorSpeed);
					leftMotor.setSpeed((int) Math.floor(maxMotorSpeed * turningSpeedPercentage));
				} else {
					rightMotor.setSpeed(maxMotorSpeed);
					leftMotor.setSpeed(maxMotorSpeed);
				}
			} else if (de.getCommand() == Command.AVOID) {
				System.out.println("START OF OBSTACLE AVOIDANCE ALGORITHM");
//				de.setObstacleDetected(true);
				
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

				Delay.msDelay(1000);
				

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
				
				long startTime = System.currentTimeMillis();
				long timeout = 10000;
				long currTime = System.currentTimeMillis();
				while (!isOnLine) {
					if (de.getColorIntensity() < lowerColorTreshold || currTime - startTime > timeout) {
						isOnLine = true;
					}
					currTime = System.currentTimeMillis();
				}
				
				System.out.println("Seen black");
				
				Delay.msDelay(100);
				while (isOnLine) {
					if (de.getColorIntensity() > upperColorThreshold || currTime - startTime > timeout) {
						isOnLine = false;
						
					}
					currTime = System.currentTimeMillis();
				}
				System.out.println("Seen white");

				Delay.msDelay(100);

				leftMotor.setSpeed(150);
				rightMotor.setSpeed(300);
				leftMotor.forward();
				rightMotor.forward();
				
				Delay.msDelay(300);
				
				System.out.println("END OF OBSTACLE AVOIDANCE...");
//				de.setObstacleDetected(false);
				de.setCommand(Command.LINE);
				

			} else {
				break;
			}

			// Allow for some time before self-correcting
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		System.out.println("END OF LINE FOLLOWER...");
		rightMotor.stop();
		leftMotor.stop();
		
	}
}
