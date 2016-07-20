package com.n42.analytics.hibernate.dao;

import java.util.List;

import n42.domain.model.SVCResult;

public class SVCResultDaoImpl extends AbstractDao implements SVCResultDao {

	@Override
	public SVCResult saveSVCResult(SVCResult result) {
		result = (SVCResult) setMetaAttributes(result);
		return (SVCResult) super.save(result);

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SVCResult> getAllSVCReslut() {
		List<SVCResult> SVCResluts = (List<SVCResult>) super
				.findAll(SVCResult.class);
		return SVCResluts;
	}

	public SVCResult getSVCResult(String serviceName, String version1,
			String version2) {
		List<SVCResult> results = (List<SVCResult>) super.findByAttributes(
				SVCResult.class, "serviceName", serviceName, "versionFirst",
				version1, "versionSecond", version2);
		if (results == null || results.isEmpty()) {
			return null;
		}
		return results.get(0);
	}

}
