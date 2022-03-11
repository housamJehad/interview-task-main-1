package com.progressoft.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.LinkedList;

public class NormalizerImpl implements Normalizer {

    @Override
    public ScoringSummary zscore(Path csvPath, Path destPath, String colToStandardize) throws FileNotFoundException {

        Splitter splitter=new Splitter();
        LinkedList<Double>originalCSVFileNumericData=splitter.splitPath_ToGetNumericValuesFromColumn(csvPath,colToStandardize);

        Calculator calculator=new Calculator(originalCSVFileNumericData.size(),originalCSVFileNumericData);
        File csvOutputFile = new File(String.valueOf(destPath));



        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
              String modifiedColumnData[]=splitter.getNewModifiedData_FromOldCSVFile(
                      csvPath,
                      calculator.getDataSize(),
                      colToStandardize,
                      new LinkedList(calculator.calcZScore(calculator.mean(),calculator.standardDeviation())), "z");

              for(int i=0;i<modifiedColumnData.length;i++){
                  pw.println(modifiedColumnData[i]);
              }
        }


        return calculator;
    }


    @Override
    public ScoringSummary minMaxScaling(Path csvPath, Path destPath, String colToNormalize) throws IllegalArgumentException,FileNotFoundException {

        Splitter splitter=new Splitter();
        LinkedList<Double>originalCSVFileNumericData=splitter.splitPath_ToGetNumericValuesFromColumn(csvPath,colToNormalize);

        Calculator calculator=new Calculator(originalCSVFileNumericData.size(),originalCSVFileNumericData);
        File csvOutputFile = new File(String.valueOf(destPath));


        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            String modifiedColumnData[]=splitter.getNewModifiedData_FromOldCSVFile(
                    csvPath,
                    calculator.getDataSize(),
                    colToNormalize,
                    new LinkedList(calculator.calcMinMaxNormalization()),"mm");


            for(int i=0;i<modifiedColumnData.length;i++){
                pw.println(modifiedColumnData[i]);
            }

        }


        return calculator;
    }
}
