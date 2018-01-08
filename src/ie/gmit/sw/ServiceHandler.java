package ie.gmit.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;


@WebServlet("/UploadServlet")
@MultipartConfig(fileSizeThreshold=1024*1024*2, 
                 maxFileSize=1024*1024*10,      
                 maxRequestSize=1024*1024*50) 
public class ServiceHandler extends HttpServlet {
	//declare variables
	private static long jobNumber = 0;
	private Worker worker;
	private Job job;
	private Shinglator s;
	private List<Shingle> sList = new ArrayList<Shingle>();
	private Document doc;
	
	//constructor
	public ServiceHandler() {
		super();
		Global.init();//initialise global variables
		worker = new Worker();//create worker
		new Thread(worker).start();//start worker thread
	}

	public void init() throws ServletException {
		ServletContext ctx = getServletContext(); 
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("text/html"); 
		
		//get handle on printwriter
		PrintWriter out = resp.getWriter(); 
		
		//get variables from form
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		Part part = req.getPart("txtDocument");
		
		//response to submission
		out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
		out.print("</head>");		
		out.print("<body>");
		
		//Create A task number if not already created 
		if (taskNumber == null){
			taskNumber = new String("T" + jobNumber);
			jobNumber++;
		}else{
			//Check out-queue for finished job with the given taskNumber
			RequestDispatcher dispatcher = req.getRequestDispatcher("/poll");
			dispatcher.forward(req,resp);
		}
		
		//Output some headings at the top of the generated page
		out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
		out.print("<H3>Document Title: " + title + "</H3>");
		
		//We can also dynamically write out a form using hidden form fields. The form itself is not
		//visible in the browser, but the JavaScript below can see it.
		out.print("<form name=\"frmRequestDetails\" action=\"poll\">");
		out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
		out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
		out.print("</form>");								
		out.print("</body>");	
		out.print("</html>");	
		
		//JavaScript to periodically poll the server for updates (this is ideal for an asynchronous operation)
		out.print("<script>");
		out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);"); //Refresh every 10 seconds
		out.print("</script>");
		
		out.print("<h3>Uploaded Document</h3>");	
		out.print("<font color=\"0000ff\">");	
		
		// break part into shingles return shingle list
		s = new Shinglator(part, taskNumber);
		sList = s.createShingles();
		
		// create a document which will be passed to the worker thread
		doc = new Document(taskNumber, title, sList);
		//Create job for worker
		job = new Job(doc);
		//add job to Global inQueue
		Global.addToInQueue(job);
		
		out.print("</font>");	
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
	
}