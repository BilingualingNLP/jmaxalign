package edu.lexicalsimilarity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import edu.main.NewCommandLineArguments;
import edu.objects.Sentence;
import edu.berkeley.nlp.fig.exec.Execution;
import edu.berkeley.nlp.wordAlignment.EMWordAligner;
import edu.berkeley.nlp.wordAlignment.Evaluator;
import edu.berkeley.nlp.wordAlignment.Main;
import edu.berkeley.nlp.wordAlignment.distortion.TreeWalkModel;

/**
 * @author kaufmann
 *
 */
public class Trainer{
	private NewCommandLineArguments cmdArgs; 




	public Trainer(NewCommandLineArguments c){
		cmdArgs = c;
		

	}
	
	



	public void analyzeTrainingData(){
		generateConfigFile();
		generateWordAlignments();
		//		generatePhraseProbabilities();
	}

	public void generateConfigFile(){
		try{
			File out = new File(cmdArgs.getBerkeleyConfigFile());
			PrintWriter configOutput = new PrintWriter(new FileWriter(out));
			configOutput.println("forwardModels	MODEL1;HMM");
			configOutput.println("reverseModels	MODEL1;HMM");
			configOutput.println("mode	JOINT;JOINT");
			configOutput.println("iters	2;2");
			configOutput.println("execDir	" + cmdArgs.getAlignFilePath());
			configOutput.println("create	true");
			configOutput.println("overwriteExecDir	true");
			configOutput.println("saveParams	true");
			configOutput.println("numThreads	3");
			configOutput.println("msPerLine	10000");
			configOutput.println("alignTraining");
			configOutput.println("leaveTrainingOnDisk");
			configOutput.println("foreignSuffix\t" + cmdArgs.getL1());
			configOutput.println("englishSuffix\t" + cmdArgs.getL2());
			configOutput.println("lowercase");
			configOutput.println("trainSources	" + cmdArgs.getTrainDir());
			configOutput.println("sentences	MAX");
			configOutput.println("testSources	" + "");
			configOutput.println("maxTestSentences	500");
			configOutput.println("offsetTestSentences	0");
			configOutput.println("saveLexicalWeights");
			configOutput.println("competitiveThresholding");
			configOutput.println("dictionary");
			configOutput.println("aligntraining");
			//			configOutput.println("allowedprecisionconflicts	8");
			//			configOutput.println("inferenceMode	VITERBI_ITG");
			//			configOutput.println("objMode    NONE");
			//			configOutput.println("pruneThresh	1e-4");
			//			configOutput.println("prunePredict	true");
			configOutput.flush();
			configOutput.close();
		}
		catch(IOException e){
			System.out.println("Can't read config file");
			System.out.println(e.getMessage());
		}
	}
	/**
	 * This method uses the Berkeley Aligner to generate word alignments from the given parallel 
	 * corpora   
	 */
	public  void generateWordAlignments(){
		//This creates an aligner 
		String[] args = new String[1];
		args[0] = "++" + cmdArgs.getBerkeleyConfigFile();
		Main main = new Main();
		Execution.init(args, main, EMWordAligner.class, Evaluator.class,
				TreeWalkModel.class);
		main.run();
		Execution.finish();

	}



	/**
	 * This method uses the Stanford Phrasal translator to transform the word alignments 
	 * into phrase alignments
	 */
//	private void generatePhraseProbabilities(){
//
//		//Do some work on the data so that phrasal can deal with it
//		try{
//			PrintWriter w = new PrintWriter(new FileWriter(j.getOptionsPath(l1, l2), true));
//			w.println("Data.foreignSuffix\t" + l1);
//			w.println("Data.englishSuffix\t" + l2);
//			w.close();
//		}
//		catch(IOException e){}
//
//
//		JAlignFileUtils.executeBashCommand("mv " + j.getBadLexweightsPath(l1, l1, l2) + " " + j.getGoodLexweightsPath(l1, l1, l2));
//		JAlignFileUtils.executeBashCommand("mv " + j.getBadLexweightsPath(l2, l1, l2) + " " + j.getGoodLexweightsPath(l2, l1, l2));
//
//		JAlignFileUtils.executeBashCommand("mv " + j.getBadTrainingFilePath(l1, l1, l2) + " " + j.getGoodTrainingFilePath(l1, l1, l2));
//		JAlignFileUtils.executeBashCommand("mv " + j.getBadTrainingFilePath(l2, l1, l2) + " " + j.getGoodTrainingFilePath(l2, l1, l2));
//		JAlignFileUtils.executeBashCommand("mv " + j.getBadAlignPath(l1, l2) + " " + j.getGoodAlignPath(l1, l2));
//		JAlignFileUtils.executeBashCommand("echo \"" + "Data.englishSuffix\\t" + l1 + "\" >> " + j.getOptionsPath(l1, l2));
//		JAlignFileUtils.executeBashCommand("echo \"" + "Data.foreignSuffix\\t" + l2 + "\" >> " + j.getOptionsPath(l1, l2));
//
//		//Now start to train phrasal
//		Properties prop = new Properties();
//		prop.setProperty("inputDir", j.getTrainingAlignDirectoryPath(l1,l2));
//		prop.setProperty("outputFile",j.getExtractedPhrasesPath(l1,l2));
//
//		System.err.println("Properties: " + prop.toString() + prop.size());
//		AbstractPhraseExtractor.setPhraseExtractionProperties(prop);
//
//		try {
//			PhraseExtract e = new PhraseExtract(prop);
//			e.extractAll();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}

	/**
	 * This method computes the lexical similarity of two sentences in the same language.
	 * The lexical similarity 
	 * @return A double that represents the lexical similarity, between 0 and 1. 
	 */
	public double score(Sentence l1, Sentence l2){
		return 0;
	}



}
