/*
 *	===============================================================================
 *	PacketTableModel.java : A subclass of DefaultTableModel.
 *  It contains a override constructors.
 *  It get the JTable value base on the getValueAt() that return object.
 *  Name: Xiaolin Li
 *	UPI: xli556
 *	===============================================================================
 */

import java.lang.*;
import javax.swing.table.DefaultTableModel;

class PacketTableModel extends DefaultTableModel {
    private Packet[] packet = null;
    private boolean isSource = true;
    private String[][] data;

    /** Constructor of the PacketTableModel
     */
    public PacketTableModel(int rowCount, int columnCount, Packet[] packets, boolean isSource){
        super(rowCount, columnCount);
        this.packet = packets;
        data = new String[this.packet.length+2][3];
        this.isSource = isSource;
    }

    /** Returns the value for the cell at columnIndex and rowIndex.
     * @param rowIndex  the row whose value is to be queried
     * @param columnIndex  the column whose value is to be queried
     * @return result - the value Object at the specified cell
     */
    public Object getValueAt(int rowIndex, int columnIndex){
        int totalSize = 0;
        double averageSize = 0.00;
        Object result = null;
        if ((columnIndex == 0)&&(rowIndex <= getRowCount()-3)) {
            for (int i=0; i<this.packet.length; i++){
                data[i][0] = String.valueOf(this.packet[i].getTimeStamp());
            }
            result = Double.parseDouble(data[rowIndex][columnIndex]);
            return result;
        }
        else if ((columnIndex == 1)&&(rowIndex <= getRowCount()-3)) {
            for (int i=0; i<this.packet.length; i++){
                if (isSource == true) {
                    data[i][1] = this.packet[i].getDestinationHost();
                }
                else if (isSource == false) {
                    data[i][1] = this.packet[i].getSourceHost();
                }
            }
            result = data[rowIndex][columnIndex];
            return result;
        }
        else if ((columnIndex == 2)&&(rowIndex <= getRowCount()-1)) {
            for (int i=0; i<this.packet.length; i++){
                data[i][2] = String.valueOf(this.packet[i].getIpPacketSize());
                totalSize += this.packet[i].getIpPacketSize();
            }
            data[this.packet.length][2] = String.valueOf(totalSize);
            averageSize = Math.ceil(totalSize / this.packet.length);
            data[this.packet.length+1][2] = String.valueOf(averageSize);
            if (rowIndex == getRowCount()-1){
                return Double.parseDouble(data[rowIndex][columnIndex]);
            }
            result = Integer.valueOf(data[rowIndex][columnIndex]);
            return result;
        }
        else {
            return result;
        }
    }

    /** Returns true if the cell at rowIndex and columnIndex is editable
     * @param row the row whose value is to be queried
     * @param column the column whose value is to be queried
     * @return true - if the cell is editable
     *         false - if the cell is not editable
     */
    public boolean isCellEditable(int row,int column)  {
        if ((column < 2) || (((row == this.getRowCount()-1) || (row == this.getRowCount()-2)) && (column == 2))){
            return false;
        }
        return true;
    }
}