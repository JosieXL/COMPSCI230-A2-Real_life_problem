/*
 *	============================================================================================
 *	A2.java : Extends JFrame and contains a panel where JTable shows.
 *	Also contains Source Hosts and Destination Hosts radioButtons that get the information base
 *  on a sourceHost or a destinationHost.
 *	Name: Xiaolin Li
 *	UPI: xli556
 *	============================================================================================
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class A2 extends JFrame {
    private Font font;
    private File file;
    private JComboBox hostComboBox;
    private Simulator simulator;
    private JRadioButton radioButtonSource, radioButtonDestination;
    private String[] srcHostArray, desHostArray;
    private String ipAdd = "";
    private boolean isSrc = true;
    private DefaultTableModel tableModel1;
    private DefaultTableModel tableModel2;
    private DefaultTableModel tableModel3;
    private Object[] columnNames;
    private DefaultComboBoxModel<String> model1;
    private JTable table;
    private JScrollPane scroll;
    private Object[][] data;
    private Packet[] packet;

    /** main method for A2
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new A2();
            }
        });
    }

    /** constructor to initialise components
     */
    public A2() {
        super("A2");
        setLayout(new BorderLayout());
        JPanel toolPanel = new JPanel();
        toolPanel.setLocation(0,0);
        add(toolPanel, BorderLayout.NORTH);
        JPanel tablePanel = new JPanel();
        tablePanel.setLocation(0,100);
        add(tablePanel, BorderLayout.CENTER);

        setSize(500, 800);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        font = new Font("Sans-serif", Font.PLAIN, 15);
        setVisible(true);

        //set up the tool panel
        JPanel radioButtonPanel = new JPanel();
        radioButtonPanel.setLayout(new FlowLayout());
        radioButtonSource = new JRadioButton("Source hosts");
        radioButtonDestination = new JRadioButton("Destination hosts");
        radioButtonPanel.add(radioButtonSource);
        radioButtonPanel.add(radioButtonDestination);
        ButtonGroup radioButtons = new ButtonGroup();
        radioButtons.add(radioButtonSource);
        radioButtonSource.setSelected(true);
        radioButtons.add(radioButtonDestination);
        toolPanel.setSize(200, 100);
        toolPanel.add(radioButtonPanel);
        hostComboBox = new JComboBox();
        hostComboBox.setVisible(false);
        toolPanel.add(hostComboBox);

        setupMenu();
        setUpRadioSource();
        setUpRadioDestination();
        setUpComboBox();
    }

    /** Set up the menu
     */
    private void setupMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        //Add shortcut for the JMenu using Alt+f
        fileMenu.setMnemonic('f');
        fileMenu.setFont(font);
        menuBar.add(fileMenu);
        JMenuItem fileMenuOpen = new JMenuItem("Open trace file");
        fileMenuOpen.setFont(font);
        fileMenuOpen.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser(".");
                        int returnVal = fileChooser.showOpenDialog(A2.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            file = fileChooser.getSelectedFile();
                            simulator = new Simulator(file);
                            //default select the source hosts radioButton
                            while (radioButtonDestination.isSelected()) {
                                JOptionPane.showMessageDialog(null, "Sorry, wrong radio button chosen, automatically change to source hosts button");
                                radioButtonSource.setSelected(true);
                                hostComboBox.setVisible(false);
                            }
                            if (radioButtonSource.isSelected()) {
                                srcHostArray = simulator.getUniqueSortedSourceHosts();
                                model1 = new DefaultComboBoxModel<>(srcHostArray);
                                columnNames = new Object[]{"Time Stamp", "Destination IP Address", "Packet Size"};
                                isSrc = true;
                            }
                            hostComboBox.setModel(model1);
                            hostComboBox.setVisible(true);
                            ipAdd = hostComboBox.getSelectedItem().toString();

                            packet = simulator.getTableData(ipAdd, isSrc);

                            displayTwoDimensionalArrayForTable();

                            //Create JTable using DefaultTableModel
                            //also set the last two rows' background colour be different with others.
                            DefaultTableModel model = new DefaultTableModel(data, columnNames);
                            table = new JTable(model){
                                public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                                    Component c = super.prepareRenderer(renderer, row, col);
                                    String status = (String)table.getModel().getValueAt(row, 1);
                                    try {
                                        if ((status.contains("192.168.0.")) || (status.contains("10.0.0."))) {
                                            c.setBackground(Color.BLACK);
                                            c.setForeground(Color.WHITE);
                                        }
                                    }catch (NullPointerException npE) {
                                        c.setBackground(super.getBackground());
                                        c.setForeground(super.getForeground());
                                    }
                                    return c;
                                }
                            };

                            centerCellObject();
                            //Sets the table's auto resize mode be proportionately resized all columns when the table is resized.
                            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                            scroll = new JScrollPane(table);
                            scroll.setVisible(true);
                            add(scroll, BorderLayout.CENTER);
                        }
                    }
                }
        );
        fileMenu.add(fileMenuOpen);
        JMenuItem fileMenuQuit = new JMenuItem("Quit");
        fileMenuQuit.setFont(font);
        fileMenu.add(fileMenuQuit);
        fileMenuQuit.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                }
        );
        JMenuItem fileMenuSave = new JMenuItem("Save");
        fileMenuSave.setFont(font);
        fileMenu.add(fileMenuSave);
        fileMenuSave.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JOptionPane.showMessageDialog(null, "Sorry, the current function is not perfect，please select the other button. Thank you.");
                    }
                }
        );
    }

    /** Set up the source Hosts radioButton
     */
    public void setUpRadioSource() {
        radioButtonSource.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    srcHostArray = simulator.getUniqueSortedSourceHosts();
                    model1 = new DefaultComboBoxModel<>(srcHostArray);
                    hostComboBox.setModel(model1);
                    hostComboBox.setVisible(true);
                    ipAdd = hostComboBox.getSelectedItem().toString();
                    isSrc = true;
                    packet = simulator.getTableData(ipAdd, isSrc);
                    columnNames = new Object[]{"Time Stamp", "Destination IP Address", "Packet Size"};
                    displayTwoDimensionalArrayForTable();
                    tableModel1 = new DefaultTableModel(data, columnNames);
                    table.setModel(tableModel1);
                    centerCellObject();
                }
                catch (NullPointerException npE){
                    System.out.println("File does not select yet.");
                }
            }
        });
    }

    /**
     * Set up the destination Hosts radioButton
     */
    public void setUpRadioDestination() {
        radioButtonDestination.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    desHostArray = simulator.getUniqueSortedDestHosts();
                    model1 = new DefaultComboBoxModel<>(desHostArray);
                    hostComboBox.setModel(model1);
                    hostComboBox.setVisible(true);
                    isSrc = false;
                    ipAdd = hostComboBox.getSelectedItem().toString();
                    packet = simulator.getTableData(ipAdd, isSrc);
                    columnNames = new Object[]{"Time Stamp", "Source IP Address", "Packet Size"};
                    displayTwoDimensionalArrayForTable();
                    tableModel2 = new DefaultTableModel(data, columnNames);
                    table.setModel(tableModel2);
                    centerCellObject();
                }
                catch (NullPointerException npE){
                    System.out.println("File does not select yet.");
                }
            }
        });
    }

    /** Set up the combo box
     */
    public void setUpComboBox() {
        hostComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == hostComboBox) {
                    String ipAddComboBox = (String) hostComboBox.getSelectedItem();
                    if (radioButtonSource.isSelected()) {
                        isSrc = true;
                        columnNames = new Object[]{"Time Stamp", "Destination IP Address", "Packet Size"};
                    }
                    else if (radioButtonDestination.isSelected()) {
                        isSrc = false;
                        columnNames = new Object[]{"Time Stamp", "Source IP Address", "Packet Size"};
                    }
                    packet = simulator.getTableData(ipAddComboBox, isSrc);
                    displayTwoDimensionalArrayForTable();
                    tableModel3 = new DefaultTableModel(data, columnNames);
                    table.setModel(tableModel3);
                    centerCellObject();
                }
            }
        });
    }

    /** Method to display the two dimensional array for JTable
     */
    public void displayTwoDimensionalArrayForTable() {
        data = new Object[packet.length + 2][3];
        DefaultTableModel tableModel = new PacketTableModel((packet.length + 2), data[0].length, packet, isSrc);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            data[i][0] = tableModel.getValueAt(i, 0);
            data[i][1] = tableModel.getValueAt(i, 1);
            data[i][2] = tableModel.getValueAt(i, 2);
        }
    }

    /** Method to make the cell objects in the JTable be CENTER_ALIGNMENT
     */
    public void centerCellObject(){
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment((int) JLabel.CENTER_ALIGNMENT);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
    }
}