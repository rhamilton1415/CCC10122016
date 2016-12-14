package uk.ac.ncl.b3013461.Cloud.CameraTests;

import org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ncl.b3013461.Cloud.Camera.ServiceBusInterface;
import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCamera;
import uk.ac.ncl.b3013461.Cloud.Camera.SubWithRules;

public class ServiceBusInterfaceTest 
{
	private static String topicName = "speedcameratopic";
	private static SubWithRules[] sWR;
	private static ServiceBusInterface sBI;
	
	@BeforeClass
	public static void initialseTopicTest()
	{
		sBI = ServiceBusInterface.getVanillaSBI();
	}
	
	@Test //run the speedcamera test to add some messages
	public void GetMessageTest()
	{
		SpeedCamera sC = SpeedCamera.makeSpeedCamera("123-Dinsdale Road-Newcastle-30"); //This will send an announcement
		for(int i = 0;i<50;i++)
		{
			sC.recordVehicle();
		}
		sC.sendRecordingBacklog();
		System.out.println("Getting priority recording messages");
		sBI.getSpeedCameraRecordingMessage("priority");
		System.out.println("Getting Speed Camera Announcement messages");
		sBI.getSpeedCameraAnnouncement("announcements");
	}
}
