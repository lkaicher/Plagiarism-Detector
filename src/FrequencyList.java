/**
 * @author Luke Kaicher
 * ID: 112356778
 * luke.kaicher@stonybrook.edu
 * Assignment #6
 * CSE214
 * R01 Daniel Calabria
 * 
 * This class creates an ArrayList of Integers to represent the frequencies
 * of a word for each of the Passages in an ArrayList. It also uses a Hashtable 
 * to store the index of each Passage's word frequency in the ArrayList of 
 * Integers. 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class FrequencyList {
	private String word;
	private ArrayList<Integer> frequencies;
	private Hashtable<String, Integer> passageIndicies;
	/**
	 * Getter for the passageIndicies Hashtable
	 * @return
	 * the passageIndicies Hashtable<String, Integer> which represents the 
	 * indicies of the frequencies of each Passage's word frequency
	 */
	public Hashtable<String, Integer> getPassageIndicies(){
		return this.passageIndicies;
	}
	/**
	 * Getter for the word 
	 * @return
	 * The word String whose frequency is being stored in the ArrayList for each
	 * Passage.
	 */
	public String getWord() {
		return this.word;
	}
	/**
	 * Constructor for a FrequencyList object.
	 * @param word
	 * The word String whose frequency is being found and stored for each 
	 * passage
	 * @param passages
	 * The ArrayList of Passages, of which each Passage's word frequency is 
	 * being found and stored.
	 */
	public FrequencyList(String word, ArrayList<Passage> passages) {
		this.word = Passage.formatWord(word);
		this.frequencies = new ArrayList<Integer>();
		this.passageIndicies = new Hashtable<String, Integer>();
		for (int i = 0; i < passages.size(); i++) {
			Integer wordFrequency =
			  (int) (passages.get(i).getWordFrequency(this.word) * 100);
			if (wordFrequency > 0) {
				this.frequencies.add(wordFrequency);
				Integer frequencyIndex = frequencies.indexOf(wordFrequency);
				String title = passages.get(i).getTitle();
				this.passageIndicies.put(title, frequencyIndex);
			}
		}
	}
	/**
	 * Adds a passage to a FrequencyList and updates the ArrayList and HashTable
	 * @param p
	 * The Passage that is being added
	 */
	public void addPassage(Passage p) {
		Integer wordFrequency = (int) (p.getWordFrequency(this.word) * 100);
 		if (wordFrequency > 0) {
			this.frequencies.add(wordFrequency);
			Integer frequencyIndex = frequencies.indexOf(wordFrequency);
			String title = p.getTitle();
			this.passageIndicies.put(title, frequencyIndex);
		}
	}
	/**
	 * Gets the frequency of the word's occurance in the given passage by using
	 * the Hashtable to find the index of the passages frequency in the 
	 * ArrayList, or returns 0 if the word does not occur in the passage
	 * @param p
	 * The Passage whose word frequency is to be found
	 * @return
	 * The word frequency in the Passage
	 */
	public int getFrequency(Passage p) {
		String title = p.getTitle();
		Integer frequencyIndex = passageIndicies.get(title);
		if (frequencyIndex == null)
			return 0;
		int frequency = frequencies.get(frequencyIndex);
		return frequency;
	}
}
