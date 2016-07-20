package com.n42.analytics.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import n42.domain.model.BucketScore;
import n42.domain.model.CanaryAnalysis;
import n42.domain.model.MetricScore;
import n42.domain.model.Service;
import n42.domain.model.ServiceVersion;
import n42.domain.model.ServiceVersionCharacterstic;

import com.n42.analytics.hibernate.dao.BucketScoreDao;
import com.n42.analytics.hibernate.dao.BucketScoreDaoImpl;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDao;
import com.n42.analytics.hibernate.dao.CanaryAnalysisDaoImpl;
import com.n42.analytics.hibernate.dao.MetricScoreDao;
import com.n42.analytics.hibernate.dao.MetricScoreDaoImpl;
import com.n42.analytics.hibernate.dao.ServiceDao;
import com.n42.analytics.hibernate.dao.ServiceDaoImpl;
import com.n42.analytics.hibernate.dao.ServiceVersionCharactersticDao;
import com.n42.analytics.hibernate.dao.ServiceVersionCharactersticDaoImpl;
import com.n42.analytics.hibernate.dao.ServiceVersionDao;
import com.n42.analytics.hibernate.dao.ServiceVersionDaoImpl;
public class ModelUtils {

	// service : name vs service
	public static Map<String, Service> getServiceNameMap(String application,
			String tenant, List<Class<?>> associations) {
		Map<String, Service> result = new HashMap<>();
		ServiceDao dao = new ServiceDaoImpl();
		List<Service> services = dao.getAllServices(application, tenant,
				associations);

		for (Service s : services) {
			result.put(s.getName(), s);
		}
		return result;
	}

	// service : id vs service
	public static Map<Integer, Service> getServiceIdMap(String application,
			String tenant, List<Class<?>> associations) {
		Map<Integer, Service> result = new HashMap<>();

		ServiceDao dao = new ServiceDaoImpl();
		List<Service> services = dao.getAllServices();

		for (Service s : services) {
			result.put(s.getN42Id(), s);
		}
		return result;
	}


	// public static Map<Integer, TenantApplication>
	// getTenantAppIdMap(List<Class<?>> associations) {
	// Map<Integer, TenantApplication> result = new HashMap<Integer,
	// TenantApplication>();
	//
	// TenantApplicationDao dao = new TenantApplicationDaoImpl();
	// List<TenantApplication> tenants =
	// dao.getAllTenantApplications(associations);
	//
	// for(TenantApplication t : tenants) {
	// result.put(t.getN42Id(), t);
	// }
	// return result;
	// }

	public static Map<Integer, ServiceVersion> getServiceVersionN42IdMap(List<Class<?>> associations) {
		Map<Integer, ServiceVersion> result = new HashMap<>();

		ServiceVersionDao dao = new ServiceVersionDaoImpl();
		List<ServiceVersion> serviceVersions = dao.getAllServiceVersion();

		for (ServiceVersion sV : serviceVersions) {
			result.put(sV.getN42Id(), sV);
		}
		return result;
	}

	public static Map<Integer, Integer> getServiceVersionIdVsServiceIdMap(List<Class<?>> associations) {
		Map<Integer, Integer> result = new HashMap<>();


		ServiceVersionDao dao = new ServiceVersionDaoImpl();
		List<ServiceVersion> serviceVersions = dao.getAllServiceVersion();

		for (ServiceVersion sV : serviceVersions) {
			result.put(sV.getN42Id(), sV.getServiceId());

		}
		return result;
	}

	public static Map<Integer, ServiceVersion> getServiceVersionIdMap(List<Class<?>> associations) {
		Map<Integer, ServiceVersion> result = new HashMap<>();

		ServiceVersionDao dao = new ServiceVersionDaoImpl();
		List<ServiceVersion> serviceVersions = dao.getAllServiceVersion();

		for (ServiceVersion serviceVersion : serviceVersions) {
			result.put(serviceVersion.getN42Id(), serviceVersion);
		}
		return result;
	}

	//canaryResult Objet basis of baseVersion and compareVersion
	public static CanaryAnalysis getCanaryBasedOnBaseVersionAndcompareVersion(Integer baseVersion ,Integer compareVersion) {
		CanaryAnalysis result = new CanaryAnalysis();
		CanaryAnalysisDao dao = new CanaryAnalysisDaoImpl();
		List<CanaryAnalysis> CanaryAnalysiss = dao.getAllCanaryAnalysis();
		for (CanaryAnalysis CanaryAnalysis : CanaryAnalysiss) {
			if(Integer.compare(CanaryAnalysis.getBaseVersionId(),baseVersion)== 0 && Integer.compare(CanaryAnalysis.getCompareVersionId(), compareVersion) == 0 ){
				result= CanaryAnalysis;
			}
		}
		return result;
	}

	//MetricScore Map of <metricN42Id,MetricScoreObject>
	public static Map<Integer, MetricScore> getMetricScoreIDMap(){
		Map<Integer,MetricScore> result = new HashMap<Integer, MetricScore>();
		MetricScoreDao dao = new MetricScoreDaoImpl();
		List<MetricScore> MetricScores = dao.getAllMetricScore();
		for (MetricScore MetricScore : MetricScores) {
			result.put(MetricScore.getN42Id(), MetricScore);
		}
		return result;
	}

	//MetricScore Map of <metricN42Id,MetricScoreObject>
	public static Map<Integer, BucketScore> getBucketScoreIDMap(){
		Map<Integer,BucketScore> result = new HashMap<Integer, BucketScore>();
		BucketScoreDao dao = new BucketScoreDaoImpl();
		List<BucketScore> BucketScores = dao.getAllBucketScore();
		for (BucketScore BucketScore : BucketScores) {
			result.put(BucketScore.getN42Id(), BucketScore);
		}
		return result;
	}



}