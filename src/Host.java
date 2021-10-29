/*
 *	==========================================================================================
 *	Host.java : Let two ip string compare to each other based on the compareTo().
 *  Name: Xiaolin Li
 *	UPI: xli556
 *	==========================================================================================
 */

public class Host implements Comparable<Host> {
    private String ip;

    /** Constructor of the Host
     */
    public Host(String newIp){
        this.ip = newIp;
    }

    /** Compare one Ip address to another
     * @param other the other host
     * @return difference - the diffences of the last digits between this IP and the other IP
     */
    public int compareTo(Host other){
        String thisIpLastDigits = this.ip.substring(10);
        int thisNum = Integer.parseInt(thisIpLastDigits);
        String otherIpLastDigits = other.ip.substring(10);
        int otherNum = Integer.parseInt(otherIpLastDigits);
        int difference = thisNum - otherNum;
        return difference;
    }

    /** To returns a string representation of the object.
     * @return this.ip - the current Ip address
     */
    public String toString(){
        return this.ip;
    }
}