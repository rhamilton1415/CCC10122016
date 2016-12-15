package uk.ac.ncl.b3013461.Cloud.Consumer;

import com.microsoft.azure.storage.table.TableServiceEntity;

import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCameraRecording;
import uk.ac.ncl.b3013461.Cloud.Camera.Vehicle;

public class SpeedCameraRecordingEntry extends TableServiceEntity
{
	public String getCamID() {
		return camID;
	}
	public void setCamID(String camID) {
		this.camID = camID;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	public int getVehicleSpeed() {
		return vehicleSpeed;
	}
	public void setVehicleSpeed(int vehicleSpeed) {
		this.vehicleSpeed = vehicleSpeed;
	}
	public boolean isPriority() {
		return priority;
	}
	public void setPriority(boolean priority) {
		this.priority = priority;
	}
	private String camID;
	private String vehicle;
	private int vehicleSpeed;
	private boolean priority;
	public SpeedCameraRecordingEntry(){};
	public SpeedCameraRecordingEntry(String c, Vehicle v, int speed)
	{
		camID = c;
		vehicle = v.getCarReg();
		this.partitionKey = vehicle;
		this.rowKey = camID;
		vehicleSpeed = speed;
	}
	public SpeedCameraRecordingEntry(SpeedCameraRecording sCR)
	{
		camID = sCR.getCamID();
		vehicle = sCR.getVehicle().getCarReg();
		vehicleSpeed = sCR.getVehicleSpeed();
		priority = sCR.getPriority();
	}
}
