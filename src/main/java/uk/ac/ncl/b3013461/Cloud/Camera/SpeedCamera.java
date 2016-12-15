package uk.ac.ncl.b3013461.Cloud.Camera;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

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
		return makeSpeedCamera(config,false,0);
	}
	/**
	 * @param config defines the parameters of the speed camera
	 * @param announce should this camera be announced
	 * @param tickrate how often does this camera get recordings
	 * @return the new camera
	 */
	/**
	 * @param config
	 * @param announce
	 * @param tickrate
	 * @return
	 */
	public static SpeedCamera makeSpeedCamera(String config, boolean announce, long tickrate)
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
		SpeedCamera s = new SpeedCamera(newID,newStreetName,newArea,Integer.parseInt(newSpeedLimit));
		if(announce)
		{
			s.announce();
		}
		if(tickrate!=0)
		{
			s.tick(tickrate);
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
	
	/**
	 * Generates a random Vehicle, adds it to the backlog and notifys the sendbacklog function to try and send the backlog
	 */
	public void recordVehicle()
	{
		Random r = new Random();
		int vSpeed = (speedLimit/2)+r.nextInt(speedLimit);
		SpeedCameraRecording sCR = new SpeedCameraRecording(this.getCameraID(),Vehicle.generateVehicle(),vSpeed);
		if(sCR.getVehicleSpeed()>(this.speedLimit))
		{
			sCR.setPriority();
		}
		recBackLog.add(sCR);
		if(sCR.getPriority())
		{
			System.out.print("SPEEDING DETECTED ");
		}
		System.out.println(this.getCameraID() +" Recorded: " + sCR.getVehicle().getCarReg() + " at " + sCR.getVehicleSpeed() + "mph (Speed Limit: "+this.getSpeedLimit()+")");
		sendRecordingBacklog();
	}
	
	/**
	 * This function will try and send all recordings in it's backlog. If the connection fails, it will try again later.
	 */
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
	private void tick(long timeout)
	{
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run()
			{
				recordVehicle();
			}
		},timeout,timeout);
	}
	public void announce()
	{
		sBI.sendSpeedCameraAnnouncement(this);
		System.out.println("Camera " + this.getCameraID() + " on " + this.getStreetName() + ", " + this.getArea() + " deployed");
	}
	@Override
	public String toString()
	{
		//build your very own speed camera!
		return this.getCameraID() + "-" + this.getStreetName() + "-" + this.getArea() + "-" + this.getSpeedLimit();
	}
}
