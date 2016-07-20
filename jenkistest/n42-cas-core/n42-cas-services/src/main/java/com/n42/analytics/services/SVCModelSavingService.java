package com.n42.analytics.services;

import org.apache.commons.lang3.StringUtils;

import com.n42.analytics.hibernate.dao.ServiceVersionCharactersticDao;
import com.n42.analytics.hibernate.dao.ServiceVersionCharactersticDaoImpl;

import n42.domain.model.ServiceVersionCharacterstic;

public class SVCModelSavingService {

	public void saveSVCCharacterizationModel(Integer appId, Integer serviceId, Integer appVersionId, Integer serviceVersionId, String json){

		ServiceVersionCharactersticDao dao = new ServiceVersionCharactersticDaoImpl();
		ServiceVersionCharacterstic sVC = new ServiceVersionCharacterstic();
//		if(appId!=null){
//			sVC.setAppId(appId);
//		}
//		if(appVersionId!=null){
//			sVC.setAppVersionId(appVersionId);
//		}
//		if(StringUtils.isNotBlank(json)){
//			sVC.setJson(json);
//		}
//		if(serviceId!=null){
//			sVC.setServiceId(serviceId);
//		}
//		if(serviceVersionId!=null){
//			sVC.setServiceVersionId(serviceVersionId);
//		}
		dao.saveServiceVersionModel(sVC);
	}

	

	public static void main(String args[]){
		SVCModelSavingService modelSavingService = new SVCModelSavingService();
	}
}
