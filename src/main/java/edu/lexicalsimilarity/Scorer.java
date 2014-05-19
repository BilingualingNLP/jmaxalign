package edu.lexicalsimilarity;

import java.io.*;
import java.util.*;

import edu.main.NewCommandLineArguments;

import edu.objects.NewSentence;
import edu.objects.NewSentencePair;
import edu.objects.Sentence;
import edu.objects.SentencePair;
import edu.objects.Word;

public class Scorer {

	private static ArrayList<String> l1StopWords, l2StopWords;
	private HashMap<String, ArrayList<String>> phrasesByWord = new HashMap<String, ArrayList<String>>();
	private HashMap<ArrayList<String>, Double> phraseProbabilities = new HashMap<ArrayList<String>, Double>();
	private HashMap<String, HashMap<String, Double>> L1toL2LexWeights = new HashMap<String, HashMap<String, Double>> ();
	private HashMap<String, HashMap<String, Double>>  L2toL1LexWeights = new HashMap<String, HashMap<String, Double>> ();
	private String l1,l2;


	NewCommandLineArguments cmdArgs;	


	public Scorer(NewCommandLineArguments cmd ){
		l1 = cmd.getL1();
		l2 = cmd.getL2();
		cmdArgs = cmd;

		//Load the words and phraes generaetd from training into memory
		//loadPhrasesIntoMemory();
		loadWordsIntoMemory();

		//		//Build Language Moels
		//		l1Model = new LanguageModel(l1, -2);
		//		l1Model.build();
		//		l2Model = new LanguageModel(l2, -2);
		//		l2Model.build();



	}


	public void score(String dataType, String l1SenPath, String l2SenPath,  PrintWriter dataWriter){
		try{
			BufferedReader l1_reader = new BufferedReader(new FileReader(new File(l1SenPath)));
			BufferedReader l2_reader = new BufferedReader(new FileReader(new File(l2SenPath)));
			String statistic;
			String l1_sentence, l2_sentence;
			int i = 0;
			while ((l1_sentence = l1_reader.readLine()) != null){
				i++;
				l2_sentence = l2_reader.readLine();
				statistic = computeLexicalSimilarity(l1_sentence, l2_sentence).outputForClassification();
				dataWriter.println(dataType + "\t" + statistic);
				//				statistic = compLexSimReturnSentencePair(l1_sentence, l2_sentence).outputForSVMClassification();
				//				dataWriter.println(statistic + "," + dataType);
				dataWriter.flush();
			}
			dataWriter.flush();
		}
		catch(IOException e){System.out.println(e.getMessage());}
	}

	/**
	 * Replaces punctuation in a string and lowercases it. This improves the accuracy of lexical detection
	 * @param s The string to be cleaned
	 * @return The cleaned version of the string 
	 */
	private static String clean(String s ){
		return s.toLowerCase().replace("{Punct}", "");
	}



