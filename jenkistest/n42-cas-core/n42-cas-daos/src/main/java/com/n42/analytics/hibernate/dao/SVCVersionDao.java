package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.SVCVersion;

public interface SVCVersionDao {

	List<SVCVersion> getAllSVCVersion();

	SVCVersion getSVCVersionDetails(String version);
	
}
