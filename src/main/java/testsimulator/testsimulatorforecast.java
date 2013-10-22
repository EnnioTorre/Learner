/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testsimulator;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */



import CsvOracles.params.CsvRgParams;
import Utilities.DataSets;
import Learners.Knearestneighbourg;
import Utilities.LearnerConfiguration;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import weka.core.Instance;
import Utilities.ParameterClassConversion;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;


public final class testsimulatorforecast {
static String path="csvfile/new/0.csv";


 static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   

   public static void main(String[] args) throws Exception{
    PropertyConfigurator.configure("conf/log4j.properties");
      
      logger.info("primo append");
     
      
      try{
      
          LearnerConfiguration LK=new LearnerConfiguration();
          for (Class c:LK.getOracles()){
              System.out.println(c);
          }
          
          System.out.println(LK.getOracleInputDescription());
      }
          
        catch(Exception e){
           e.printStackTrace();
        }
      
          
          DataSets i=new DataSets("csvfile");/*
          InputOracle csvI =new CsvReader(new CsvRgParams(path));
          
          
          
          
          Knearestneighbourg kn= new Knearestneighbourg("EuclideanDistance","-D",10,"throughput");
          OutputOracle result=kn.forecast(csvI);
         //Instance n=kn.getNeighboughood().instance(3);
          System.out.println(kn.getNeighboughood());
          //System.out.println(DataSets.InstancesMap.get(n.toStringNoWeight()));
          //System.out.println(DataSets.ValidationSet.get(DataSets.InstancesMap.get(n.toStringNoWeight())));
          System.out.println(result.throughput(0)+"\n"+result.throughput(1));
         */
   }




}

