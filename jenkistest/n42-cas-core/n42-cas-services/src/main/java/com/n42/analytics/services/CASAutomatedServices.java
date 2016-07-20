package com.n42.analytics.services;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import com.n42.analytics.exceptions.AutomatedActionsNotFoundException;
import com.n42.analytics.hibernate.dao.CanaryDao;
import com.n42.analytics.hibernate.dao.CanaryDaoImpl;

import n42.domain.model.Action;
import n42.domain.model.Canary;
import n42.domain.model.Status;

public class CASAutomatedServices {

	public void startAutomatedActions(Canary canary) {
		try {
			if(canary.getActionsForUnhealthyCanary()!=null && !canary.getActionsForUnhealthyCanary().isEmpty()){
				
				List<Action> actions= canary.getActionsForUnhealthyCanary();
				
				CanaryDao  canaryDao = new CanaryDaoImpl() ;
				canary =  canaryDao.getCanaryById(canary.getN42Id());
				
				Status status = new Status();
				
				for(Action action : actions){
					if( action.getDelayBeforeActionInMins()!= null && action.getDelayBeforeActionInMins()> 0){
						TimeUnit.MINUTES.sleep((long)action.getDelayBeforeActionInMins().intValue());
					}
					if(StringUtils.equalsIgnoreCase(action.getAction(),"DISABLE")){
						status.setStatus("DISABLED");
						status.setComplete(false);
					}
					else if(StringUtils.equalsIgnoreCase(action.getAction(),"CANCEL")){
						status.setStatus("CANCELED");
						status.setComplete(true);
					}
					else if(StringUtils.equalsIgnoreCase(action.getAction(),"COMPLETE")){
						status.setStatus("COMPLETED");
						status.setComplete(true);
					}
					else if(StringUtils.equalsIgnoreCase(action.getAction(),"FAIL")){
						status.setStatus("FAILED");
						status.setComplete(true);
					}
					else if(StringUtils.equalsIgnoreCase(action.getAction(),"TERMINATE")){
						status.setStatus("TERMINATED");
						status.setComplete(true);
					}
					else{
						throw new AutomatedActionsNotFoundException("Action Not Recognized");
					}
					canary.setStatus(status);
					canaryDao.updateCanary(canary);
				}
			}
			else{
				throw new AutomatedActionsNotFoundException("Automated Actions Not Found In Canary Details");
			}

		} catch (AutomatedActionsNotFoundException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}