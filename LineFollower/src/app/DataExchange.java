package app;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import lejos.utility.Delay;




public class DataExchange extends Thread{

	private Command command = Command.LINE;
	private boolean obstacleDetected = false;
	private boolean isKilled = false;
	private int obstacleAmount = 1;
	private int obstaclesDetectedNum = 0;
	
	
	
	// Obstacle Detector data
	private int obstacleDistance;
	private int distanceThresh = 20; // distance from the obstacle to start obstacle avoidance algorithm
	
	// Color sensor data
	private float colorIntensity;
			
	// Timing variables
	long startTime = 0;
	long endTime = 0;

	
	
	public void run() {
		
		System.out.println("Data exchange start");
		while (getCommand() != Command.KILLSWITCH) {
			
			
			// line follower switching logic
			 if (!obstacleDetected && getCommand() != Command.LINE) { // switching to line follower
				 setCommand(Command.LINE);
				 System.out.println("Switching to line follower");
				 
			 }
			
			// obstacle avoidance logic
			if (obstacleDistance <= distanceThresh) { // checks whether there is obstacle in the way
				setCommand(Command.AVOID); // sets to obstacle avoidance command
				if (obstaclesDetectedNum < 1) {
					startTime = System.currentTimeMillis();
				}
				
				
				if (!obstacleDetected) { // checks to avoid continuous printing
					System.out.println("Obstactle detected!");
					
					
					
					if (obstaclesDetectedNum >= obstacleAmount) {
						obstaclesDetectedNum = 0;
						endTime = System.currentTimeMillis();
						long timeTaken = endTime - startTime;
						
						SimpleDateFormat obj = new SimpleDateFormat("mm:ss:SSS");     
		                Date res = new Date(timeTaken);   
						
						System.out.println(obj.format(res));
					}
					obstaclesDetectedNum++;
				}
				obstacleDetected = true;
				
				
			}
			
			
			// small delay to avoid infinite looping
			Delay.msDelay(50);
		}
		
		System.out.println("END OF DATA EXCHANGE");
		
	}
	
	
	
	
	public int getObstacleDistance() {
		return obstacleDistance;
	}
	public void setObstacleDistance(int obstacleDistance) {
		this.obstacleDistance = obstacleDistance;
	}
	public float getColorIntensity() {
		return colorIntensity;
	}
	public void setColorIntensity(float colorIntensity) {
		this.colorIntensity = colorIntensity;
	}
	public boolean isKilled() {
		return isKilled;
	}
	public void setKilled(boolean isKilled) {
		this.isKilled = isKilled;
	}
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public boolean isObstacleDetected() {
		return obstacleDetected;
	}
	public void setObstacleDetected(boolean obstacleDetected) {
		this.obstacleDetected = obstacleDetected;
	}
	
	
	
}
