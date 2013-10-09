/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testsimulator;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */



import Utilities.DataSets;
import Learners.Knearestneighbourg;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import weka.core.Instance;


public final class testsimulatorforecast {
static String path="csvfile/new/0.csv";


 static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   

   public static void main(String[] args) throws Exception{
    PropertyConfigurator.configure("conf/log4jLearner.properties");
      
      logger.info("primo append");
     
          
          DataSets i=new DataSets("csvfile");
          InputOracle csvI =new CsvReader(path);
          
          
          Knearestneighbourg kn= new Knearestneighbourg("EuclideanDistance","-D",10,"throughput");
          OutputOracle result=kn.forecast(csvI);
          Instance n=kn.getNeighboughood().instance(3);
          System.out.println(n);
          System.out.println(DataSets.InstancesMap.get(n.toStringNoWeight()));
          System.out.println(DataSets.ValidationSet.get(DataSets.InstancesMap.get(n.toStringNoWeight())));
          System.out.println(result.throughput(0)+"\n"+result.throughput(1));
         
   }




}

