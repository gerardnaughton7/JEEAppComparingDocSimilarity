package ie.gmit.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Handles poll request to the blocking queue and outputs waiting page and results page
 * 
 * @author Gerard Naughton
 *
 */
public class ServicePollHandler extends HttpServlet {
	//declare variables
	private BlockingQueue<List<Result>> outQueue = new ArrayBlockingQueue<List<Result>>(100);
	private List<Result> rList = new ArrayList<Result>();
	
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html"); 
		//get handle on printwriter
		PrintWriter out = resp.getWriter(); 
		
		//get variables from req
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		int counter = 1;
		if (req.getParameter("counter") != null){
			counter = Integer.parseInt(req.getParameter("counter"));
			counter++;
		}
		//check queue to see if your job is in the outqueue
		try {
			rList = checkQueue(title);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//if rList != null output result of your text against other documents in db40
		if(rList != null)
		{
			//print out results page
			out.printf("<html><head><title>A JEE Application for Measuring Document Similarity</title>");
			out.print("<div><table style=\"border: 3px solid black\">");
		    out.printf("<h1><b>Document Being Compared: %s</b></h1>", title);
		    out.print("<tr><th>Uploaded Doc</th><th>Saved Docs</th><th>Similarity</th></tr>");
		    for (int i =0; i<rList.size(); i++) {
			out.print("<tr><td>");
			out.print(rList.get(i).getNewDoc());
			out.print("</td><td>");
			out.print(rList.get(i).getOldDoc());
			out.print("</td><td>");
			out.printf("%.0f %%", Double.valueOf(rList.get(i).getResult())*100);
			out.print("</td></tr>");
		    }
		    out.println();
		    out.print("</table></div>");
		    // Home button
		    out.printf("<p>"
			    + "<button onclick=\"window.location.href='index.jsp'\">Home</button>"
			    + "</p>");
		    out.print("</body></html>");
		}
		else//output waiting page that shows how many times the queue has been polled
		{
			
			out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
			out.print("</head>");		
			out.print("<body>");
			out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
			out.print("<H3>Document Title: " + title + "</H3>");
			out.print("<b><font color=\"ff0000\">A total of " + counter + " polls have been made for this request.</font></b> ");
			out.print("<form name=\"frmRequestDetails\">");
			out.print("<input name=\"txtTitle\" type=\"hidden\" value=\"" + title + "\">");
			out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
			out.print("<input name=\"counter\" type=\"hidden\" value=\"" + counter + "\">");
			out.print("</form>");								
			out.print("</body>");	
			out.print("</html>");	
			
			out.print("<script>");
			out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 5000);"); //Refresh every 5 seconds
			out.print("</script>");
		}
		
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
 	}
	
	// checkQueue for the job u requested by searching the queue
		private List checkQueue(String title) throws InterruptedException  {
			List<Result> temp = new ArrayList<Result>();
			boolean found = false;
			//get handle of outQueue		
			outQueue = Global.getOutQueue();
			//loop through queue until your job title is found
			for(int i = 0;i < outQueue.size();i++)
			{
				temp = outQueue.peek();
				
				//if job found title, mark found as true and return temp 
				if(temp != null && temp.get(i).getNewDoc().equals(title))
				{
					found = true;
					temp = outQueue.poll();
				}
			}
			if(found == true)
			{
				return temp;
			}
			else
			{
				return temp = null;	
			}
		}
	
}