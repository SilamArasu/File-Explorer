
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.apache.commons.io.FileUtils;

public class nfd extends JFrame {

    public nfd cls;
    JFrame frame;
    nfdtable model;
    JTable table;
    int r = 0;
    ArrayList<String> ad = new ArrayList<String>();
    ArrayList<String> fad = new ArrayList<String>();
    JButton sho;
    String pat;
    int flag = 0;
    JPanel d;

    public nfd(String a) {
        pat = a;
        model = new nfdtable();
        table = new JTable(model);
        frame = new JFrame("Null Finder Finder");
        setLayout(new FlowLayout());
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setSize(new Dimension(600, 400));
        frame.setLocation(250, 100);
        d = new JPanel();
        frame.setVisible(true);
        try {
            getFolderSize(new File(pat));
        } catch (Exception e) {
            e.printStackTrace();
        }

        sho = new JButton("Delete");
        d.add(sho, BorderLayout.SOUTH);
        frame.add(d, BorderLayout.SOUTH);
        sho.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                StringBuilder str = new StringBuilder("");
                int[] a = table.getSelectedRows();
                int n = table.getSelectedRowCount();
                String[] value = new String[n];
                int c = 0;
                for (int t : a) {
                    value[c] = table.getModel().getValueAt(t, 1).toString();
                    System.out.println(value[c]);
                    str.append(value[c]);
                    str.append("\n");
                    c++;
                }
                int response = JOptionPane.showConfirmDialog(null, "Do you want to Delete ?\n" + str, "Confirm",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    for (int i = 0; i < c; i++) {
                        if (!new File(value[i]).delete()) {
                            JOptionPane.showMessageDialog(null, "Could not delete the file", "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            System.out.println(value[i]);
                            ad.clear();
                            fad.clear();
                            flag = 0;
                            getFolderSize(new File(pat));
                            nfdtable model1 = new nfdtable();
                            table.setModel(model1);

                        }
                        /*
                        ad.clear();
                        fad.clear();
                        flag = 0;
                        getFolderSize(new File(pat));
                        nfdtable model1 = new nfdtable();
                        table.setModel(model1);*/
                    }
                }

            }
        });

    }

    public void getFolderSize(File dir) {
        if (flag == 0) {
            r = 0;
        }
        long size = 0;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    if (file.length() == 0) {
                        ad.add(file.getName());
                        fad.add(file.getAbsolutePath());
                        r++;
                    }
                } else if (file.isDirectory()) {
                    if (FileUtils.sizeOfDirectory(file) == 0) {
                    }
                    if (FileUtils.sizeOfDirectory(file) > 0) {
                        getFolderSize(file);
                        flag++;
                    }
                }
            }
        }

    }

    class nfdtable extends AbstractTableModel {

        String[] columnNames = new String[]{
            "Name", "Path"};
        protected Class[] columnClasses = new Class[]{
            String.class, String.class};

        @Override
        public String getColumnName(int column) {
            return columnNames[column];
        }

        @Override
        public int getRowCount() {
            return r; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getColumnCount() {
            return 2; //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Class getColumnClass(int col) {
            return columnClasses[col];
        }

        public void removeRow(int row) {
            fireTableRowsDeleted(row, row);
        }

        @Override
        public Object getValueAt(int i, int j) {

            switch (j) {
                case 0:
                    return ad.get(i);
                case 1:
                    return fad.get(i);

                default:
                    return null;
            }
        }

    }

}
