package ie.gmit.sw;

import javax.servlet.http.Part;

public class Job {
	//declare variables
	private Document doc;

	//constructor
	public Job(Document doc) {
		super();
		this.doc = doc;
	}
	
	//getters setters
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}	
	
}
