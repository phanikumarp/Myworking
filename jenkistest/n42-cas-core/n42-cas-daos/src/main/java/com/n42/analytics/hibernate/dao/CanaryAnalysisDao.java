package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.CanaryAnalysis;

public interface CanaryAnalysisDao {

	CanaryAnalysis saveCanaryAnalysis(CanaryAnalysis canaryAnalysis);
	
	List<CanaryAnalysis> getAllCanaryAnalysis();
	
	void deleteCanaryAlaysis(Integer id);
	
	int updateCanaryResultById(Integer id, float canaryResultScore);

	float getCanaryResultById(Integer id);

	CanaryAnalysis getCanaryAnalysisById(Integer id);

}
