package edu.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import edu.lexicalsimilarity.Scorer;
import edu.lexicalsimilarity.Trainer;
import edu.utils.Fscore;
import edu.classify.ClassifierWrapper;

public class JAligner{

	NewCommandLineArguments cmdArgs;
	//some Variables that govern the settings of an indvidual run 


	int numNegativeTrainingSentences;
	int numNegativeTestingSentences;
	int numNegativeEvalSentences;


	String  positiveL1TestingFile;
	String  positiveL2TestingFile;
	String  positiveL1TrainingFile;
	String  positiveL2TrainingFile;


	String negativeL1TestingFile;
	String negativeL2TestingFile;
	String negativeL1TrainingFile;
	String negativeL2TrainingFile;

	String  positiveL1EvalFile;
	String  positiveL2EvalFile;
	String negativeL1EvalFile;
	String negativeL2EvalFile;



	public static void main(String[] args){

		NewCommandLineArguments c = new NewCommandLineArguments(args[0]);
		//Get word alignetmsn
		if (args[1].equals("-setup")){
			System.out.println("Removing files from last run...");
			cleanDirs(c);
			System.out.println("Generating word alignments...");
			Trainer wordAligner = new Trainer(c);
			wordAligner.generateConfigFile();
			System.out.println("Generating sentence features...");

			wordAligner.generateWordAlignments();
		}
		JAligner jmaxalign = new JAligner(c);
		if (args[1].equals("-train")){
			System.out.println("Training a MaxEnt classifier...");
			jmaxalign.train();
		}
		if (args[1].equals("-test")){
			System.out.println("Evaluating test sentences...");

			jmaxalign.test();
		}
		if (args[1].equals("-eval")){
			System.out.println("Evaluating eval sentences...");
			jmaxalign.eval();
		}
	}

	public static void cleanDirs(NewCommandLineArguments args){
		String l1 = args.getL1();
		String l2 = args.getL2();
		File f = new File(args.getRoot());
		if (f.isDirectory()){
			for (File dir : f.listFiles()){
				if (dir.isDirectory()){
					String name = dir.getName();
					if (name.equals("train")){
						for (File f2 : dir.listFiles()){
							if (!f2.getName().equals("train." + l2) && !f2.getName().equals("train." + l1)){
								System.out.println(f2 + " DELETED");
								f2.delete();
							}
						}
					}
					else if (name.equals("test")){
						for (File f2 : dir.listFiles()){
							if (!f2.getName().equals("test." + l2) && !f2.getName().equals("test." + l1)){
								System.out.println(f2 + " DELETED");
								f2.delete();
							}
						}

					}
					else if (name.equals("eval")){
						for (File f2 : dir.listFiles()){
							if (!f2.getName().equals("eval." + l2) && !f2.getName().equals("eval." + l1)){
								System.out.println(f2 + " DELETED");
								f2.delete();
							}
						}
					}
				}
			}
		}
	}


	public JAligner(NewCommandLineArguments c){
		cmdArgs = c;
		numNegativeTrainingSentences 	= 	c.getNegativeTrainingSentences();
		numNegativeTestingSentences 	= 	c.getNegativeTestingSentences();
		numNegativeEvalSentences 	= 	c.getNegativeTestingSentences();

		positiveL1TestingFile			= 	c.getL1TestAbsoluteFile();
		negativeL1TestingFile			= 	c.getTestDir() +  "neg-" + c.getL1TestFileName();
		positiveL2TestingFile			= 	c.getL2TestAbsoluteFile();
		negativeL2TestingFile			= 	c.getTestDir() + "neg-" + c.getL2TestFileName();

		positiveL2TrainingFile			=	c.getL2TrainAbsoluteFile();
		negativeL2TrainingFile			= 	c.getTrainDir()  + "neg-" + c.getL2TrainFileName();
		positiveL1TrainingFile			= 	c.getL1TrainAbsoluteFile();
		negativeL1TrainingFile			= 	c.getTrainDir() + "neg-" + c.getL1TrainFileName();

		positiveL2EvalFile			= 	c.getL2EvalFile();
		negativeL2EvalFile			= 	c.getEvalDir() + "neg-" + c.getL2EvalFileName();
		positiveL1EvalFile			=	c.getL1EvalFile();
		negativeL1EvalFile			= 	c.getEvalDir()  + "neg-" + c.getL1EvalFileName();

	}


