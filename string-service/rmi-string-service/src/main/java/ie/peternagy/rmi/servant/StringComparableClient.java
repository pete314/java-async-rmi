/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComparableClient - Sample client for testing only
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import ie.peternagy.rmi.string.algo.StringComparable;
import ie.peternagy.rmi.string.algo.DamerauLevenshtein;
import ie.peternagy.rmi.string.algo.HammingDistance;
import ie.peternagy.rmi.string.algo.Levenshtein;
import java.rmi.Naming;
import java.util.ArrayList;

public class StringComparableClient {
    public static void main(String[] args) throws Throwable{
        StringComparisonService scm = (StringComparisonService) Naming.lookup("rmi://localhost:1099/stringComparisonService");
        
        //Testing from here
        int executionSize = 1000;
        String str1 = "Levenshtein distance is named after the Russian scientist Vladimir Levenshtein, who devised the algorithm in 1965.";
        String str2 = "Levenshtein distance is named after the Russian scientist Vladimir Levenshtein, devised who  the algorithm in 1965.";
        ArrayList<StringComparable> scList = new ArrayList<>();
        StringComparable sc;
        
        for(int i = 0; i < executionSize; i++){
            double num = Math.random();
            if(num < 0.33d) scList.add(new HammingDistance(str1, str2));
            else if(num < 0.66d) scList.add(new DamerauLevenshtein(str1, str2));
            else scList.add(new Levenshtein(str1, str2));
            scm.execute(scList.get(i));
        }
        
        for(StringComparable scItem : scList){
            System.out.println("Is processed 1: " + scItem.isProcessed());
            while(!scItem.isProcessed()){
                System.out.println("Waiting for result...");
                Thread.sleep(1000);
            }
            System.out.println("Is processed 2: " + scItem.isProcessed());

            System.out.println("The result is =====> " + scItem.getResult());
        }
        
        System.exit(1);
    }
}