	public NewSentencePair computeLexicalSimilarity(String l1Sen, String l2Sen){

		NewSentence[] sentences = new NewSentence[2];


		sentences[0] = new NewSentence(l1Sen, l1);
		sentences[1] = new NewSentence(l2Sen, l2);


		//		//Mark stop words 
		//		l1StopWords = l1Model.detectStopWords();
		//		sentences[0].markStopWords(l1StopWords);
		//
		//		l2StopWords  = l2Model.detectStopWords();
		//		sentences[0].markStopWords(l2StopWords);

		//Mark the OOV tokens
		sentences[0].markOOVWords(L1toL2LexWeights);
		sentences[1].markOOVWords(L2toL1LexWeights);


		ArrayList<Word> wordList = new ArrayList<Word>();
		for (int i = 0; i < sentences[0].getLength(); i++){
			Word w = sentences[0].getWords().get(i);
			if (L1toL2LexWeights.containsKey(w.getThisWord())){
				w.setAlignmentScore(0);
				w.setOutOfVocabularyWord(false);
				HashMap<String, Double> candidateTranslations = L1toL2LexWeights.get(w.getThisWord());
				for (String translation : candidateTranslations.keySet()){ //each translation 
					for (Word potentialMatch : sentences[1].getWords()){ //each word on the other side 
						if (potentialMatch.getThisWord().equals(translation)){ //match found 
							w.setAligned(true);
							w.addAlignedWord(potentialMatch, candidateTranslations.get(translation));
							w.setNumberAlignedWords(w.getNumberAlignedWords() +1);
						}
					}
				}

			}
			wordList.add(w);
		}
		sentences[0].setWords(wordList);

		wordList = new ArrayList<Word>();
		for (int i = 0; i < sentences[1].getLength(); i++){
			Word w = sentences[1].getWords().get(i);
			if (L1toL2LexWeights.containsKey(w.getThisWord())){
				w.setOutOfVocabularyWord(false);
				w.setAlignmentScore(0);
				HashMap<String, Double> candidateTranslations = L1toL2LexWeights.get(w.getThisWord());
				for (String translation : candidateTranslations.keySet()){ //each translation 
					for (Word potentialMatch : sentences[0].getWords()){ //each word on the other side 
						if (potentialMatch.getThisWord().equals(translation)){ //match found 

							w.setAligned(true);
							w.addAlignedWord(potentialMatch, candidateTranslations.get(translation));
							w.setNumberAlignedWords(w.getNumberAlignedWords() +1);
						}
					}
				}
			}
			wordList.add(w);
		}
		sentences[1].setWords(wordList);
		/*
		 * 
	private ArrayList<Word> alignedWords  = new ArrayList<Word>();
	private int numberAlignedWords;

		 */

		NewSentencePair sentencePair = new NewSentencePair(sentences[0], sentences[1]);
		return sentencePair;


	}

	private  String computeLexicalSimilarity(String l1Sen, String l2Sen, String parallelOrComporable){


		Sentence l1Sentence = new Sentence(l1Sen, l1);
		Sentence l2Sentence = new Sentence(l2Sen, l2);


		//Get some statistics about the sentence


		//Get the words in each sentence 
		ArrayList<String> l1Words = l1Sentence.getWords();
		ArrayList<String> l2Words = l2Sentence.getWords();


		//Mark the OOV tokens
		l1Sentence.markOOVWords(L1toL2LexWeights);
		l2Sentence.markOOVWords(L2toL1LexWeights);

		//Generate the SentencePair
		SentencePair sentences = new SentencePair(l1Sentence, l2Sentence);


		int l1Matches = 0;
		int l2Matches = 0;
		//		//calculate lexical overlap from l1 to l2 

		for (int i = 0; i < l1Words.size(); i++){
			ArrayList<String> alignments = new ArrayList<String>();
			String word = clean(l1Words.get(i));					
			sentences.setl1IsAligned(i, false);
			HashMap<String, Double> candidateTranslations = L1toL2LexWeights.get(word);
			if (candidateTranslations != null){
				sentences.setl1Alignments(i, candidateTranslations.size());

				//Go over each translation, and see if it occurs on the other side 
				for (String translation : candidateTranslations.keySet()){ //each translation 
					for (String potentialMatch : l2Words){ //each word on the other side 
						if (clean(potentialMatch).equals(clean(translation))){ //match found 
							sentences.setl1IsAligned(i, true);
							alignments.add(potentialMatch);
							System.out.println(potentialMatch   + " matches" + translation);

							l1Matches++;
						}
					}
				}
				sentences.setl1AlignedValues(i, alignments);
				sentences.setl1AlignmentsOnOtherSide(i, alignments.size());

				//Mark the number of matches 


			}
			else{
				sentences.setl1Alignments(i, 0);
				sentences.setl1AlignedValues(i, null);
				sentences.setl1AlignmentsOnOtherSide(i, 0);

			}

		}

		//;
		//calculate lexical overlap from l2 to l1
		for (int i = 0; i < l2Words.size(); i++){
			String word = clean(l2Words.get(i));
			sentences.setl2IsAligned(i, false);
			ArrayList<String> alignments = new ArrayList<String>();

			HashMap<String, Double> candidateTranslations = L2toL1LexWeights.get(word);
			if (candidateTranslations != null){
				sentences.setl2Alignments(i, candidateTranslations.size());
				//If we have candidate translations, check if they appear in the other sentence
				for (String translation : candidateTranslations.keySet()){
					for (String potentialMatch : l1Words){
						if (clean(potentialMatch).equals(translation)){
							l2Matches++;
							sentences.setl2IsAligned(i, true);
							sentences.setl2AlignedValues(i, alignments);
						}
					}
				}
			}
			else{
				sentences.setl2Alignments(i, 0);
			}
			sentences.setl2AlignmentsOnOtherSide(i, l2Matches);
			sentences.setl1AlignedValues(i, alignments);


		}

		//
		//		//calculate phrasal overlap from l1 to l2 
		//		for (String word : l1Words){
		//			HashMap<String, Double> candidateTranslations = L1toL2LexWeights.get(word);
		//			if (candidateTranslations != null){
		//				//If we have candidate translations, check if they appear in the other sentence
		//				for (String translation : candidateTranslations.keySet()){
		//					for (String potentialMatch : l2Words){
		//						if (potentialMatch.equals(translation)){
		//							l1Matches++;
		//						}
		//					}
		//				}
		//			}
		//		}
		System.out.println("***");
		System.out.println(sentences);
		//		System.out.println(sentences.outputForClassification(parallelOrComporable));
		System.out.println("***");

		return sentences.outputForClassification(parallelOrComporable);

	}


