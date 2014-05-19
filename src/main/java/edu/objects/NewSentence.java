package edu.objects;

import java.util.ArrayList;
import java.util.HashMap;

public class NewSentence {

	private ArrayList<Word> words = new ArrayList<Word>();
	private String language;
	private int length; 
	private String sen;

	public NewSentence(String statement, String l){
		sen = statement;
		language = l;
		for (String s : statement.replaceAll("\\p{Punct}|\\d","").toLowerCase().split(" ")){
			words.add(new Word(s));
		}
		length = words.size();
	}



	public String toString(){
		return sen;
	}
	public String getLanguage() {
		return language;
	}


	public void setLanguage(String language) {
		this.language = language;
	}


	public int getLength() {
		return length;
	}


	public void setLength(int length) {
		this.length = length;
	}


	public ArrayList<Word> getWords() {
		return words;
	}


	public void setWords(ArrayList<Word> words) {
		this.words = words;
	}

	
	


	public void markStopWords(ArrayList<String> listOfStopWords){
		for (int i = 0; i < words.size(); i ++ ){
			if (listOfStopWords.contains(words.get(i))){
				words.get(i).setStopWord(true);
			}
			else
				words.get(i).setStopWord(false);
		}
	}
	
	public void markOOVWords(HashMap<String, HashMap<String, Double>> listofOOVWords){
		for (int i = 0; i < words.size(); i ++ ){
			if (!(listofOOVWords.containsKey(words.get(i)))){
				words.get(i).setOutOfVocabularyWord(true);
			}
			else
				words.get(i).setOutOfVocabularyWord(false);
		}
	}
	
	public String wordStatistics(){
		String s = "";
		for (Word w : this.getWords()){
			s+=w;
			s+="\n";
		}
		return s;
	}
	
}

