package edu.objects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

public class NewSentencePair {

	NewSentence l1Sentence, l2Sentence;
	ArrayList<NewSentence> sentences = new ArrayList<NewSentence>();

	public NewSentencePair(NewSentence sen1, NewSentence sen2) {
		if ((sen1 == null) || (sen2 == null)){
			System.out.println("One of the input sentences is null;");
		}
		
		l1Sentence = sen1;
		l2Sentence = sen2;
		sentences.add(l1Sentence);
		sentences.add(l2Sentence);


	}



	
	public static float divideIntegers(int i, int j){
		return (float) i * 100.0f / (float) j;
	}
	public String outputForClassification(){
		String s = "";
//		s+= l1Sentence.getLength();
//		s+="\t";
//		s+= l2Sentence.getLength();
//		s+="\t";
		s+= lengthDifference();
		s+="\t";
		s+= lengthRatio();
		s+="\t";
		s+=percentageL1WordsThatHaveL2Translation();
		s+="\t";
		s+=percentageL2WordsThatHaveL1Translation();
		s+="\t";
		s+= numberUnAlignedWords();
		s+="\t";
		s+= percentageUnAlignedWords();
		s+="\t";
		s+= numFertilities(1);
		s+="\t";
		s+= numFertilities(2);
		s+="\t";
		s+= numFertilities(3);
		s+="\t";
		s+= longestContigiousSpan();
		s+="\t";
		s+= alignmentScore();
		s+="\t";
		return s;
	}
	
	public String outputForSVMClassification(){
		String s = "";
//		s+= l1Sentence.getLength();
//		s+=",";
//		s+= l2Sentence.getLength();
//		s+=",";
		s+= lengthDifference();
		s+=",";
		s+= lengthRatio();
		s+=",";
		s+=percentageL1WordsThatHaveL2Translation();
		s+=",";
		s+=percentageL2WordsThatHaveL1Translation();
		s+=",";
		s+= numberUnAlignedWords();
		s+=",";
		s+= percentageUnAlignedWords();
		s+=",";
		s+= numFertilities(1);
		s+=",";
		s+= numFertilities(2);
		s+=",";
		s+= numFertilities(3);
		s+=",";
		s+= longestContigiousSpan();
		s+=",";
		s+= alignmentScore();
		s+=",";
		return s;
	}
	
	public int lengthDifference(){
		return l1Sentence.getLength() - l2Sentence.getLength();
	}

	public float   lengthRatio(){
		return divideIntegers(l1Sentence.getLength(), l2Sentence.getLength());
	}

	public float percentageUnAlignedWords(){
		int  unalignedWords = numberUnAlignedWords();
		int totalWords = l1Sentence.getLength() + l2Sentence.getLength() ;
		return divideIntegers(unalignedWords, totalWords);
	}

	public int numberUnAlignedWords(){
		int unalignedWords = 0;

		for (NewSentence sen: sentences){
			for (Word w : sen.getWords()){
				if (!(w.isAligned())){
					unalignedWords++;
				}
			}

		}
		return unalignedWords; 
	}

	public int numberUnAlignedAndUnkownWords(){
		int unalignedWords = 0;

		for (NewSentence sen: sentences){
			for (Word w : sen.getWords()){
				if ( (!(w.isAligned())) && (w.isOutOfVocabularyWord())) {
					unalignedWords++;
				}
			}
		}
		return unalignedWords; 
	}

	public int numberUnknownWords(){
		int unalignedWords = 0;

		for (NewSentence sen: sentences){
			for (Word w : sen.getWords()){
				if (!(w.isOutOfVocabularyWord())){
					unalignedWords++;
				}
			}
		}
		return unalignedWords; 
	}

	public float  percentageL1WordsThatHaveL2Translation(){
		int alignedWords = 0;
		int totalWords = l1Sentence.getWords().size();
		for (Word w : l1Sentence.getWords()){
			if (w.isAligned())
				alignedWords++;
		}

		if (alignedWords != 0)
			return divideIntegers(alignedWords, totalWords);
		else
			return 0;
	}

	public float  percentageL2WordsThatHaveL1Translation(){
		int alignedWords = 0;
		int totalWords = l2Sentence.getWords().size();
		for (Word w : l2Sentence.getWords()){
			if (w.isAligned())
				alignedWords++;
		}

		if (alignedWords != 0)
			return divideIntegers(alignedWords, totalWords);
		else
			return 0;
	}


	public double percentageUnknownWords(){
		double unalignedWords = numberUnknownWords();
		double totalWords = l1Sentence.getLength() + l2Sentence.getLength() ;
		return unalignedWords / totalWords;
	}



	public int longestContigiousSpan(){
		int[] lcs = new int[2];

		for (int i = 0; i < 2; i++){
			NewSentence s = sentences.get(i);
			for (Word w : s.getWords()){
				if (w.isAligned()){
					lcs[i]++;
				}
				else
					lcs[i] = 0;
			}
		}

		if (lcs[0] > lcs[1])
			return lcs[0];

		else
			return lcs[1];
	}


	public TreeSet<Integer> largestThreeFertilities(){
		TreeSet<Integer>  fertilities = new TreeSet<Integer>();
		for (NewSentence sen: sentences){
			for (Word w : sen.getWords()){
				int fertility = w.getNumberAlignedWords();
				fertilities.add(fertility);
			}
		}

		TreeSet<Integer>  largestFertilities= new TreeSet<Integer>();
		Iterator<Integer> itr = fertilities.descendingIterator();
		for (int i = 0; i < 2; i++){
			if (itr.hasNext()){
				largestFertilities.add(itr.next());
			}
		}


		return largestFertilities;
	}

	public int numFertilities(int size){
		int numFertilities = 0;
		for (NewSentence sen: sentences){
			for (Word w : sen.getWords()){
				if (size == w.getNumberAlignedWords())
					numFertilities++;
			}
		}
		return numFertilities;


	}

	public double alignmentScore(){
		double score = 0;
		for (NewSentence sen: sentences){
			for (Word w : sen.getWords()){
				score *= w.getAlignmentScore();
			}
		}

		return score;
	}


	public String toString(){
		String s = "";
		s += l1Sentence.getLanguage() + ": " + l1Sentence.toString() + "\n"; //+ l1Sentence.wordStatistics();
		s += l2Sentence.getLanguage() + ": " + l2Sentence.toString() + "\n"; // + l2Sentence.wordStatistics();
		return s;
	}
}
