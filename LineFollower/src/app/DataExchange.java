package app;

public class DataExchange extends Thread{

	private int command = 1;
	private boolean obstacleDetected = false;
	private boolean isKilled = false;
	
	// command 0 - avoiding obstacle
	// command 1 - line following
	// command 2 - kill switch
	
	
	
	public boolean isKilled() {
		return isKilled;
	}
	public void setKilled(boolean isKilled) {
		this.isKilled = isKilled;
	}
	public int getCommand() {
		return command;
	}
	public void setCommand(int command) {
		this.command = command;
	}
	public boolean isObstacleDetected() {
		return obstacleDetected;
	}
	public void setObstacleDetected(boolean obstacleDetected) {
		this.obstacleDetected = obstacleDetected;
	}
	
	
	
}
