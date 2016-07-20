package com.n42.analytics.exceptions;

//Custom exceptions 
public class CASException extends Exception{

	//ParametSerless Constructor
	public CASException() {}

	//Constructor that accepts a message
	public CASException(String message)
	{
		super(message);
	}

	public void exception(String message){
		fillInStackTrace();
		printStackTrace();
	}

}
