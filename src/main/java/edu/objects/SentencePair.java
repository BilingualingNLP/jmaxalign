package edu.objects;

import java.util.ArrayList;


/**
 * @author Max Kaufmann
 * This class contains 
 */
/**
 * @author kaufmann
 *
 */
public class SentencePair {

	//The sentences that comprise the sentence pair
	private Sentence l1Sentence, l2Sentence;

	//The ratio of word and character lengths of the two sentences. 
	private double wordLengthRatio, characterLengthRatio; 

	//This class makes use of parallel arrays to store data about each of the words in the sentence. 
	//The governing parallel array is l1Words and l2Words. All arrayList twih the l1 prefix are 
	//gauranteed to be as long as the l1Words list. Likewise, all ArrayLists that start with the l2 prefix
	//will be as long as the l2Words list. 
	//So to decide if the 5th word in the l1 sentence is a stop word, you could use l1StopWords.get(4)
	private ArrayList<String> l1Words, l2Words;
	private ArrayList<Boolean> l1WordAligned= new ArrayList<Boolean>();
	private ArrayList<Boolean> l2WordAligned= new ArrayList<Boolean>();

	private ArrayList<Boolean> l1StopWords;
	private ArrayList<Boolean> l2StopWords;
	private ArrayList<Boolean> l1OOVWords, l2OOVWords;
	private ArrayList<AlignmentTypes> l1AlignmentTypes, l2AlignmentTypes;
	private ArrayList<Integer> l1TotalAlignments = new ArrayList<Integer>();
	private ArrayList<Integer> l2TotalAlignments= new ArrayList<Integer>();
	private ArrayList<Integer> l1AlignmentsOnOtherSide;
	private ArrayList<Integer> l2AlignmentsOnOtherSide;
	private ArrayList<ArrayList<String>> l1AlignedValues;
	private ArrayList<ArrayList<String>> l2AlignedValues;

	//The following methods are setters, which allow you to set the value for individual elements in the arrays
	//Array Creation is handled by these methods, so you don't have to worry about initializing them. 

	public void setl1AlignedValues(int index, ArrayList<String> value){
		if (l1AlignedValues == null){
			l1AlignedValues = new ArrayList<ArrayList<String>>(l1Words.size());
			l1AlignedValues.add(value);
		}
		else{
			l1AlignedValues.add(index, value);
		}
	}

	public void setl2AlignedValues(int index, ArrayList<String> value){
		if (l2AlignedValues == null){
			l2AlignedValues = new ArrayList<ArrayList<String>>(l1Words.size());
			l2AlignedValues.add(value);
		}
		else{
			l2AlignedValues.add(index, value);
		}
	}

	public void setl1Alignments(int index, int value){
		if (l1TotalAlignments == null){
			l1TotalAlignments = new ArrayList<Integer>(l1Words.size());
			l1TotalAlignments.add(value);
		}
		else{
			l1TotalAlignments.add(index, value);
		}
	}
	public void setl2Alignments(int index, int value){
		if (l2TotalAlignments == null){
			l2TotalAlignments = new ArrayList<Integer>(l2Words.size());
			l2TotalAlignments.add(value);
		}
		else{
			l2TotalAlignments.add(index, value);
		}

	}
	public void setl1IsAligned(int index, boolean value){
		if (l1WordAligned == null){
			l1WordAligned = new ArrayList<Boolean>(l1Words.size());
			l1WordAligned.add(value);
		}
		else{
			l1WordAligned.add(index, value);
		}
	}
	public void setl2IsAligned(int index, boolean value){
		if (l2WordAligned == null){
			l2WordAligned = new ArrayList<Boolean>(l2Words.size());
			l2WordAligned.add(value);
		}
		else{
			l2WordAligned.add(index, value);
		}
	}	public void setl1AlignmentsOnOtherSide(int index, int value){
		if (l1AlignmentsOnOtherSide == null){
			l1AlignmentsOnOtherSide = new ArrayList<Integer>(l1Words.size());
			l1AlignmentsOnOtherSide.add(value);
		}
		else{
			l1AlignmentsOnOtherSide.add(index, value);
		}
	}	public void setl2AlignmentsOnOtherSide(int index, int value){
		if (l2AlignmentsOnOtherSide == null){
			l2AlignmentsOnOtherSide = new ArrayList<Integer>(l2Words.size());
			l2AlignmentsOnOtherSide.add( value);
		}
		else{
			l2AlignmentsOnOtherSide.add(index, value);
		}
	}

