/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description DamerauLevenshtein - Implementation of the algorithm - details https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;

public class DamerauLevenshtein extends UnicastRemoteObject implements StringComparable{
    private static final long serialVersionUID = 1L;
    private final UUID objectId = UUID.randomUUID();
    private int result;
    private boolean isProcessed = false;
    private String str1;
    private String str2;

    public DamerauLevenshtein(String str1, String str2) throws RemoteException{
        this.str1 = str1;
        this.str2 = str2;
    }

    @Override
    public int distance(String s, String t) throws RemoteException {
        int[][] distance = new int[s.length() + 1][t.length() + 1];
        for (int i = 0; i <= s.length(); i++) distance[i][0] = i;
        for (int j = 0; j <= t.length(); j++) distance[0][j] = j;

        for (int i = 1; i <= s.length(); i++){
            for (int j = 1; j <= t.length(); j++){
                distance[i][j] = Math.min(distance[i - 1][j] + 1, Math.min(distance[i][j - 1] + 1, distance[i - 1][j - 1] + ((s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1)));
            
                if ((i > 1) && (j > 1) && (s.charAt(i-1) == t.charAt(j-2)) && (s.charAt(i-2) == t.charAt(j-1))){
                    distance[i][j] = Math.min(distance[i][j], distance[i-2][j-2] + ((s.charAt(i - 1) == t.charAt(j - 1)) ? 0 : 1));
                }
            }
    
        }
        return distance[s.length()][t.length()];
    }

    @Override
    public void run() throws RemoteException{
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
    
    
}
