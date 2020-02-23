/**
 * @author Luke Kaicher
 * ID: 112356778
 * luke.kaicher@stonybrook.edu
 * Assignment #6
 * CSE214
 * R01 Daniel Calabria
 * 
 * This class is an interface for determining the similarity percentages
 * between text files in a folder and which are likely written by the same
 * author, using Passage Hashtables. The user inputs the file path of the 
 * folder containing the text files, including the StopWords file, and the 
 * program prints a formatted table of each Passage in the folder and their
 * simularity percentages between the other Passages in the file. Then, the 
 * program prints which Passages are likely written by the same author.
 */
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
public class TextAnalyzer {
	public FrequencyTable frequencyTable;

	public static void main(String[] args) {
		System.out.print("Enter the directory of a folder of text files: ");
		Scanner stdin = new Scanner(System.in);
		String directory = stdin.nextLine();
		stdin.close();
		System.out.println("Reading texts...");
		File[] directoryOfFiles = new File(directory).listFiles();
		for(File i : directoryOfFiles){
			File stopWordsFile = new File(directory +"StopWords.txt");
			ArrayList<String> sameAuthorArray  = new ArrayList<String>();
			Passage[] passageArray;
			if (stopWordsFile.canRead())
				passageArray = new Passage[directoryOfFiles.length - 1];
			else
				passageArray = new Passage[directoryOfFiles.length];
			for (int j = 0, k = 0; j < directoryOfFiles.length; j++) {
				if (directoryOfFiles[j].getName()
				  .equals(stopWordsFile.getName())){
					k = 1;
					continue;
				}
				String passageTitle = directoryOfFiles[j].getName()
				  .replaceAll(".txt", "");
				Passage passage = new Passage(passageTitle,
				  directoryOfFiles[j], stopWordsFile);
				passageArray[j - k] = passage;
			}
			for (int j = 0; j < passageArray.length - 1; j++) {
				for (int k = j + 1; k < passageArray.length; k++) {
					double simularity = Passage.cosineSimularity
					  (passageArray[j], passageArray[k]);
					if (simularity >= 60) {
						sameAuthorArray.add("'" + passageArray[j].getTitle() 
						  +"' and '" + passageArray[k].getTitle() + "' may have"
						  + " the same author (" + simularity + "% similar).");
					}
				}
			}
			System.out.printf("\n%-25s%-55s\n%-80s","Text (title)",
			  "| Similarities (%)", "------------------------------------------"
			    + "--------------------------------------");
			for (int j = 0; j < passageArray.length; j++) {
				String[] splitArray = passageArray[j].toString().split(" ");
				String[] similarTextArray=new String[(splitArray.length + 1)/2];
				for (int k = 0; k < splitArray.length; k = k + 2) {
					similarTextArray[k/2] = splitArray[k];
					if (k + 1 < splitArray.length)
						similarTextArray[k/2] += " " + splitArray[k+1];
				}
				System.out.printf("\n%-25s%-55s", passageArray[j].getTitle(),
				  "| " + similarTextArray[0]);
				for (int t = 1; t < similarTextArray.length; t++) {
					System.out.printf("\n%-25s%-55s", "",
					  "| " + similarTextArray[t]);
				}
				System.out.print("\n-------------------------------------------" 
				  + "-------------------------------------");
			}
			System.out.println("\nSuspected Texts With Same Authors");
			System.out.println("-------------------------------------------" 
			  + "-------------------------------------");
			for (int j = 0; j < sameAuthorArray.size(); j++) {
				System.out.println(sameAuthorArray.get(j));
			}
			break;
		}
	}
}
