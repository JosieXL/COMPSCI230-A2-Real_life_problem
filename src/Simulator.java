/*
 *	==========================================================================================
 *	Simulator.java : Open file and put the packets into an array.
 *	It can get valid packets based on the getValidPackets().
 *  It also contains two methods getUniqueSortedSourceHosts and getUniqueSortedDestHosts to return
 *  the array that contain uniqued and sorted IP Address.
 *  It can get the table data based on the getTableData().
 *  Name: Xiaolin Li
 *	UPI: xli556
 *	==========================================================================================
 */

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.Arrays;
public class Simulator {
    private ArrayList<Packet> packetArraylist = new ArrayList<Packet>();
    private ArrayList<Packet> packArr = new ArrayList<Packet>();
    private ArrayList<String> srcHostArrayList = new ArrayList<String>();
    private ArrayList<String> desHostArrayList = new ArrayList<String>();

    /** Constructor of the Simulator
     */
    public Simulator(File file){
        Scanner input = null;
        try{
            input = new Scanner(Paths.get(file.getPath()));
            while(input.hasNext()){
                String currentLine = input.nextLine();
                Packet pack = new Packet(currentLine);
                packArr.add(pack);
            }
        }
        catch (IOException ioE){
            System.out.println("java.io.FileNotFoundException: " + file.getPath() + " (No such file or directory)");
        }
    }

    /** Get the valid Packets
     * @return packetArray - an ArrayList with type Packet to contain the valid packets
     */
    public ArrayList<Packet> getValidPackets(){
        for (Packet packs:packArr){
            if (packs.toString().contains("src=192.168.0.")){
                packetArraylist.add(packs);
            }
        }
        return packetArraylist;
    }

    /** Get the unique and sorted source hosts
     * @return srcHostArray - an array with type String to contain the unique and sorted source hosts
     */
    public String[] getUniqueSortedSourceHosts(){
        try{
            for (int i=1; i<packArr.size(); i++){
                String packsSrc = packArr.get(i).getSourceHost();
                if ((!srcHostArrayList.contains(packsSrc))&&(packArr.get(i).toString().contains("src=192.168.0."))){
                    srcHostArrayList.add(packsSrc);
                }
            }
            String[] srcHostArray = new String[srcHostArrayList.size()];
            srcHostArray = srcHostArrayList.toArray(srcHostArray);
            Arrays.sort(srcHostArray, new Comparator<String>() {
                public int compare(String o1, String o2){
                    String thisIpLastDigits = String.valueOf(o1).substring(10);
                    int thisNum = Integer.parseInt(thisIpLastDigits);
                    String otherIpLastDigits = String.valueOf(o2).substring(10);
                    int otherNum = Integer.parseInt(otherIpLastDigits);
                    return (thisNum - otherNum);
                }
            });
            return srcHostArray;
        }
        catch (IndexOutOfBoundsException ioE){
            String[] srcHostArray = new String[0];
            return srcHostArray;
        }
    }

    /** Get the unique and sorted destination hosts
     * @return desHostArray - an array with type String to contain the unique and sorted destination hosts
     */
    public String[] getUniqueSortedDestHosts(){
        try{
            for (int i=1; i<packArr.size(); i++){
                String packsDes = packArr.get(i).getDestinationHost();
                if ((packArr.get(i).toString().contains("dest=10.0.0."))&&(!desHostArrayList.contains(packsDes))){
                    desHostArrayList.add(packsDes);
                }
            }
            String[] desHostArray = new String[desHostArrayList.size()];
            desHostArray = desHostArrayList.toArray(desHostArray);
            Arrays.sort(desHostArray, new Comparator<String>() {
                public int compare(String o1, String o2){
                    String thisIpLastDigits = String.valueOf(o1).substring(7);
                    int thisNum = Integer.parseInt(thisIpLastDigits);
                    String otherIpLastDigits = String.valueOf(o2).substring(7);
                    int otherNum = Integer.parseInt(otherIpLastDigits);
                    return (thisNum - otherNum);
                }
            });
            return desHostArray;
        }
        catch (IndexOutOfBoundsException ioE){
            String[] desHostArray = new String[0];
            return desHostArray;
        }
    }

    /** Get the table data
     * @param ipAdd a string of IP Address
     * @param isSrcHost a boolean to check if ipAdd is a source host
     * @return tableData - an array with type Packet to contain the Packets for the specific ipAdd
     */
    public Packet[] getTableData(String ipAdd, boolean isSrcHost){
        ArrayList<Packet> packArrForTableData = new ArrayList<Packet>();
        if (isSrcHost){
            for (Packet value : packArr) {
                Object packsSrc = value.getSourceHost();
                if (value.toString().contains("src=" + ipAdd + ",")) {
                    packArrForTableData.add(value);
                }
            }
        }
        else {
            for (Packet value : packArr) {
                Object packsDes = value.getDestinationHost();
                if (value.toString().contains("dest=" + ipAdd + ",")) {
                    packArrForTableData.add(value);
                }
            }
        }
        Packet[] tableData = new Packet[packArrForTableData.size()];
        tableData = packArrForTableData.toArray(tableData);
        return tableData;
    }
}