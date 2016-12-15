package uk.ac.ncl.b3013461.Cloud.Camera;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class SpeedCameraDeployment {

	//Keep config in the format config then on the next line the timeout
	public static void main(String[] args) 
	{
		ArrayList<SpeedCamera> cameras = new ArrayList<SpeedCamera>();
		try
		{
			Scanner s = new Scanner(new File("src\\configs.txt"));
			while(s.hasNextLine())
			{
				cameras.add(SpeedCamera.makeSpeedCamera(s.nextLine(),true,Long.parseLong(s.nextLine())));
			}
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}

}
