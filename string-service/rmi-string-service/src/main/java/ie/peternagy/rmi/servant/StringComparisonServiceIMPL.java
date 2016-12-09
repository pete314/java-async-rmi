/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComparisonServiceIMPL - Short description
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import ie.peternagy.rmi.string.algo.StringComparable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringComparisonServiceIMPL extends UnicastRemoteObject implements StringComparisonService{
    private static final long serialVersionUID = 1L;
    private BlockingQueue<Runnable> inputQueue;
    private ThreadPoolExecutor tpExecutor;

    public StringComparisonServiceIMPL() throws RemoteException {
        initializeThreadPool();
    }

    @Override
    public void execute(StringComparable sc) throws RemoteException{
        System.out.printf("\nReceived new job: %s", sc.getClass().getName());
        tpExecutor.execute(() -> {
            try {
                Thread.sleep(1000);
                sc.execute();
            } catch (RemoteException | InterruptedException ex) {
                Logger.getLogger(StringComparisonServiceIMPL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void initializeThreadPool() {
        inputQueue = new LinkedBlockingQueue<>();
        tpExecutor = new ThreadPoolExecutor(5, 20, 5000, TimeUnit.MILLISECONDS, inputQueue);
        
        //Handle rejected executions in case all threads are in use (put element back in queue)
        tpExecutor.setRejectedExecutionHandler((Runnable r, ThreadPoolExecutor executor) -> {
            try {
                executor.getQueue().put( r );
            } catch (InterruptedException ex) {
                Logger.getLogger(StringComparisonServiceIMPL.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // Strat all threads
        tpExecutor.prestartAllCoreThreads();
    }
    
    
}
