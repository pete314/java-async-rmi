/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringAlgo - Short description
 * @package ie.peternagy.rmi.string.algo
 */
package ie.peternagy.rmi.string.algo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class StringAlgo extends UnicastRemoteObject implements StringComparable{
    protected static final long serialVersionUID = 1L;
    protected final UUID objectId = UUID.randomUUID();
    protected int result;
    protected boolean isProcessed = false;
    protected String str1;
    protected String str2;

    public StringAlgo(String str1, String str2) throws RemoteException{
        this.str1 = str1;
        this.str2 = str2;
    }
    
    @Override
    public abstract int distance(String s, String t) throws RemoteException;
    
    @Override
    public void execute() throws RemoteException{
        result = distance(str1, str2);
        isProcessed = true;
    }

    @Override
    public boolean isProcessed() throws RemoteException{
        return isProcessed;
    }

    @Override
    public int getResult() throws RemoteException{
        return result;
    }

    public String getStr1() {
        return str1;
    }

    public void setStr1(String str1) {
        this.str1 = str1;
    }

    public String getStr2() {
        return str2;
    }

    public void setStr2(String str2) {
        this.str2 = str2;
    }

    public UUID getObjectId() {
        return objectId;
    }
    
    
}
