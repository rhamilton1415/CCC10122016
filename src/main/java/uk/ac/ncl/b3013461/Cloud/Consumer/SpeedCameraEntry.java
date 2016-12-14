package uk.ac.ncl.b3013461.Cloud.Consumer;

import com.microsoft.azure.storage.table.TableServiceEntity;

import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCamera;

public class SpeedCameraEntry extends TableServiceEntity
{
	private String cameraID;
	private String streetName;
	private String area;
	private int speedLimit;
	public SpeedCameraEntry(SpeedCamera s)
	{
		cameraID = s.getCameraID();
		this.partitionKey = cameraID;
		streetName = s.getStreetName();
		this.rowKey = streetName;
		area = s.getArea();
		speedLimit = s.getSpeedLimit();
	}
	public SpeedCameraEntry(){}
	public String getCameraID() {
		return cameraID;
	}
	public void setCameraID(String cameraID) {
		this.cameraID = cameraID;
	}
	public String getStreetName() {
		return streetName;
	}
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getSpeedLimit() {
		return speedLimit;
	}
	public void setSpeedLimit(int speedLimit) {
		this.speedLimit = speedLimit;
	}
	public String toSpeedCameraConfig()
	{
		return cameraID + "-" + streetName + "-" + area + "-" + speedLimit;
	}
	
}
