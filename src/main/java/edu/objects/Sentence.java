package edu.objects;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * @author kaufmann
 * This is the data structure for storing a sentence from a particular langauge
 */
public class Sentence {

	
	/**
	 * The content of the sentence
	 */
	private String  statement;
	
	/**
	 * The language of the sentence
	 */
	private String language;
	
	/**
	 * The words in the sentence
	 */
	private ArrayList<String> words = new ArrayList<String>(); 
	
	
	public ArrayList<Boolean> getStopWords() {
		return stopWords;
	}

	public void setStopWords(ArrayList<Boolean> stopWords) {
		this.stopWords = stopWords;
	}

	public ArrayList<Boolean> getOOVWords() {
		return OOVWords;
	}

	public void setOOVWords(ArrayList<Boolean> oOVWords) {
		OOVWords = oOVWords;
	}
	private ArrayList<Boolean> stopWords = new ArrayList<Boolean>(); 
	private ArrayList<Boolean> OOVWords = new ArrayList<Boolean>(); 

	private int length;
	private int outOfVocabTokens;


	public ArrayList<String> getWords() {
		return words;
	}

	/**
	 * This modifies the ArrayList stopWords so that the boolean values reflect whether 
	 * the corresponding word in the ArrayList words is a stop word. Example:
	 * words = 		[This, is, a, crazy, sentence]
	 * stopwords = 	[F, T, T, F, F]
	 * @param stopWords A list of stop words
	 */
	public void markStopWords(ArrayList<String> listOfStopWords){
		for (int i = 0; i < words.size(); i ++ ){
			if (listOfStopWords.contains(words.get(i))){
				stopWords.add(i, true);
			}
			else
				stopWords.add(i, false);
		}
	}
	/**
	 * This modifies the ArrayList OOVTokens so that the boolean values reflect whether 
	 * the corresponding word in the ArrayList words is a stop word. Example:
	 * words = 		[This, is, a, crazy, sentence]
	 * OOVWords = 	[T, T, T, T, T] (if we know translations for all the words) 
	 * This method also calculates the total number of OOV tokens
	 * @param stopWords A list of stop words
	 */
	public void markOOVWords(HashMap<String, HashMap<String, Double>> listofOOVWords){
		int oovTokens = 0;
		for (int i = 0; i < words.size(); i ++ ){
			if (!(listofOOVWords.containsKey(words.get(i)))){
				OOVWords.add(i, true);
				oovTokens++;
			}
			else
				OOVWords.add(i, false);
		}
		outOfVocabTokens = oovTokens;
		
	}

/**
 * Gets the length of the sentence
 * @return  The language of the sentence, using ISO-639-2 codes
 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets the length of the sentence
	 * @return The number of words in thh sentence
	 */
	public int getLength() {
		return words.size();
	}

	/**
	 * Gets the number of OOV tokens of the sentence
	 * @return The number of words in the sentence that have no known translations
	 */
	public int getOutOfVocabTokens() {
		return outOfVocabTokens;
	}

 /**
  * Gets the text of the sentence
  * @return The sentence, formatted as a String
  */
	public String getStatement() {
		return statement;
	}
/**
 * Creates a Sentence
 * @param sen The string containing the sentence
 * @param lang The language of the sentence
 */
	public Sentence(String sen, String lang){
		statement = sen;
		language = lang;

		//Split the sentence into words
		String[] temp  = sen.split(" ");
		for (String s : temp)
			words.add(s.toLowerCase().trim());
	}
	
	
}
