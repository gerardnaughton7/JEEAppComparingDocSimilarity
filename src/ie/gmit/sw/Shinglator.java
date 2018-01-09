package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;

/**
 * Shinglator breaks the part or file text into Shingles(groups 3 words into a hashCode) and returns a list of shingles
 * 
 * @author Gerard Naughton
 *
 */
public class Shinglator {
	//declare variables
	private Part part;
	private String docID;
	private File textFile;
	private Shingle shingle;
	private String [] words = null;
	private String line = null;
	private BufferedReader br;
	private List<Shingle> s = new ArrayList<Shingle>();
	
	//constructor
	public Shinglator(Part part, String docID) {
		super();
		this.part = part;
		this.docID = docID;
	}
	//overloaded constructor depending on what type of doc it takes in either from part or file
	public Shinglator(File textFile, String docID)
	{
		super();
		this.textFile = textFile;
		this.docID = docID;
	}
	
	public List createShingles() throws IOException
	{
		int j = 0;// counts to see if shingle size is reached
		//if part != null read in using part
		if(part != null)
		{
			br = new BufferedReader(new InputStreamReader(part.getInputStream()));// reads in from our document
		}
		else//else use FileReader
		{
			br = new BufferedReader(new FileReader(textFile));// reads in from our document
		}
		StringBuilder sb = new StringBuilder();//builds a string of 3 words
		while ((line = br.readLine()) != null) {//loops while line does not equal null
			//adds to word array ignores commas spaces and other punctuation
			words = line.split("\\W+");
			//loops through the words array and when j = 3 it creates a shingle and adds it to our shingle list s
			for(int i = 0; i < words.length; i++) {
				j++;
				
				sb.append(words[i]);
				if(j == GlobalVars.getSHINGLE_SIZE())
				{
					shingle = new Shingle(this.docID, sb.toString().hashCode());
					s.add(shingle);
					//reset j and stringbuilder once shingle is created 
					sb.delete(0, sb.length());
					j = 0;
				}
				
			}
		}
		//return shingle list
		return s;
	}

}
