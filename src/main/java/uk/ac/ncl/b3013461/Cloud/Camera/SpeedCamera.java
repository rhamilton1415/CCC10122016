package uk.ac.ncl.b3013461.Cloud.Camera;

import java.util.ArrayList;
import java.util.Random;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class SpeedCamera extends TableServiceEntity implements java.io.Serializable
{
	private final String cameraID;
	private final String streetName;
	private final String area;
	private final int speedLimit;
	private static ServiceBusInterface sBI = ServiceBusInterface.getVanillaSBI();
	private ArrayList<SpeedCameraRecording> recBackLog = new ArrayList<SpeedCameraRecording>();
	
	private SpeedCamera(String camID, String strName, String areaName, int spLimit)
	{
		cameraID = camID;
		this.partitionKey = cameraID;
		streetName = strName;
		area = areaName;
		speedLimit = spLimit;
		this.rowKey = streetName;
		//announce
	}
	public static SpeedCamera makeSpeedCamera(String config)
	{
		return makeSpeedCamera(config,false);
	}
	public static SpeedCamera makeSpeedCamera(String config, boolean announce)
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
		SpeedCamera s = new SpeedCamera(newID,newStreetName,newArea,Integer.parseInt(newSpeedLimit));
		if(announce)
		{
			s.announce();
		}
		return s;
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
		SpeedCameraRecording sCR = new SpeedCameraRecording(this.getCameraID(),Vehicle.generateVehicle(),vSpeed);
		if(sCR.getVehicleSpeed()>this.speedLimit)
		{
			sCR.setPriority();
		}
		recBackLog.add(sCR);
	}
	public void sendRecordingBacklog()
	{
		for(int i = 0;i<recBackLog.size();i++)
		{
			if(sBI.sendSpeedCameraMessage(recBackLog.get(i)))
			{
				recBackLog.remove(i); //if the message sends successfully, the speed camera no longer needs to store the recording locally
			}
			else
			{
				System.out.println("Connection failed");
				return; //connection failed
			}
		}
	}
	public void announce()
	{
		sBI.sendSpeedCameraAnnouncement(this);
	}
	@Override
	public String toString()
	{
		//build your very own speed camera!
		return this.getCameraID() + "-" + this.getStreetName() + "-" + this.getArea() + "-" + this.getSpeedLimit();
	}
}
