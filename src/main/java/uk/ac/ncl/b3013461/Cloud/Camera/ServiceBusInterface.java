package uk.ac.ncl.b3013461.Cloud.Camera;
////Endpoint=sb://speedcamerasb.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=9sG8mh6JbBbbBbrwv4z//DPuu+Zj0VFvrevhQkmWwvI=

import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.*;
import com.microsoft.windowsazure.exception.ServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.xml.datatype.*;
public class ServiceBusInterface 
{
	private final ServiceBusContract service;
	private final String topicName;
	//The constructor will try and connect to a Topic, or create one if one does not exist
	public ServiceBusInterface(String tN, SubWithRules[] subscriptions)
	{
		topicName = tN;
		System.setErr(null); //lalalalala i can't hear you
		Configuration c = ServiceBusConfiguration.configureWithSASAuthentication(
				"SpeedCameraSB",
				"RootManageSharedAccessKey",
				"9sG8mh6JbBbbBbrwv4z//DPuu+Zj0VFvrevhQkmWwvI=",
				".servicebus.windows.net"
				);
		service = ServiceBusService.create(c);
		TopicInfo tI = new TopicInfo(topicName);
		tI.setMaxSizeInMegabytes((long)5120);
		try //Try to create the topic
		{
			try{service.getTopic(topicName);} //this will throw if it doesn't exist
			catch(Exception e)
			{
				System.out.println("Topic does not exist - creating new Topic");
				CreateTopicResult r = service.createTopic(tI);
				System.out.println("New Topic: \""+topicName+"\" created");
			}
			for(int i = 0;i<subscriptions.length;i++) //Try to create all of the subscriptions
			{
				try //move on to the next sub if it already exists
				{
					CreateSubscriptionResult r = service.createSubscription(topicName, subscriptions[i].getSub());
					System.out.println("New Subscription: \"" +subscriptions[i].getSub().getName() + "\" created");
				}
				catch(Exception e)
				{
					//Sub already exists
				}
				for(int j = 0; j<subscriptions[i].getRules().size();j++) //for each subscription, add all the rules
				{
					try
					{
						//CreateRuleResult rR = service.createRule(topicName, subscriptions[i].getSub().getName(), subscriptions[i].getRules().get(j));
						//System.out.println("New Rule for " + subscriptions[i].getSub().getName() + ": \""+subscriptions[i].getRules().get(j).getName()+"\"");
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
				}
			}
		}
		catch(ServiceException e)
		{
			System.out.println("f");
		}
		
	}
	
	public boolean sendSpeedCameraMessage(SpeedCameraRecording sCR)
	{
		try
		{
			//Serialize the object for sending
			ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
			ObjectOutputStream oOS = new ObjectOutputStream(bAOS);
			oOS.writeObject(sCR);
			BrokeredMessage m = new BrokeredMessage(new ByteArrayInputStream(bAOS.toByteArray()));
			
			service.sendTopicMessage(topicName, m);
			System.out.println("Message sending succeeded");
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public SpeedCameraRecording getMessage(String subscriptionName)
	{
		try
		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
			ReceiveSubscriptionMessageResult resultSubMsg = service.receiveSubscriptionMessage(topicName, subscriptionName);
			BrokeredMessage m = resultSubMsg.getValue();
			if(m==null)
			{
				System.out.println("No messages");
			}
			else
			{
				//SpeedCameraRecording sCR = (SpeedCameraRecording) m.getProperty("SpeedCameraRecording");
				ObjectInputStream in = new ObjectInputStream(m.getBody());
				SpeedCameraRecording sCR = (SpeedCameraRecording)in.readObject();
				System.out.println(sCR.getVehicleSpeed());
				/*
				byte[] b = new byte[200];
				String s = null;
				int numRead = m.getBody().read(b);
				while(-1 != numRead)
				{
					s = new String(b);
					s = s.trim();
					System.out.print(s);
					numRead = m.getBody().read(b);
				}
				*/
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return null;
	}
}
