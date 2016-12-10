package uk.ac.ncl.b3013461.Cloud;

import java.util.Random;

public class SpeedCameraRecording 
{
	private final SpeedCamera cam;
	private final Vehicle vehicle;
	private final int vehicleSpeed;
	public SpeedCameraRecording(SpeedCamera c, Vehicle v, int speed)
	{
		cam = c;
		vehicle = v;
		vehicleSpeed = speed;
	}
	public SpeedCamera getCam() {
		return cam;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public int getVehicleSpeed() {
		return vehicleSpeed;
	}
}
