/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csv;
import CsvOracles.RadargunCsvInputOracle;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;

import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import CsvOracles.params.CsvRgParams;



/**
 *
 * @author etorre
 */
public class CsvReader extends RadargunCsvInputOracle implements InputOracle {
    
    static Logger logger = Logger.getLogger(CsvReader.class.getName());   

    public CsvReader(CsvRgParams param) throws Exception {
        super(param);
    }
     

  
   
   public double throughput (int i){
                
            
       switch(i){
           case 0:       
               return csvParser.readThroughput();
                      
           case 1:
               return csvParser.writeThroughput();
           default:
           {
               logger.warn("Troughput ( " + i + ") is not present");
               throw new IllegalArgumentException("Troughput ( " + i + ") is not present");
   
           }
       }
   }
   
   
   public double responseTime (int i){
       
       switch(i){
           case 0:
               return csvParser.totalResponseTimeROXact();
           case 1:
               return csvParser.totalResponseTimeWrXact();
           default:{
               logger.warn("responseTime ( " + i + ") is not present");
               throw new IllegalArgumentException("responseTime ( " + i + ") is not present");
   
           }
       }
   }
       
   
   
   
   
   public double abortRate (int i){
                
       switch(i){
           case 0:
               return 0D;
           case 1:
               return 1-csvParser.writeXactCommitProbability();
           default:{
               logger.warn("abortRate ( " + i + ") is not present");
               
               throw new IllegalArgumentException("abortRate ( " + i + ") is not present");
   
           }
       }
       
   }
}


   





