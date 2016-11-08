/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description JaroWinkler - Implementation of JaroWinkler -
 * https://github.com/tdebatty/java-string-similarity/blob/master/src/main/java/info/debatty/java/stringsimilarity/JaroWinkler.java
 * @package ie.peternagy.rmi.string.algo
 */
package ie.peternagy.rmi.string.algo;

import java.rmi.RemoteException;
import java.util.Arrays;

public class JaroWinkler extends StringAlgo {
    protected static final long serialVersionUID = 4L;
    private static final double DEFAULT_THRESHOLD = 0.7;
    private static final double JW_COEF = 0.1;

    public JaroWinkler(String str1, String str2) throws RemoteException {
        super(str1, str2);
    }

    @Override
    public int distance(String s, String t) throws RemoteException {
        return (int) Math.round((1.0 - similarity(s, t)) * 10);
    }

    
    public final double similarity(final String s1, final String s2) throws RemoteException {
        int[] mtp = matches(s1, s2);
        float m = mtp[0];
        if (m == 0) {
            return 0f;
        }
        double j = ((m / s1.length() + m / s2.length() + (m - mtp[1]) / m))
                / 3;
        double jw = j;

        if (j > DEFAULT_THRESHOLD) {
            jw = j + Math.min(JW_COEF, 1.0 / mtp[3]) * mtp[2] * (1 - j);
        }
        return jw;
    }

    /**
     * Find matching characters
     *
     * @param s1
     * @param s2
     * @return
     */
    private int[] matches(final String s1, final String s2) throws RemoteException {
        String max, min;
        if (s1.length() > s2.length()) {
            max = s1;
            min = s2;
        } else {
            max = s2;
            min = s1;
        }
        int range = Math.max(max.length() / 2 - 1, 0);
        int[] matchIndexes = new int[min.length()];
        Arrays.fill(matchIndexes, -1);
        boolean[] matchFlags = new boolean[max.length()];
        int matches = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            char c1 = min.charAt(mi);
            for (int xi = Math.max(mi - range, 0),
                    xn = Math.min(mi + range + 1, max.length()); xi < xn; xi++) {
                if (!matchFlags[xi] && c1 == max.charAt(xi)) {
                    matchIndexes[mi] = xi;
                    matchFlags[xi] = true;
                    matches++;
                    break;
                }
            }
        }
        char[] ms1 = new char[matches];
        char[] ms2 = new char[matches];
        for (int i = 0, si = 0; i < min.length(); i++) {
            if (matchIndexes[i] != -1) {
                ms1[si] = min.charAt(i);
                si++;
            }
        }
        for (int i = 0, si = 0; i < max.length(); i++) {
            if (matchFlags[i]) {
                ms2[si] = max.charAt(i);
                si++;
            }
        }
        int transpositions = 0;
        for (int mi = 0; mi < ms1.length; mi++) {
            if (ms1[mi] != ms2[mi]) {
                transpositions++;
            }
        }
        int prefix = 0;
        for (int mi = 0; mi < min.length(); mi++) {
            if (s1.charAt(mi) == s2.charAt(mi)) {
                prefix++;
            } else {
                break;
            }
        }
        return new int[]{matches, transpositions / 2, prefix, max.length()};
    }
}
