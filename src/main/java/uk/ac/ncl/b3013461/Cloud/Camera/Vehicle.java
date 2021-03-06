package uk.ac.ncl.b3013461.Cloud.Camera;

import java.util.Random;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class Vehicle extends TableServiceEntity implements java.io.Serializable
{
	private final String carReg;
	private final VehicleType type;
	private Vehicle(String reg, VehicleType t)
	{
		carReg = reg;
		type = t;
		this.partitionKey = carReg;
		this.rowKey = type.toString(); 
	}
	public String getCarReg() {
		return carReg;
	}
	public VehicleType getType() {
		return type;
	}
	/**
	 * Generates a random Vehicle by generating a random reg number and using it to determine what type the vehicle will be
	 * @return a random Vehicle
	 */
	public static Vehicle generateVehicle()
	{
		String newCarReg = getRandomLetter()+getRandomLetter()+getRandomNumber()+getRandomNumber()+getRandomLetter()+getRandomLetter()+getRandomLetter();
		VehicleType t=VehicleType.TYPE_CAR;
		switch(Math.abs(newCarReg.hashCode()%3))
		{
		case 0:
			t = VehicleType.TYPE_CAR;
			break;
		case 1:
			t = VehicleType.TYPE_MOTORCYCLE;
			break;
		case 2:
			t = VehicleType.TYPE_TRUCK;
			break;
		}
		return new Vehicle(newCarReg,t);
		
	}
	private static String getRandomLetter()
	{
		Random r = new Random();
		return Character.toString((char)(r.nextInt(26)+65));
	}
	private static String getRandomNumber()
	{
		Random r = new Random();
		//return Character.toString((char)(r.nextInt(9)));
		return Integer.toString(r.nextInt(9));
	}
}
