/*
 *	==========================================================================================
 *	Packet.java : Use a String as parameter, to split it in order to get four needed part of objects.
 *	For each needed objects, it contains the setter and getter methods.
 *  Name: Xiaolin Li
 *	UPI: xli556
 *	==========================================================================================
 */

import java.util.*;
class Packet{
    private String sourceHost;
    private String destinationHost;
    private double timeStamp;
    private int size;

    /** Constructor of the Packet
     * @param str       a string contains information
     */
    public Packet(String str){
        try{
            String newString = str.replaceAll("\\t\\t\\t", "\t \t");
            String st = newString.replace(" ", "");
            String[] strArr = st.split("\t");
            List<String> fixedLengthList = Arrays.asList(strArr);
            ArrayList<String> strList = new ArrayList<String>(fixedLengthList);
            this.sourceHost = strList.get(2);
            this.destinationHost = strList.get(4);
            this.timeStamp = Double.parseDouble(strList.get(1));
            this.size = Integer.parseInt(strList.get(7));
        }
        catch(IndexOutOfBoundsException ioe){
            this.size = 0;
        }
    }

    /** get the source host
     * @return this.sourceHost - the current source host
     */
    public String getSourceHost(){
        return this.sourceHost;
    }

    /** set the source host
     * @param newSourceHost	the new source host
     */
    public void setSourceHost(String newSourceHost){
        this.sourceHost = newSourceHost;
    }

    /** get the destination host
     * @return this.destinationHost - the current destination host
     */
    public String getDestinationHost(){
        return this.destinationHost;
    }

    /** set the destination host
     * @param newDestinationHost	the new destination host
     */
    public void setDestinationHost(String newDestinationHost){
        this.destinationHost = newDestinationHost;
    }

    /** get the time stamp
     * @return this.timeStamp - the current time stamp
     */
    public double getTimeStamp(){
        return this.timeStamp;
    }

    /** set the time stamp
     * @param newTimeStamp	the new time stamp
     */
    public void setTimeStamp(double newTimeStamp){
        this.timeStamp = newTimeStamp;
    }

    /** get the Ip Packet Size
     * @return this.size - the current Ip Packet Size
     */
    public int getIpPacketSize(){
        return this.size;
    }

    /** set the Ip Packet Size
     * @param newSize	the new Ip Packet Size
     */
    public void setIpPacketSize(int newSize){
        this.size = newSize;
    }

    /** Returns a string representation of the object.
     * @return infoStr - the information string
     */
    public String toString(){
        String infoStr = "src=" + this.getSourceHost() + ", dest=" + this.getDestinationHost() +", time=" + String.format("%.2f", this.getTimeStamp())  + ", size=" + ""+ this.getIpPacketSize();
        return infoStr;
    }
}