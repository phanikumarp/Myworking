require(rjson)
require(stringr)
require(httr)
args = commandArgs(trailingOnly=TRUE)
print("inside comp")
print(args)
print("hello")
args[1]<-gsub(pattern = "\"",replacement="",x=args[1],fixed=T)
args[1]<-gsub(pattern = "\\",replacement="\"",x=args[1],fixed=T)
args[2]<-gsub(pattern = "\"",replacement="",x=args[2],fixed=T)
args[2]<-gsub(pattern = "\\",replacement="\"",x=args[2],fixed=T)
svcId1<-args[3]
svcId2<-args[4]
model1<-fromJSON(json_str=args[1],method = "C")
model2<-fromJSON(json_str=args[2],method = "C")
print(model1)
print(model2)
print("helloo")
print(model1$models$service==model2$models$service)
if(model1$models$service==model2$models$service){
  output<-paste("{\"bucketComparison\":{\"processID\":\"\",\"appID\":\"\",\"serviceID\":\"\",",sep="")
 if(model1$models$loadMetric==model2$models$loadMetric){
   if(model1$models$responseMetric==model2$models$responseMetric){
     
     output<-paste(output,"\"versionID\":[{\"version1\":\"\",\"version2\":\"\"}],",sep="")
     
    #model1$models$buckets
     
    lowLoad1<-as.numeric(str_split(model1$models$loadRange,pattern = "-")[[1]][1])
    upLoad1<-as.numeric(str_split(model1$models$loadRange,pattern = "-")[[1]][2])
     
    lowLoad2<-as.numeric(str_split(model2$models$loadRange,pattern = "-")[[1]][1])
    upLoad2<-as.numeric(str_split(model2$models$loadRange,pattern = "-")[[1]][2])
     
    buckets1<- sapply(model1$models$buckets,FUN = "[[",2)
    buckets2<- sapply(model2$models$buckets,FUN = "[[",2)
    
    matchingBuckets<-match(buckets1,buckets2)
    print(length(which(is.na(matchingBuckets))))
    print(length(buckets1))
    if(length(which(is.na(matchingBuckets)))<length(buckets1)){
      comparisonResult<-list()
      count=0
      output<-paste(output,"\"errorStatus\":[{\"error\":\"0\",\"errorString\":\"successful\"}],",sep="")
      output<-paste(output,"\"buckets\":[",sep="")
      for(i in 1:length(matchingBuckets)){
        if(!is.na(matchingBuckets[i])){
          count<-count+1
          lowerLoad<-as.numeric(str_split(string = model1$models$buckets[[i]]$load_range,pattern = "-")[[1]][1])
          upperLoad<-as.numeric(str_split(string = model1$models$buckets[[i]]$load_range,pattern = "-")[[1]][2])
          
          
          calcResponseRange<-function(x,upLoad,lowLoad){
            upBound<-x$Intercept+(x$load*upLoad)
            lowBound<-x$Intercept+(x$load*lowLoad)
            return(data.frame(tau = x$tau, upBound = upBound,lowBound = lowBound))
          }
          
          model1Response<-lapply(X = model1$models$buckets[[i]]$coefficients,calcResponseRange,upperLoad,lowerLoad)
          model1Response<-c(model1Response,model1$models$buckets[[i]]$stats)
          model2Response<-lapply(X = model2$models$buckets[[matchingBuckets[i]]]$coefficients,calcResponseRange,upperLoad,lowerLoad)
          model2Response<-c(model2Response,model2$models$buckets[[matchingBuckets[i]]]$stats)
          
          threshold<-((abs(model1$models$buckets[[i]]$stats[[1]]$average-
                          model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$average)/
                         (model1$models$buckets[[i]]$stats[[1]]$average+
                          model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$average))+
                     (abs(model1$models$buckets[[i]]$stats[[1]]$variance-
                          model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$variance)/
                         (model1$models$buckets[[i]]$stats[[1]]$variance+
                          model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$variance)))/2
        
          
        if(threshold>0.3){
          versionDifferent = "TRUE"
          if(model1$models$buckets[[3]]$stats[[1]]$average>model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$average){
            betterVersion = "version2"
            colorCode = 2
          }else{
            betterVersion = "version1"
            colorCode = 1
          }
        }else{
          versionDifferent = "FALSE"
          betterVersion = "none"
          colorCode = 0
        }  
        
        
        
        
        if(length(which(!is.na(matchingBuckets)))==1){
          buckets<-paste("{\"bucketID\":\"bucket",count,"\",",sep = "")
          buckets<-paste(buckets,"\"loadRange\":\"",lowerLoad,"-",upperLoad,"\",",sep = "")
          buckets<-paste(buckets,"\"stats\":[{\"version\":\"version1\",",sep="")
          buckets<-paste(buckets,"\"average\":",model1$models$buckets[[i]]$stats[[1]]$average,sep="")
          buckets<-paste(buckets,",\"std_dev\":",model1$models$buckets[[i]]$stats[[1]]$std_dev,sep="")
          buckets<-paste(buckets,",\"median\":",model1$models$buckets[[i]]$stats[[1]]$median,sep="")
          buckets<-paste(buckets,",\"variance\":",model1$models$buckets[[i]]$stats[[1]]$variance,"},",sep="")
          
          buckets<-paste(buckets,"{\"version\":\"version2\",",sep="")
          buckets<-paste(buckets,"\"average\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$average,sep="")
          buckets<-paste(buckets,",\"std_dev\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$std_dev,sep="")
          buckets<-paste(buckets,",\"median\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$median,sep="")
          buckets<-paste(buckets,",\"variance\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$variance,"}],",sep="")
          
          buckets<-paste(buckets,"\"versionDifferent\":\"",versionDifferent,"\",",sep="")
          buckets<-paste(buckets,"\"comparisonResult\":[{\"colorCode\":",colorCode,",",sep="")
          buckets<-paste(buckets,"\"betterVersion\":\"",betterVersion,"\"}]}",sep="")
        }else{
          if(count==1){
            buckets<-paste("{\"bucketID\":\"bucket",count,"\",",sep = "")
            buckets<-paste(buckets,"\"loadRange\":\"",lowerLoad,"-",upperLoad,"\",",sep = "")
            buckets<-paste(buckets,"\"stats\":[{\"version\":\"version1\",",sep="")
            buckets<-paste(buckets,"\"average\":",model1$models$buckets[[i]]$stats[[1]]$average,sep="")
            buckets<-paste(buckets,",\"std_dev\":",model1$models$buckets[[i]]$stats[[1]]$std_dev,sep="")
            buckets<-paste(buckets,",\"median\":",model1$models$buckets[[i]]$stats[[1]]$median,sep="")
            buckets<-paste(buckets,",\"variance\":",model1$models$buckets[[i]]$stats[[1]]$variance,"},",sep="")
            
            buckets<-paste(buckets,"{\"version\":\"version2\",",sep="")
            buckets<-paste(buckets,"\"average\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$average,sep="")
            buckets<-paste(buckets,",\"std_dev\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$std_dev,sep="")
            buckets<-paste(buckets,",\"median\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$median,sep="")
            buckets<-paste(buckets,",\"variance\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$variance,"}],",sep="")
            
            buckets<-paste(buckets,"\"versionDifferent\":\"",versionDifferent,"\",",sep="")
            buckets<-paste(buckets,"\"comparisonResult\":[{\"colorCode\":",colorCode,",",sep="")
            buckets<-paste(buckets,"\"betterVersion\":\"",betterVersion,"\"}]}",sep="")
          }else{
            buckets<-paste(buckets,",{\"bucketID\":\"bucket",count,"\",",sep = "")
            buckets<-paste(buckets,"\"loadRange\":\"",lowerLoad,"-",upperLoad,"\",",sep = "")
            buckets<-paste(buckets,"\"stats\":[{\"version\":\"version1\",",sep="")
            buckets<-paste(buckets,"\"average\":",model1$models$buckets[[i]]$stats[[1]]$average,sep="")
            buckets<-paste(buckets,",\"std_dev\":",model1$models$buckets[[i]]$stats[[1]]$std_dev,sep="")
            buckets<-paste(buckets,",\"median\":",model1$models$buckets[[i]]$stats[[1]]$median,sep="")
            buckets<-paste(buckets,",\"variance\":",model1$models$buckets[[i]]$stats[[1]]$variance,"},",sep="")
            
            buckets<-paste(buckets,"{\"version\":\"version2\",",sep="")
            buckets<-paste(buckets,"\"average\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$average,sep="")
            buckets<-paste(buckets,",\"std_dev\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$std_dev,sep="")
            buckets<-paste(buckets,",\"median\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$median,sep="")
            buckets<-paste(buckets,",\"variance\":",model2$models$buckets[[matchingBuckets[i]]]$stats[[1]]$variance,"}],",sep="")
            
            buckets<-paste(buckets,"\"versionDifferent\":\"",versionDifferent,"\",",sep="")
            buckets<-paste(buckets,"\"comparisonResult\":[{\"colorCode\":",colorCode,",",sep="")
            buckets<-paste(buckets,"\"betterVersion\":\"",betterVersion,"\"}]}",sep="")
          }
        }
        }
	}
	output<-paste(output,buckets,"]}}",sep="")
	print("comparison done!!")
	print(output)
	#p<-POST(url = "http://54.67.112.100:8161/n42-services/resources/svc/saveSVCComparatorAnalyzedModel?sVCId1=1&sVCId2=2",body=output,encode = "json")
	p<-POST(url = paste("http://54.67.112.100:8161/n42-services/resources/svc/saveSVCComparatorAnalyzedModel?sVCId1=",
svcId1,"&sVCId2=",svcId2,sep=""),body=output,encode = "json")


    }else{
      
      print("No Buckets to compare")
      output<-paste(output,"\"errorStatus\":[{\"error\":\"1\",\"errorString\":\"Comaprison Unsuccessful. No common buckets found!!! \"}],",sep="")
      output<-paste(output,"\"buckets\":[]}}",sep="")
	p<-POST(url = paste("http://54.67.112.100:8161/n42-services/resources/svc/saveSVCComparatorAnalyzedModel?sVCId1=",
svcId1,"&sVCId2=",svcId2,sep=""),body=output,encode = "json")
    }
    
    #commonLower<-max(lowLoad1,lowLoad2)
    #commonUpper<-min(upLoad1,upLoad2)
      
   }
 } 
}
