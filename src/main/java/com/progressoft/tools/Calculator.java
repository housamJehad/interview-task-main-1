package com.progressoft.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.LinkedList;

public class Calculator implements ScoringSummary{
    private int pathDataSize;
    private LinkedList<Double>pathData;
    private double numericDataMax=Double.MIN_VALUE;
    private double numericDataMin=Double.MAX_VALUE;
    private double dataMean=0.0;
    private double dataStandardDeviation=0.0;


    public Calculator(int pathDataSize, LinkedList<Double> pathData) {
        this.pathDataSize = pathDataSize;
        this.pathData = pathData;
        calcMinMax();
    }

    public int getDataSize(){return pathDataSize;}

    public LinkedList<Double> getNumericData(){return new LinkedList<Double>(pathData);}

    @Override
    public BigDecimal mean(){
        if(dataMean>0){
            return  new BigDecimal(dataMean).setScale(2,RoundingMode.HALF_EVEN);
        }else{
            double numericDataMean;
            double numericDataSum=calcSum();
            numericDataMean=numericDataSum/(double)pathDataSize;
            BigDecimal result=new BigDecimal(Math.round(numericDataMean));
            dataMean=result.doubleValue();
            return result.setScale(2,RoundingMode.HALF_EVEN);
        }
      }


    @Override
    public BigDecimal standardDeviation(){
        if(dataStandardDeviation>0){
            return new BigDecimal(dataStandardDeviation).setScale(2,RoundingMode.CEILING);
        }else{

            if(dataMean==0){
                mean();
            }
            int numericDataSize=pathDataSize;
            double sumSquareSubtract=0.0;
            LinkedList<Double> bufferPathData=new LinkedList<>();

            while(numericDataSize>0){
                sumSquareSubtract+= Math.pow(pathData.getFirst()-dataMean,2);
                bufferPathData.addLast(pathData.getFirst());
                pathData.removeFirst();
                numericDataSize--;
            }

            returnOriginalData(bufferPathData);
            BigDecimal result=new BigDecimal(Math.sqrt(sumSquareSubtract/(double)pathDataSize));
            dataStandardDeviation=result.doubleValue();
            return result.setScale(2,RoundingMode.CEILING);
        }
    }


   @Override
   public BigDecimal max(){
        return new BigDecimal(numericDataMax).setScale(2,RoundingMode.HALF_EVEN);
   }
   @Override
   public BigDecimal min(){
        return new BigDecimal(numericDataMin).setScale(2,RoundingMode.HALF_EVEN);

   }
   @Override
   public BigDecimal variance(){
        if (dataStandardDeviation==0){
            dataStandardDeviation=standardDeviation().doubleValue();
        }
      return new BigDecimal(Math.round(Math.pow(standardDeviation().doubleValue(),2))).setScale(2,RoundingMode.HALF_EVEN);
   }
   @Override
   public BigDecimal median(){

      int medianItemIndex=0,numericDataSize=pathDataSize;
       LinkedList<Double>bufferPathData=new LinkedList<>();
       while (numericDataSize>0){
           bufferPathData.addLast(pathData.getFirst());
           pathData.removeFirst();
           numericDataSize--;
       }
       returnOriginalData(bufferPathData);
       Collections.sort(pathData);
       medianItemIndex=pathDataSize/2;

      return new BigDecimal(pathData.get(medianItemIndex)).setScale(2,RoundingMode.HALF_EVEN);
   }



    public LinkedList<Double> calcZScore(BigDecimal mean,BigDecimal sDeviation){
        int numericDataSize=pathDataSize;
        LinkedList<Double>bufferPathData=new LinkedList<>();
        LinkedList<Double>zScoredData=new LinkedList<>();

        while (numericDataSize>0){
            double newValue=(pathData.getFirst()-mean.doubleValue())/sDeviation.doubleValue();
            bufferPathData.addLast(pathData.getFirst());
            pathData.removeFirst();
            numericDataSize--;
            zScoredData.addLast(new BigDecimal(newValue).setScale(2,RoundingMode.HALF_EVEN).doubleValue());
        }

        returnOriginalData(bufferPathData);
        return new LinkedList<Double>(zScoredData);
    }


    private void calcMinMax(){
        int numericDataSize=pathDataSize;
        LinkedList<Double>bufferPathData=new LinkedList<>();


        while (numericDataSize>0){
            if(pathData.getFirst()>numericDataMax){
                numericDataMax=pathData.getFirst();
            }
            if(pathData.getFirst()<numericDataMin){
                numericDataMin=pathData.getFirst();
            }
            bufferPathData.addLast(pathData.getFirst());
            pathData.removeFirst();
            numericDataSize--;
        }


        returnOriginalData(bufferPathData);
    }


    public LinkedList<Double> calcMinMaxNormalization(){
        int numericDataSize=pathDataSize;
        LinkedList<Double>bufferPathData=new LinkedList<>();
        LinkedList<Double>minMaxData=new LinkedList<>();


        while (numericDataSize>0){
            double newValue=(pathData.getFirst()-numericDataMin)/(numericDataMax-numericDataMin);
            bufferPathData.addLast(pathData.getFirst());
            pathData.removeFirst();
            numericDataSize--;
            minMaxData.addLast(new BigDecimal(newValue).setScale(2,RoundingMode.HALF_EVEN).doubleValue());
        }

        returnOriginalData(bufferPathData);
        return new LinkedList<Double>(minMaxData);
    }


    public double calcSum(){
        double numericDataSum=0;
        int numericDataSize=pathDataSize;
        LinkedList<Double> bufferPathData=new LinkedList<>();


        while(numericDataSize>0){
            numericDataSum+=pathData.getFirst();
            bufferPathData.addLast(pathData.getFirst());
            pathData.removeFirst();
            numericDataSize--;
        }

        returnOriginalData(bufferPathData);
        return numericDataSum;
    }

    private void returnOriginalData(LinkedList<Double>bufferPathData){
        int bufferDataSize=pathDataSize;
        while (bufferDataSize>0){
            pathData.addLast(bufferPathData.getFirst());
            bufferPathData.removeFirst();
            bufferDataSize--;
        }
    }

}
