package com.progressoft.tools;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;

import java.util.LinkedList;
import java.util.Scanner;

public class Splitter {

    private int columnNumericIndex=-1;

     /*
      This method for :
      1- split the  old csv file
      2-Find the column index in the file
      3-return the numeric data from specific column name
    */
    public LinkedList<Double> splitPath_ToGetNumericValuesFromColumn(Path path,String columnName) throws IllegalArgumentException{
        LinkedList<Double>bufferData=new LinkedList<>();
        int columnIndex=0;
        Scanner pathScanner;

        try{
              pathScanner =new Scanner(new File(path.toString()));
            }catch (Exception e){
                throw new IllegalArgumentException("source file not found");
            }

        while(pathScanner.hasNext()){
            String row=pathScanner.nextLine()+" ";
            String rowData[]=row.split(",");
            if(columnIndex==0){
                for(int i=0;i<rowData.length;i++){
                   if(rowData[i].trim().equals(columnName)){
                      columnNumericIndex=i;
                   }
                }
                if(columnNumericIndex==-1){
                    throw new IllegalArgumentException("column "+columnName+" not found");
                }
                columnIndex++;
                continue;
            }
                try {
                    double columnIndexValue=0;
                    columnIndexValue=Double.parseDouble(rowData[columnNumericIndex].trim());
                    bufferData.addLast(columnIndexValue);
                }catch (Exception e){
                    throw new IllegalArgumentException("Invalid numeric data");
                }
              columnIndex++;
        }
        pathScanner.close();
     return bufferData;
    }



    /*
      This method for :
       1-create string array with old csv file data with new modified column
    */

    public String [] getNewModifiedData_FromOldCSVFile(Path csvPath,int rowsLength,String columnName,LinkedList<Double>columnData,String normalizationType) {
        int rowIndex=0;
        Scanner pathScanner;
        String modifiedData[]=new String[rowsLength+1];

        try{
          pathScanner =new Scanner(new File(csvPath.toString()));
        }catch (Exception e){
          throw new IllegalArgumentException("source file not found");
        }
        while(pathScanner.hasNext()) {
            String bufferSplitter[]=pathScanner.nextLine().split(",");
            modifiedData[rowIndex]="";
            int bufferSplitterLength=0;
            boolean isLastIndex=false;

            if(columnNumericIndex==-1){
                splitPath_ToGetNumericValuesFromColumn(csvPath,columnName);
            }
            if(columnNumericIndex==bufferSplitter.length-1){
                bufferSplitterLength=bufferSplitter.length+1;
                isLastIndex=true;
            }else{
                bufferSplitterLength=bufferSplitter.length;
            }

               for(int i=0;i<bufferSplitterLength;i++){
                   if(rowIndex==0){
                       if((i-1)==columnNumericIndex&&i>0&&isLastIndex){
                           modifiedData[0]+=columnName+"_"+normalizationType;
                       }else if((i-1)==columnNumericIndex&&i>0){
                           modifiedData[0]+=columnName+"_"+normalizationType+","+bufferSplitter[i];
                       }else{
                           modifiedData[0]+=bufferSplitter[i]+",";
                       }
                   } else {
                       if((i-1)==columnNumericIndex&&i>0){
                           if(isLastIndex){
                               modifiedData[rowIndex]+=new BigDecimal(columnData.getFirst())
                                       .setScale(2,RoundingMode.HALF_EVEN);
                               columnData.removeFirst();

                           }else{
                               modifiedData[rowIndex]+=new BigDecimal(columnData.getFirst())
                                       .setScale(2, RoundingMode.HALF_EVEN)+","+bufferSplitter[i];
                               columnData.removeFirst();

                           }
                       }else{
                           modifiedData[rowIndex]+=bufferSplitter[i]+",";
                       }
                   }
               }
                rowIndex++;
                continue;
            }

        pathScanner.close();
        return modifiedData;
    }
}
