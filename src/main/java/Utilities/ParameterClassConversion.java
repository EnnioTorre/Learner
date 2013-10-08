/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;

/**
 *
 * @author ennio
 */
public class ParameterClassConversion {
    
    static Object ConvertTo(Object param,Object value) throws Exception{
        
        //PARAM
      Double Value=GetaDouble(value);
      
      
      if(param instanceof Param){
          return GetRequiredParamType((Param)param,Value);
      
    } 
      if( ForecastParam.class.isInstance(param)){
          
          //System.out.println("conversion FP"+param+"class"+param.getClass()+value);
          Object v= GetRequiredForecastParamType((ForecastParam)param,Value);
          //System.out.println("conversion FP"+param+"ok");
          return v;
    }
      if(EvaluatedParam.class.isInstance(param)){
         // System.out.println("conversion EP"+param+value);
          Object v= GetRequiredEvaluatedParamType((EvaluatedParam)param,Value);
         // System.out.println("conversion FP"+param+"ok");
          return v;
      
    }
      else
          throw new IllegalArgumentException(param.getClass()+"not valid as Parameter Type");
      
    }

   
   

  
   

        
     

private static Double GetaDouble(Object value){

    String temp=value+"";
    Double v=new Double(temp);
    return v;
}

private static Object GetRequiredParamType(Param param,Double Value) throws Exception{

       switch ((Param)param) {
         case NumNodes:
            return Value.longValue();
         case ReplicationDegree:
            return Value.longValue();
         case AvgPutsPerWrTransaction:
            return Value;
         case AvgPrepareCommandSize:
            return Value.longValue();
         case MemoryInfo_used:
            return Value.longValue();
         case AvgGetsPerROTransaction:
            return Value.longValue();
         case AvgGetsPerWrTransaction:
            return Value.longValue();
         case LocalUpdateTxLocalServiceTime:
            return Value.longValue();
         case LocalUpdateTxPrepareServiceTime:
            return Value.longValue();
         case LocalUpdateTxCommitServiceTime:
            return Value.longValue();
         case LocalUpdateTxLocalRollbackServiceTime:
            return Value.longValue();
         case LocalUpdateTxRemoteRollbackServiceTime:
            return Value.longValue();
         case RemoteGetServiceTime:
            return Value.longValue();
         case GMUClusteredGetCommandServiceTime:
            return Value;
         case RemoteUpdateTxPrepareServiceTime:
            return Value.longValue();
         case RemoteUpdateTxCommitServiceTime:
            return Value.longValue();
         case RemoteUpdateTxRollbackServiceTime:
            return Value.longValue();
         case ReadOnlyTxTotalCpuTime:
            return Value;
         case PercentageSuccessWriteTransactions:
            return Value;
         // parameter added to make this class DAGS compliant
         case PercentageWriteTransactions:
            return Value;
         case AvgLocalGetTime:
            return Value.longValue();
         case LocalUpdateTxPrepareResponseTime:
            return Value.longValue();
         case LocalUpdateTxLocalResponseTime:
            return Value.longValue();
         case AverageWriteTime:
            return Value.longValue();
         //these are not present in csvfile
         case AvgTxArrivalRate:
            return Value;
         case AvgNTCBTime:
            return Value.longValue();
         case NumberOfEntries:
            return Value.intValue();
         case AvgClusteredGetCommandReplySize:
             return Value.longValue();
         default:
            throw new Exception("Param " + param + " is not present");
      }

   }

private static Object GetRequiredForecastParamType(ForecastParam forecastParam,Double Value) throws Exception {
      switch (forecastParam) {
         case ReplicationProtocol:
         {
             if (Value==2)
             return ReplicationProtocol.TWOPC;
             if (Value==1)
             return ReplicationProtocol.PB;
             if (Value==0)
             return ReplicationProtocol.TO;
         
         }
         case ReplicationDegree:
            return Value.longValue();
         case NumNodes:
            return Value.longValue();
         default:
            throw new Exception("Param " + forecastParam + " is not present");
      }
   }

private static Object GetRequiredEvaluatedParamType(EvaluatedParam evaluatedParam,Double Value) throws Exception {
      switch (evaluatedParam) {
         case MAX_ACTIVE_THREADS:
            return Value.intValue();
         case ACF:
            return Value;
         case CORE_PER_CPU:
            return Value.intValue();
         default:
            throw new Exception("Param " + evaluatedParam + " is not present");
      }
   }

}
    

