package com.n42.analytics.restservice;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.n42.analytics.services.CanaryAnalysisService;

@Path("/canaryConfigs")	
public class CanaryAnalysisConfigRestService {
	
	@GET
	@Produces("application/json")
	public String getCanaryConfigs(@QueryParam("application") String application) {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.getCanaryConfigs(application);
	}
	
	@GET
	@Produces("application/json")
	@Path("/{name}")
	public String getCanaryStatus(@PathParam("name") String application) {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.getCanaryConfigs(application);
	}
}
