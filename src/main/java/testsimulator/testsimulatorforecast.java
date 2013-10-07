/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testsimulator;

/**
 *
 * @author etorre
 */



import Utilities.DataSets;
import Learners.Knearestneighbourg;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import weka.core.Instance;

/**
 * @author Sebastiano Peluso
 */
public final class testsimulatorforecast {
static String path="csvfile/new/infinispan4_cloudtm_4.csv";


 static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   

   public static void main(String[] args) throws Exception{
    PropertyConfigurator.configure("conf/log4jLearner.properties");
      
      logger.info("primo append");
     
          
          DataSets i=new DataSets("csvfile");
          
          Knearestneighbourg kn= new Knearestneighbourg(DataSets.ARFFDataSet,DataSets.ARFFDataSet.instance(5),"EuclideanDistance","-D");
          Instance n=kn.getNeighboughood().instance(3);
          System.out.println(n);
          System.out.println(DataSets.InstancesMap.get(n.toStringNoWeight()));
          System.out.println(DataSets.ValidationSet.get(DataSets.InstancesMap.get(n.toStringNoWeight())));
         
     
   }




}

