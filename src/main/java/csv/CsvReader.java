/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package csv;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import static eu.cloudtm.autonomicManager.commons.EvaluatedParam.ACF;
import static eu.cloudtm.autonomicManager.commons.EvaluatedParam.CORE_PER_CPU;
import static eu.cloudtm.autonomicManager.commons.EvaluatedParam.MAX_ACTIVE_THREADS;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import static eu.cloudtm.autonomicManager.commons.ForecastParam.NumNodes;
import static eu.cloudtm.autonomicManager.commons.ForecastParam.ReplicationDegree;
import static eu.cloudtm.autonomicManager.commons.Param.AverageWriteTime;
import static eu.cloudtm.autonomicManager.commons.Param.AvgClusteredGetCommandReplySize;
import static eu.cloudtm.autonomicManager.commons.Param.AvgGetsPerROTransaction;
import static eu.cloudtm.autonomicManager.commons.Param.AvgGetsPerWrTransaction;
import static eu.cloudtm.autonomicManager.commons.Param.AvgLocalGetTime;
import static eu.cloudtm.autonomicManager.commons.Param.AvgNTCBTime;
import static eu.cloudtm.autonomicManager.commons.Param.AvgPrepareCommandSize;
import static eu.cloudtm.autonomicManager.commons.Param.AvgPutsPerWrTransaction;
import static eu.cloudtm.autonomicManager.commons.Param.AvgTxArrivalRate;
import static eu.cloudtm.autonomicManager.commons.Param.GMUClusteredGetCommandServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxCommitServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxLocalResponseTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxLocalRollbackServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxLocalServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxPrepareResponseTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxPrepareServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.LocalUpdateTxRemoteRollbackServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.MemoryInfo_used;
import static eu.cloudtm.autonomicManager.commons.Param.NumberOfEntries;
import static eu.cloudtm.autonomicManager.commons.Param.PercentageSuccessWriteTransactions;
import static eu.cloudtm.autonomicManager.commons.Param.PercentageWriteTransactions;
import static eu.cloudtm.autonomicManager.commons.Param.ReadOnlyTxTotalCpuTime;
import static eu.cloudtm.autonomicManager.commons.Param.RemoteGetServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.RemoteUpdateTxCommitServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.RemoteUpdateTxPrepareServiceTime;
import static eu.cloudtm.autonomicManager.commons.Param.RemoteUpdateTxRollbackServiceTime;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import java.io.File;
import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import parse.radargun.Ispn5_2CsvParser;
import testsimulator.testsimulatorforecast;
/**
 *
 * @author etorre
 */
public class CsvReader {
    public Ispn5_2CsvParser csvParser;
    static Logger logger = Logger.getLogger(testsimulatorforecast.class.getName());   
     
   
  
    
    public CsvReader(String path) {
      try {
         PropertyConfigurator.configure("conf/log4jLearner.properties"); 
         this.csvParser = new Ispn5_2CsvParser(path);
      } catch (IOException e) {
         throw new IllegalArgumentException("Path " + path + " is nonexistent");
      }
   }
    
     public CsvReader(File f) {
      try {
          PropertyConfigurator.configure("conf/log4jLearner.properties");
         this.csvParser = new Ispn5_2CsvParser(f.getAbsolutePath());
      } catch (IOException e) {
         throw new IllegalArgumentException("Path " + f.getAbsolutePath() + " is nonexistent");
      }
   }
    
     
   public Double getParam(Param param) {
         switch (param) {
         case NumNodes:
            return  numNodes();
         case ReplicationDegree:
            return  replicationDegree();
         case AvgPutsPerWrTransaction:
            return putsPerWrXact();
         case AvgPrepareCommandSize:
            return  prepareCommandSize();
         case MemoryInfo_used:
            return  memory();
         case AvgGetsPerROTransaction:
            return  getsPerRoXact();
         case AvgGetsPerWrTransaction:
            return  getsPerWrXact();
         case LocalUpdateTxLocalServiceTime:
            return  localUpdateTxLocalServiceTime();
         case LocalUpdateTxPrepareServiceTime:
            return  localUpdateTxPrepareServiceTime();
         case LocalUpdateTxCommitServiceTime:
            return  localUpdateTxCommitServiceTime();
         case LocalUpdateTxLocalRollbackServiceTime:
            return  localUpdateTxLocalRollbackServiceTime();
         case LocalUpdateTxRemoteRollbackServiceTime:
            return  localUpdateTxRemoteRollbackServiceTime();
         case RemoteGetServiceTime:
            return  remoteGetServiceTime();
         case GMUClusteredGetCommandServiceTime:
            return gmuClusterGetCommandServiceTime();
         case RemoteUpdateTxPrepareServiceTime:
            return  remoteUpdateTxPrepareServiceTime();
         case RemoteUpdateTxCommitServiceTime:
            return  remoteUpdateTxCommitServiceTime();
         case RemoteUpdateTxRollbackServiceTime:
            return  remoteUpateTxRollbackServiceTime();
         case ReadOnlyTxTotalCpuTime:
            return localReadOnlyTxTotalCpuTime();
         case PercentageSuccessWriteTransactions:
            return writePercentage();
         // parameter added to make this class DAGS compliant
         case PercentageWriteTransactions:
            return writePercentage();
         case AvgLocalGetTime:
            return  AvgLocalGetTime();
         case LocalUpdateTxPrepareResponseTime:
            return  LocalUpdateTxPrepareResponseTime();
         case LocalUpdateTxLocalResponseTime:
            return  LocalUpdateTxLocalResponseTime();
         case AverageWriteTime:
            return  AverageWriteTime();
         //these are not present in csvfile
         case AvgTxArrivalRate:
            return AvgTxArrivalRate();
         case AvgNTCBTime:
            return  AvgNTCBTime();
         case NumberOfEntries:
            return   numberOfEntries();
         case AvgClusteredGetCommandReplySize:
             return  AvgClusteredGetCommandReplySize();
         default:
         {logger.warn("Param " + param + " is not present ");
             throw new IllegalArgumentException("Param " + param + " is not present");
         }
      }

   }

  
   public double getEvaluatedParam(EvaluatedParam evaluatedParam) {
      switch (evaluatedParam) {
         case MAX_ACTIVE_THREADS:
            return   numThreadsPerNode();
         case ACF:
            return acf();
         case CORE_PER_CPU:
            return   cpus();
         default:
            throw new IllegalArgumentException("Param " + evaluatedParam + " is not present");
      }
   }
   
