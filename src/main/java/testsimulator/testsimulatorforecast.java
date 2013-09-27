/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package testsimulator;

/**
 *
 * @author etorre
 */


import eu.cloudtm.autonomicManager.simulator.SimulatorOracle;
import csv.CsvInputOracle;

import tasOracle.TasOracle;
import Utilities.DataSetCreator;
import Utilities.NewClass;

import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import morphr.MorphR;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * @author Sebastiano Peluso
 */
public final class testsimulatorforecast {
static String path="csvfile/new/infinispan4_cloudtm_4.csv";


 static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   

   public static void main(String[] args) throws Exception{
    PropertyConfigurator.configure("conf/log4jLearner.properties");
      
      logger.info("primo append");
     
          NewClass z=new NewClass();
          z.CsvLoader(path);
          DataSetCreator i=new DataSetCreator("csvfile");
         
         SimulatorOracle simulatorOracle = new SimulatorOracle();
         CsvInputOracle input=new CsvInputOracle(path);
         TasOracle t = new TasOracle();
         MorphR morphr = new MorphR();

         OutputOracle o = morphr.forecast(input);

         System.out.println(o.responseTime(1));
         System.out.println(t.forecast(input).responseTime(1));
         System.out.println(simulatorOracle.forecast(input).responseTime(1));
         
     
   }




}

