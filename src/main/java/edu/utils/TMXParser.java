package edu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TMXParser {

	private static String opusFile;

	public static void main(String[] args) {
		String l1 = "en";							
		String l2 = "es";
		String root = "/home/max/week/" + l1 + "-" + l2 + "/"; 
		TMXParser.opusFile = root + "OpenSubtitles2011" + l1 + "-" + l2 + ".tmx";
		splitIntoTrainingAndTestingData(l1, l2, 
				root + "train/train."+ l1, 
				root + "train/train." + l2, 
				root + "eval/eval." + l1, 
				root + "eval/eval." + l2,
				15000, //train data 
				10000); //eval data
	}
	/**
	 * This class can parse a TMX file downloadable from the Open Source Parallel Subtitles corpus V2,
	 * and output the translation into two separate files. The locations of the files are determined by the FilePaths class.
	 * @param args
	 */
	public static void splitIntoTrainingAndTestingData(String l1, String l2, 
			String l1TrainOutputPath, String l2TrainOutputPath, 
			String l1TestOutputPath, String l2TestOutputPath,
			int trainingData, int testingData){

		boolean switchedToTest = false;
		try{

			BufferedReader tmxReader = new BufferedReader(new FileReader(new File(opusFile)));			
			PrintWriter l1_output = new PrintWriter(new FileWriter(new File(l1TrainOutputPath)));
			PrintWriter l2_output = new PrintWriter(new FileWriter(new File(l2TrainOutputPath)));

			String line;

			int i = 0;
			while ((line = tmxReader.readLine()) != null){

				//We parsed all the testing data we want
				if (i>= trainingData && (!switchedToTest) ){
					switchedToTest = true;

					l1_output.flush();
					l2_output.flush();
					l1_output.close();
					l2_output.close();
					 l1_output = new PrintWriter(new FileWriter(new File(l1TestOutputPath)));
					 l2_output = new PrintWriter(new FileWriter(new File(l2TestOutputPath)));

				}
				//we also parsed all the raining data
				if (i>= (testingData + trainingData)  && (switchedToTest) ){
					break;
				}
				//				if(line.contains("<tu>")){
				//
				//					String doc = xmldoc.toString();
				//					l1_output.println(getSentences(doc));
				//					//					l2_output.println(getSentences(l2));
				//					xmldoc = new StringBuilder();
				//					xmldoc.append(line);
				//				}
				//				else{
				//					xmldoc.append(line + "\n");
				//				}
				if ( (!(line.contains("srclang"))) && (!(line.contains("adminlang"))) ){
					if(line.contains("\"" + l1 + "\"")){				

						line = line.replaceAll("<tuv xml:lang=\"" + l1 + "\">", "")
						.replaceAll("</seg>","")
						.replaceAll("<seg>", "").
						replaceAll("</tuv>", "");
						l1_output.println(line.trim());
					}
					if(line.contains("\"" + l2 + "\"")){
						i++;
						line = line.replaceAll("<tuv xml:lang=\"" + l2 + "\">", "")
						.replaceAll("</seg>","")
						.replaceAll("<seg>", "").
						replaceAll("</tuv>", "");
						l2_output.println(line.trim());
					}
				}
			}
			l1_output.flush();
			l2_output.flush();
			l1_output.close();
			l2_output.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}


}
