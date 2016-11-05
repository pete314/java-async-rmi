/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description RemoteExecutionHandler - this class keeps track of remote executions and it's results
 * @package ie.peternagy.web.string.service
 */
package ie.peternagy.web.string.service;

import ie.peternagy.rmi.string.algo.AlgorithmFactory;
import ie.peternagy.rmi.string.algo.StringComparable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
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
    private final StringComparisonService SCM;
    private final String RMI_CONNECTION_PATTERN = "rmi://%s:%d/%s";
    
    public RemoteExecutionHandler(String serviceHost, int servicePort, String serviceName) throws Exception {
        IN_QUEUE = new LinkedBlockingQueue<>();
        REQUEST_OBJECT_MAP = new ConcurrentHashMap<>();
        SCM = (StringComparisonService) Naming.lookup(String.format(RMI_CONNECTION_PATTERN, serviceHost, servicePort, serviceName));
    }
    
    public UUID addTask(String algorithmName, String str1, String str2){
        try {
            StringComparable sc = algorithmFactory.newAlgorithm(algorithmName, str1, str2);
            REQUEST_OBJECT_MAP.put(sc.getObjectId(), sc);
            IN_QUEUE.add(sc);
        } catch (RemoteException ex) {
            Logger.getLogger(RemoteExecutionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public void run() {
        while(true){
            
        }
    }
    
}
