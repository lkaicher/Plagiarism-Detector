import java.util.HashSet;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Set;
import java.io.*;

public class Passage extends Hashtable<String, Double>{
	private String title;
	private int wordCount = 0;
	private Hashtable<String, Double> similarTitles;
	private File stopWordsFile;
	public String getTitle() {
		return this.title;
	}
	public int getWordCount() {
		return this.wordCount;
	}
	public Hashtable<String, Double> getSimilarTitles(){
		return this.similarTitles;
	}
	public File getStopWordsFile() {
		return this.stopWordsFile;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}
	public void setSimilarTitles(Hashtable<String, Double> similarTitles) {
		this.similarTitles = similarTitles;
	}
	public void setStopWordsFile(File stopWords) {
		this.stopWordsFile = stopWords;
	}
	public Passage(String title, File file) {
		this.title = title;
		this.parseFile(file);
	}
	public Passage(String title, File file, File stopWords) {
		this.title = title;
		this.stopWordsFile = stopWords;
		this.similarTitles = new Hashtable<String, Double>();
		System.out.println("1");
		this.parseFile(file);
		System.out.println("2");
	}
	public void parseFile(File file) {
		BufferedReader fileIn;
		try {
			fileIn = new BufferedReader(new FileReader(stopWordsFile));
		} catch (FileNotFoundException e) {
			System.out.println("Stop words file not found");
			return;
		}
		int stopWordsCount = 0;
		String stopWord = fileIn.readLine();
		while (stopWord != null) {
			stopWordsCount++;
			stopWord = fileIn.readLine();
		}
		fileIn.close();
		try {
			fileIn = new BufferedReader(new FileReader(stopWordsFile));
		} catch (FileNotFoundException e) {
			System.out.println("Stop words file not found");
			return;
		}
		String[] stopWordsArray = new String[stopWordsCount];
		for (int i = 0; i < stopWordsCount; i++) {
			stopWordsArray[i] = formatWord(fileIn.readLine());
		}		
		fileIn.close();
		//System.out.println("fuck");
		try {
			fileIn = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.out.println(file.getName() + "not found.");
			return;
		}
		String fileLine = fileIn.readLine();
		whileLoop: while(fileLine != null) {
			//System.out.println("this");
			String[] fileWordArray = fileLine.split(" ");
			for (int c = 0; c < fileWordArray.length; c++) {
				String fileWord = fileWordArray[c];
				fileWord = formatWord(fileWord);
				for (int i = 0; i < stopWordsArray.length; i++) {
					if (fileWord.equals(stopWordsArray[i])) {
						continue;
					}
				}
				// System.out.println("shit");

				if (!this.getOrDefault(fileWord, -1.0).equals(-1.0)) {
					continue;
				}
				// System.out.println("n word");

				BufferedReader br;
				try {
					br = fileIn;
				} catch (FileNotFoundException e) {
					System.out.println(file.getName() + "not found.");
					return;
				}
				// System.out.println("pew");

				double wordOccurances = 0;
				String passageLine = fileLine;
//			try {
//				passageLine = br.readLine();
//			} catch (IOException e) {
//				System.out.println(file.getName() + "could not be read");
//				try {
//					br.close();
//				} catch (IOException e1) {
//					System.out.println("Buffered reader could not be closed");
//					return;
//				}
//				return;
//			}
				// System.out.println("die");

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
						break;
					}
				}
				// System.out.println("pie");

				try {
					br.close();
				} catch (IOException e) {
					System.out.println("Buffered reader could not be closed");
					break;
				}

				this.put(fileWord, wordOccurances);
				this.wordCount++;
			}
			fileLine = fileIn.readLine();
		}
		fileIn.close();
		return;
	}
	private static String formatWord(String word) {
		String formattedWord = word.toLowerCase().trim();
		char[] wordCharArray = formattedWord.toCharArray();
		for (int i = 0; i < wordCharArray.length; i++) {
			if (wordCharArray[i] < 97 || wordCharArray[i] > 122) {
				wordCharArray[i] = ' ';
			}
		}
		formattedWord = new String(wordCharArray);
		formattedWord.replaceAll(" ", "");
		return formattedWord;
	}
	public double getWordFrequency(String word) {
		double wordOccurances;
		if (this.get(formatWord(word)) == null)
			wordOccurances = 0.0;
		else
			wordOccurances = this.get(formatWord(word));
		double wordFrequency = wordOccurances / (double) this.wordCount;
		return wordFrequency;
	}
	public Set<String> getWords(){
		return this.keySet();
	}
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
//			System.out.print("wwee");
		}
		denominatorLeft = Math.sqrt(denominatorLeft);
		denominatorRight = Math.sqrt(denominatorRight);
		double denominator = denominatorLeft * denominatorRight;
		double simularity = (numerator / denominator) * 100;
		simularity = Math.round(simularity);
		System.out.println(simularity);
		System.out.println(passage2.getTitle());
		System.out.println(passage1.getTitle());
		passage1.similarTitles.put(passage2.getTitle(), simularity);
		passage2.similarTitles.put(passage1.getTitle(), simularity);
		return simularity;
	}
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
//				if (i % 2 != 0) {
//					passageString += "\n";
//				}
			}
		}
		return passageString;
 
	}
	
}
