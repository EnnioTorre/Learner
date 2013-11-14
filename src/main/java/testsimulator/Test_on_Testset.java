/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package testsimulator;

import CsvOracles.params.CsvRgParams;
import Learners.Knearestneighbourg;

import Utilities.LearnerConfiguration;
import csv.CsvReader;
import csv.PrintDataOnCsv;

import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import java.io.File;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class Test_on_Testset {
    
    
    
public void test(String Directory_path) throws ClassNotFoundException, InstantiationException, IllegalAccessException, Exception{
        Oracle oracles[]=new Oracle[LearnerConfiguration.getInstance().getOracles().length+1];
        
        int NumOracles=0;
        
        for (Class c:LearnerConfiguration.getInstance().getOracles()){
                  oracles[NumOracles++]=(Oracle)c.newInstance();
                   System.out.println(c);
                 }
        oracles[NumOracles]=new Knearestneighbourg("EuclideanDistance","",10,"throughput");
        
        OutputOracle result;
        CsvReader Input;
        String OutputOracles="";
        File dir = new File(Directory_path);
      for (File nextdir : dir.listFiles()) {
         if (nextdir.isDirectory()) {
            for (File csv : nextdir.listFiles()) {
               if (!csv(csv)) {
                  continue;
                  
               }
               else{
                   
                    Input=new CsvReader(new CsvRgParams(csv.getPath()));
                    
                    OutputOracles="csvPath,"+csv.getAbsolutePath()+",";
                   for (Oracle o:oracles){
                      result=o.forecast(Input);
                      OutputOracles=OutputOracles+","+OutputOracleToString(result,o)+",";
                      
                               }
                   OutputOracles=OutputOracles+","+OutputOracleToString(Input)+",";
                  
               }
             PrintDataOnCsv.PrintStringOnCsvFile("csv_testset.csv", OutputOracles);  
            }
            
            
         }
      }
      

    }
    
    
    public String OutputOracleToString(OutputOracle o,Oracle or){
               
        return or.toString().split("@")[0]+"throughputRO,"+o.throughput(0)+","+
               or.toString().split("@")[0]+"throughputWO,"+o.throughput(1)+","+
               or.toString().split("@")[0]+"abortRateRO,"+o.abortRate(0)+","+
               or.toString().split("@")[0]+"abortRateWO,"+o.abortRate(1)+","+
               or.toString().split("@")[0]+"responseTimeRO,"+o.responseTime(0)+","+
               or.toString().split("@")[0]+"responseTimeWO,"+o.responseTime(1);
              
    }
    
    public String OutputOracleToString(CsvReader o){
               
        return "throughputRO,"+o.throughput(0)+","+
               "throughputWO,"+o.throughput(1)+","+
               "abortRateRO,"+o.abortRate(0)+","+
               "abortRateWO,"+o.abortRate(1)+","+
               "responseTimeRO,"+o.responseTime(0)+","+
               "responseTimeWO,"+o.responseTime(1);
              
    }

 public boolean csv(File f) {
      
      return f.toString().endsWith("csv");
   }
}
