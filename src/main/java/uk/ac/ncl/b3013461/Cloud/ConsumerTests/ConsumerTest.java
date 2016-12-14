package uk.ac.ncl.b3013461.Cloud.ConsumerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCamera;
import uk.ac.ncl.b3013461.Cloud.Consumer.SpeedCameraConsumer;

public class ConsumerTest {

	SpeedCameraConsumer sCC = new SpeedCameraConsumer();
	@Test
	public void createTableTest() 
	{
	}
	
	@Test
	public void addSpeedCameraEntry()
	{
		SpeedCamera sC = SpeedCamera.makeSpeedCamera("123-Dinsdale Road-Newcastle-30");
		assertTrue(sCC.addCamera(sC));
	}
	@Test
	public void getSpeedCameraEntry()
	{
		sCC.getCamera("123");
	}

}
