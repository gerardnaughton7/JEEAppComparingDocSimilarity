package ie.gmit.sw;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Part;

public class Shinglator {
	
	private Part part;
	private String docID;
	private Shingle shingle;
	private List<Shingle> s = new ArrayList<Shingle>();
	
	public Shinglator(Part part, String docID) {
		super();
		this.part = part;
		this.docID = docID;
	}
	
	public List createShingles() throws IOException
	{
		String [] words = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(part.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;
		sb.append(s);
		while ((line = br.readLine()) != null) {
			//Break each line up into shingles and do something. The servlet really should act as a
			//contoller and dispatch this task to something else... Divide and conquer...! I've been
			//telling you all this since 2nd year...!
			
			words = line.split(" ");
			for(int i = 0; i < words.length; i++) {
				if(words[i] != null)
				{
					sb.append(words[i]);
					if(i % 3 == 0 )
					{
						System.out.println(sb.toString());
						shingle = new Shingle(this.docID, sb.toString().hashCode());
						s.add(shingle);
						sb.delete(0, sb.length());
					}
				}
			}
		}
		
		return s;
	}

}
