package com.n42.analytics.exceptions;

public class CanaryNotFoundException  extends Exception{
	//Parameterless Constructor
	public CanaryNotFoundException() {}

	//Constructor that accepts a message
	public CanaryNotFoundException(String message)
	{
		super(message);
	}
}

