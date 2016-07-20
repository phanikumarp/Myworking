require(quantreg)
require(ggplot2)
require(rjson)
require(httr)
args = commandArgs(trailingOnly=TRUE)
print(args)
#metadata<-fromJSON(file = "/home/tushar/SvcTesthaproxy.json",method = "C")
metadata<-fromJSON(args)
#############################################################################

createRequest<-function(x,metricName){
  
  s<-"{\"metrics\": [{\"tags\": {\""
  
  m<-1
  while(m<=length(x$tagsData)){
    if(m==length(x$tagsData)){
      s<-paste(s,x$tagsData[[m]]$tagName,"\":[\"",sep="")
      s<-paste(s,x$tagsData[[m]]$tagValue,"\"]",sep="")
    }else{
      s<-paste(s,x$tagsData[[m]]$tagName,"\":[\"",sep="")
      s<-paste(s,x$tagsData[[m]]$tagValue,"\"],",sep="")
    }
    m=m+1
  }
  
  if(x$aggregator$Function!=""){
    s<-paste(s,"},\"name\": \"",metricName,"\",",sep="")
    s<-paste(s,"\"aggregators\":[{\"name\":\"",x$aggregator$Function,"\",\"align_sampling\":true,\"sampling\":{\"value\":\"",
             x$aggregator$time,"\",\"unit\":\"",x$aggregator$unit,"\"}}]",sep="")
  }else{
    s<-paste(s,"},\"name\": \"",metricName,"\"",sep="")
  }
  if(x$endTime=="null"){
    s<-paste(s,"}],\"cache_time\": 0,\"start_absolute\":",x$startTime,"}",sep="")
  }else{
    s<-paste(s,"}],\"cache_time\": 0,\"start_absolute\":",x$startTime,
             ",\"end_absolute\":",x$endTime,"}",sep="")
  }
  print(s)
  return(s)
}

#################################################################################

