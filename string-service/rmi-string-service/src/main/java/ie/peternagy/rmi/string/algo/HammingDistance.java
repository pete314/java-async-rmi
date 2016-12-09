/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since      November 2016
 * @version     0.1
 * @description HammingDistance - Implementation of the algorithm - details https://en.wikipedia.org/wiki/Hamming_distance
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.string.algo;

import java.rmi.RemoteException;

public class HammingDistance extends StringAlgo{
    protected static final long serialVersionUID = 2L;

    public HammingDistance(String str1, String str2) throws RemoteException {
        super(str1, str2);
    }

    @Override
    public int distance(String s, String t) throws RemoteException {
        if (s.length() != t.length()) return -1; //Similar length strings only
        int counter = 0;

        for (int i = 0; i < s.length(); ++i){
                if (s.charAt(i) != t.charAt(i)) counter++;
        }
        return  counter;
    }
}