   public double getForecastParam(ForecastParam forecastParam) {
      switch (forecastParam) {
         case ReplicationProtocol:
            return replicationProtocol();
         case ReplicationDegree:
            return replicationDegree();
         case NumNodes:
            return numNodes();
         default:
            throw new IllegalArgumentException("Param " + forecastParam + " is not present");
      }
   }

  
   

   /**
    * AD HOC METHODS *
    */

   private double numNodes() {
       
      return csvParser.getNumNodes();
   }

   private double replicationDegree() {
      return csvParser.replicationDegree();
   }

   private double putsPerWrXact() {
      return csvParser.putsPerWrXact();
   }

   private double numThreadsPerNode() {
      return csvParser.numThreads();
   }

   private double prepareCommandSize() {
      return csvParser.sizePrepareMsg();
   }

   private double acf() {
      return 1.0D / csvParser.numKeys();
   }

   private double memory() {
      return 1e-6 * csvParser.mem();
   }

   private double cpus() {
      return 2;
   }

   private double replicationProtocol() {
      
       
        return 2;  
          
      
              }

   private double getsPerRoXact() {
      return csvParser.readsPerROXact();
   }

   private double getsPerWrXact() {
      return csvParser.readsPerWrXact();
   }

   private double localUpdateTxLocalServiceTime() {
      return csvParser.localServiceTimeWrXact();
   }

   private double localUpdateTxPrepareServiceTime() {
      return csvParser.getAvgParam("LocalUpdateTxPrepareServiceTime");
   }

   private double localUpdateTxCommitServiceTime() {
      return csvParser.getAvgParam("LocalUpdateTxCommitServiceTime");
   }

   private double localUpdateTxLocalRollbackServiceTime() {
      return csvParser.getAvgParam("LocalUpdateTxLocalRollbackServiceTime");
   }

   private double localUpdateTxRemoteRollbackServiceTime() {
      return csvParser.getAvgParam("LocalUpdateTxRemoteRollbackServiceTime");
   }

   private double localReadOnlyTxTotalCpuTime() {
      return csvParser.localServiceTimeROXact();
   }

   private double remoteGetServiceTime() {
      return csvParser.localRemoteGetServiceTime();
   }

   private double remoteUpdateTxPrepareServiceTime() {
      return csvParser.remotePrepareServiceTime();
   }

   private double remoteUpdateTxCommitServiceTime() {
      return csvParser.remoteCommitCommandServiceTime();
   }

   private double remoteUpateTxRollbackServiceTime() {
      return csvParser.remoteRollbackServiceTime();
   }

   private double gmuClusterGetCommandServiceTime() {
      return csvParser.remoteRemoteGetServiceTime();
   }

   private double writePercentage() {
      return csvParser.writePercentageXact();
   }

   private double AvgTxArrivalRate() {
      return csvParser.usecThroughput() * csvParser.getNumNodes();
   }

   private double AvgNTCBTime() {
      return 0D;
   }


   private double AvgLocalGetTime() {
      return csvParser.getAvgParam("AvgLocalGetTime");
   }

   private double LocalUpdateTxPrepareResponseTime() {
      return csvParser.getAvgParam("LocalUpdateTxPrepareResponseTime");
   }

   private double LocalUpdateTxLocalResponseTime() {
      return csvParser.getAvgParam("LocalUpdateTxLocalResponseTime");
   }

   private double AverageWriteTime() {
      return csvParser.getAvgParam("LocalUpdateTxLocalServiceTime");//adapted
   }

   private double numberOfEntries() {
      return csvParser.numKeys();
   }
   
   private double AvgClusteredGetCommandReplySize(){
   
       return csvParser.getAvgParam("AvgClusteredGetCommandReplySize");
   }
   
   public double ThroughputRO (){
   
       return csvParser.usecThroughput()*csvParser.numReadXact();
   }
   
   public double ThroughputWR (){
   
       return csvParser.usecThroughput()*csvParser.numWriteXact();
   }
   
   public double ResponseTimeRO (){
   
       return csvParser.localResponseTimeROXact();
   }
   
   public double ResponseTimeWR (){
   
       return ((numThreadsPerNode() * numNodes())/csvParser.usecThroughput()-((1-csvParser.writePercentageXact()))/csvParser.localResponseTimeROXact());
   }
   
   public double AbortRateRO (){
   
       return 0D;
   }
   
   public double AbortRateWR (){
   
       return csvParser.numAborts();
   }
   

}



