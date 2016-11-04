/**
 *
 * @author     Peter Nagy - https://peternagy.ie
 * @since November 2016
 * @version 0.1
 * @description StringComaparable - Short description
 * @package ie.peternagy.rmi.servant
 */
package ie.peternagy.rmi.servant;

public interface StringComparable {
    public int distance(String s, String t);
    public void run();
    public boolean isProcessed();
    public int getResult();
}
