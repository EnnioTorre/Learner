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
import static Utilities.DataSets.predictionResults;
import Utilities.DatasetOutputOracle;
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
import eu.cloudtm.autonomicManager.oracles.Oracle;
import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;


public final class testsimulatorforecast {
static String path="csvfile/new/0.csv";


 static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   

   public static void main(String[] args) {
    PropertyConfigurator.configure("conf/log4j.properties");
      
      logger.info("primo append");
    try {
        DataSets i=new DataSets("csvfile");
        //InputOracle csvI =new CsvReader(new CsvRgParams(path));
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(testsimulatorforecast.class.getName()).log(Level.SEVERE, null, ex);
    }
          
          
          
          
          Test_on_Testset test=new Test_on_Testset();
    try {
        test.test("csvtest");
        
        
        //Knearestneighbourg kn= new Knearestneighbourg("EuclideanDistance","-D",10,"throughput");
        //OutputOracle result=kn.forecast(csvI);
        //Instance n=kn.getNeighboughood().instance(3);
        //System.out.println(kn.getNeighboughood());
        //System.out.println(DataSets.InstancesMap.get(n.toStringNoWeight()));
        //System.out.println(result.throughput(0)+result.toString());
        //System.out.println(result.throughput(0)+result.toString());
    } catch (ClassNotFoundException ex) {
        java.util.logging.Logger.getLogger(testsimulatorforecast.class.getName()).log(Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
        java.util.logging.Logger.getLogger(testsimulatorforecast.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
        java.util.logging.Logger.getLogger(testsimulatorforecast.class.getName()).log(Level.SEVERE, null, ex);
    } catch (Exception ex) {
        java.util.logging.Logger.getLogger(testsimulatorforecast.class.getName()).log(Level.SEVERE, null, ex);
    }
         
   }
   
   
    

}