	public static void executeBashCommand(String command){
		try{
			Runtime.getRuntime().exec(command).waitFor();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}

	}

	public void train(){

		try {
			generateNewNegativeSentences(numNegativeTrainingSentences, 
					positiveL1TrainingFile,
					positiveL2TrainingFile,
					negativeL1TrainingFile,
					negativeL2TrainingFile);
			String l1 = cmdArgs.getL1();
			String l2 = cmdArgs.getL2();
			//Make Negative Examples
			executeBashCommand("mv " + cmdArgs.getBadLexweightsPath(l1) + " " + cmdArgs.getGoodLexweightsPath(l1));
			executeBashCommand("mv " + cmdArgs.getBadLexweightsPath(l2) + " " + cmdArgs.getGoodLexweightsPath(l2));


			ClassifierWrapper standfordClassifier = new ClassifierWrapper(cmdArgs);
			standfordClassifier.generateConfigFile();
			Scorer s = new Scorer(cmdArgs);
			PrintWriter dataWriter = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierTrainFile())));

			s.score("parallel", positiveL1TrainingFile, positiveL2TrainingFile, dataWriter);
			s.score("nonparallel", negativeL1TrainingFile, negativeL2TrainingFile, dataWriter);
			dataWriter.flush();
			dataWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eval(){
		try{
			generateNewNegativeSentences(numNegativeEvalSentences, 
					positiveL1EvalFile,
					positiveL2EvalFile,
					negativeL1EvalFile,
					negativeL2EvalFile);
			Scorer s = new Scorer(cmdArgs);
			PrintWriter dataWriter = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierEvalFile())));
			s.score("parallel", positiveL1EvalFile, positiveL2EvalFile, dataWriter);
			s.score("nonparallel", negativeL1EvalFile, negativeL2EvalFile, dataWriter);
			dataWriter.flush();
			dataWriter.close();
			
			
			ClassifierWrapper standfordClassifier = new ClassifierWrapper(cmdArgs);
			standfordClassifier.parseResultsEval();
			System.out.println("Extracted parallel sentences available at: " + cmdArgs.getParallelSentenceFile());
			Fscore.getScores(cmdArgs);	

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test(){
		try{
			generateNewNegativeSentences(numNegativeTestingSentences, 
					positiveL1TestingFile,
					positiveL2TestingFile,
					negativeL1TestingFile,
					negativeL2TestingFile);
			Scorer s = new Scorer(cmdArgs);
			PrintWriter dataWriter = new PrintWriter(new FileWriter(new File(cmdArgs.getClassifierTestFile())));
			s.score("unknown", positiveL1TestingFile, positiveL2TestingFile, dataWriter);
			s.score("unknown", negativeL1TestingFile, negativeL2TestingFile, dataWriter);
			dataWriter.flush();
			dataWriter.close();
			ClassifierWrapper standfordClassifier = new ClassifierWrapper(cmdArgs);
			standfordClassifier.parseResults();
			System.out.println("Extracted parallel sentences available at: " + cmdArgs.getParallelSentenceFile());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void generateNewNegativeSentences(int number, String l1In, String l2In, String l1Out, String l2Out){
		try {

			BufferedReader l1_input = new BufferedReader(new FileReader(new File(l1In)));
			BufferedReader l2_input = new BufferedReader(new FileReader(new File(l2In)));
			PrintWriter l1_writer = new PrintWriter(new FileWriter(new File(l1Out)));
			PrintWriter l2_writer = new PrintWriter(new FileWriter(new File(l2Out)));
			ArrayList<String> l1Sens = new ArrayList<String>();
			ArrayList<String> l2Sens = new ArrayList<String>();
			String line = "";
			int i = 0;
			while ((line = l1_input.readLine()) != null){
				if (++i > number)
					break;
				l1Sens.add(line);
			}

			i = 0;
			while ((line = l2_input.readLine()) != null){
				if (++i > number)
					break;
				l2Sens.add(line);
			}

			assert(l1Sens.size() == l2Sens.size());
			int firstIndex = 0;
			int lastIndex = l1Sens.size();
			for (; firstIndex  < l1Sens.size(); firstIndex++){
				lastIndex--;
				l1_writer.println(l1Sens.get(firstIndex));
				l2_writer.println(l2Sens.get(lastIndex));

			}

			l1_writer.flush();
			l1_writer.close();
			l2_writer.flush();
			l2_writer.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public  void generateNegativeSentences(int type) {
		try{


			String l1SentencesPath;
			String l2SentencesPath;
			String l1NegSentencesPath;
			String l2NegSentencesPath;
			int maxSentences;
			if(type ==0){ //training
				l1SentencesPath= positiveL1TrainingFile;
				l2SentencesPath= positiveL2TrainingFile;
				l1NegSentencesPath= negativeL1TrainingFile;
				l2NegSentencesPath= negativeL2TrainingFile;
				maxSentences = numNegativeTrainingSentences;
			}
			else if (type ==1){ //testing 
				l1SentencesPath= positiveL1TestingFile;
				l2SentencesPath= positiveL2TestingFile;
				l1NegSentencesPath= negativeL1TestingFile;
				l2NegSentencesPath= negativeL2TestingFile;
				maxSentences = numNegativeTestingSentences;
			}
			else if (type ==2){ //eval 
				l1SentencesPath= positiveL1EvalFile;
				l2SentencesPath= positiveL2EvalFile;
				l1NegSentencesPath= negativeL1EvalFile;
				l2NegSentencesPath= negativeL2EvalFile;
				maxSentences = numNegativeEvalSentences;
			}
			else{
				l1SentencesPath= null;
				l2SentencesPath= null;
				l1NegSentencesPath= null;
				l2NegSentencesPath= null;
				maxSentences = 0;
			}

			System.out.println(l1SentencesPath);
			System.out.println(l2SentencesPath);
			System.out.println(l1NegSentencesPath);
			System.out.println(l2NegSentencesPath);
			BufferedReader l1_input = new BufferedReader(new FileReader(new File(l1SentencesPath)));
			BufferedReader l2_input = new BufferedReader(new FileReader(new File(l2SentencesPath)));
			PrintWriter l1_writer = new PrintWriter(new FileWriter(new File(l1NegSentencesPath), true));
			PrintWriter l2_writer = new PrintWriter(new FileWriter(new File(l2NegSentencesPath), true));

			ArrayList<String> l1Sentences = new ArrayList<String>();
			ArrayList<String> l2Sentences = new ArrayList<String>();
			String line;
			while ((line = l1_input.readLine()) != null){
				l1Sentences.add(line);
				l2Sentences.add(l2_input.readLine());
			}

			int  currentSentences = 0;
			for (int i = 0; i < l1Sentences.size(); i++){
				String l1Sen = l1Sentences.get(i);
				for (String l2Sen : l2Sentences){
					if (currentSentences == maxSentences)
						break;
					else{


						l1_writer.println(l1Sen);
						l2_writer.println(l2Sen);
						currentSentences++;

					}
				}
			}
			l1_writer.flush();
			l2_writer.flush();
			l1_writer.close();
			l2_writer.close();
		}
		catch(Exception e){System.out.println(e.getMessage());}
	}
	public static float divideIntegers(int i, int j){
		return (float) i * 100.0f / (float) j;
	}



}