	//Returns the L1 sentence
	public Sentence getL1Sentence() {
		return l1Sentence;
	}


	public Sentence getL2Sentence() {
		return l2Sentence;
	}
	public ArrayList<Boolean> getL1WordAligned() {
		return l1WordAligned;
	}



	public void setL1WordAligned(ArrayList<Boolean> l1WordAligned) {
		this.l1WordAligned = l1WordAligned;
	}

	public ArrayList<Boolean> getL2WordAligned() {
		return l2WordAligned;
	}

	public void setL2WordAligned(ArrayList<Boolean> l2WordAligned) {
		this.l2WordAligned = l2WordAligned;
	}



	/**
	 * Takes two sentences, and combines them into a SentencePair. 
	 * 
	 * @param sen1 A sentence in one language
	 * @param sen2 A sentence in a different language
	 */
	public SentencePair(Sentence sen1, Sentence sen2) {
		if ((sen1 == null) || (sen2 == null)){
			System.out.println("One of the input sentences is null;");
		}

		l1Sentence = sen1;
		l2Sentence = sen2;

		//Calcualte the word and length ratios between the two sentences
		wordLengthRatio = calculateWordLengthRatio();
		characterLengthRatio = calculateCharacterLengthRatio();


		//Copy some of the information from the individual Sentences into variables in the SentencePair
		//so they are easier to reference later. 
		l1Words = l1Sentence.getWords();
		l2Words = l2Sentence.getWords();

		l1StopWords = l1Sentence.getStopWords();
		l2StopWords = l2Sentence.getStopWords();

		l1OOVWords = l1Sentence.getOOVWords();
		l2OOVWords = l2Sentence.getOOVWords();

	}



