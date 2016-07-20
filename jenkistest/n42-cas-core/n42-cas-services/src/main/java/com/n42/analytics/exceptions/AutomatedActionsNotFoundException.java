package com.n42.analytics.exceptions;

public class AutomatedActionsNotFoundException extends CASException{

	//Parameterless Constructor
	public AutomatedActionsNotFoundException() {}

	//Constructor that accepts a message
	public AutomatedActionsNotFoundException(String message)
	{
		super(message);
	}
}