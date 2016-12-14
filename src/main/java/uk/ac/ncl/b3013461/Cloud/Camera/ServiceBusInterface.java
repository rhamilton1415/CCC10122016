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
public class ServiceBusInterface implements java.io.Serializable
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
						CreateRuleResult rR = service.createRule(topicName, subscriptions[i].getSub().getName(), subscriptions[i].getRules().get(j));
						service.deleteRule(topicName,subscriptions[i].getSub().getName(),"$Default"); //default rule needs to be deleted if custom rules are specified
						System.out.println("New Rule for " + subscriptions[i].getSub().getName() + ": \""+subscriptions[i].getRules().get(j).getName()+"\"");
					}
					catch(Exception e)
					{
						//rule already exists
					}
				}
			}
		}
		catch(ServiceException e)
		{
			System.out.println(e.getErrorMessage());
		}
		
	}
	
	//senders
	public boolean sendSpeedCameraMessage(SpeedCameraRecording sCR)
	{
		try
		{
			//Serialize the object for sending
			ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
			ObjectOutputStream oOS = new ObjectOutputStream(bAOS);
			oOS.writeObject(sCR);
			BrokeredMessage m = new BrokeredMessage(new ByteArrayInputStream(bAOS.toByteArray()));
			m.setProperty("Announcement", false); //Apparently default isn't false
			if(sCR.getPriority())
			{
				m.setProperty("HighPriority", true);
			}
			else
			{
				m.setProperty("HighPriority", false);
			}
			m.setLabel("Speed Camera Recording");
			service.sendTopicMessage(topicName, m);
		}
		catch(Exception e)
		{
			return false;
		}
		return true;
	}
	
	public boolean sendSpeedCameraAnnouncement(SpeedCamera sC)
	{
		try
		{
			//Serialize the object for sending
			ByteArrayOutputStream bAOS = new ByteArrayOutputStream();
			ObjectOutputStream oOS = new ObjectOutputStream(bAOS);
			oOS.writeObject(sC.toString()); //sends a string from which an identical speed camera can be built
			BrokeredMessage m = new BrokeredMessage(new ByteArrayInputStream(bAOS.toByteArray()));
			m.setLabel("Speed Camera Announcement");
			m.setProperty("Announcement", true);
			service.sendTopicMessage(topicName, m);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	//receivers
	/**
	 * This will check the given subscription for messages with the label "Speed Camera Recording" and will return SpeedCameraRecording objects from valid messages
	 * @param subscriptionName the name of the subscription to get messages from
	 * @return SpeedCameraRecording the valid SpeedCameraRecording object or null if none exist
	 */
	public SpeedCameraRecording getSpeedCameraRecordingMessage(String subscriptionName)
	{
		try
		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
			ReceiveSubscriptionMessageResult resultSubMsg = service.receiveSubscriptionMessage(topicName, subscriptionName);
			BrokeredMessage m = resultSubMsg.getValue();
			if(m==null) //If there are no messages in this sub
			{
				System.out.println("No messages");
			}
			else if(m.getLabel().equals("Speed Camera Recording")) //If there is a message but it isn't a recording
			{
				ObjectInputStream in = new ObjectInputStream(m.getBody());
				SpeedCameraRecording sCR = (SpeedCameraRecording)in.readObject();
				service.deleteMessage(m);
				return sCR;
			}
		}
		catch(Exception e)
		{
			System.out.println("No messages or Exception");
		}
		return null;
	}
	public SpeedCamera getSpeedCameraAnnouncement(String subscriptionName)
	{
		try
		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
			ReceiveSubscriptionMessageResult resultSubMsg = service.receiveSubscriptionMessage(topicName, subscriptionName);
			BrokeredMessage m = resultSubMsg.getValue();
			if(m==null)
			{
				System.out.println("No new Speed Camera Announcements");
			}
			else if(m.getLabel().equals("Speed Camera Announcement"))
			{
				ObjectInputStream in = new ObjectInputStream(m.getBody());
				SpeedCamera sC = SpeedCamera.makeSpeedCamera((String)in.readObject()); //Speed Cameras are built with these strings 
				service.deleteMessage(m);
				System.out.println(sC.toString());
				return sC;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.toString());
		}
		return null;
	}
	
	public static ServiceBusInterface getVanillaSBI()
	{
		String topicName = "speedcameratopic";
		SubWithRules prioritySub = new SubWithRules("priority");
		prioritySub.addRule("ruleone","HighPriority = true");
		SubWithRules nonPrioritySub = new SubWithRules("nonpriority");
		SubWithRules announcementsSub = new SubWithRules("announcements");
		announcementsSub.addRule("announceCheck", "Announcement = true");
		SubWithRules vehicleCheckSub = new SubWithRules("vehiclecheck");
		SubWithRules sWR[] = {prioritySub,nonPrioritySub,vehicleCheckSub,announcementsSub};
		return new ServiceBusInterface(topicName, sWR);
	}
}
