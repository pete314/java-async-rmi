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
    public static void main(String[] args) throws Exception{
        StringComparisonService scm = (StringComparisonService) Naming.lookup("rmi://localhost:1099/string-comparison-service");
        StringComparable sc = new DamerauLevenshtein("sadas", "asda");
        scm.execute(sc);
        System.out.println("RemoteMessage object ID=====> ");
    }
}
