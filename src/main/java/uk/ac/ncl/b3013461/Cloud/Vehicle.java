package uk.ac.ncl.b3013461.Cloud;

import java.util.Random;

public class Vehicle 
{
	private final String carReg;
	private final VehicleType type;
	private Vehicle(String reg, VehicleType t)
	{
		carReg = reg;
		type = t;
	}
	public String getCarReg() {
		return carReg;
	}
	public VehicleType getType() {
		return type;
	}
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
