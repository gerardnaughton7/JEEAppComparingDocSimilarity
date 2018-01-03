package ie.gmit.sw;

import javax.servlet.http.Part;

public class Job {
	private String id;
	private Part part;
	
	public Job(String id, Part part) {
		super();
		this.id = id;
		this.part = part;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}
	
}
