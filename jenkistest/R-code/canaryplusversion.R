library(rjson)
library(httr)
library(stringr)
#library(quantreg)
options(digits = 8)
#metadata<-fromJSON(file = "/home/tushar/newtest1.json",method = "C")
args = commandArgs(trailingOnly=TRUE)
metadata = fromJSON(args)

#####################################################################################################

format<-function(x){                                  #function to parse metadata
  metricName=sapply(x$metrics,FUN = "[[","metricName")
  metricType=sapply(x$metrics,FUN = "[[","metricType")
  tagsData=sapply(x$metrics,FUN = "[[","tagsData",simplify = F)
  aggregator=sapply(x$metrics,FUN = "[[","aggregator",simplify = F)
  return(mapply(FUN = list,metricName=metricName,metricType=metricType,tagsData=tagsData,aggregator=aggregator,SIMPLIFY = F))
}

getData<-function(x,startTime,endTime){               #function to query database for several metrics
  
  s<-"{\"metrics\": [{\"tags\": {"
  if(length(x$tagsData)>0){
  m<-1
  while(m<=length(x$tagsData)){
    if(m==length(x$tagsData)){
      s<-paste(s,"\"",x$tagsData[[m]]$tagName,"\":[\"",sep="")
      s<-paste(s,x$tagsData[[m]]$tagValue,"\"]",sep="")
    }else{
      s<-paste(s,"\"",x$tagsData[[m]]$tagName,"\":[\"",sep="")
      s<-paste(s,x$tagsData[[m]]$tagValue,"\"],",sep="")
      }
    m=m+1
    }
  }
  #add the condition to check if aggregator exists
  #if(x$aggregator$Function!=""){
  if("aggregator" %in% names(x)){
    #only one aggregator can be used for now
    if(length(x$aggregator)>1){
      if(x$aggregator$Function %in% c("avg","min","max","dev","sum","count")){
      s<-paste(s,"},\"name\": \"",x$metricName,"\",",sep="")
      s<-paste(s,"\"aggregators\":[{\"name\":\"",x$aggregator$Function,"\",\"align_sampling\":true,\"sampling\":{\"value\":\"",
               x$aggregator$time,"\",\"unit\":\"",x$aggregator$unit,"\"}}]",sep="")
      }else if(x$aggregator$Function=="rate"){
        s<-paste(s,"},\"name\": \"",x$metricName,"\",",sep="")
        s<-paste(s,"\"aggregators\":[{\"name\":\"",x$aggregator$Function,"\",\"unit\":\"",x$aggregator$unit,"\"}}]",sep="")
      }
    }else{
      s<-paste(s,"},\"name\": \"",x$metricName,"\"",sep="")
    }
  }else{
    s<-paste(s,"},\"name\": \"",x$metricName,"\"",sep="")
  }
  
  if(endTime=="null"){
    s<-paste(s,"}],\"cache_time\": 0,\"start_absolute\":",startTime,"}",sep="")
  }else{
    s<-paste(s,"}],\"cache_time\": 0,\"start_absolute\":",startTime,
             ",\"end_absolute\":",endTime,"}",sep="")
  }
  print(s)
  
  #################################################################################################
  
  r<-POST(url="http://52.8.104.253:8484/api/v1/datapoints/query",body=s,encode='json')
  print(r)
  if(r$status_code==200){
    d<-rawToChar(r$content)
    e<-fromJSON(d)
    
    if(e$queries[[1]]$sample_size>0){
      xldata<-e$queries[[1]]$results[[1]]$values
      xldata<-sapply(X = xldata,FUN = c)   #new addition
      xldata<-as.data.frame.matrix(t(xldata))   #new addition
      names(xldata)<-c("timestamp","value")     #new addition
      return(list(status_code=r$status,data=xldata))
    }else{
      #return(data.frame())
      #"/opt/AWSmappingFile/Rlog.txt"
      if(file.exists("/home/tushar/Rlog.txt")){
        write(x= paste("Status code:",r$status_code," ",x$metricName, " has zero datapoints.",sep=" "),
              file ="/home/tushar/Rlog.txt" ,ncolumns = 1,sep = "\n")
      }else{
        write(x= paste("Status code:",r$status_code," ",x$metricName, " has zero datapoints.",sep=" "),
              file ="/home/tushar/Rlog.txt" ,ncolumns = 1,sep = "\n",append=T)
      }
      return(list(status_code=r$status,data=data.frame()))
    }
  }else{
    #return(paste("Status code:",r$status_code,"Bad response. Check request.",sep=" "))
    if(file.exists("/home/tushar/Rlog.txt")){
      write(x= paste("Status code:",r$status_code," ",x$metricName, " has zero datapoints.",sep=" "),
            file ="/home/tushar/Rlog.txt" ,ncolumns = 1,sep = "\n")
    }else{
      write(x= paste("Status code:",r$status_code," ",x$metricName, " has zero datapoints.",sep=" "),
            file ="/home/tushar/Rlog.txt" ,ncolumns = 1,sep = "\n",append=T)
    }
    return(list(status_code=r$status,data=data.frame()))
    #return(data.frame())
    
  }
}

