/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComparisonServer - Entry point for application runtime
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class StringComparisonServer {
    public static void main(String[] args) throws Exception{
        StringComparisonService scm = new StringComparisonServiceIMPL();
        LocateRegistry.createRegistry(1099);
        Naming.rebind("string-comparison-service", scm);
        System.out.println("Server ready.");
    }
}
