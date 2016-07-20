package com.n42.analytics.exceptions;

public class ClusterMissingException extends CASException{
	//Parameterless Constructor
		public ClusterMissingException() {}

		//Constructor that accepts a message
		public ClusterMissingException(String message)
		{
			super(message);
		}
}
