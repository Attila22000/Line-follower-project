package app;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import lejos.utility.Delay;

public class DataExchange extends Thread {

	private Command command = Command.LINE;
	private boolean obstacleDetected = false;
	private boolean isKilled = false;
	private int obstacleAmount = 1;
	private int obstaclesDetectedNum = 0;
	private double turningSpeedPercentage = 0.3;
	private float lowerColorTreshold = 0.12f;
	private float upperColorThreshold = 0.17f;

	private int maxMotorSpeed = 300;

	// Obstacle Detector data
	private int obstacleDistance;
	private int distanceThresh = 20; // distance from the obstacle to start obstacle avoidance algorithm

	// Color sensor data
	private float colorIntensity;

	// Timing variables
	long startTime = 0;
	long endTime = 0;

	private boolean isPreparedToDie = false;
	
	private LinkedList<Integer> obstacleTimestamps = new LinkedList<Integer>();

	private void getKilled() {
		URL url = null;
		HttpURLConnection conn = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		String s = null;

		try {
			url = new URL("http://192.168.84.5:8080/rest/lego/getkill");
			conn = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("URL Problem");
		}

		InputStream is = null;
		try {
			is = conn.getInputStream();
		} catch (Exception e) {
			System.out.println("Exception conn.getInputSteam()");
			e.printStackTrace();
			System.out.println("Cannot get InputStream!");
		}
		isr = new InputStreamReader(is);
		br = new BufferedReader(isr);

		String data;
		try {
			data = br.readLine();
			if (data == null) {
				System.out.println("Getting null");
			} else if (data.equals("1")) {
				if (!isPreparedToDie) {
				System.out.println("Prepare to die");
				}
				isPreparedToDie = true;
			} else {
//				System.out.println(data);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Cannot read line");
		}

	}

	private void getData() {
		URL url = null;
		HttpURLConnection conn = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		String s = null;
		try {
			url = new URL("http://192.168.84.5:8080/rest/lego/getvalues");
			conn = (HttpURLConnection) url.openConnection();

			InputStream is = null;
			try {
				is = conn.getInputStream();
			} catch (Exception e) {
				System.out.println("Exception conn.getInputSteam()");
				e.printStackTrace();
				System.out.println("Cannot get InputStream!");
			}
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String data = br.readLine();

			String dataVals[] = data.split("#");
			int maxSpeed = Integer.parseInt(dataVals[0]);
			double turningPercentage = Double.parseDouble(dataVals[1]);
			float lowerThresh = Float.parseFloat(dataVals[2]);
			float upperThresh = Float.parseFloat(dataVals[3]);

			if (maxMotorSpeed != maxSpeed || turningSpeedPercentage != turningPercentage
					|| lowerColorTreshold != lowerThresh || upperColorThreshold != upperThresh) {
				System.out.println("NEW DATA...");
				System.out.println("Speed: " + maxSpeed);
				System.out.println("Turning: " + turningPercentage);
				System.out.println("Lower: " + lowerThresh);
				System.out.println("Upper: " + upperThresh);
			}

			maxMotorSpeed = maxSpeed;
			turningSpeedPercentage = turningPercentage;
			lowerColorTreshold = lowerThresh;
			upperColorThreshold = upperThresh;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some problem!");
		}
	}
	
	
	private void sendTimestamp(int timestamp) {
		URL url = null;
		HttpURLConnection conn = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		String s = null;
		try {
			url = new URL("http://192.168.84.5:8080/rest/lego/settimestamp/" + timestamp);
			conn = (HttpURLConnection) url.openConnection();

			InputStream is = null;
			try {
				is = conn.getInputStream();
			} catch (Exception e) {
				System.out.println("Exception conn.getInputSteam()");
				e.printStackTrace();
				System.out.println("Cannot get InputStream!");
			}
			
			isr = new InputStreamReader(is);
			br = new BufferedReader(isr);

			String data = br.readLine();
			System.out.println(data);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Some problem!");
		}
	}

	public void run() {

		System.out.println("Data exchange start");
		while (getCommand() != Command.KILLSWITCH) {

			// adjusting the data from the data given from the server
			getData();
			getKilled();
			
			
			// command switching logic
			if (getCommand() == Command.LINE && isPreparedToDie) { // check if the robot is in line mode and killswitch was activated
				// turn on killswitch
				System.out.println("SETTING TO KILLSWITCH");
				setCommand(Command.KILLSWITCH);
				
			}
			if (getCommand() == Command.LINE && !isPreparedToDie) {
				// normal code when the robot is supposed to be alive
				
				if (obstacleDistance <= distanceThresh) {
					System.out.println("SETTING TO THE OBSTACLE DETECTION: DATAEXCHANGE");
					setCommand(Command.AVOID);
					obstacleTimestamps.add((int)System.currentTimeMillis());
					sendTimestamp(obstacleTimestamps.get(obstacleTimestamps.size()-1));
					
					
					if (obstacleTimestamps.size() >= 2) {
						long timeTaken = (long) obstacleTimestamps.get(obstacleTimestamps.size()-1) - obstacleTimestamps.get(obstacleTimestamps.size()-2);
						SimpleDateFormat obj = new SimpleDateFormat("mm:ss:SSS");
						Date res = new Date(timeTaken);
						
						System.out.println(obj.format(res));
					}
					
				}
			}

			// small delay to avoid infinite looping
			Delay.msDelay(10);
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

	public double getTurningSpeedPercentage() {
		return turningSpeedPercentage;
	}

	public void setTurningSpeedPercentage(double turningSpeedPercentage) {
		this.turningSpeedPercentage = turningSpeedPercentage;
	}

	public float getLowerColorTreshold() {
		return lowerColorTreshold;
	}

	public void setLowerColorTreshold(float lowerColorTreshold) {
		this.lowerColorTreshold = lowerColorTreshold;
	}

	public float getUpperColorThreshold() {
		return upperColorThreshold;
	}

	public void setUpperColorThreshold(float upperColorThreshold) {
		this.upperColorThreshold = upperColorThreshold;
	}

	public int getMaxMotorSpeed() {
		return maxMotorSpeed;
	}

	public void setMaxMotorSpeed(int maxMotorSpeed) {
		this.maxMotorSpeed = maxMotorSpeed;
	}

}
