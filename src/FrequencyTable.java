/**
 * @author Luke Kaicher
 * ID: 112356778
 * luke.kaicher@stonybrook.edu
 * Assignment #6
 * CSE214
 * R01 Daniel Calabria
 * 
 * This class is an ArrayList of FrequencyLists for every word that occurs in
 * the Passages from an ArrayList of Passages.
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FrequencyTable extends ArrayList<FrequencyList>{
	private static ArrayList<Passage> passagesArray;
	/**
	 * Constructor for a FrequencyTable
	 * @param passages
	 * The ArrayList of Passages, of which a FrequencyList will be built for 
	 * every unique word, excluding stop words, and added to the 
	 * FrequencyTable's ArrayList.
	 * @return
	 * A FrequencyTable object
	 */
	public static FrequencyTable buildTable(ArrayList<Passage> passages) {
		passagesArray = passages;
		Set<String> wordsSet = new HashSet<String>();		
		for (int i = 0; i < passages.size(); i++) {
			wordsSet.addAll(passages.get(i).getWords());
		}
		String[] wordsArray =  wordsSet.toArray(new String[0]);
		ArrayList<FrequencyList> freqTable = new ArrayList<FrequencyList>();
		for (int i = 0; i < wordsArray.length; i++) {
			FrequencyList freqList = new FrequencyList(wordsArray[i], passages);
			freqTable.add(freqList);
		}
		return (FrequencyTable) freqTable;
	}
	/**
	 * Adds a Passage to a FrequencyTable.
	 * @param p
	 * The Passage that is to be added
	 * @throws IllegalArgumentException
	 * If the Passage p is empty or null.
	 */
	public void addPassage(Passage p) throws IllegalArgumentException{
		if (p.isEmpty() || p == null) {
			throw new IllegalArgumentException();
		}
		passagesArray.add(p);
		FrequencyTable newTable = buildTable(passagesArray);
		for (int i = 0; i < this.size(); i++) {
			this.set(i, newTable.get(i));
		}
		for (int i = this.size(); i < newTable.size(); i++) {
			this.add(newTable.get(i));
		}
	}
	/**
	 * Returns the frequency of the given word in the given Passage
	 * @param word
	 * The string whose frequency is to be found
	 * @param p
	 * The Passage whose word frequency is being found 
	 * @return
	 * The frequency of String word in Passage p
	 */
	public int getFrequency(String word, Passage p) {
		String formatWord = Passage.formatWord(word);
		for (int i = 0; i < this.size(); i++) {
			if (this.get(i).getWord().equals(formatWord)){
				return this.get(i).getFrequency(p);
			}
		}
		return 0;
	}
}
