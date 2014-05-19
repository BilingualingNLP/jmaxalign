package edu.classify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import edu.main.NewCommandLineArguments;
import edu.stanford.nlp.classify.Classifier;
import edu.stanford.nlp.classify.ColumnDataClassifier;
import edu.stanford.nlp.ling.Datum;
import edu.stanford.nlp.objectbank.ObjectBank;

public class ClassifierWrapper{

	NewCommandLineArguments cmdArgs; 

	public ClassifierWrapper(NewCommandLineArguments args){
		cmdArgs= args;
	}

	public void generateConfigFile(){
		try{
			File out = new File(cmdArgs.getClassifierPropertiesFile());
			PrintWriter configOutput = new PrintWriter(new FileWriter(out));
			configOutput.println("#");
			configOutput.println("# Features");
			configOutput.println("#");
			configOutput.println("useClassFeature=true");
			configOutput.println("1.useNGrams=false");
			configOutput.println("1.usePrefixSuffixNGrams=true");
			configOutput.println("1.maxNGramLeng=4");
			configOutput.println("1.minNGramLeng=1");
			configOutput.println("1.binnedLengths=10,20,30");
			configOutput.println("#");
			configOutput.println("# Printing");
			configOutput.println("#");
			configOutput.println("# printClassifier=HighWeight");
			configOutput.println("printClassifierParam=200");
			configOutput.println("#");
			configOutput.println("# Mapping");
			configOutput.println("#");
			configOutput.println("goldAnswerColumn=0");
			configOutput.println("displayedColumn=1");
			configOutput.println("#");
			configOutput.println("# Optimization");
			configOutput.println("#");
			configOutput.println("intern=true");
			configOutput.println("sigma=3");
			configOutput.println("useQN=true");
			configOutput.println("QNsize=15");
			configOutput.println("tolerance=1e-4");
			configOutput.println("#");
			configOutput.println("# Training input");
			configOutput.println("#");
			configOutput.println("trainFile=" + cmdArgs.getClassifierTrainFile());
			configOutput.flush();
			configOutput.close();
		}
		catch(IOException e){
			System.out.println("Can't write config file");
			System.out.println(e.getMessage());
		}
	}





	public void parseResults(){
		try{


			PrintWriter out = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierOutputRawFile())));

			//	generateConfigFile(l1,l2);
			ColumnDataClassifier cdc = new ColumnDataClassifier(cmdArgs.getClassifierPropertiesFile());
			Classifier<String,String> cl =
					cdc.makeClassifier(cdc.readTrainingExamples(cmdArgs.getClassifierTrainFile()));
			ArrayList<Integer> correctLines = new ArrayList<Integer>();
			int i = 0;
			for (String line : ObjectBank.getLineIterator(cmdArgs.getClassifierTestFile())) {
				i++;
				Datum<String,String> d = cdc.makeDatumFromLine(line, 0);
				if (cl.classOf(d).equals("parallel")){
					correctLines.add(i);
				}
				out.println(line + "  ==>  " + cl.classOf(d));
			}
			extractParallelSentences(cmdArgs.getL1TestAbsoluteFile(), cmdArgs.getL2TestAbsoluteFile(), cmdArgs.getParallelSentenceFile(), correctLines);


			out.flush();
		}
		catch(IOException e){System.out.println(e.getMessage());
		}
	}

	public void parseResultsEval(){
		try{
			PrintWriter out = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierOutputRawFile())));

			//	generateConfigFile(l1,l2);
			ColumnDataClassifier cdc = new ColumnDataClassifier(cmdArgs.getClassifierPropertiesFile());
			Classifier<String,String> cl =
					cdc.makeClassifier(cdc.readTrainingExamples(cmdArgs.getClassifierTrainFile()));
			ArrayList<Integer> correctLines = new ArrayList<Integer>();
			int i = 0;
			for (String line : ObjectBank.getLineIterator(cmdArgs.getClassifierEvalFile())) {
				i++;
				Datum<String,String> d = cdc.makeDatumFromLine(line, 0);
				if (cl.classOf(d).equals("parallel")){
					correctLines.add(i);
				}
				out.println(line + "  ==>  " + cl.classOf(d));
			}
			extractParallelSentences(cmdArgs.getL1EvalFile(), cmdArgs.getL2EvalFile(), cmdArgs.getParallelSentenceFile(), correctLines);


			out.flush();
		}
		catch(IOException e){System.out.println(e.getMessage());
		}
	}


	public void extractParallelSentences(String l1SenPath, String l2SenPath, String outputPath, ArrayList<Integer> correctLines){
		try{
			BufferedReader l1_reader = new BufferedReader(new FileReader(new File(l1SenPath)));
			BufferedReader l2_reader = new BufferedReader(new FileReader(new File(l2SenPath)));
			PrintWriter parallelDataWriter = new PrintWriter(new FileWriter(new File(outputPath)));

			String l1_sentence, l2_sentence;
			int i = 0;
			while ((l1_sentence = l1_reader.readLine()) != null){
				i++;
				l2_sentence = l2_reader.readLine();
				if (correctLines.contains(i)){
					parallelDataWriter.println("**"+ i + "**");
					parallelDataWriter.println(l1_sentence);
					parallelDataWriter.println(l2_sentence);
				}
				else{
					if (correctLines.contains(i)){
						parallelDataWriter.println("**"+ i + "**");
						parallelDataWriter.println(l1_sentence);
						parallelDataWriter.println(l2_sentence);
					}
				}
				parallelDataWriter.flush();
			}
			parallelDataWriter.flush();
			parallelDataWriter.close();
		}
		catch(IOException e){System.out.println(e.getMessage());}
	}

	public void evaluateResults(){
		try{


			PrintWriter out = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierOutputRawFile())));

			//	generateConfigFile(l1,l2);
			ColumnDataClassifier cdc = new ColumnDataClassifier(cmdArgs.getClassifierPropertiesFile());
			Classifier<String,String> cl =
					cdc.makeClassifier(cdc.readTrainingExamples(cmdArgs.getClassifierTrainFile()));
			ArrayList<Integer> correctLines = new ArrayList<Integer>();
			int i = 0;
			for (String line : ObjectBank.getLineIterator(cmdArgs.getClassifierTestFile())) {
				i++;
				Datum<String,String> d = cdc.makeDatumFromLine(line, 0);
				if (cl.classOf(d).equals("parallel")){
					correctLines.add(i);
				}
				out.println(line + "  ==>  " + cl.classOf(d));
			}
			extractParallelSentences(cmdArgs.getL1TestAbsoluteFile(), cmdArgs.getL2TestAbsoluteFile(), cmdArgs.getParallelSentenceFile(), correctLines);


			out.flush();
		}
		catch(IOException e){System.out.println(e.getMessage());
		}
	}


}
