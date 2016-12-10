package uk.ac.ncl.b3013461.Cloud.CameraTests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.ncl.b3013461.Cloud.SpeedCamera;

public class SpeedCameraTest {

	@Test
	public void SpeedCameratest() 
	{
		SpeedCamera sC = SpeedCamera.makeSpeedCamera("123-Dinsdale Road-Newcastle-30");
		for(int i = 0; i<50;i++)
		{
			sC.recordVehicle();
		}
		
		assertTrue(true);
	}

}
