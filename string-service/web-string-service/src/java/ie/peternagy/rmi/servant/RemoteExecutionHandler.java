/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description RemoteExecutionHandler - this class keeps track of remote executions and it's results
 * @package ie.peternagy.web.string.service
 */
package ie.peternagy.rmi.servant;

import ie.peternagy.rmi.servant.StringComparisonService;
import ie.peternagy.rmi.string.algo.AlgorithmFactory;
import ie.peternagy.rmi.string.algo.StringComparable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteExecutionHandler implements Runnable{
    //This is not really required as the remote service is threaded
    private final BlockingQueue<StringComparable> IN_QUEUE;
    private final Map<UUID, StringComparable> REQUEST_OBJECT_MAP;
    private final AlgorithmFactory algorithmFactory = AlgorithmFactory.getInstance();
    private static StringComparisonService scm;
    private final String RMI_CONNECTION_PATTERN = "rmi://%s:%d/%s";
    
    public RemoteExecutionHandler(){
        IN_QUEUE = new LinkedBlockingQueue<>();
        REQUEST_OBJECT_MAP = new ConcurrentHashMap<>();
        
    }
    
    public boolean initializeConnection(String serviceHost, int servicePort, String serviceName){
        try {
            scm = (StringComparisonService) Naming.lookup(String.format(RMI_CONNECTION_PATTERN, serviceHost, servicePort, serviceName));
            return true;
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            Logger.getLogger(RemoteExecutionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    /**
     * Add new task to execute remotely
     * 
     * @param algorithmName - the algorithm name to execute to
     * @param str1 - #1 string to work with
     * @param str2 - #1 string to work with
     * @return the instance uuid
     */
    public UUID addTask(String algorithmName, String str1, String str2){
        try {
            StringComparable sc = algorithmFactory.newAlgorithm(algorithmName, str1, str2);
            REQUEST_OBJECT_MAP.put(sc.getObjectId(), sc);
            IN_QUEUE.add(sc);
            
            return sc.getObjectId();
        } catch (RemoteException ex) {
            System.out.println("\n============ Exception+++++++++++\n" + ex);
            Logger.getLogger(RemoteExecutionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    /**
     * Check if the task is processed
     * 
     * @param itemUuid - the instance uuid
     * @return 
     */
    public boolean isProcessed(UUID itemUuid){
        boolean status = false;
        if(REQUEST_OBJECT_MAP.containsKey(itemUuid)){
            try {
                status = (REQUEST_OBJECT_MAP.get(itemUuid)).isProcessed();
            } catch (RemoteException ex) {
                Logger.getLogger(RemoteExecutionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return status;
    }
    
    /**
     * Get the result of an element 
     * @param itemUuid
     * @return 
     */
    public int getResult(UUID itemUuid){
        int result = -99999999;
        if(REQUEST_OBJECT_MAP.containsKey(itemUuid)){
            try {
                result = REQUEST_OBJECT_MAP.get(itemUuid).getResult();
                REQUEST_OBJECT_MAP.remove(itemUuid);
            } catch (RemoteException ex) {
                Logger.getLogger(RemoteExecutionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    @Override
    public void run() {
        System.out.println("Supervisor thread is running...");
        while(true){
            try {
                StringComparable sc = IN_QUEUE.take();
                System.out.printf("\nExecuting new job, id: %s", sc.getObjectId());
                
                scm.execute(sc);
            } catch (InterruptedException | RemoteException ex) {
                Logger.getLogger(RemoteExecutionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