createDataframe<-function(r){
  print(r)
  if(r$status_code==200){
    d<-rawToChar(r$content)
    e<-fromJSON(d)
    
    if(e$queries[[1]]$sample_size>0){
      xldata<-e$queries[[1]]$results[[1]]$values
      xldata<-as.data.frame(unlist(xldata))
      colnames(xldata)<- c("object")
      rownames(xldata)<-seq(from =1, to= nrow(xldata), by=1)
      
      vec1<-subset(x=xldata,subset=as.numeric(rownames(xldata))%%2!=0,select=c("object"))
      vec2<-subset(x=xldata,subset=as.numeric(rownames(xldata))%%2==0,select=c("object"))
      
      vec1<-cbind(vec1,vec2)
      xlfile<-as.data.frame(vec1)
      names(xlfile)<-c("timestamp","value")
  
      return(xlfile)
    }
  }else{
    return(paste("Status code:",r$status_code,"Bad response. Check request.",sep=" "))
  }
}
#################################################################################
print(length(metadata$service))
if(length(metadata$service)==6){
  if(length(metadata$service$Metrics)==2){
    if(metadata$service$Metrics[[1]]$metricType=="Load"){
      print("args receieved")
      loadMetricName<-metadata$service$Metrics[[1]]$metricName
      loadMetricReq<-createRequest(metadata$service$Metrics[[1]],loadMetricName)
      loadhttpres<-POST(url="http://52.8.104.253:8484/api/v1/datapoints/query",body=loadMetricReq,encode='json')
      loadData<-createDataframe(loadhttpres)
      
      responseMetricName<-metadata$service$Metrics[[2]]$metricName
      responseMetricReq<-createRequest(metadata$service$Metrics[[2]],responseMetricName)
      responsehttpres<-POST(url="http://52.8.104.253:8484/api/v1/datapoints/query",body=responseMetricReq,encode='json')
      responseData<-createDataframe(responsehttpres)
      
    }else{
      loadMetricName<-metadata$service$Metrics[[2]]$metricName
      loadMetricReq<-createRequest(metadata$service$Metrics[[2]],loadMetricName)
      loadhttpres<-POST(url="http://52.8.104.253:8484/api/v1/datapoints/query",body=loadMetricReq,encode='json')
      loadData<-createDataframe(loadhttpres)
        
      responseMetricName<-metadata$service$Metrics[[1]]$metricName
      responseMetricReq<-createRequest(metadata$service$Metrics[[1]],responseMetricName)
      resonsehttpres<-POST(url="http://52.8.104.253:8484/api/v1/datapoints/query",body=responseMetricReq,encode='json')
      responseData<-createDataframe(responsehttpres)
    }
    
    
    check1<-which(is.na(match(loadData$timestamp,responseData$timestamp))==T)
    if(length(check1)>0){
      loadData<-loadData[-check1,]
    }
    check2<-which(is.na(match(responseData$timestamp,loadData$timestamp))==T)
    if(length(check2)>0){
      responseData<-responseData[-check2,]
    }
    data<-data.frame(load=loadData$value,res=responseData$value)
	print(data)
	print(nrow(data))
    breakss<-pretty(data$load)
    labelss<-sapply(X = seq(1,(length(breakss)-1),1),FUN = function(t)paste("set",t,sep=""))
    sets<-split(x = data,f = cut(x = data$load,breaks = breakss,labels = labelss),drop = F)
    checklength<-sapply(X = sets,FUN = nrow,simplify = T)
    insufficientDataset<-which(checklength<=1)
    if(length(insufficientDataset)>0){
      sets<-sets[-insufficientDataset]
    }
    
    bw<-function(inputdata){
      inputdata<-with(data = inputdata,inputdata[order(load,decreasing = F),])
      q<-quantile(inputdata$load)
      ul<-q[4]+(1.5)*(q[4]-q[2])
      ll<-q[2]-(1.5)*(q[4]-q[2])
      removeIndexes<-which(inputdata$load>ul | inputdata$load<ll)
      if(length(removeIndexes)>0){
        inputdata<-inputdata[-removeIndexes,]
      }
      return(inputdata)
    }
    
    if(length(sets)>0){
      sets<-lapply(X = sets,FUN = bw)
      
      calcStat<-function(x){
        average <- mean(x)
        std_dev <- sd(x)
        median <- median(x)
        variance <- var(x)
        
        return(data.frame(average = average,std_dev = std_dev, median = median, variance = variance))
      }
      load_sets<-lapply(sets,FUN = "[[",2)  
      measures<-lapply(load_sets,FUN = calcStat) 
    
      prefix<-sapply(names(sets),FUN = function(t)paste("sets$",t,"$",sep=""))
      createForm<-function(p){
        term1<-paste(p,"res",sep="")
        term2<-paste(p,"load",sep="")
        formula<-paste(term1,"~",term2,sep="")
        return(formula(x = formula))
      }
      forml<-sapply(X = prefix,FUN = createForm)
      fit<-lapply(forml,FUN = rq,tau=c(0.05,0.95))
      coeff<-lapply(fit,FUN = coef)
      
      
      
     
      
    output<-paste("{\"models\":{\"service\":\"",metadata$service$serviceName,"\",\"appId\":",metadata$service$appID,",",sep="")
	output<-paste(output,"\"applicationVersionID\":",metadata$service$applicationVersionID,",\"serviceVersionId\":",metadata$service$serviceVersionId,",\"serviceID\":",metadata$service$serviceId,",",sep="")
    output<-paste(output,"\"loadMetric\":\"",loadMetricName,"\",\"responseMetric\":\"",responseMetricName,"\",",sep="")
    output<-paste(output,"\"loadRange\":\"",breakss[1],"-",breakss[length(breakss)],"\",",sep="")
    output<-paste(output,"\"buckets\":[",sep="")
    setno<-as.numeric(gsub(pattern = "[a-zA-Z*]",replacement = "",x = names(sets)))
    for(i in 1:length(coeff)){
      if(i==1){
        batch<-paste("{\"bucket_id\":","\"bucket",i,"\",",sep="")
      }else{
        batch<-paste(batch,",{\"bucket_id\":","\"bucket",i,"\",",sep="") 
      }
      batch<-paste(batch,"\"load_range\":\"",breakss[setno[i]],"-",breakss[setno[i]+1],"\",",sep="")
      batch<-paste(batch,"\"stats\":[",sep="")
      batch<-paste(batch,"{\"average\":",measures[[i]]$average,",",sep="")
      batch<-paste(batch,"\"std_dev\":",measures[[i]]$std_dev,",",sep="")
      batch<-paste(batch,"\"median\":",measures[[i]]$median,",",sep="")
      batch<-paste(batch,"\"variance\":",measures[[i]]$variance,"}],",sep="")
       
      batch<-paste(batch,"\"coefficients\":[",sep="")
      for(j in 1:length(fit[[i]]$tau)){
        if(j==1){
          batch<-paste(batch,"{\"tau\":",fit[[i]]$tau[j],",",sep="")
        }else{
          batch<-paste(batch,",{\"tau\":",fit[[i]]$tau[j],",",sep="")
        }
        batch<-paste(batch,"\"Intercept\":",fit[[i]]$coefficients[1,j],",",sep="")
        batch<-paste(batch,"\"load\":",fit[[i]]$coefficients[2,j],"}",sep="")
      } 
      batch<-paste(batch,"]}",sep="")
    }
        output<-paste(output,batch,"]}}",sep="")
	print(output)
   # write(x = output,file = "characterization1.json")
    json<-toJSON(x = output,method = "C")
     url<-paste("http://54.67.112.100:8161/n42-services/resources/svc/saveSVCModel?appId=",metadata$service$appID,"&serviceId=",
metadata$service$serviceId,"&appVersionId=",metadata$service$applicationVersionID,"&serviceVersionId=",
metadata$service$serviceVersionId,sep="")
	print(url)
    POST(url = url,body = json,encode = "json")
   # POST(url = "http://54.67.112.100:8161/n42-services/resources/svc/saveSVCModel?appId=23&serviceId=122&appVersionId=1&serviceVersionId=1",body = json,encode = "json")
    
      
    }else{
      print("Insufficient data in sets after outlier removal.")
    }
  }else{
    "One of load-response metric information missing."
  }
  
}






















