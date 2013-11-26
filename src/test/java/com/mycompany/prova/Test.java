/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.prova;

import CsvOracles.params.CsvRgParams;
import Learners.Knearestneighbourg;
import Utilities.DataSets;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import testsimulator.testsimulatorforecast;
import weka.core.Instance;

/**
 *
 * @author etorre
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    static String path="csvfile/new/0.csv";


 static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   

   public static void main(String[] args) throws Exception{
    PropertyConfigurator.configure("conf/log4j.properties");
      
      logger.info("primo append");
     
          
          DataSets i=new DataSets("csvfile");
          InputOracle csvI =new CsvReader(new CsvRgParams(path));
          
          
       /* Knearestneighbourg kn= new Knearestneighbourg("EuclideanDistance","-D",10,"throughput");
         // OutputOracle result=kn.forecast(csvI);
         // Instance n=kn.getNeighboughood().instance(3);
          System.out.println(n);
          System.out.println(DataSets.InstancesMap.get(n.toStringNoWeight()));
          System.out.println(DataSets.ValidationSet.get(DataSets.InstancesMap.get(n.toStringNoWeight())));
          System.out.println(result.throughput(0)+"\n"+result.throughput(1));
       */  
   }
}
