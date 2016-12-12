package uk.ac.ncl.b3013461.Cloud;

import java.util.ArrayList;
import java.util.Random;

public class SpeedCamera 
{
	private final String cameraID;
	private final String streetName;
	private final String area;
	private final int speedLimit;
	private ArrayList<SpeedCameraRecording> recBackLog = new ArrayList<SpeedCameraRecording>();
	
	private SpeedCamera(String camID, String strName, String areaName, int spLimit)
	{
		cameraID = camID;
		streetName = strName;
		area = areaName;
		speedLimit = spLimit;
		//announce
	}
	public static SpeedCamera makeSpeedCamera(String config)
	{
		//config should be in the format: cameraID-streetName-Area-speedLimit
		int stringIndex = 0;
		String newID = "";
		String newStreetName = "";
		String newArea = "";
		String newSpeedLimit = "";
		while(config.charAt(stringIndex)!='-')
		{
			newID = newID + config.charAt(stringIndex);
			stringIndex++;
		}
		stringIndex++;
		while(config.charAt(stringIndex)!='-')
		{
			newStreetName = newStreetName + config.charAt(stringIndex);
			stringIndex++;
		}
		stringIndex++;
		while(config.charAt(stringIndex)!='-')
		{
			newArea = newArea + config.charAt(stringIndex);
			stringIndex++;
		}
		stringIndex++;
		while(stringIndex<config.length())
		{
			newSpeedLimit = newSpeedLimit + config.charAt(stringIndex);
			stringIndex++;
		}
		//Check to see if this ID is already in use
		return new SpeedCamera(newID,newStreetName,newArea,Integer.parseInt(newSpeedLimit));
	}
	public ArrayList<SpeedCameraRecording> getRecBackLog() {
		return recBackLog;
	}
	public String getCameraID() {
		return cameraID;
	}
	public String getStreetName() {
		return streetName;
	}
	public String getArea() {
		return area;
	}
	public int getSpeedLimit() {
		return speedLimit;
	}
	public void recordVehicle()
	{
		Random r = new Random();
		int vSpeed = (speedLimit/2)+r.nextInt(speedLimit);
		SpeedCameraRecording sCR = new SpeedCameraRecording(this,Vehicle.generateVehicle(),vSpeed);
		if(sCR.getVehicleSpeed()>this.speedLimit)
		{
			sCR.setPriority();
		}
		recBackLog.add(sCR);
	}
}
