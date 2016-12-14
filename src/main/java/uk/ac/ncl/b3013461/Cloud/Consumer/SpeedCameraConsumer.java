package uk.ac.ncl.b3013461.Cloud.Consumer;

import java.util.Timer;
import java.util.TimerTask;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;

import uk.ac.ncl.b3013461.Cloud.Camera.ServiceBusInterface;
import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCamera;

public class SpeedCameraConsumer {
	private static String storageConnectionString = 
			"DefaultEndpointsProtocol=https;"+
			"AccountName=coursework;"+
			"AccountKey=d99QUOCXWTv0LruFV7Gczg7UDLMJpEPRd1/QrEDJnkNi0DmrkKYV2BNxb4EbWzmk+vbeJn2fXiazr44ty2MUuQ==";
	private static ServiceBusInterface sBI = ServiceBusInterface.getVanillaSBI();
	private CloudTable cT;
	public static void main(String[] args) 
	{
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask()
				{
					SpeedCameraConsumer sSC = new SpeedCameraConsumer();
					@Override
					public void run()
					{
						announcementsExist:
							while(true)
							{
								SpeedCamera sC = sBI.getSpeedCameraAnnouncement("Announcements");
								if(sC!=null)
								{
									sSC.addCamera(sC);
								}
								else
								{
									break announcementsExist;
								}
							}
					}
				}, 10*1000,10*1000);
	}
	public SpeedCameraConsumer()
	{
		try
		{
			CloudStorageAccount cSA = CloudStorageAccount.parse(storageConnectionString);
			CloudTableClient tC = cSA.createCloudTableClient();
			cT = new CloudTable("SpeedCameras",tC);
			cT.createIfNotExists();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
	}
	public boolean addCamera(SpeedCamera sC)
	{
		try
		{
			TableOperation tO = TableOperation.insertOrReplace(new SpeedCameraEntry(sC));
			cT.execute(tO);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public void getCamera(String targetCamID)
	{
		try
		{
			final String PARTITION_KEY = "PartitionKey";
			final String ROW_KEY = "RowKey";
			final String TIMESTAMP = "TimeStamp";
			String partitionFilter = TableQuery.generateFilterCondition(PARTITION_KEY, QueryComparisons.EQUAL,targetCamID);
			TableQuery<SpeedCameraEntry> tQ = TableQuery.from(SpeedCameraEntry.class).where(partitionFilter);
			for(SpeedCameraEntry s : cT.execute(tQ))
			{
				System.out.println(SpeedCamera.makeSpeedCamera(s.toSpeedCameraConfig()));
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
