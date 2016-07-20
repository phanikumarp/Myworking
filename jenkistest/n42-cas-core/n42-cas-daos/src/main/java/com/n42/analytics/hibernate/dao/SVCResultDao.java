package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.SVCResult;

public interface SVCResultDao {
	
	SVCResult saveSVCResult(SVCResult result); 
	
	public List<SVCResult> getAllSVCReslut();

	SVCResult getSVCResult(String serviceName, String version1, String version2);

}
