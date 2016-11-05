/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComparableClient - Short description
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import java.rmi.Naming;

public class StringComparableClient {
    public static void main(String[] args) throws Throwable{
        StringComparisonService scm = (StringComparisonService) Naming.lookup("rmi://localhost:1099/stringComparisonService");
        StringComparable sc = new DamerauLevenshtein("Distributed Systems", "Disturbed Systems");
        System.out.println("Is processed: " + sc.isProcessed());
        scm.execute(sc);
        System.out.println("Is processed 1: " + sc.isProcessed());
        while(!sc.isProcessed()){
            System.out.println("Waiting for result...");
            Thread.sleep(1000);
        }
        System.out.println("Is processed 2: " + sc.isProcessed());
        
        System.out.println("The result is =====> " + sc.getResult());
    }
}
