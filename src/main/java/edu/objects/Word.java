package edu.objects;

import java.util.HashMap;

public class Word {

	private String thisWord;
	private double alignmentScore = 0;
	private boolean aligned = false;
	private HashMap<String, Double> alignedWords = null;
	private int numberAlignedWords = 0; 
	private boolean stopWord = false;
	private boolean outOfVocabularyWord = false;





	public void addAlignedWord(Word w, double d){
		if (alignedWords == null)
			alignedWords = new HashMap<String, Double>();
		alignedWords.put(w.getThisWord(), d);
	}

	public String getThisWord() {
		return thisWord;
	}

	public void setThisWord(String thisWord) {
		this.thisWord = thisWord;
	}

	public boolean isStopWord() {
		return stopWord;
	}

	public void setStopWord(boolean stopWord) {
		this.stopWord = stopWord;
	}

	public boolean isOutOfVocabularyWord() {
		return outOfVocabularyWord;
	}

	public void setOutOfVocabularyWord(boolean outOfVocabularyWord) {
		this.outOfVocabularyWord = outOfVocabularyWord;
	}


	public Word(String w){
		thisWord = w;
	}

	public int getNumberAlignedWords() {
		if (alignedWords == null){
			return 0;
		}
		numberAlignedWords = alignedWords.size();
		return numberAlignedWords;
	}

	public void setNumberAlignedWords(int numberAlignedWords) {
		this.numberAlignedWords = numberAlignedWords;
	}


	public double getAlignmentScore() {
		if (!(this.isAligned()))
			return 0;
		else{
			double score  = 0;
			for (String s : alignedWords.keySet()){
				if (alignedWords.get(s) > score )
					score = alignedWords.get(s);
			}
			return score;
		}

	}
	public void setAlignmentScore(double alignmentScore) {
		this.alignmentScore = alignmentScore;
	}
	public boolean isAligned() {
		return aligned;
	}
	public void setAligned(boolean alig) {
		this.aligned = alig;
	}
	public HashMap<String, Double> getAlignedWords() {
		return alignedWords;
	}
	public void setAlignedWords(HashMap<String, Double> alignedWords) {
		this.alignedWords = alignedWords;
	}


	public String toString(){
		String s =  "Word: " + this.getThisWord()+ "\n";
		s+= "AlignmentScore: " + this.getAlignmentScore() + "\n";
		if (this.isAligned()){
			s+= "Number Aligned Words: " + this.getNumberAlignedWords() + "\n";
			String words = "";
			for (String st : this.getAlignedWords().keySet()){
				words+= st + " (" + alignedWords.get(st) + ")";
			}
			s+= "alignedWords: " + words + "\n";
		}
		else
			s+="Unaligned\n";
		s+= "Stop Word: " + this.isStopWord() + "\n";
		s+= "OOV Word: " + this.isOutOfVocabularyWord() + "\n";

		return s;
	}

}
