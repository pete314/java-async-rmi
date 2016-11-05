/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComparisonService - Remote service interface
 * @package ie.peternagy.web.string.service (origin - ie.peternagy.rmi.servant)
 */
package ie.peternagy.rmi.servant;

import ie.peternagy.rmi.string.algo.StringComparable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface StringComparisonService extends Remote{
    public void execute(StringComparable sc) throws RemoteException;
}
