package com.n42.analytics.restservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;

import com.n42.analytics.hibernate.dao.CanaryAnalysisDao;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDaoImpl;
import com.n42.analytics.services.CanaryAnalysisService;
import com.n42.analytics.services.RegisterCanary;

@Path("/canaries")
public class CanaryAnalysisRestService {

	@SuppressWarnings("unchecked")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Produces("application/json")
	@Path("/saveCanaryAnalysisModel")
	public String saveCanaryAnalysisModel(String comparisonData) {
		System.out.println("input from R:::"+comparisonData);
		JSONObject status = new JSONObject();
		if(comparisonData != null){
			CanaryAnalysisService canaryAnalysisService = new CanaryAnalysisService();
			status.put("status", canaryAnalysisService.saveCanaryAnalysisResult(comparisonData));
		}else{
			status.put("status", "Input json from R is empty");
		}
		return status.toString();
	}
	
	@GET
	@Produces("application/json")
	@Path("/update/{id}/result")
	public String updateCanaryResultScore(@PathParam("id") int canaryId, @QueryParam("result") int canaryResultScore){
		CanaryAnalysisDao canaryDao = new CanaryAnalysisDaoImpl();
		int result = canaryDao.updateCanaryResultById(canaryId, canaryResultScore);
		
		if(result ==  1){
			return "Success";
		}
		else {
			return "Failed";
		}
		
	}
	
	@DELETE
	@Produces("application/json")
	@Path("{id}/cancel")
	public String deleteCanary(@PathParam("id") int id, @QueryParam("reason") String reason){
		CanaryAnalysisService aanaryAnalysisService = new CanaryAnalysisService();
		return aanaryAnalysisService.deleteCanary(id, reason).toString();
	}
	
	@GET
	@Produces("application/json")
	@Path("/{id}")
	public String getCanaryByCanaryId(@PathParam("id") int id) {
		RegisterCanary registerCanary = new RegisterCanary() ;
		return registerCanary.getCanaryAsJSONObject(id);
	}
	
	@GET
	@Produces("application/json")
	public String getAllCanaries() {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.getAllCanaries();
	}
	
	@GET
	@Produces("application/json")
	@Path("/{id}/status")
	public String getCanaryStatus(@PathParam("id") int id) {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.getCanaryStatus(id);
	}
	
	@GET
	@Produces("application/json")
	@Path("/{id}/health")
	public String getCanaryHealth(@PathParam("id") int id) {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.getCanaryHealth(id);
	}
	
	@GET
	@Produces("application/json")
	@Path("/{id}/disable")
	public String getCanaryDisable(@PathParam("id") int id, @QueryParam("reason") String reason) {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.disableCanary(id, reason);
	}
	
	@GET
	@Produces("application/json")
	@Path("metricdata")
	public String getMetricGraphData(@PathParam("id") int id, @QueryParam("metric") String metric) {
		CanaryAnalysisService anaryAnalysisService = new CanaryAnalysisService();
		return anaryAnalysisService.getMetricGraphData(id, metric);
	}
	
}
