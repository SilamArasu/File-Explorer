

import java.lang.Object;
import org.apache.commons.io.*;
import org.apache.commons.io.FilenameUtils;
import javax.swing.*;
import javax.swing.table.*;
import java.io.*;
import java.util.Date;
import java.awt.*;
import static java.awt.BorderLayout.CENTER;
import static java.awt.SystemColor.desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.zip.*;

public class FileExplorer extends JFrame {

    int flagcpmov = 0;
    JPanel centerp, topp, downp;
    static JTextField addr;
    JButton go, openFile, cd, rename, copy, paste, del, arch, unarch, mov, nff, enc, dec;
    FileTableModl model;
    static JTable table;
    private File currentFile;
    private Desktop desktop;
    File cpy;
    String nffp = System.getProperty("user.dir");
    JFrame f;
    String destDir, cpdn;
    ArrayList<String> enp = new ArrayList<String>();
    ArrayList<String> enk = new ArrayList<String>();
    String key = "ASDFQWERZXCVUIOP";

    public static void refresh(File c) {

        if (c.exists() && c.isDirectory()) {
            table.setModel(new FileTableModl(c));
        }
        System.setProperty("user.dir", c.getAbsolutePath());
        addr.setText(c.getAbsolutePath());
    }

    public FileExplorer() {
        this.destDir = System.getProperty("user.home") + "\\temp";

        f = new JFrame("File Manager");

        // desktop = Desktop.getDesktop();
        f.setLayout(new BorderLayout());

        centerp = new JPanel(new BorderLayout());
        topp = new JPanel();
        downp = new JPanel(new GridLayout(2, 6));
        addr = new JTextField(50);
        go = new JButton("GO");
        cd = new JButton("up");
        rename = new JButton("Rename");
        copy = new JButton("Copy");
        paste = new JButton("Paste");
        openFile = new JButton("Open");
        openFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    int column = 0;
                    int row = table.getSelectedRow();
                    String value = table.getModel().getValueAt(row, column).toString();
                    desktop = Desktop.getDesktop();
                    System.setProperty("user.dir", addr.getText());
                    File f = new File(new File(value).getAbsolutePath());
                    if (f.isDirectory()) {
                        refresh(f);
                    } else {
                        desktop.open(new File(f.getAbsolutePath()));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        File dir = new File("E:");
        addr.setText(dir.getAbsolutePath());
        System.setProperty("user.dir", addr.getText());
        System.out.println("user" + System.getProperty("user.dir"));
        topp.add(addr, BorderLayout.WEST);

        go.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    File c = new File(addr.getText());
                    refresh(c);
                    System.out.println("user" + System.getProperty("user.dir"));
                    System.out.println("address" + addr.getText());
                } catch (Exception t) {
                    System.out.println("error");
                }

            }
        });

        cd = new JButton("UP");
        cd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    System.setProperty("user.dir", addr.getText());
                    File f = new File((new File(addr.getText()).getAbsolutePath()));
                    f = new File(f.getParent());
                    if (f.isDirectory()) {
                        refresh(f);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        rename.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {

                    int row = table.getSelectedRow();
                    String value = table.getModel().getValueAt(row, 0).toString();
                    String rmstr = JOptionPane.showInputDialog("Enter New Name");
                    System.setProperty("user.dir", addr.getText());
                    File src = new File(new File(value).getAbsolutePath());
                    File dest = new File(System.getProperty("user.dir") + "\\" + rmstr);
                    if (!src.renameTo(dest)) {
                        JOptionPane.showMessageDialog(null, "Could not rename the file", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    refresh(new File(System.getProperty("user.dir")));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        del = new JButton("Delete");
        del.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {

                    int row = table.getSelectedRow();
                    String value = table.getModel().getValueAt(row, 0).toString();
                    System.setProperty("user.dir", addr.getText());
                    int response = JOptionPane.showConfirmDialog(null, "Do you want to Delete " + value + " ?", "Confirm",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        File f = new File(new File(value).getAbsolutePath());
                        if (f.isFile()) {
                            f.delete();
                        } else {
                            FileUtils.deleteDirectory(f);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                refresh(new File(System.getProperty("user.dir")));
            }
        });

        copy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    String temp = null;
                    flagcpmov = 0;
                    int row = table.getSelectedRow();
                    String value = table.getModel().getValueAt(row, 0).toString();
                    File f = new File(new File(value).getAbsolutePath());
                    System.setProperty("user.dir", addr.getText());
                    if (f.isDirectory()) {
                        temp = new String(destDir + "\\" + f.getName());
                        cpdn = f.getAbsolutePath();
                        if (!new File(temp).exists()) {
                            new File(temp);
                        }
                        FileUtils.copyDirectory(f, new File(temp));
                        cpy = new File(temp);
                    } else if (f.isFile()) {
                        cpy = f;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mov = new JButton("Move");
        mov.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    copy.doClick();
                    flagcpmov = 1;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        paste.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {

                    File p = new File(addr.getText() + "\\" + cpy.getName());
                    if (cpy.getAbsolutePath().equals(p.getAbsolutePath())) {
                        String ext = FilenameUtils.getExtension(p.getAbsolutePath());
                        String nam = FilenameUtils.getBaseName(p.getAbsolutePath());
                        p = new File(addr.getText() + "\\" + nam + " - Copy." + ext);
                    }
                    if (cpy.isDirectory()) {
                        FileUtils.copyDirectory(cpy, p);
                    } else {
                        Files.copy(cpy.toPath(), p.toPath());
                    }

                    if (flagcpmov == 1) {
                        if (cpy.isDirectory()) {
                            FileUtils.deleteDirectory(cpy);
                        }
                        if (cpy.isFile()) {
                            cpy.delete();
                        }
                    }
                    cpy = null;
                    cpdn = null;

                } catch (FileAlreadyExistsException e) { //System.out.println("same");

                    JOptionPane.showMessageDialog(null, "File Already exists", "Error", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Could not paste the file", "Error", JOptionPane.ERROR_MESSAGE);
                }
                refresh(new File(addr.getText()));
            }
        });

        nff = new JButton("NullFileFinder");
        nff.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                nfd n = new nfd(addr.getText());
                go.doClick();

            }
        }
        );

        arch = new JButton("Zip");
        arch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int column = 0;
                int row = table.getSelectedRow();
                String value = table.getModel().getValueAt(row, column).toString();
                String val = new File(value).getAbsolutePath();
                zip.compress(val, val + ".zip");
                refresh(new File(System.getProperty("user.dir")));

            }
        }
        );
        unarch = new JButton("Unzip");
        unarch.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    int row = table.getSelectedRow();
                    String value = table.getModel().getValueAt(row, 0).toString();
                    zip.unzip(new File(value).getAbsolutePath());
                    refresh(new File(System.getProperty("user.dir")));
                } catch (Exception t) {
                    t.printStackTrace();
                }

            }
        });

        enc = new JButton("Encrypt");
        enc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                try {
                    int row = table.getSelectedRow();
                    String p = addr.getText() + "\\" + table.getModel().getValueAt(row, 0).toString();
                    String k = JOptionPane.showInputDialog("Register Key ");
                    enp.add(p);
                    enk.add(k);
                    Encryption.encrypt(key, new File(p), new File(p));
                } catch (CryptoException ex) {
                    ex.printStackTrace();
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }

            }
        });
        dec = new JButton("Decrypt");
        dec.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                try {
                    System.out.println("i");
                    int row = table.getSelectedRow();
                    String p = addr.getText() + "\\" + table.getModel().getValueAt(row, 0).toString();
                    String k;
                    for (int t = 0; t < enp.size(); t++) {
                        if (p.equals(enp.get(t))) {
                            k = JOptionPane.showInputDialog("Enter Key");
                            if (k.equals(enk.get(t))) {
                                Encryption.decrypt(key, new File(p), new File(p));
                                enp.remove(t);
                                enk.remove(t);
                            }
                        }
                    }

                } catch (CryptoException ex) {
                    ex.printStackTrace();
                } catch (NoSuchAlgorithmException ex) {
                    ex.printStackTrace();
                }

            }
        });

        topp.add(go);
        topp.add(cd);
        downp.add(openFile);
        downp.add(rename);
        downp.add(del);
        downp.add(copy);
        downp.add(mov);
        downp.add(paste);
        downp.add(arch);
        downp.add(unarch);
        downp.add(nff);
        downp.add(enc);
        downp.add(dec);
        JButton dummy1 = new JButton();
        dummy1.setVisible(false);
        downp.add(dummy1);

        model = new FileTableModl(dir);
        table = new JTable(model);
        centerp = new JPanel(new BorderLayout());
        centerp.add(new JScrollPane(table));
        f.add(topp, BorderLayout.NORTH);
        f.add(downp, BorderLayout.SOUTH);
        f.add(centerp, BorderLayout.CENTER);
    }
}

class FileTableModl extends AbstractTableModel {

    protected File dir;
    protected String[] filenames;

    protected String[] columnNames = new String[]{
        "name", "size", "last modified"};

    protected Class[] columnClasses = new Class[]{
        String.class, Long.class, Date.class};

    public FileTableModl(File dir) {
        this.dir = dir;
        this.filenames = dir.list();
    }

    public int getColumnCount() {
        return 3;
    }

    public int getRowCount() {
        return filenames.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    public Object getValueAt(int row, int col) {
        File f = new File(dir, filenames[row]);
        switch (col) {
            case 0:
                return filenames[row];
            case 1:
                if (f.isFile()) {
                    return new Long(f.length());
                } else {
                    return new Long(FileUtils.sizeOfDirectory(f));
                }
            case 2:
                return new Date(f.lastModified());
            default:
                return null;
        }

    }

    public static void main(String[] args) {
        FileExplorer gui = new FileExplorer();
        gui.f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.f.setSize(new Dimension(600, 400));
        gui.f.setLocation(250, 100);
        gui.f.pack();
        gui.f.setVisible(true);
    }
}
