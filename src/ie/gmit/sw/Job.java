package ie.gmit.sw;

import javax.servlet.http.Part;

public class Job {
	private Document doc;

	public Job(Document doc) {
		super();
		this.doc = doc;
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}	
	
}
