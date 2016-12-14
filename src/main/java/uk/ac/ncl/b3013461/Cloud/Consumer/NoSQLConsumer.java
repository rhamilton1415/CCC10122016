package uk.ac.ncl.b3013461.Cloud.Consumer;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;

public class NoSQLConsumer {
	private static String storageConnectionString = 
			"DefaultEndpointsProtocol=https;"+
			"AccountName=coursework;"+
			"AccountKey=d99QUOCXWTv0LruFV7Gczg7UDLMJpEPRd1/QrEDJnkNi0DmrkKYV2BNxb4EbWzmk+vbeJn2fXiazr44ty2MUuQ==";
	public static void main(String[] args) 
	{
		try
		{
			CloudStorageAccount cSA = CloudStorageAccount.parse(storageConnectionString);
			CloudTableClient tC = cSA.createCloudTableClient();
			CloudTable cT = new CloudTable("SpeedCameras",tC);
			cT.createIfNotExists();
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}
		
	}

}
