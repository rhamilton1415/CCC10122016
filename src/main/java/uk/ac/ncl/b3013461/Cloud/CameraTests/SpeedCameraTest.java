package uk.ac.ncl.b3013461.Cloud.CameraTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCamera;

public class SpeedCameraTest {

	static SpeedCamera sC;
	@BeforeClass
	public static void init()
	{
		sC = SpeedCamera.makeSpeedCamera("123-Dinsdale Road-Newcastle-30");
	}
	@Test
	public void SpeedCameraInitialisetest() 
	{
		/*
		for(int i = 0; i<50;i++)
		{
			sC.recordVehicle();
		}
		*/
		assertTrue(true);
	}
	@Test
	public void SpeedCameraSendMessageTest()
	{
		sC.recordVehicle();
		sC.sendRecordingBacklog();
		assertTrue(true);
	}

}
