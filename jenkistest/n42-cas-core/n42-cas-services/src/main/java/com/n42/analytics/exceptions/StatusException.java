package com.n42.analytics.exceptions;

public class StatusException extends CASException {

	//Parameterless Constructor
	public StatusException() {}

	//Constructor that accepts a message
	public StatusException(String message)
	{
		super(message);
	}
}
