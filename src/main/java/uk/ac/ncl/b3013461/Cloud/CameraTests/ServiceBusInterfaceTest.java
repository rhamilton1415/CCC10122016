package uk.ac.ncl.b3013461.Cloud.CameraTests;

import static org.junit.Assert.*;

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
		SubWithRules prioritySub = new SubWithRules("priority");
		prioritySub.addRule("ruleone","priority = true");
		SubWithRules nonPrioritySub = new SubWithRules("nonpriority");
		SubWithRules vehicleCheckSub = new SubWithRules("vehiclecheck");
		SubWithRules s[] = {prioritySub,nonPrioritySub,vehicleCheckSub};
		sWR = s;
		sBI = new ServiceBusInterface(topicName, sWR);
	}
	
	@Test //run the speedcamera test to add some messages
	public void GetMessageTest()
	{
		sBI.getMessage("priority");
	}

}
