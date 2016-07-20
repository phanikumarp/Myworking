package com.n42.analytics.exceptions;

public class NullInputFoundException extends CASException{
	//Parameterless Constructor
		public NullInputFoundException() {}

		//Constructor that accepts a message
		public NullInputFoundException(String message)
		{
			super(message);
		}

}
