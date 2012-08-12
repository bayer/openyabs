/*
 *  This file is part of YaBS.
 *
 *      YaBS is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      YaBS is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with YaBS.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * PrinitingComboBox.java
 *
 * Created on 17.02.2009, 09:53:14
 */
package mpv5.ui.beans;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import mpv5.db.common.Context;
import mpv5.db.common.DatabaseObject;
import mpv5.logging.Log;
import mpv5.ui.dialogs.DialogForFile;
import mpv5.utils.arrays.ArrayUtilities;
import mpv5.utils.date.DateConverter;
import mpv5.utils.files.FileActionHandler;
import mpv5.utils.files.FileReaderWriter;
import mpv5.utils.files.TableHtmlWriter;
import mpv5.utils.models.MPComboBoxModelItem;
import mpv5.utils.print.FilePrintJob;
import mpv5.utils.print.PrintJob;
import mpv5.utils.print.PrintJob2;

/**
 *
 *  
 */
public class PrinitingComboBox extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    public static final int MODE_DO = 0;
    public static final int MODE_TABLE = 1;
    private int mode = 0;
    private Object dataowner;
    public static final int MODE_COMPONENT = 2;

    /** Creates new form PrinitingComboBox
     */
    public PrinitingComboBox() {
        initComponents();
    }

    /**
     *
     * @param dataowner DatabaseObject or JTable
     */
    public void init(Object dataowner) {
        if (dataowner instanceof DatabaseObject
                && (((DatabaseObject) dataowner).getContext().equals(Context.getContact())
                || ((DatabaseObject) dataowner).getContext().equals(Context.getSupplier())
                || ((DatabaseObject) dataowner).getContext().equals(Context.getManufacturer())
                || ((DatabaseObject) dataowner).getContext().equals(Context.getCustomer())
                || ((DatabaseObject) dataowner).getContext().equals(Context.getUser())) ){

            jComboBox1.setModel(new DefaultComboBoxModel(new Object[]{new MPComboBoxModelItem(-1, ""),
                        new MPComboBoxModelItem(0, "Printer"),
                        new MPComboBoxModelItem(1, "VCF Export"),
                        new MPComboBoxModelItem(2, "CSV Export"),
                        new MPComboBoxModelItem(3, "XML Export")}));
            mode = MODE_DO;
        } else if (dataowner instanceof DatabaseObject) {

            jComboBox1.setModel(new DefaultComboBoxModel(new Object[]{new MPComboBoxModelItem(-1, ""),
                        new MPComboBoxModelItem(0, "Printer"),
                        new MPComboBoxModelItem(2, "CSV Export"),
                        new MPComboBoxModelItem(3, "XML Export")}));
            mode = MODE_DO;
        } else if (dataowner instanceof JTable) {
            jComboBox1.setModel(new DefaultComboBoxModel(new Object[]{new MPComboBoxModelItem(-1, ""),
                        new MPComboBoxModelItem(0, "Printer"),
                        new MPComboBoxModelItem(2, "CSV Export"),
                        new MPComboBoxModelItem(1, "HTML Export")}));
            mode = MODE_TABLE;
        } else if (dataowner instanceof JComponent) {
            jComboBox1.setModel(new DefaultComboBoxModel(new Object[]{new MPComboBoxModelItem(-1, ""),
                        new MPComboBoxModelItem(0, "Printer"),
                        new MPComboBoxModelItem(1, "Export")
                    }));
            mode = MODE_COMPONENT;
        }
        this.dataowner = dataowner;


    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();

        setName("Form"); // NOI18N
        setOpaque(false);
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("mpv5/resources/languages/Panels"); // NOI18N
        jLabel1.setText(bundle.getString("PrinitingComboBox.jLabel1.text")); // NOI18N
        jLabel1.setMaximumSize(new java.awt.Dimension(333, 333));
        jLabel1.setName("jLabel1"); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 18));
        add(jLabel1);

        jComboBox1.setFont(jComboBox1.getFont());
        jComboBox1.setName("jComboBox1"); // NOI18N
        jComboBox1.setPreferredSize(new java.awt.Dimension(100, 28));
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        add(jComboBox1);
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComboBox1ItemStateChanged
        JComboBox cb = (JComboBox) evt.getSource();
        // Get the affected item
        MPComboBoxModelItem item = (MPComboBoxModelItem) evt.getItem();

        switch (mode) {
            case MODE_DO:
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    switch (Integer.valueOf(item.getId().toString())) {
                        case 0:
                            new PrintJob().print((DatabaseObject) dataowner);
                            break;
                        case 1:
                            new FilePrintJob((DatabaseObject) dataowner).toVCF();
                            break;
                        case 2:
                            new FilePrintJob((DatabaseObject) dataowner).toCSV();
                            break;
                        case 3:
                            new FilePrintJob((DatabaseObject) dataowner).toXML();
//                            ((Contact) dataowner).toXML();
                            break;
                    }
                }
                break;

            case MODE_TABLE:
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    switch (Integer.valueOf(item.getId().toString())) {
                        case 0:
                            try {
                                ((JTable) dataowner).print();
                            } catch (PrinterException ex) {
                                Log.Debug(this, ex);
                            }
                            break;
                        case 1:
                            DialogForFile dialog = new DialogForFile(DialogForFile.FILES_ONLY, "export-" + DateConverter.getTodayDefDate() + ".html");
                            if (dialog.saveFile()) {
                                File f = new TableHtmlWriter((JTable) dataowner, dialog.getFile(), ((JTable) dataowner).getShowHorizontalLines(), ((JTable) dataowner).getShowVerticalLines()).createHtml();
                                FileActionHandler.open(f);
                            }
                            break;
                        case 2:
                            DialogForFile dialog2 = new DialogForFile(DialogForFile.FILES_ONLY, "export-" + DateConverter.getTodayDefDate() + ".csv");
                            if (dialog2.saveFile()) {
                                FileReaderWriter r = new FileReaderWriter(dialog2.getFile());
                                String title = "";
                                for (int i = 0; i < ((JTable) dataowner).getColumnCount(); i++) {
                                    title += ((JTable) dataowner).getColumnName(i) + ",";
                                }
                                title = title.substring(0, title.length() - 1);
                                title += "\n";
                                Object[][] ar = ArrayUtilities.tableModelToArray((JTable) dataowner);
                                r.writeOnce(title);
                                boolean write = r.write(ar, ",", true);
                                FileActionHandler.open(dialog2.getSelectedFile());
                            }
                            break;
                    }
                    break;
                }

            case MODE_COMPONENT:
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    switch (Integer.valueOf(item.getId().toString())) {
                        case 0:
                            new PrintJob2((Component) dataowner);
                            break;
                        case 1:
                            Component comp = (Component) dataowner;
                            int w = comp.getWidth(),
                             h = comp.getHeight();
                            BufferedImage image = new BufferedImage(w, h,
                                    BufferedImage.TYPE_INT_RGB);
                            Graphics2D g2 = image.createGraphics();
                            comp.paint(g2);
                            g2.dispose();
                            DialogForFile d = new DialogForFile(new File("export.jpg"));
                            if (d.saveFile()) {
                                try {
                                    ImageIO.write(image, "jpeg", d.getFile());
                                } catch (IOException ex) {
                                    Log.Debug(ex);
                                }
                            }
                            break;
                    }
                }
                break;
        }
        cb.setSelectedIndex(-1);
    }//GEN-LAST:event_jComboBox1ItemStateChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
