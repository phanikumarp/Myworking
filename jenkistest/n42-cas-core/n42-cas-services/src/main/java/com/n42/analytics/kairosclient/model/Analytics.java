package com.n42.analytics.kairosclient.model;

import com.n42.analytics.kairosclient.enums.Status;

public class Analytics {

	public Analytics() {
	}
	
	private String value;
	private String description;
	private String services;
	private Status status;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return this.value + " : " + this.status;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getServices() {
		return services;
	}
	public void setServices(String services) {
		this.services = services;
	}

}
