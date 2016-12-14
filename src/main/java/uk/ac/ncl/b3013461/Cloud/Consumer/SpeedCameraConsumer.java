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
import uk.ac.ncl.b3013461.Cloud.Camera.SpeedCameraRecording;

public class SpeedCameraConsumer {
	private static String storageConnectionString = 
			"DefaultEndpointsProtocol=https;"+
			"AccountName=coursework;"+
			"AccountKey=d99QUOCXWTv0LruFV7Gczg7UDLMJpEPRd1/QrEDJnkNi0DmrkKYV2BNxb4EbWzmk+vbeJn2fXiazr44ty2MUuQ==";
	private static ServiceBusInterface sBI = ServiceBusInterface.getVanillaSBI();
	private CloudTable cloudAnnounceT;
	private CloudTable cloudRecordingsT;
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
							while(true) //loop until you get a null announce
							{
								SpeedCamera sC = sBI.getSpeedCameraAnnouncement("Announcements");
								if(sC!=null)
								{
									System.out.println(sC.toString());
									sSC.addCamera(sC);
								}
								else
								{
									break announcementsExist;
								}
							}
						recordingsExist:
							while(true) //loop until you get a null recording
							{
								SpeedCameraRecording sCR = sBI.getSpeedCameraRecordingMessage("priority");
								if(sCR!=null)
								{
									System.out.println(sCR.getVehicle().getCarReg() + " " + sCR.getVehicleSpeed());
									sSC.addRecording(sCR);
								}
								else
								{
									break recordingsExist;
								}
							}
					}
				}, 10*1000,10*1000);
	}
	public SpeedCameraConsumer()
	{
		try 
		{
			//create the annoucement catcher
			CloudStorageAccount cSA = CloudStorageAccount.parse(storageConnectionString);
			CloudTableClient tC = cSA.createCloudTableClient();
			cloudAnnounceT = new CloudTable("SpeedCameras",tC);
			cloudAnnounceT.createIfNotExists(); 
			
			//create the recording catcher
			cloudRecordingsT = new CloudTable("SpeedCameraRecordings",tC);
			cloudRecordingsT.createIfNotExists();
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
			cloudAnnounceT.execute(tO);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public boolean addRecording(SpeedCameraRecording sCR)
	{
		try
		{
			TableOperation tO = TableOperation.insertOrReplace(sCR);
			cloudAnnounceT.execute(tO);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	public boolean getCamera(String targetCamID)
	{
		try
		{
			final String PARTITION_KEY = "PartitionKey";
			final String ROW_KEY = "RowKey";
			final String TIMESTAMP = "TimeStamp";
			String partitionFilter = TableQuery.generateFilterCondition(PARTITION_KEY, QueryComparisons.EQUAL,targetCamID);
			TableQuery<SpeedCameraEntry> tQ = TableQuery.from(SpeedCameraEntry.class).where(partitionFilter);
			for(SpeedCameraEntry s : cloudAnnounceT.execute(tQ))
			{
				return true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
}