# check1<-which(is.na(match(version1.requests$timestamp,version1.responsetime$timestamp))==T)
# { 
#   if(length(check1)>0){
#     version1.requests<-version1.requests[-check1,]
#     version1.responsetime<-version1.responsetime[-check1,]
#   }
#   data1<-data.frame(req=version1.requests$value,res=version1.responsetime$value)
#   
#   bw<-function(inputdata){
#     inputdata<-with(data = inputdata,inputdata[order(req,decreasing = F),])
#     q<-quantile(inputdata$req)
#     ul<-q[4]+(1.5)*(q[4]-q[2])
#     ll<-q[2]-(1.5)*(q[4]-q[2])
#     removeIndexes<-which(inputdata$req>ul | inputdata$req<ll)
#     if(length(removeIndexes)>0){
#       inputdata<-inputdata[-removeIndexes,]
#     }
#     return(inputdata)
#   }
#   
#   data1<-bw(data1)
#   
#   #plot(haproxy.requests$value,haproxy.responsetime$value,type = "p",col="skyblue")
#   forml<-formula(x = "data1$res ~ data1$req")
#   fit1<-rq(formula = forml,tau = c(0.05,0.5,0.95))
#   
#   coeff1<-coef(fit1)
#   
#     
#     p1<-ggplot(data = data1, aes(x = req, y = res))+geom_point()
#     
#     p1<-p1+geom_abline(intercept = coeff1[1,1] ,slope =coeff1[2,1])
#     p1<-p1+geom_abline(intercept = coeff1[1,2] ,slope =coeff1[2,2])  
#     p1<-p1+geom_abline(intercept = coeff1[1,3] ,slope =coeff1[2,3])
# }
# 
# check2<-which(is.na(match(version2.requests$timestamp,version2.responsetime$timestamp))==T)
# {
#   if(length(check2)>0){
#     version2.requests<-version2.requests[-check2,]
#     version2.responsetime<-version2.responsetime[-check2,]
#   }
#   
#   
#   data2<-data.frame(req=version2.requests$value,res=version2.responsetime$value)
#   
#   bw<-function(inputdata){
#     inputdata<-with(data = inputdata,inputdata[order(req,decreasing = F),])
#     q<-quantile(inputdata$req)
#     ul<-q[4]+(1.5)*(q[4]-q[2])
#     ll<-q[2]-(1.5)*(q[4]-q[2])
#     removeIndexes<-which(inputdata$req>ul | inputdata$req<ll)
#     if(length(removeIndexes)>0){
#       inputdata<-inputdata[-removeIndexes,]
#     }
#     return(inputdata)
#   }
#   
#   data2<-bw(data2)
#   
#   #plot(haproxy.requests$value,haproxy.responsetime$value,type = "p",col="skyblue")
#   forml<-formula(x = "data2$res ~ data2$req")
#   fit2<-rq(formula = forml,tau = c(0.05,0.5,0.95))
#   
#   coeff2<-coef(fit2)
#   
#   
#   p2<-ggplot(data = data2, aes(x = req, y = res))+geom_point()
#   
#   p2<-p2+geom_abline(intercept = coeff2[1,1] ,slope =coeff2[2,1])
#   p2<-p2+geom_abline(intercept = coeff2[1,2] ,slope =coeff2[2,2])  
#   p2<-p2+geom_abline(intercept = coeff2[1,3] ,slope =coeff2[2,3])  
# }
# 
# summary1<-data.frame(version="version1",avg=mean(data1$res),var=var(data1$res),stdev=sd(data1$res),med=median(data1$res),max=max(data1$res),min=min(data1$res))
# summary2<-data.frame(version="version2",avg=mean(data2$res),var=var(data2$res),stdev=sd(data2$res),med=median(data2$res),max=max(data2$res),min=min(data2$res))
# 
# require(rjson)
# 
# 
# 
# 
# write.csv(file="summary.csv",x = rbind(summary1,summary2))
# 
# 
# 
