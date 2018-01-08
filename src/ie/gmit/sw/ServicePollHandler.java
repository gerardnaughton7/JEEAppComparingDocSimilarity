package ie.gmit.sw;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.servlet.*;
import javax.servlet.http.*;

public class ServicePollHandler extends HttpServlet {
	private BlockingQueue<List<Result>> outQueue = new ArrayBlockingQueue<List<Result>>(100);
	private List<Result> rList = new ArrayList<Result>();
	
	public void init() throws ServletException {
		ServletContext ctx = getServletContext();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/html"); 
		PrintWriter out = resp.getWriter(); 
		
		String title = req.getParameter("txtTitle");
		String taskNumber = req.getParameter("frmTaskNumber");
		int counter = 1;
		if (req.getParameter("counter") != null){
			counter = Integer.parseInt(req.getParameter("counter"));
			counter++;
		}
		try {
			rList = checkQueue(title);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(rList != null)
		{
			System.out.println("im here guys");
			out.printf("<html><head><title>A JEE Application for Measuring Document Similarity</title>");
		    out.print("<div class='centered'><table>");
		    out.printf("<h1 align=\"center\"><b>%s</b></h1>", title);
		    out.print("<tr><th>Document title</th><th>Similarity</th></tr>");
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
		    out.printf("<p align=\"center\">"
			    + "<button onclick=\"window.location.href='index.jsp'\">Home</button>"
			    + "</p>");
		    out.print("</body></html>");
		}
		else
		{
			out.print("<html><head><title>A JEE Application for Measuring Document Similarity</title>");		
			out.print("</head>");		
			out.print("<body>");
			out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
			out.print("<H3>Document Title: " + title + "</H3>");
			out.print("<b><font color=\"ff0000\">A total of " + counter + " polls have been made for this request.</font></b> ");
			out.print("Place the final response here... a nice table (or graphic!) of the document similarity...");
			
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
	
	// checkQueue for the job u requested by searching head of queue for id
		private List checkQueue(String title) throws InterruptedException  {
			List<Result> temp = new ArrayList<Result>();
			boolean found = false;
			
			//keep searching till job is found in queue and retrieve job with result definition
			
			outQueue = Global.getOutQueue();
			//System.out.println("hfkkjhjkfdhjkhd"+temp.get(0).getNewDoc());
			for(int i = 0;i < outQueue.size();i++)
			{
				temp = outQueue.peek();
				System.out.println("in while loop"+ temp.get(i).getNewDoc());
				System.out.println("in while loop"+ title);
				String tString = temp.get(i).getNewDoc();
				if(temp != null && tString.equals(title))
				{
					found = true;
					temp = outQueue.poll();//poll the queue every 10 second
				}
			}
			if(found == true)
			{
				System.out.println("im true");
				return temp;
			}
			else
			{
				return temp = null;	
			}
		}
	
}