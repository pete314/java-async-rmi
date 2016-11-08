/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description AlgorithmFactory - Singleton factory for algorithms 
 * @package ie.peternagy.rmi.string.algo
 */
package ie.peternagy.rmi.string.algo;

import java.rmi.RemoteException;

public class AlgorithmFactory {
    private static AlgorithmFactory algoFactory;
    
    private AlgorithmFactory() {}
    
    /**
     * Get singleton instance
     * 
     * @return AlgorithmFactory instance
     */
    public static AlgorithmFactory getInstance(){
        if(null == algoFactory)
            algoFactory = new AlgorithmFactory();
        return algoFactory;
    }
    
    /**
     * Get new instance of StringComparable algorithm
     * 
     * @param algoName - the algorithms founder(s) name
     * @param str1 - #1 string to work with
     * @param str2 - #1 string to work with
     * @return initialized object
     * @throws RemoteException 
     */
    public StringComparable newAlgorithm(String algoName, String str1, String str2) throws RemoteException{
        switch(algoName){
            case "Damerau-Levenshtein":
                return new DamerauLevenshtein(str1, str2);
            case "Hamming":
                return new HammingDistance(str1, str2);
            case "Levenshtein":
                return new Levenshtein(str1, str2);
            case "Jaro–Winkler":
                return new JaroWinkler(str1, str2);
            case "Euclidean":
            case "Hirschberg's":
            case "Needleman-Wunsch":
            case "Smith":
            default:
                return new DamerauLevenshtein(str1, str2);
        }
    }
}
