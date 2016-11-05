/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComparisonServiceIMPL - Short description
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class StringComparisonServiceIMPL extends UnicastRemoteObject implements StringComparisonService{
    private static final long serialVersionUID = 1L;

    public StringComparisonServiceIMPL() throws RemoteException {}

    @Override
    public void execute(StringComparable sc) throws RemoteException{
        System.out.println("[INFO] ERxecuting remote method getMessage() on MessageServiceImpl.");
        System.out.println("[INFO] Method parameter (Encodable) has an object ID of: ");
        sc.run();
    }
    
}
