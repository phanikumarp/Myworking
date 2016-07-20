package com.n42.analytics.restservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.n42.analytics.services.RegisterCanary;

@Path("/registerCanary")
public class RegisterCanaryRestService {

	@SuppressWarnings("unchecked")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces(MediaType.TEXT_PLAIN)
	public Response registerCanary(String canary ) {
		System.out.println("Register Canary input json details:"+canary);
		RegisterCanary registerCanary = new RegisterCanary();
		JSONParser jsonParser = new JSONParser();
		JSONObject canaryObj = null;
		try {
			canaryObj = (JSONObject) jsonParser.parse(canary);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return Response.status(200).entity(registerCanary.registerCanary(canaryObj).toString()).build();
	}
	
//	public static void main(String[] args) {
//		String json = JSONUtils.createJsonFromFile("/register.json");
//		RegisterCanaryRestService  canaryRestService = new RegisterCanaryRestService();
//		canaryRestService.registerCanary(json);
//	}
}