	public void setL1Sentence(Sentence l1Sentence) {
		this.l1Sentence = l1Sentence;
	}
	public void setL2Sentence(Sentence l2Sentence) {
		this.l2Sentence = l2Sentence;
	}
	/**
	 * @return The ratio of the amount of words between two sentences, such that Length of the first sentence* ratio = The length of the second sentence
	 */
	public double calculateWordLengthRatio(){
		double l1Length = l1Sentence.getLength();
		double l2Length = l2Sentence.getLength();

		return (l2Length / l1Length);

	}
	/**
	 * @return The ratio of the amount of characters between two sentences, such that Length of the first sentence* ratio = The length of the second sentence
	 */
	public double calculateCharacterLengthRatio(){
		double l1Length = l1Sentence.getStatement().toCharArray().length;
		double l2Length = l2Sentence.getStatement().toCharArray().length;
		return (l2Length / l1Length);

	}






	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("L1 Sentence: " + l1Sentence.getStatement() + "\n");
		sb.append("L2 Sentence: " + l2Sentence.getStatement() + "\n");
		sb.append("Word Length Ratio: " + wordLengthRatio + "\n");
		sb.append("Character Length Ratio: " + characterLengthRatio + "\n");
		for (int i = 0; i < l1Words.size(); i++){
			sb.append("\t" + l1Words.get(i)  + "\n");
			sb.append("\t\tAligned: " + l1WordAligned.get(i) + "\n");
			if (l1WordAligned.get(i)){
				sb.append("\t\t\tNumber of words aligned to in corpus: " + l1TotalAlignments.get(i) + "\n");
				sb.append("\t\t\tNumber of words aligned to in other sentence: " + l1AlignmentsOnOtherSide.get(i)+ "\n");
				
					sb.append("\t\t\tWords aligned to in other sentence: ");
					for (String str : l1AlignedValues.get(i)){
						sb.append(str + "a\t");
					}
				

			}
			sb.append("\t\tStopWords: " + l1StopWords.get(i) + "\n");
			sb.append("\t\tOOV: " + l1OOVWords.get(i) + "\n");
		}
//		for (int i = 0; i < l2Words.size(); i++){
//			sb.append("\t" + l2Words.get(i)  + "\n");
//			sb.append("\t\tAligned: " + l2WordAligned.get(i) + "\n");
//			if (l2WordAligned.get(i)){
//				sb.append("\t\t\tNumber of words aligned to in corpus: " + l2TotalAlignments.get(i) + "\n");
//				sb.append("\t\t\tNumber of words aligned to in other sentence: " + l2AlignmentsOnOtherSide.get(i)+ "\n");
//				
//				if (l2AlignedValues.get(i) != null){
//					sb.append("\t\t\tWords aligned to in other sentence: ");
//					for (String s : l2AlignedValues.get(i)){
//						sb.append(s+ "\t");
//					}
//				}
//
//			}
//			sb.append("\t\tStopWords: " + l2StopWords.get(i) + "\n");
//			sb.append("\t\tOOV: " + l2OOVWords.get(i) + "\n");
//		}
		return sb.toString();
	}

	/**
	 * @param parallelOrComporable, there are two acceptable for values for this "Parallel", or "Comporable
	 * "Parallel" should be used if the sentences being classified are parallel
	 * "Comporable" shoudl be used if it is unknown whether the sentences being classified are parallel
	 * @return The string to be output to a classification file for the Stanford Classifier
	 */
	public String outputForClassification(String parallelOrComporable){

		StringBuilder sb = new StringBuilder();
		sb.append(parallelOrComporable);
		sb.append("\t");
		sb.append(wordLengthRatio); 
		sb.append("\t");
		sb.append(percentAlignedWordsExcludingStopWords());  
		sb.append("\t");
		sb.append(percentAlignedWordsIncludingStopWords()); 


		return sb.toString();
	}

	private double computeAlingmentScore(){
		double score = 0;

		return score;
	}


	//Util
	/**
	 * Summarizes each item in a list of Integers. 
	 * @param list, A list of Integers
	 * @return the total of all the items added together
	 */
	private  int sumIntArrayList(ArrayList<Integer> list){
		int sum = 0;
		for (Integer i : list)
			sum +=i;
		return sum;
	}

	/**
	 * Counts the number of booleans in a list of Booleans.
	 * @param list, a list of Booleans
	 * @return The number of true booleans. 
	 */
	private int countBooleansInArrayList(ArrayList<Boolean> list){
		int sum = 0;
		for (boolean b : list)
			if (b == true)
				sum++;
		return sum;
	}


	//Statistics

	//not done
	private double percentAlignedWordsExcludingStopWords(){
		int alignedWords = 0;
		double percent = 0;
		for (int i = 0; i < l1Words.size(); i++){
			if (l1WordAligned.get(i) && (!(l1StopWords.get(i)))){
				alignedWords++;
			}
		}
		//		for (int i = 0; i < l2Words.size(); i++){
		//			if (l2WordAligned.get(i) && (!(l2StopWords.get(i)))){
		//				alignedWords++;
		//			}
		//		}
		percent = (double)alignedWords / l1Words.size();  
		return percent;
	}


	//not done 
	private double percentAlignedWordsIncludingStopWords(){
		int alignedWords = 0;
		double percent = 0;

		for (int i = 0; i < l1Words.size(); i++){
			if (l1WordAligned.get(i) ){
				alignedWords++;
			}
		}		
		//		for (int i = 0; i < l2Words.size(); i++){
		//			if (l2WordAligned.get(i)){
		//				alignedWords++;
		//			}
		//		}
		percent = (double)alignedWords / l1Words.size();  
		return percent;

	}



}
