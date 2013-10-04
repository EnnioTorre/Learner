/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import eu.cloudtm.autonomicManager.oracles.OutputOracle;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class DatasetOutputOracle implements OutputOracle {

    
    private double ThroughputWR;
    private double ThroughputRO;
    private double AbortRateWR;
    private double AbortRateRO;
    private double ResponseTimeWR;
    private double ResponseTimeRO;
    
    
    @Override
    public double throughput(int i) {
        switch(i){
        
            case 0:
                return this.ThroughputRO;
            case 1:
                return this.ThroughputWR;
            default:
                return -1D;
                
        }
    }

    @Override
    public double abortRate(int i) {
        switch(i){
        
            case 0:
                return this.AbortRateRO;
            case 1:
                return this.AbortRateWR;
            default:
                return -1D;
                
        }
    }

    @Override
    public double responseTime(int i) {
        switch(i){
        
            case 0:
                return this.ResponseTimeRO;
            case 1:
                return this.ResponseTimeWR;
            default:
                return -1D;
                
        }
    }

    @Override
    public double getConfidenceThroughput(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getConfidenceAbortRate(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getConfidenceResponseTime(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void setThroughputWR(double throughputWR) {
        this.ThroughputWR = throughputWR;
    }

    public void setThroughputRO(double throughputRO) {
        this.ThroughputRO = throughputRO;
    }

    public void setAbortRateWR(double abortRateWR) {
        this.AbortRateWR = abortRateWR;
    }

    public void setAbortRateRO(double abortRateRO) {
        this.AbortRateRO = abortRateRO;
    }

    public void setResponseTimeWR(double responseTimeWR) {
        this.ResponseTimeWR = responseTimeWR;
    }

    public void setResponseTimeRO(double responseTimeRO) {
        this.ResponseTimeRO = responseTimeRO;
    }

}
