package com.n42.analytics.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PythonInvoker {
	
	public static void main(String[] args) throws IOException {
		
		String s="/home/ipsg/loading.gif";
		Process shell = Runtime.getRuntime().exec(new String[] {"python","/home/ipsg/lalit/ImpWork/Imageaccept.py",s});
		BufferedReader reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
		String line;
		while ((line = reader.readLine()) != null) {
			System.out.println("hi every body");
			System.out.println(line);

		}
	}

}