characterize<-function(data,breakss){
  #breakss<-pretty(data$load)
  labelss<-sapply(X = seq(1,(length(breakss)-1),1),FUN = function(t)paste("set",t,sep=""))
  sets<-split(x = data,f = cut(x = data$load,breaks = breakss,labels = labelss),drop = F)
  checklength<-sapply(X = sets,FUN = nrow,simplify = T)
  insufficientDataset<-which(checklength<=15)
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
  }else{
    return(list(error="Not enough data in buckets to characterize."))
  }
  
#  prefix<-sapply(names(sets),FUN = function(t)paste("sets$",t,"$",sep=""))
#   createForm<-function(p){
#     term1<-paste(p,"res",sep="")
#     term2<-paste(p,"load",sep="")
#     formula<-paste(term1,"~",term2,sep="")
#     return(formula(x = formula))
#   }
#   forml<-sapply(X = prefix,FUN = createForm)
#   fit<-lapply(forml,FUN = rq,tau=c(0.05,0.95))
#   coeff<-lapply(fit,FUN = coef)
  quant<-lapply(lapply(lapply(sets,"["),"[[","res"),quantile,c(0.05,0.95))
  stats<-lapply(lapply(lapply(sets,"["),"[[","res"),summary)
  setno<-as.numeric(gsub(pattern = "[a-zA-Z*]",replacement = "",x = names(sets)))
  loadRange<-sapply(setno,function(t){paste(breakss[t],"-",breakss[t+1],sep="")})
  sets<-lapply(sets,"[[",2)
  labelss<-sapply(seq(1:length(loadRange)),function(t){paste("bucket",t,sep="")})
  json<-mapply(FUN = list,bucketname=labelss,bucket.load.range=loadRange,bucket.stats=stats,bucket.quantiles=quant,bucket.raw.data=sets,SIMPLIFY = F,USE.NAMES = T)
  return(list(sets=sets,loadRange=loadRange,stats=stats,quant=quant,error="No error",bucketinfo=json))
}



interpolate<-function(load,res){
  freq1<-median(diff(load$timestamp))
  freq2<-median(diff(res$timestamp))
  l<-length(load$timestamp)
  l1<-length(res$timestamp)
  
  if(freq1==freq2){
      start=max(load$timestamp[1],res$timestamp[1])
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq1
  }else if(freq1>freq2){
    if(load$timestamp[1]>res$timestamp[1]){
      start=load$timestamp[1]
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq1
    }else if(load$timestamp[1]<res$timestamp[1]){
      start=res$timestamp[1]
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq1
    }else{
      start=max(load$timestamp[1],res$timestamp[1])
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq1
    }
  }else{
    if(load$timestamp[1]>res$timestamp[1]){
      start=load$timestamp[1]
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq2
    }else if(load$timestamp[1]<res$timestamp[1]){
      start=res$timestamp[1]
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq2
    }else{
      start=max(load$timestamp[1],res$timestamp[1])
      end=min(load$timestamp[l],res$timestamp[l1])
      timediff=freq2
    }
  }
  
  
  start<-max(load$timestamp[1],res$timestamp[1])
  end<-min(load$timestamp[l],res$timestamp[l1])
  #check the difference in successive values in time and accordingly choose the value of by
  if(start>end){
    return(df<-data.frame())
  }else{
    s<-seq(from = start,to = end, by = timediff)
    
    cords<-xy.coords(load$timestamp,load$value)
    load<-as.data.frame(approx(cords$x,cords$y,s,method = "constant"))
    names(load)<-c("timestamp","value")
    
    cordsk<-xy.coords(res$timestamp,res$value)
    res<-as.data.frame(approx(cordsk$x,cordsk$y,s,method = "constant"))
    names(res)<-c("timestamp","value")
    
    return(data.frame(load=load$value,res=res$value))
  }
}


