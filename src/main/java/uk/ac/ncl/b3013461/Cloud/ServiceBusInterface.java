package uk.ac.ncl.b3013461.Cloud;
////Endpoint=sb://speedcamerasb.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=9sG8mh6JbBbbBbrwv4z//DPuu+Zj0VFvrevhQkmWwvI=

import com.microsoft.windowsazure.services.servicebus.*;
import com.microsoft.windowsazure.services.servicebus.models.*;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.core.*;
import com.microsoft.windowsazure.exception.ServiceException;

import java.util.ArrayList;

import javax.xml.datatype.*;
public class ServiceBusInterface 
{
	private final static String topicName = "speedcameratopic";
	private final static String[] subscriptionNames = {"priority","nonpriority","vehiclecheck"};
	private final ServiceBusContract service;
	//The constructor will try and connect to a Topic, or create one if one does not exist
	public ServiceBusInterface()
	{
		System.setErr(null); //stfu error codes
		Configuration c = ServiceBusConfiguration.configureWithSASAuthentication(
				"SpeedCameraSB",
				"RootManageSharedAccessKey",
				"9sG8mh6JbBbbBbrwv4z//DPuu+Zj0VFvrevhQkmWwvI=",
				".servicebus.windows.net"
				);
		service = ServiceBusService.create(c);
		TopicInfo tI = new TopicInfo(topicName);
		tI.setMaxSizeInMegabytes((long)5120);
		
		try
		{
			if(service.getTopic(topicName)!=null)
			{
				//Topic already exists
				System.out.println("Topic already exists - proceeding");
			}
			else
			{
				System.out.println("Topic does not exist - creating new Topic");
				CreateTopicResult r = service.createTopic(tI);
				System.out.println("New Topic: \""+topicName+"\"");
			}
			//create the subscriptions
			SubscriptionInfo prioritySubInfo = new SubscriptionInfo("Priority");
			//CreateSubscriptionResult r = service.createSubscription(topicName, prioritySubInfo);
			for(int i = 0;i<subscriptionNames.length;i++)
			{
				try
				{
					SubscriptionInfo sI = new SubscriptionInfo(subscriptionNames[i]);
					CreateSubscriptionResult r = service.createSubscription(topicName, sI);
				}
				catch(Exception e)
				{
					System.out.println("Subscription: \"" +subscriptionNames[i] + "\" already exists - proceeding");
				}
			}
		}
		catch(ServiceException e)
		{
			System.out.println(e.getHttpStatusCode());
		}
		
	}
}
