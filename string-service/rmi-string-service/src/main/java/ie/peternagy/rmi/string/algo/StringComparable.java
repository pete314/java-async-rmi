/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComaparable - Short description
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.string.algo;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface StringComparable extends Remote {
    public int distance(String s, String t) throws RemoteException;
    public void execute() throws RemoteException;
    public boolean isProcessed() throws RemoteException;
    public int getResult() throws RemoteException;
    public UUID getObjectId() throws RemoteException;
}