calcpval<-function(x){
  if(length(x[[1]])>9 & length(x[[2]])>9){
    if(sd(x[[1]])>0 & sd(x[[2]])>0){
      return(wilcox.test(x[[1]],x[[2]]))
    }else{
      error<-"Warning: Set has zero standard deviation. Set discarded."
      return(list(score=NA,error=error))
    }
  }else{
    error<-"Warning: Set has insufficient datapoints. Test aborted."
    return(list(score=NA,error=error))
  }
}    

analyse<-function(res1,res2,name,metricType,startTime1,startTime2,endTime1,endTime2){
  
  load1=load.data1
  load2=load.data2
  #data<-doCheck(x,y)
  
  if(res1$status_code!=200 || res2$status_code!=200){
    if(res1$status_code!=200){
      error<-paste("Http response: ",res1$status_code," for ",name,"while querying data. Check metadata or agent health for this metric.",sep="")
    }else{
      error<-paste("Http response: ",res2$status_code," for ",name,"while querying data. Check metadata or agent health for this metric.",sep="")
    }
    json<-list(metricName=name,metricType=metricType,score=NA,error=error)
    return(json)
  }else if(nrow(res1$data)==0 || nrow(res2$data)==0){
    error<-paste("Dataset for ",name," has zero data points. Excluded from canary.",sep="")
    json<-list(metricName=name,metricType=metricType,score=NA,error=error)
    return(json)
  }else{
    
    if(nrow(res1$data)<15 || nrow(res2$data)<15){
      error<-paste("Dataset for ",name," has insufficient datapoints for comparison!!!",sep="")
      json<-list(metricName=name,metricType=metricType,score=NA,error=error)
      return(json)
    }else{
      stats<-lapply(lapply(lapply(list(version1=res1$data,version2=res2$data),"["),"[[",2),summary)
      quant<-lapply(lapply(lapply(list(version1=res1$data,version2=res2$data),"["),"[[",2),quantile,c(0.05,0.95))
      ver1<-interpolate(load1$data,res1$data)
      ver2<-interpolate(load2$data,res2$data)
      if(nrow(ver1)==0 || nrow(ver2)==0){
        error<-paste("Incorrect timestamps: Check starting and ending timestamps for ",name,sep="")
        json<-list(metricName=name,metricType=metricType,score=NA,error=error)
        return(json)
      }else{
        breakss<-pretty(ver1$load)
        charmodel1<-characterize(ver1,breakss)
        charmodel2<-characterize(ver2,breakss)
          
        if(charmodel1$error=="No error" && charmodel2$error=="No error"){
            matchingBuckets<-match(charmodel1$loadRange,charmodel2$loadRange)
            na.index<-which(is.na(matchingBuckets))
            if(length(na.index)>=length(charmodel1$loadRange)){
                error<-"No matching buckets found for comparison."
                json<-list(metricName=name,metricType=metricType,score=NA,error=error)
                return(json)
                #return or error here
            }else{
              if(length(na.index)>0){
                matchingBuckets<-matchingBuckets[-na.index]
              }
            }
            
            
        if(length(matchingBuckets)>0){
              data=mapply(FUN = list,charmodel1$sets,charmodel2$sets[matchingBuckets],SIMPLIFY = F,USE.NAMES = F) 
              w<-lapply(data,calcpval)
              
              length.sets<-sapply(X = lapply(X = w,FUN = "["),FUN = length,USE.NAMES = F)
              error.sets<-which(length.sets<7)
              if(length(error.sets)>0){
                w<-w[-error.sets]
              }
              #charmodel1<-charmodel1[-1]
              #charmodel2<-charmodel2[-1]
              if(length(w)>0){
                #p.values<-sapply(X = sapply(X = lapply(X =w,FUN = "["),FUN = "[",3,USE.NAMES = F),FUN = c,USE.NAMES = F)
                p.values=lapply(lapply(lapply(w,"["),"[","p.value"),c)
                scores<-sapply(X = p.values,function(t){if(t<=0.05){return(0)}else{return(100)}})
                json<-list(metricName=name,metricType=metricType,score=mean(scores),error="No error",
                           metricstats=list(statinfo=stats,quantileinfo=quant),
                           version1.model.char=charmodel1$bucketinfo,
                           version2.model.char=charmodel2$bucketinfo,
                           bucketscores=mapply(list,
                                               sapply(seq(1:length(charmodel1$loadRange)),
                                                      function(t){paste("bucket",t,sep="")}),
                                               scores,USE.NAMES = T))
                return(json)
              }else{
                error="Sets have insufficient datapoints or zero standard deviation."
                json<-list(metricName=name,metricType=metricType,score=NA,error=error)
                return(json)
              }
            }  
        }else{
          #give version wise message for metric.
          error<-paste("Characterization could not be done for ",name,sep="")
          json<-list(metricName=name,metricType=metricType,score=NA,error=error)
          return(json)
        }
      }
    }
  }
}

