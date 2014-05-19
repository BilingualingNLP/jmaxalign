package edu.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class NewCommandLineArguments {

	public NewCommandLineArguments(String filePath){
		try {
			BufferedReader b = new BufferedReader(new FileReader(new File(filePath)));
			String line = "";
			while ((line = b.readLine()) != null){
				parseLine(line);
			}
			if (root == null){
				root = new File("").getAbsolutePath() + "/";
			}
			
			//make ncessary files 
			File align = new File(root + "align/");
			if (!align.exists()){
				align.mkdir();
			}
			alignDir = align.getAbsolutePath() + "/";
			
			
			File test = new File(root + "test/");
			if (!test.exists()){
				test.mkdir();

			}
			testFilePath = test.getAbsolutePath()  + "/";

			
			File train = new File(root + "train/");
			if (!train.exists()){
				train.mkdir();	
			}
			trainFilePath = train.getAbsolutePath() + "/";
			
			File eval = new File(root + "eval/");
			if (!train.exists()){
				train.mkdir();	
			}
			evalDir = eval.getAbsolutePath() + "/";
			

			File classify = new File(root + "edu/classify/");
			if (!classify.exists()){
				classify.mkdir();		

			}
			classifyDir = classify.getAbsolutePath() + "/";
			
			
			l1TrainFile = trainFilePath + l1TrainFileName;
			l2TrainFile = trainFilePath + l2TrainFileName;
			l1TestFile = testFilePath + l1TestFileName;
			l2TestFile = testFilePath + l2TestFileName;
			l1EvalFile = evalDir+ l1EvalFileName;
			l2EvalFile = evalDir + l2EvalFileName;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public String getL1EvalFile() {
		return l1EvalFile;
	}



	public void setL1EvalFile(String l1EvalFile) {
		this.l1EvalFile = l1EvalFile;
	}



	public String getEvalDir() {
		return evalDir;
	}



	public void setEvalDir(String evalDir) {
		this.evalDir = evalDir;
	}



	public  String getBadLexweightsPath(String lang){
		if (lang.equals(l1))
			return alignDir + "2.lexweights";
		else if (lang.equals(l2))
			return alignDir + "1.lexweights";
		else 
			return null;
	}

	public  String getGoodLexweightsPath(String lang){
		if (lang.equals(l1))
			return alignDir + l1 + ".lexweights";
		else if (lang.equals(l2))
			return alignDir + l2 + ".lexweights";
		else 
			return null;
	}
	
	public  String getOutputPath(){
		return root +  "output.txt";
	}
	
	public  String getClassifierTrainFile(){
		return classifyDir + "train.prop";
	}

	
	public  String getClassifierEvalFile(){
		return classifyDir + "eval.prop";
	}

	public  String getClassifierTestFile(){
		return classifyDir + "test.prop";
	}
	public String getClassifierPropertiesFile(){
		return classifyDir + "data.prop";
	}
	
	public String getBerkeleyConfigFile(){
		return alignDir + "train.config";
	}

	public String getGeneratedLexWeightsPath(String lang){
		return "";
	}
	
	public String getClassifierOutputRawFile(){
		return classifyDir + "results.txt";
	}
	
	public String getParallelSentenceFile(){
		return classifyDir + "extracted-parallel-sentences.txt";
	}


	//Accessors



	public String getL1TrainAbsoluteFile() {
		return l1TrainFile;
	}
	public void setL1Train(String l1Train) {
		this.l1TrainFile = l1Train;
	}
	public String getL2TrainAbsoluteFile() {
		return l2TrainFile;
	}
	public void setL2Train(String l2Train) {
		this.l2TrainFile = l2Train;
	}
	public String getL1TestAbsoluteFile() {
		return l1TestFile;
	}
	public void setL1Test(String l1Test) {
		this.l1TestFile = l1Test;
	}
	public String getL2TestAbsoluteFile() {
		return l2TestFile;
	}
	public void setL2Test(String l2Test) {
		this.l2TestFile = l2Test;
	}
	public String getRoot() {
		return root;
	}
	public void setRoot(String root) {
		this.root = root;
	}

	public Integer getPositiveTrainingSentences() {
		return positiveTrainingSentences;
	}
	public void setPositiveTrainingSentences(Integer positiveTrainingSentences) {
		this.positiveTrainingSentences = positiveTrainingSentences;
	}
	public Integer getNegativeTrainingSentences() {
		return negativeTrainingSentences;
	}
	public void setNegativeTrainingSentences(Integer negativeTrainingSentences) {
		this.negativeTrainingSentences = negativeTrainingSentences;
	}
	public Integer getPositiveTestingSentences() {
		return positiveTestingSentences;
	}
	public void setPositiveTestingSentences(Integer positiveTestingSentences) {
		this.positiveTestingSentences = positiveTestingSentences;
	}
	public Integer getNegativeTestingSentences() {
		return negativeTestingSentences;
	}
	public void setNegativeTestingSentences(Integer negativeTestingSentences) {
		this.negativeTestingSentences = negativeTestingSentences;
	}


	public String getAlignFilePath() {
		return alignDir;
	}
	public void setAlignFilePath(String alignFilePath) {
		this.alignDir = alignFilePath;
	}
	public String getTrainDir() {
		return trainFilePath;
	}
	public void setTrainFilePath(String trainFilePath) {
		this.trainFilePath = trainFilePath;
	}
	public String getL1() {
		return l1;
	}
	public void setL1(String l1) {
		this.l1 = l1;
	}
	public String getL2() {
		return l2;
	}
	public void setL2(String l2) {
		this.l2 = l2;
	}
	public String getTestDir() {
		return testFilePath;
	}

	public void setTestFilePath(String testFilePath) {
		this.testFilePath = testFilePath;
	}
	public String getClassifyFilePath() {
		return classifyDir;
	}

	public void setClassifyFilePath(String classifyFilePath) {
		this.classifyDir = classifyFilePath;
	}

	private String root;
	private String evalDir;	
	private String alignDir;
	private String trainFilePath;
	private String testFilePath;
	private String classifyDir;
	private String l1;
	private String l2;
	private String l1TrainFile;
	private String l2TrainFile;
	private String l1TestFile;
	private String l2TestFile;
	private String l1EvalFile;
	private String l2EvalFile;
	private String l1TrainFileName;
	private String l2TrainFileName;
	private String l1TestFileName;
	private String l2TestFileName;
	private String l1EvalFileName;
	private String l2EvalFileName;
	private Integer positiveTrainingSentences; 
	private Integer negativeTrainingSentences;
	private Integer positiveTestingSentences;
	private Integer negativeTestingSentences;
	private Integer positiveEvalSentences;
	public Integer getPositiveEvalSentences() {
		return positiveEvalSentences;
	}



	public void setPositiveEvalSentences(Integer positiveEvalSentences) {
		this.positiveEvalSentences = positiveEvalSentences;
	}



	public Integer getNegativeEvalSentences() {
		return negativeEvalSentences;
	}



	public void setNegativeEvalSentences(Integer negativeEvalSentences) {
		this.negativeEvalSentences = negativeEvalSentences;
	}

	private Integer negativeEvalSentences;
	


	public boolean parseLine(String line){
		boolean parsed = false;
		String[] temp = line.split("=");
		if (temp.length != 2){
			System.out.println("Invalid line" + line);
			return false;
		}
		String field = temp[0];
		String value = temp[1].trim();
		if (field.equals("root")) {
			root = value;
			parsed = true;
		}
		else if (field.equals("l1")) {
			l1 = value;
			l1TrainFileName = "train." + l1;
			l1TestFileName = "test." + l1;
			l1EvalFileName = "eval." + l1;
			parsed = true;
		}
		else if (field.equals("l2")) {
			l2 = value;
			l2TrainFileName = "train." + l2;
			l2TestFileName = "test." + l2;
			l2EvalFileName = "eval." + l2;
			parsed = true;
		}

		else if (field.equals("positiveTrainingSentences")) {
			positiveTrainingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("negativeTrainingSentences")) {
			negativeTrainingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("positiveTestingSentences")) {
			positiveTestingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("negativeTestingSentences")) {
			negativeTestingSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("positiveEvalSentences")) {
			positiveEvalSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("negativeEvalSentences")) {
			negativeEvalSentences = Integer.parseInt(value);
			parsed = true;
		}
		else if (field.equals("\n")) {
			parsed=true;
		}
		else if (field.startsWith("#")){
			parsed=true;
		}

		return parsed;

	}




	public String getL1TrainFileName() {
		return l1TrainFileName;
	}



	public String getL2EvalFile() {
		return l2EvalFile;
	}



	public void setL2EvalFile(String l2EvalFile) {
		this.l2EvalFile = l2EvalFile;
	}



	public String getL1EvalFileName() {
		return l1EvalFileName;
	}



	public void setL1EvalFileName(String l1EvalFileName) {
		this.l1EvalFileName = l1EvalFileName;
	}



	public String getL1TrainFile() {
		return l1TrainFile;
	}



	public void setL1TrainFile(String l1TrainFile) {
		this.l1TrainFile = l1TrainFile;
	}



	public String getL2EvalFileName() {
		return l2EvalFileName;
	}



	public void setL2EvalFileName(String l2EvalFileName) {
		this.l2EvalFileName = l2EvalFileName;
	}



	public void setL1TrainFileName(String l1TrainFileName) {
		this.l1TrainFileName = l1TrainFileName;
	}



	public String getL2TrainFileName() {
		return l2TrainFileName;
	}



	public void setL2TrainFileName(String l2TrainFileName) {
		this.l2TrainFileName = l2TrainFileName;
	}



	public String getL1TestFileName() {
		return l1TestFileName;
	}



	public void setL1TestFileName(String l1TestFileName) {
		this.l1TestFileName = l1TestFileName;
	}



	public String getL2TestFileName() {
		return l2TestFileName;
	}



	public void setL2TestFileName(String l2TestFileName) {
		this.l2TestFileName = l2TestFileName;
	}



	@Override
	public String toString() {
		return "NewCommandLineArguments [root=" + root + ",\n evalDir=" + evalDir
				+ ",\n alignDir=" + alignDir + ",\n trainFilePath=" + trainFilePath
				+ ",\n testFilePath=" + testFilePath + ",\n classifyDir="
				+ classifyDir + ",\n l1=" + l1 + ",\n l2=" + l2 + ",\n l1TrainFile="
				+ l1TrainFile + ",\n l2TrainFile=" + l2TrainFile
				+ ",\n l1TestFile=" + l1TestFile + ",\n l2TestFile=" + l2TestFile
				+ ",\n l1EvalFile=" + l1EvalFile + ",\n l2EvalFile=" + l2EvalFile
				+ ",\n l1TrainFileName=" + l1TrainFileName + ",\n l2TrainFileName="
				+ l2TrainFileName + ",\n l1TestFileName=" + l1TestFileName
				+ ",\n l2TestFileName=" + l2TestFileName + ",\n l1EvalFileName="
				+ l1EvalFileName + ",\n l2EvalFileName=" + l2EvalFileName
				+ ",\n positiveTrainingSentences=" + positiveTrainingSentences
				+ ",\n negativeTrainingSentences=" + negativeTrainingSentences
				+ ",\n positiveTestingSentences=" + positiveTestingSentences
				+ ",\n negativeTestingSentences=" + negativeTestingSentences
				+ ",\n positiveEvalSentences=" + positiveEvalSentences
				+ ",\n negativeEvalSentences=" + negativeEvalSentences + "]";
	}







}
