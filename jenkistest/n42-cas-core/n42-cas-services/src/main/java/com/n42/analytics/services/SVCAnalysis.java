package com.n42.analytics.services;

import n42.domain.model.SVCResult;

import org.json.simple.JSONObject;

import com.n42.analytics.hibernate.dao.SVCResultDao;
import com.n42.analytics.hibernate.dao.SVCResultDaoImpl;
import com.n42.analytics.util.JSONUtils;

public class SVCAnalysis {
	
	public void saveSVCResult(String comparisonResult) {
		SVCResult result = new SVCResult();
		SVCResultDao dao = new SVCResultDaoImpl(); 
		JSONUtils jsonUtil = new JSONUtils();
		JSONObject jsonobject = jsonUtil.parseJsonFromInputString(comparisonResult);
		JSONObject comresult = (JSONObject) jsonobject.get("comparison_output");

		result.setSvcId("123");
		String fv1 = (String) comresult.get("version1name");
		String fv2 = (String) comresult.get("version2name");
		String serviceName = (String) comresult.get("serviceName");
		System.out.println("first v" + fv1 + "sceond v" + fv2);
		result.setVersionFirst(fv1);
		result.setVersionSecond(fv2);
		result.setServiceName(serviceName);
		result.setResult(comparisonResult);
		dao.saveSVCResult(result);
		
	}

}
