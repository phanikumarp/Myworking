package com.n42.analytics.restservice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import n42.domain.model.SVCService;
import n42.domain.model.SVCVersion;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.n42.analytics.hibernate.dao.SVCServiceDao;
import com.n42.analytics.hibernate.dao.SVCServiceDaoImpl;
import com.n42.analytics.hibernate.dao.SVCVersionDao;
import com.n42.analytics.hibernate.dao.SVCVersionDaoImpl;
import com.n42.analytics.hibernate.util.HibernateFactory;
import com.n42.analytics.services.SVCAnalysis;
import com.n42.analytics.services.SVCInput;
import com.n42.analytics.util.JSONUtils;



@Path("/svc")
public class SVCAnalysisRestService {

	@SuppressWarnings("unchecked")
	@POST
	@Consumes({ MediaType.APPLICATION_JSON})
	@Path("/saveSVCResults")
	public void saveSVCResults(String SVCResult ) {
		System.out.println("SVC Results Details:"+SVCResult);
		SVCAnalysis svcr = new SVCAnalysis();
		svcr.saveSVCResult(SVCResult);
	}
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/compareSVC")
	public String compareSVC(@QueryParam("serviceName") String serviceName, @QueryParam("version1") String version1 , @QueryParam("version2") String version2) throws IOException {
		
		SVCInput svcinp = new SVCInput();
		return svcinp.svcComparision(serviceName, version1, version2);
	}
	
	
	@SuppressWarnings("unchecked")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/getSVCServices")
	public String getSVCServices() {
		
		SVCServiceDao serviceDao = new SVCServiceDaoImpl();
		List<SVCService> svcServices = serviceDao.getAllSVCServices();
		
		SVCVersionDao svcVersionDao = new SVCVersionDaoImpl();
		
		Map<String, Object> root = new LinkedHashMap<>();
		List<Object> servicesArray = new ArrayList<Object>();
		
		root.put("services", servicesArray);
		
		Set<Integer> serviceIds = new HashSet<>();
		if(CollectionUtils.isNotEmpty(svcServices)) {
			for(SVCService s : svcServices) {
				if(s == null || !serviceIds.add(s.getN42Id())) {
					continue;
				}
				
				Map<String, Object> serviceObj = new LinkedHashMap<>();
				serviceObj.put("serviceId", s.getN42Id());
				serviceObj.put("serviceName", s.getServiceName());
				
				List<Object> versionsArray = new ArrayList<Object>();
				
				serviceObj.put("serviceVersions", versionsArray);
				
				List<String> versions = s.getServiceVersions();
				
				for(String version : versions) {
					SVCVersion svcVersion = svcVersionDao.getSVCVersionDetails(version);
					
					if(svcVersion == null) {
						continue;
					}
					Map<String, Object> versionObj = new LinkedHashMap<>();
					versionObj.put("versionId", svcVersion.getN42Id());
					versionObj.put("versionName", svcVersion.getVersionName());
					versionObj.put("versionStartTime", svcVersion.getStartTime());
					versionObj.put("versionEndTime", svcVersion.getEndTime());
					
					versionsArray.add(versionObj);
				}
				
				servicesArray.add(serviceObj);
				
			}
		}
		
		return JSONUtils.getFormattedJson(root);
		
	}
	
	
	public static void main(String[] args) {
		SVCAnalysisRestService analysisRestService = new SVCAnalysisRestService();
		String result = analysisRestService.getSVCServices();
		System.out.println(result);
	}
}
