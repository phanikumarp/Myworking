package com.n42.analytics.exceptions;

public class InvalidTimeUnitException extends CASException{

	//Parameterless Constructor
	public InvalidTimeUnitException() {}

	//Constructor that accepts a message
	public InvalidTimeUnitException(String message)
	{
		super(message);
	}

}
