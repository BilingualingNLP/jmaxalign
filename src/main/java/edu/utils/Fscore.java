package edu.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import edu.main.NewCommandLineArguments;


public class Fscore {

	public static void getScores(NewCommandLineArguments cmdArgs){

		int truePositive = 0; //is paralle, found parallel 
		int falsePositive = 0; //is parallel, found not parallel 
		int trueNegative = 0; //is nonparallel, found non parallel
		int falseNegative = 0;  //is nonparallel, found parallel
		String parallelClass = "parallel";
		String nonParallelClass = "nonparallel";
		
		try{
			BufferedReader b  = new BufferedReader(new FileReader(cmdArgs.getClassifierOutputRawFile()));
			String line = "";
			while ((line = b.readLine()) != null){
				String[] datum = line.split("\t");
				int lastIndex = datum.length - 1;
				String actualClass = datum[0];
				String classifiedClass = datum[lastIndex].replaceAll("[^a-zA-Z]", "");
				if ( (actualClass.equals(parallelClass)) && classifiedClass.equals(parallelClass)){
					truePositive++;
				}
				else if ( (actualClass.equals(parallelClass)) && classifiedClass.equals(nonParallelClass)){
					falsePositive++;
				}
				else if ( (actualClass.equals(nonParallelClass)) && classifiedClass.equals(nonParallelClass)){
					trueNegative++;
				}
				else if ( (actualClass.equals(nonParallelClass)) && classifiedClass.equals(parallelClass)){
					falseNegative++;			
				}
				
			}
		
			

			float  percision = divideIntegers(truePositive, (truePositive + falsePositive));
			float  recall = divideIntegers(truePositive, (truePositive + falseNegative));
			int total = truePositive + trueNegative + falsePositive + falseNegative;
			float  accuracy = divideIntegers((truePositive + trueNegative), total);

		
			
		
			double  f = 0;
			f = ((2 * percision * recall)) / (percision + recall);
	
			
			System.out.println("------------------");
			System.out.println("True Positive: " + truePositive);
			System.out.println("False Positive: " + falsePositive);
			System.out.println("True Negative: " + trueNegative);
			System.out.println("False Negative: " + falseNegative);
			System.out.println("------------------");
			System.out.println("Precision:" + percision);
			System.out.println("Recall: " + recall);			
			System.out.println("Accuracy " + accuracy);
			System.out.println("F-Score:" + f);
			System.out.println("------------------");
		
			
			System.out.flush();
			
		}
		catch(IOException e){System.out.println(e.getMessage());}
	}
	public static float divideIntegers(int i, int j){
		return (float) i * 100.0f / (float) j;
	}
	
	
}
