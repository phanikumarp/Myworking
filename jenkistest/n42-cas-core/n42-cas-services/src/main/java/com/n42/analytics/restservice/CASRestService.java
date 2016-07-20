package com.n42.analytics.restservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.dialect.IngresDialect;
import org.json.simple.JSONObject;

import com.n42.analytics.services.ServiceVersionModelGenerator;
import com.n42.analytics.services.CASTrigger;
import com.n42.analytics.services.CASUIService;
import com.n42.analytics.services.CanaryAnalysisService;
import com.n42.analytics.services.SVCModelSavingService;
import com.n42.analytics.services.SVCUIService;
import com.n42.analytics.util.N42Properties;

@Path("/cas")
public class CASRestService {

	//	private static final Logger LOG = LoggerFactory.getLogger(SVCRestService.class);
	private String path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
	private String R_HOME = N42Properties.getInstance().getProperty("r.env.path");
	private String CHARACTERIZATION_SCRIPT = path+N42Properties.getInstance().getProperty("characterization.script.path");
	private String COMPARATOR_SCRIPT = path+N42Properties.getInstance().getProperty("comparator.script.path");

//	@POST
//	@Produces(MediaType.APPLICATION_JSON)
//	@Path("/triggerCAS")
//	public String triggerCAS(@QueryParam("timeDuration") int timeDuration, 
//			@QueryParam("durationUnit") String durationUnit, @QueryParam("timeSlice") int timeSlice, 
//			@QueryParam("sliceUnit") String sliceUnit, JSONObject sDEJSON) {
//		CASTrigger service = new CASTrigger();
//		String result = service.triggerCAS(sDEJSON, timeDuration, durationUnit, timeSlice, sliceUnit).toString();
//		return result;
//	}

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/svcChecker")
	//public String svcChecker(@QueryParam("appId") Integer appId,@QueryParam("serviceName") String serviceName,  @QueryParam("appVersionIds") List<Integer> appVersionIds, @QueryParam("serviceVersionIds") List<Integer> serviceVersionIds) {
	public String svcChecker(@QueryParam("appId") Integer appId, @QueryParam("serviceName") String serviceName, @QueryParam("serviceId") Integer serviceId, @QueryParam("appVersionId") Integer appVersionId, @QueryParam("versions") List<Integer> versions, @QueryParam("serviceVersion") Boolean serviceVersion) {
		ServiceVersionModelGenerator service =  new ServiceVersionModelGenerator();
		SVCUIService uIService = new SVCUIService();
		//Check if characterization json is present in db,if not call then get the call the characterization script.
		List<String> result = service.svcChecker(appId,appVersionId,versions,serviceVersion,serviceName,serviceId);

		List<String> compartorInput = new ArrayList<String>() ;
		//if not null , pass the characterization json input to R script
		BufferedReader reader =  null;
		Process shell = null;
		if(CollectionUtils.isNotEmpty(result)) {

			for(String inputToR : result){
				try {
					shell = Runtime.getRuntime().exec(new String[] { R_HOME ,CHARACTERIZATION_SCRIPT,inputToR});
					reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
					String line;
					while ((line = reader.readLine()) != null) {
						System.out.println(line);

					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// get the comparator input from the db
		result = service.comparatorInput(appId, versions, serviceVersion);
		String comparatorUIResult = new String();

		System.out.println("comparator model result: "+result);
		//call Comparator R script
		//  /home/ipsg/lalit/comparatornew.R  COMPARATOR_SCRIPT
		List<Integer> chcIds = service.sVCID;
		try {
			shell = Runtime.getRuntime().exec(new String[] { R_HOME, COMPARATOR_SCRIPT ,result.get(0),result.get(1),chcIds.get(0).toString(),chcIds.get(1).toString()});
			reader = new BufferedReader(new InputStreamReader(shell.getInputStream())); 
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);

			}
			if(serviceVersion){
				//				comparatorUIResult= uIService.getVersionData(appId, serviceName, chcIds, versions, true).toString();
			}
			else{
				//				comparatorUIResult = 	uIService.getVersionData(appId, null, chcIds, versions, false).toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}



		return comparatorUIResult;
	}


	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getCanaryOutput")
	public String getCanaryOutput(@QueryParam("canaryId") int canaryId){
		CASUIService service = new CASUIService();
			JSONObject result =	service.getCanaryAanalysisOutput(canaryId);
		return result.toJSONString();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getVersionData1")
	public int getVersionData1(@QueryParam("appid") int appId){
		//SVCUIService service = new SVCUIService();
		//		service.getVersionData(appId, serviceName, versions, null, serviceComparator);
		System.out.println("hai");
		return 3;
	}


	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/saveSVCComparatorAnalyzedModel")
	public void saveSVCComparatorAnalyzedModel(@QueryParam("sVCId1") int sVCId1, @QueryParam("sVCId2") int sVCId2,
			String json) {
		SVCModelSavingService service = new SVCModelSavingService();
		//		service.saveSVCComparatorAnalyzedModel(sVCId1, sVCId2, json);
	}

	//	@GET
	//	@Produces(MediaType.APPLICATION_JSON)
	//	@Path("/getVersionData")
	//	public int getVersionData(@QueryParam("appid") int appId, @QueryParam("versionId1") int versionId1,
	//			@QueryParam("versionId2") int versionId2, @QueryParam("serviceName") String serviceName,
	//			@QueryParam("comparatorOutput") JSONObject comparatorOutput, @QueryParam("serviceComparator") boolean serviceComparator){
	//		SVCUIService service = new SVCUIService();
	////		long start = System.currentTimeMillis();
	////		System.out.println("START TIME : "+new Date(start));
	//		
	//		service.getVersionData(appId, comparatorOutput, serviceName, versionId1, versionId2, serviceComparator);
	////		long end = System.currentTimeMillis();
	////		System.out.println("END TIME : "+new Date(end));
	////		System.out.println("TIME DIFF : "+(end - start));
	//		return 1;
	////		return null;
	//	}

	
	

}