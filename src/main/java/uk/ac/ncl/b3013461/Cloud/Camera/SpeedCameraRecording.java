package uk.ac.ncl.b3013461.Cloud.Camera;

import java.util.Random;

import com.microsoft.azure.storage.table.TableServiceEntity;

import uk.ac.ncl.b3013461.Cloud.Consumer.SpeedCameraRecordingEntry;

public class SpeedCameraRecording extends TableServiceEntity implements java.io.Serializable
{
	private final String camID;
	private final Vehicle vehicle;
	private final int vehicleSpeed;
	private boolean priority = false; //defaults to false
	/**
	 * @param c the speed camera id
	 * @param v the vehicle recorded
	 * @param speed the speed the vehicle was going at
	 */
	public SpeedCameraRecording(String c, Vehicle v, int speed)
	{
		camID = c;
		vehicle = v;
		vehicleSpeed = speed;
	}
	public String getCamID() {
		return camID;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public int getVehicleSpeed() {
		return vehicleSpeed;
	}
	public void setPriority()
	{
		priority = true;
	}
	public boolean getPriority()
	{
		return priority;
	}
}
