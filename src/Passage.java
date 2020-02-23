/**
 * @author Luke Kaicher
 * ID: 112356778
 * luke.kaicher@stonybrook.edu
 * Assignment #6
 * CSE214
 * R01 Daniel Calabria
 * 
 * This class is a hash table that reads a text file, determines the number of 
 * occurrences for each word (excluding stop words), and maps the number of 
 * occurrences to each word in the hash table.
 */
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.io.*;

public class Passage extends Hashtable<String, Double> {
	private String title;
	private int wordCount = 0;
	private Hashtable<String, Double> similarTitles;
	private File stopWordsFile;
/**
 * Getter for the title of the Passage
 * @return
 * the title of the Passage
 */
	public String getTitle() {
		return this.title;
	}
/**
 * Getter for the total word count, excluding stopwords, of the Passage 
 * @return
 * The word count of the Passage
 */
	public int getWordCount() {
		return this.wordCount;
	}
/**
 * Getter for the SimilarTitles Hashtable
 * @return
 * the SimilarTitles Hashtable, which holds additional Passages and the 
 * similarity percantage between the Passage and each additional Passage
 */
	public Hashtable<String, Double> getSimilarTitles(){
		return this.similarTitles;
	}
/**
 * Getter for the stopWordsFile of the Passage
 * @return
 * the stopWordsFile File object
 */
	public File getStopWordsFile() {
		return this.stopWordsFile;
	}
/**
 * Setter for the title of the Passage
 * @param title
 * the String that will be set to the title
 */
	public void setTitle(String title) {
		this.title = title;
	}
/**
 * Setter for the wordCount of the Passage
 * @param wordCount
 * the int that will be set to the wordCount
 */
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
/**
 * Setter for the similarTitles Hashtable of the Passage
 * @param similarTitles
 * the Hashtable that will be set to SimilarTitles
 */
	public void setSimilarTitles(Hashtable<String, Double> similarTitles) {
		this.similarTitles = similarTitles;
	}
/**
 * Setter for the stopWordsFile of the Passage
 * @param stopWords
 * the File that will be set to the StopWordsFile
 */
	public void setStopWordsFile(File stopWords) {
		this.stopWordsFile = stopWords;
	}
	/**
	 * Constructor for a Passage Hashtable.
	 * @param title
	 * The title of the passage
	 * @param file
	 * The text file of the passage. file is parsed to generate the Hashtable.
	 */
	public Passage(String title, File file) {
		this.title = title.replace(".txt", "");
		this.similarTitles = new Hashtable<String, Double>();
		this.parseFile(file);
	}
	/**
	 * Constructor for a Passage Hashtable.
	 * @param title
	 * The title of the passage
	 * @param file
	 * The text file of the passage. file is parsed to generate the Hashtable.
	 * @param stopWords
	 * The text file that contains the stop words to ignore when parsing.
	 */
	public Passage(String title, File file, File stopWords) {
		this.title = title;
		this.stopWordsFile = stopWords;
		this.similarTitles = new Hashtable<String, Double>();
		this.parseFile(file);
	}
	/**
	 * Parses a text file to generate the Passage Hashtable
	 * @param file
	 * The file that is to be parsed
	 */
	public void parseFile(File file) {
		BufferedReader fileIn;
		try {
			fileIn = new BufferedReader(new FileReader(stopWordsFile));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Stop words file not found");
			return;
		}
		int stopWordsCount = 0;
		String stopWord = null;
		try {
			stopWord = fileIn.readLine();
		} catch (IOException e1) {
			System.out.print("Error: StopWords file could not be read");
			return;
		}
		while (stopWord != null) {
			stopWordsCount++;
			try {
				stopWord = fileIn.readLine();
			} catch (IOException e) {
				System.out.print("Error: StopWords file could not be read");
				return;
			}
		}
		try {
			fileIn.close();
		} catch (IOException e1) {
			System.out.print("Error: StopWords bufferedreader could not be "
			  + "closed");
			return;
		}
		try {
			fileIn = new BufferedReader(new FileReader(stopWordsFile));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Stop words file not found");
			return;
		}
		String[] stopWordsArray = new String[stopWordsCount];
		for (int i = 0; i < stopWordsCount; i++) {
			try {
				stopWordsArray[i] = formatWord(fileIn.readLine());
			} catch (IOException e) {
				System.out.print("Error: StopWords file could not be read");
				return;
			}
		}		
		try {
			fileIn.close();
		} catch (IOException e1) {
			System.out.print("Error: StopWords bufferedreader could not be "
			 + "closed");
			return;
		}
		try {
			fileIn = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + file.getName() + " not found.");
			return;
		}
		String fileLine = null;
		try {
			fileLine = fileIn.readLine();
		} catch (IOException e1) {
			System.out.println("Error: "+file.getName()+" could not be read.");
			return;
		}
		while(fileLine != null) {
			String[] fileWordArray = fileLine.split(" ");
			arrayLoop: for (int c = 0; c < fileWordArray.length; c++) {
				String fileWord = fileWordArray[c];
				fileWord = formatWord(fileWord);
				if (fileWord.isEmpty())
					continue arrayLoop;
				for (int i = 0; i < stopWordsArray.length; i++) {
					if (fileWord.equals(stopWordsArray[i])) {
						continue arrayLoop;
					}
				}
				this.wordCount++;
				if (!this.getOrDefault(fileWord, -1.0).equals(-1.0)) {
					continue arrayLoop;
				}
				BufferedReader br = null;
				try {
					br = new BufferedReader(new FileReader(file));
				} catch (FileNotFoundException e1) {
					System.out.println("Error: " + file.getName() +
					  " could not be read.");
					return;
				}
				double wordOccurances = 0;
				String passageLine = null;
				try {
					passageLine = br.readLine();
				} catch (IOException e) {
					System.out.println(file.getName() + "could not be read");
					return;
				}
				while (passageLine != null) {
					String[] wordArray = passageLine.split(" ");
					for (int i = 0; i < wordArray.length; i++) {
						String checkWord = formatWord(wordArray[i]);
						if (checkWord.equals(fileWord)) {
							wordOccurances++;
						}
					}
					try {
						passageLine = br.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("Error: BufferedReader could not be "
					  + "closed.");
					return;
				}
				this.put(fileWord, wordOccurances);
			}
			try {
				fileLine = fileIn.readLine();
			} catch (IOException e) {
				System.out.println("Error: " + file.getName() +
				  " could not be read.");
				return;
			}
			
		}
		try {
			fileIn.close();
		} catch (IOException e) {
			System.out.println("Error: BufferedReader could not be closed.");
			return;
		}
		return;
	}
	/**
	 * Helper method which formats a String for use in Passage objects and 
	 * methods
	 * @param word
	 * The word String that is to be formatted
	 * @return
	 * The input word in all lowercase, without special characters or spaces.
	 */
	public static String formatWord(String word) {
		String formattedWord = word.toLowerCase().trim();
		char[] wordCharArray = formattedWord.toCharArray();
		for (int i = 0; i < wordCharArray.length; i++) {
			if (wordCharArray[i] < 97 || wordCharArray[i] > 122) {
				wordCharArray[i] = ' ';
			}
		}
		formattedWord = new String(wordCharArray);
		formattedWord = formattedWord.replaceAll(" ", "");
		return formattedWord;
	}
	/**
	 * Gets the frequency that a word occurs in a Passage
	 * @param word
	 * the word String that the frequency is being found for
	 * @return
	 * The frequency of the word in the Passage.
	 */
	public double getWordFrequency(String word) {
		double wordOccurances;
		if (this.get(formatWord(word)) == null)
			wordOccurances = 0.0;
		else
			wordOccurances = this.get(formatWord(word));
		double wordFrequency = wordOccurances / (double) this.wordCount;
		return wordFrequency;
	}
	/**
	 * Gets a Set of all the unique word Strings, excluding stop words, that
	 * appear in the text file
	 * @return
	 * A set of all the String keys in the Object Hashtable, which are the 
	 * unique words in the text file. 
	 */
	public Set<String> getWords(){
		return this.keySet();
	}
	/**
	 * Calculates the simularity percentage between two passages
	 * @param passage1
	 * The first passage that is to be used in the calculation
	 * @param passage2
	 * The second passage that is to be used in the calculation
	 * @return
	 * A double value that represents the simularity percentage between the 
	 * two works of text represented by the Passages.
	 */
	public static double cosineSimularity(Passage passage1, Passage passage2) {
		Set<String> wordsSet = new HashSet<String>();
		wordsSet.addAll(passage1.getWords());
		wordsSet.addAll(passage2.getWords());
		String[] wordsArray =  wordsSet.toArray(new String[0]);
		Double numerator =0.0, denominatorLeft = 0.0, denominatorRight = 0.0;
		for (int i = 0; i < wordsArray.length; i++) {
			Double u = passage1.getWordFrequency(wordsArray[i]);
			Double v = passage2.getWordFrequency(wordsArray[i]);
			numerator = numerator + (u * v);
			denominatorLeft = denominatorLeft + (u * u);
			denominatorRight = denominatorRight + (v * v);
		}
		denominatorLeft = Math.sqrt(denominatorLeft);
		denominatorRight = Math.sqrt(denominatorRight);
		double denominator = denominatorLeft * denominatorRight;
		double simularity = (numerator / denominator) * 100;
		simularity = Math.round(simularity);
		passage1.similarTitles.put(passage2.getTitle(), simularity);
		passage2.similarTitles.put(passage1.getTitle(), simularity);
		return simularity;
	}
	/** 
	 * Returns a String representation of the SimilarTitles Hashtable of the 
	 * Passage
	 * @return
	 * A string that contains all of the Passages in the SimilarTitles Hashtable
	 * and the simularity percentages between the calling Passage and the 
	 * other Passages.
	 */
	public String toString() {
		String[] titlesArray = this.getSimilarTitles().keySet()
		  .toArray(new String[0]);
		String passageString = "";
		for (int i = 0; i < titlesArray.length; i++) {
			passageString += titlesArray[i];
			passageString += "(" + this.getSimilarTitles().get(titlesArray[i])
			  + "%)";
			if (i < titlesArray.length - 1) {
				passageString += ", ";
			}
		}
		return passageString;
 
	}
	
}