	private void loadWordsIntoMemory(){
		L1toL2LexWeights = loadWeightsIntoMemory(l1, .3);
		L2toL1LexWeights= loadWeightsIntoMemory(l2, .3);
	}

	public HashMap<String, HashMap<String, Double>> loadWeightsIntoMemory(String lang,  double thresh){
		HashMap<String, HashMap<String, Double>> everything = new HashMap<String, HashMap<String, Double>>();
		HashMap<String, Double> translations = new HashMap<String, Double>();
		try{
			BufferedReader wordFile = new BufferedReader(new FileReader(cmdArgs.getGoodLexweightsPath(lang)));
			String line;

			line = wordFile.readLine();
			String[] temp = line.split("\\t");
			String key = temp[0].trim();

			while ((line = wordFile.readLine()) != null){
				//key
				if (line.contains("nTrans")){
					everything.put(key, translations);	
					translations = new HashMap<String, Double>();
					temp = line.split("\\t");
					key = temp[0].trim();

				}
				else{
					temp = line.split(":");
					if (temp.length > 1){
						try{
							Double d = Double.parseDouble(temp[1]);
							//							if (d > thresh)
							translations.put(temp[0].trim(), d);
						}
						catch(NumberFormatException e){}


					}
				}
			}

			return everything;

		}
		catch(IOException e){System.out.println(e.getMessage());}
		return everything;
	}

	private void loadPhrasesIntoMemory(){
		try{
			BufferedReader phraseFile = new BufferedReader(new FileReader(""));
			String line;

			while ((line = phraseFile.readLine()) != null){

				String[] phrases = line.split("\\|\\|\\|");
				String phrase = phrases[0].trim();
				String translationPhrase = phrases[1].trim();
				String[] phraseArray = phrase.split(" ");
				String[] translationPhraseArray = translationPhrase.split(" ");

				ArrayList<String> phrasePairs = new ArrayList<String>();
				phrasePairs.add(phrase);
				phrasePairs.add(translationPhrase);

				for (String s: phraseArray)
					phrasesByWord.put(s, phrasePairs);
				for (String s: translationPhraseArray)
					phrasesByWord.put(s, phrasePairs);


				String[] probability = phrases[4].split(" ");
				phraseProbabilities.put(phrasePairs, Double.parseDouble(probability[1]));
			}

		}
		catch(IOException e){}
	}
}