#############################################################################################################

count=0
while(count<1){
  startTime1<-metadata$service$version1$startTime
  endTime1<-metadata$service$version1$endTime
  startTime2<-metadata$service$version2$startTime
  endTime2<-metadata$service$version2$endTime
  meta1<-format(x = metadata$service$version1)
  meta2<-format(x = metadata$service$version2)
  metricType<-sapply(X = sapply(X = meta1,FUN = "[",simplify = F),FUN = "[[","metricType")
  loadindex<-which(metricType=="load")
  ver1.name<-metadata$service$version1Name
  ver2.name<-metadata$service$version2Name
  
  #assuming unique metric names
  if(length(meta1)>length(meta2)){
    meta1<-meta1[-which(is.na(match(names(meta1),names(meta2))))]
  }else if(length(meta1)<length(meta2)){
    meta2<-meta2[-which(is.na(match(names(meta2),names(meta1))))]
  }else{
    if(length(which(is.na(match(names(meta1),names(meta2)))))>0){
      meta1<-meta1[-which(is.na(match(names(meta1),names(meta2))))]
    }
    if(length(which(is.na(match(names(meta2),names(meta1)))))>0){
      meta2<-meta2[-which(is.na(match(names(meta2),names(meta1))))]
    }
  }
  
  #condition can be added to check if the length of meta is >0 
  
  if(length(meta1)==0 || length(meta2)==0){
    #     results<-toJSON(list(score="NA",error="Issue with metadata. Metadata missing."))
    results<-list(score=NA,error="Issue with metadata. Metadata missing.")
    count=count+1
    break
  }else{
    if(length(loadindex)==0){
      results<-list(score=NA,error="No load metric found. Comparision cannot be done. Check metadata.")
      count=count+1
      break
    }else{
      version1<-lapply(X = lapply(X = meta1,"["),FUN = getData,startTime1,endTime1)
      version2<-lapply(X = lapply(X = meta2,"["),FUN = getData,startTime2,endTime2)
    }
  }
  
  if(length(version1)<1 | length(version2)<1){
    results<-list(score=NA,error="Data could not be downloaded. Check input json or agents.")
    count=count+1
    break
  }
  
  index<-unique(c(which(sapply(sapply(version1,"[[",2,simplify = F),nrow)==0),which(sapply(version1,"[[",1)!=200)))
  if(loadindex %in% index){
    error<-"Data not available for load metric. Comaprision cannot be done."
    results<-list(score=NA,error=error)
    count=count+1
    break
  }else if(length(index)>0){
    version1<-version1[-index]
    version2<-version2[-index]
    metricType<-metricType[-index]
  }
  
  #index<-which(sapply(version2,nrow)==0)
  index<-unique(c(which(sapply(sapply(version2,"[[",2,simplify = F),nrow)==0),which(sapply(version2,"[[",1)!=200)))
  if(loadindex %in% index){
    error<-"Data not available for load metric. Comaprision cannot be done."
    results<-list(score=NA,error=error)
    count=count+1
    break
  }else if(length(index)>0){
    version1<-version1[-index]
    version2<-version2[-index]
    metricType<-metricType[-index]
  }
  
  load.data1<-version1[[loadindex]]
  load.data2<-version2[[loadindex]]
  version1<-version1[-loadindex]
  version2<-version2[-loadindex]
  metricType<-metricType[-loadindex]
  
  if(nrow(load.data1$data)<15 || nrow(load.data2$data)<15){
    error<-paste("Dataset for loadmetric has insufficient datapoints for comparison!!!",sep="")
    results<-list(score=NA,error=error)
    count=count+1
    break
  }
  
  if(length(version1)==0 | length(version2)==0){
    error<-"None of the metrics has greater than zero records or data could not be downloaded.
    Comparison could not be done!!!"
    results<-list(score=NA,error=error)
    count=count+1
    break
  }else{
    if(length(version1)!=length(version2)){ #check if length of list of metrics of both version is equal
      if(length(version1)>length(version2)){
        metric.not.found<-which(is.na(match(names(version1),names(version2))))
        version1<-version1[-metric.not.found]
        metricType<-metricType[-metric.not.found]
      }else{
        metric.not.found<-which(is.na(match(names(version2),names(version1))))
        version2<-version2[-metric.not.found]
        metricType<-metricType[-metric.not.found]
      }
      
      if(file.exists("/home/tushar/Rlog.txt")){
        write(x= "Base version and Canary version do not have same number of metrics. Uncommon metrics removed.",
              file ="/home/tushar/Rlog.txt" ,ncolumns = 1,sep = "\n")
      }else{
        write(x= "Base version and Canary version do not have same number of metrics. Uncommon metrics removed.",
              file ="/home/tushar/Rlog.txt" ,ncolumns = 1,sep = "\n",append=T)
      }
      
      
      #check if metrics with data have count atleast =1
      matchingMetrics<-match(x = names(version1),table = names(version2))
      results<-mapply(FUN = analyse,version1,version2[order(matchingMetrics)],names(version1),metricType,startTime1,startTime2,endTime1,endTime2,SIMPLIFY = F)
      print(results)
    }else{                                  #length of metrics are equal
      matchingMetrics<-match(x = names(version1),table = names(version2))
#               for(i in 1:length(version1)){
#                 results<-analyse(version1[[i]],version2[[i]],names(version1)[i],metricType[i],load.data1,load.data2,startTime1,startTime2,endTime1,endTime2)
#               }
      results<-mapply(FUN = analyse,version1,version2[order(matchingMetrics)],names(version1),metricType,startTime1,startTime2,endTime1,endTime2,SIMPLIFY = F)
      count=count+1
      print(results)
    }
  }
  }  

# output<-list(canary_output=list(comparisionId=metadata$service$comparisionID,serviceName=metadata$service$serviceName,
#                                 startTime=metadata$application$startTime,endTime=metadata$application$endTime,
#                                 comparisionScore=mean(sapply(results,"[[",3,simplify = T),na.rm=T),results=results))
output<-list(comparison_output=list(serviceName=metadata$service$serviceName,version1name=ver1.name,
                                    version2name=ver2.name,comparisionScore=mean(sapply(results,"[[",3,simplify = T),na.rm=T),
                                    results=results))

output<-toJSON(output)
print(output)

print("DB saving -------------------------------------------------------------------------888888888888888888888888888888")
POST(url = "http://localhost:8080/n42-cas-services/resources/svc/saveSVCResults",body=output,encode = "json")
print("DB saving ")

#tryCatch(p<-POST(url = "http://localhost:8080/n42-cas-services/resources/svc/saveSVCResults",
#                 body=output,encode = "json"),warning=function(w){print(w)},error=function(e){print(e)})
#if(p$status_code==200){
#  print("DB update successfully!!!")
#}else{
#  print(paste("DB saving request response code: ",p$status_code,sep=""))
#}

