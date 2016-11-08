/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description ServiceHandler - Short description
 * @package  ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import java.io.*;
import java.util.UUID;
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
        
        remoteExecutionHandler = new RemoteExecutionHandler();
        if(!remoteExecutionHandler.initializeConnection(remoteHost, remotePort, remoteServiceName))
            throw new RuntimeException("Could not initialize remote connection");
        
        //Start a supervision thread on the job Queue
        new Thread(remoteExecutionHandler){
            @Override
            public void run() {
                remoteExecutionHandler.run();
            }
        }.start();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();

        //Initialise some request varuables with the submitted form info. These are local to this method and thread safe...
        String algorithm = req.getParameter("cmbAlgorithm");
        String s = req.getParameter("txtS");
        String t = req.getParameter("txtT");
        String taskNumber = req.getParameter("frmTaskNumber");
        String result = "";

        if (taskNumber == null) {
            String[] algorithmParts = algorithm.split(" ");
            UUID taskUUID = remoteExecutionHandler.addTask(algorithmParts[0], s, t);
            taskNumber = taskUUID.toString();
        } else {
            UUID taskUUID = UUID.fromString(taskNumber);
            if(remoteExecutionHandler.isProcessed(taskUUID)){
                result = ""+remoteExecutionHandler.getResult(taskUUID);
            }
        }
        
        //Print the header
        out.print(getGeneralHeader());

        //Print the general body content
        out.print(getGeneralBodyWithParams(algorithm, t, t, taskNumber, remoteHost));
        
        if(!result.equals("")){
            //Print the result
            out.print(getResultContainer(result));
        }else{
            //Print hidden field
            out.print(getBodyHiddenForm(algorithm, t, t, taskNumber, taskNumber, 1000));
        }
        
        //Print the footer content
        out.print(getGeneralFooter());
    }

    
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    /**
     * Generate simple header
     * @return header html string
     */
    private String getGeneralHeader(){
        return "<html><head><title>Distributed Systems Assignment</title>" 
                + "</head>"
                + "<body>";
    }
    
    /**
     * Generate simple html closing 
     * @return closing html string
     */
    private String getGeneralFooter(){
        return "</body></html>";
    }
    
    /**
     * Generate the string content for html body
     * @param algorithm
     * @param str1
     * @param str2
     * @param taskNumber
     * @param hostname
     * @return 
     */
    private String getGeneralBodyWithParams(String algorithm, String str1, String str2, String taskNumber, String hostname){
        return  "<center>"
                + "<H1>Processing request for Job#:" + taskNumber + "</H1>" 
                + "<div id=\"r\"></div>"
                + "<font color=\"#993333\"><b>"
                + "RMI Server is located at " + hostname
                + "<br>Algorithm: " + algorithm
                + "<br>String <i>s</i> : " + str1
                + "<br>String <i>t</i> : " + str2
                + "</b></font>"
                + "</center>";
    }
    
    /**
     * Get body hidden form with resubmit script
     * 
     * @param algorithm - the current execution algorithm
     * @param str1 - #1 string to run the algorithm with 
     * @param str2 - #2 string to run the algorithm with 
     * @param taskNumber - the internal task id
     * @param hostname - the remote server host name
     * @param resubmitInterval - re-submit interval for the form
     * @return 
     */
    private String getBodyHiddenForm(String algorithm, String str1, String str2, String taskNumber, String hostname, int resubmitInterval){
        return "<form name=\"frmRequestDetails\">"
                + "<input name=\"cmbAlgorithm\" type=\"hidden\" value=\"" + algorithm + "\">"
                + "<input name=\"txtS\" type=\"hidden\" value=\"" + str1 + "\">"
                + "<input name=\"txtT\" type=\"hidden\" value=\"" + str2 + "\">"
                + "<input name=\"frmTaskNumber\" type=\"hidden\" value=\"" + taskNumber + "\">"
                + "</form>"
                + "<script>"
                + "var wait=setTimeout(\"document.frmRequestDetails.submit();\", "+ resubmitInterval +");"
                + "</script>";
    }
    
    /**
     * Generate result html container
     * @param result - the result to include in output
     * @return 
     */
    private String getResultContainer(String result){
        return "<center><br><font color=\"#993333\"><b>"
                + "<br>The result is: " + result
                + "</b></font></center>";
    }
}
