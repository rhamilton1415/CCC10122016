package uk.ac.ncl.b3013461.Cloud.Camera;
//Endpoint=sb://speedcamerasb.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=9sG8mh6JbBbbBbrwv4z//DPuu+Zj0VFvrevhQkmWwvI=
import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.table.TableQuery.*;
public class ConnectionTest {

	private static String storageConnectionString = 
			"DefaultEndpointsProtocol=https;"+
			"AccountName=coursework;"+
			"AccountKey=d99QUOCXWTv0LruFV7Gczg7UDLMJpEPRd1/QrEDJnkNi0DmrkKYV2BNxb4EbWzmk+vbeJn2fXiazr44ty2MUuQ==";
			
	public static void main(String[] args) {
		try
		{
			CloudStorageAccount cSA = CloudStorageAccount.parse(storageConnectionString);
			CloudTableClient tC = cSA.createCloudTableClient();
			CloudTable cT = new CloudTable("testTable",tC);
			cT.createIfNotExists();
			System.out.println("yay");
			
		}
		catch(Exception e)
		{
			e.printStackTrace(System.err);
		}

	}

}
