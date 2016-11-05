/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description ServiceHandler - Short description
 * @package ie.peternagy.web.string.service
 */
package ie.peternagy.rmi.servant;

import ie.peternagy.rmi.servant.RemoteExecutionHandler;
import java.io.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.*;

public class ServiceHandler extends HttpServlet {
    private String remoteHost = null;
    private String remoteServiceName = null;
    private int remotePort = 0;
    private RemoteExecutionHandler remoteExecutionHandler;
    
    public void init() throws ServletException {
        ServletContext ctx = getServletContext();
        
        remoteHost = ctx.getInitParameter("RMI_SERVER_IP");
        remoteServiceName = ctx.getInitParameter("RMI_SERVICE_NAME"); //Reads the value from the <context-param> in web.xml
        remotePort = Integer.parseInt(ctx.getInitParameter("RMI_SERVICE_PORT"));
        
        try {
            remoteExecutionHandler = new RemoteExecutionHandler(remoteHost, remotePort, remoteServiceName);
            new Thread(){
                @Override
                public void run() {
                    remoteExecutionHandler.run();
                }
            };
        } catch (Exception ex) {
            System.out.println("============================" + ex);
            Logger.getLogger(ServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        //Initialise some request varuables with the submitted form info. These are local to this method and thread safe...
        String algorithm = req.getParameter("cmbAlgorithm");
        String s = req.getParameter("txtS");
        String t = req.getParameter("txtT");
        String taskNumber = req.getParameter("frmTaskNumber");

        out.print("<html><head><title>Distributed Systems Assignment</title>");
        out.print("</head>");
        out.print("<body>");

        if (taskNumber == null) {
            String[] algorithmParts = algorithm.split(" ");
            UUID taskUUID = remoteExecutionHandler.addTask(algorithmParts[0], s, t);
            taskNumber = taskUUID.toString();
        } else {
            UUID taskUUID = UUID.fromString(taskNumber);
            if(remoteExecutionHandler.isProcessed(taskUUID)){
            }
        }
        

        out.print("<H1>Processing request for Job#: " + taskNumber + "</H1>");
        out.print("<div id=\"r\"></div>");

        out.print("<font color=\"#993333\"><b>");
        out.print("RMI Server is located at " + remoteHost);
        out.print("<br>Algorithm: " + algorithm);
        out.print("<br>String <i>s</i> : " + s);
        out.print("<br>String <i>t</i> : " + t);
        out.print("<br>This servlet should only be responsible for handling client request and returning responses. Everything else should be handled by different objects.");
        out.print("Note that any variables declared inside this doGet() method are thread safe. Anything defined at a class level is shared between HTTP requests.");
        out.print("</b></font>");

        out.print("<form name=\"frmRequestDetails\">");
        out.print("<input name=\"cmbAlgorithm\" type=\"hidden\" value=\"" + algorithm + "\">");
        out.print("<input name=\"txtS\" type=\"hidden\" value=\"" + s + "\">");
        out.print("<input name=\"txtT\" type=\"hidden\" value=\"" + t + "\">");
        out.print("<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">");
        out.print("</form>");
        out.print("</body>");
        out.print("</html>");

        out.print("<script>");
        out.print("var wait=setTimeout(\"document.frmRequestDetails.submit();\", 10000);");
        out.print("</script>");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
