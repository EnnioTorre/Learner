package csv;

import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import static eu.cloudtm.autonomicManager.commons.EvaluatedParam.ACF;
import static eu.cloudtm.autonomicManager.commons.EvaluatedParam.CORE_PER_CPU;
import static eu.cloudtm.autonomicManager.commons.EvaluatedParam.MAX_ACTIVE_THREADS;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import static eu.cloudtm.autonomicManager.commons.ForecastParam.NumNodes;
import static eu.cloudtm.autonomicManager.commons.ForecastParam.ReplicationDegree;
import eu.cloudtm.autonomicManager.commons.Param;
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
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import parse.radargun.Ispn5_2CsvParser;


import java.io.File;
import java.io.IOException;

/**
 * @author Diego Didona, didona@gsd.inesc-id.pt Date: 27/08/13
 */
public class CsvInputOracle implements InputOracle {

   Ispn5_2CsvParser csvParser;

   public CsvInputOracle(Ispn5_2CsvParser csvParser) {
      this.csvParser = csvParser;
   }

   public CsvInputOracle(String path) {
      try {
         this.csvParser = new Ispn5_2CsvParser(path);
      } catch (IOException e) {
         throw new IllegalArgumentException("Path " + path + " is nonexistent");
      }
   }

   public CsvInputOracle(File f) {
      try {
         this.csvParser = new Ispn5_2CsvParser(f.getAbsolutePath());
      } catch (IOException e) {
         throw new IllegalArgumentException("Path " + f.getAbsolutePath() + " is nonexistent");
      }
   }

   @Override
   public Object getParam(Param param) {
      switch (param) {
         case NumNodes:
            return (long)numNodes();
         case ReplicationDegree:
            return (long)replicationDegree();
         case AvgPutsPerWrTransaction:
            return putsPerWrXact();
         case AvgPrepareCommandSize:
            return (long)prepareCommandSize();
         case MemoryInfo_used:
            return (long)memory();
         case AvgGetsPerROTransaction:
            return (long)getsPerRoXact();
         case AvgGetsPerWrTransaction:
            return (long)getsPerWrXact();
         case LocalUpdateTxLocalServiceTime:
            return (long)localUpdateTxLocalServiceTime();
         case LocalUpdateTxPrepareServiceTime:
            return (long)localUpdateTxPrepareServiceTime();
         case LocalUpdateTxCommitServiceTime:
            return (long)localUpdateTxCommitServiceTime();
         case LocalUpdateTxLocalRollbackServiceTime:
            return (long)localUpdateTxLocalRollbackServiceTime();
         case LocalUpdateTxRemoteRollbackServiceTime:
            return (long)localUpdateTxRemoteRollbackServiceTime();
         case RemoteGetServiceTime:
            return (long)remoteGetServiceTime();
         case GMUClusteredGetCommandServiceTime:
            return gmuClusterGetCommandServiceTime();
         case RemoteUpdateTxPrepareServiceTime:
            return (long)remoteUpdateTxPrepareServiceTime();
         case RemoteUpdateTxCommitServiceTime:
            return (long)remoteUpdateTxCommitServiceTime();
         case RemoteUpdateTxRollbackServiceTime:
            return (long)remoteUpateTxRollbackServiceTime();
         case ReadOnlyTxTotalCpuTime:
            return localReadOnlyTxTotalCpuTime();
         case PercentageSuccessWriteTransactions:
            return writePercentage();
         // parameter added to make this class DAGS compliant
         case PercentageWriteTransactions:
            return writePercentage();
         case AvgLocalGetTime:
            return (long)AvgLocalGetTime();
         case LocalUpdateTxPrepareResponseTime:
            return (long)LocalUpdateTxPrepareResponseTime();
         case LocalUpdateTxLocalResponseTime:
            return (long)LocalUpdateTxLocalResponseTime();
         case AverageWriteTime:
            return (long)AverageWriteTime();
         //these are not present in csvfile
         case AvgTxArrivalRate:
            return AvgTxArrivalRate();
         case AvgNTCBTime:
            return (long)AvgNTCBTime();
         case NumberOfEntries:
            return (int)numberOfEntries();
         case AvgClusteredGetCommandReplySize:
             return (long)AvgClusteredGetCommandReplySize();
         default:
            throw new IllegalArgumentException("Param " + param + " is not present");
      }

   }

   @Override
   public Object getEvaluatedParam(EvaluatedParam evaluatedParam) {
      switch (evaluatedParam) {
         case MAX_ACTIVE_THREADS:
            return (int)numThreadsPerNode();
         case ACF:
            return acf();
         case CORE_PER_CPU:
            return (int)cpus();
         default:
            throw new IllegalArgumentException("Param " + evaluatedParam + " is not present");
      }
   }

   @Override
   public Object getForecastParam(ForecastParam forecastParam) {
      switch (forecastParam) {
         case ReplicationProtocol:
            return replicationProtocol();
         case ReplicationDegree:
            return (long)replicationDegree();
         case NumNodes:
            return (long)numNodes();
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

   private ReplicationProtocol replicationProtocol() {
       System.out.println(csvParser.getReplicationProtocol());
       if(csvParser.getReplicationProtocol().equals("2PC"))
        return ReplicationProtocol.TWOPC;  
          
      return ReplicationProtocol.valueOf(csvParser.getReplicationProtocol());
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

}

