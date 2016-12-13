package uk.ac.ncl.b3013461.Cloud.Consumer;

import com.microsoft.windowsazure.services.servicebus.models.*;

public class testConsumer 
{
	public testConsumer()
	{
		try
		{
			ReceiveMessageOptions opts = ReceiveMessageOptions.DEFAULT;
			opts.setReceiveMode(ReceiveMode.PEEK_LOCK);
			while(true)
			{
				
			}
		}
		catch(Exception e)
		{
			
		}
	}
}
