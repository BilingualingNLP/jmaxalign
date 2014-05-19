package edu.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OPUS {

	public static void main(String[] args){

		String l1 = "en";
		String l2 = "es";
		String positiveL1TrainingPath = "/home/max/alignment/corpora/en-es/en.train";
		String positiveL2TrainingPath= "/home/max/alignment/corpora/en-es/es.train";
		String positiveL1TestingPath = "/home/max/alignment/corpora/en-es/en.test";
		String positiveL2TestingPath= "/home/max/alignment/corpora/en-es/es.test";
		String input = "/home/max/alignment/corpora/en-es/OpenSubtitles2011en-es.tmx";
		int numPositiveTrainingSentences = 10000;
		int numPositiveTestingSentences = 10000;
		
		boolean switchedToTest = false;
		try{
			BufferedReader tmxReader = new BufferedReader(new FileReader(new File(input)));

			//			
			PrintWriter l1_output = new PrintWriter(new FileWriter(new File(positiveL1TrainingPath), true));
			PrintWriter l2_output = new PrintWriter(new FileWriter(new File(positiveL2TrainingPath), true));
			//			
			String line;

			int i = 0;
			while ((line = tmxReader.readLine()) != null){

				//We parsed all the testing data we want
				if (i>= numPositiveTrainingSentences && (!switchedToTest) ){
					switchedToTest = true;

					l1_output.flush();
					l2_output.flush();
					l1_output.close();
					l2_output.close();
					l1_output = new PrintWriter(new FileWriter(new File(positiveL1TestingPath), true));
					l2_output = new PrintWriter(new FileWriter(new File(positiveL2TestingPath), true));

				}
				//we also parsed all the training data
				if (i>= (numPositiveTestingSentences + numPositiveTrainingSentences)  && (switchedToTest) ){
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
		catch(IOException e){System.out.println(e.getMessage());}
	}

}
