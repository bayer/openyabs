/*
 * CustomerPicker.java
 *
 * Created on 15. Januar 2008, 07:02
 */

package mp3.classes.layer.visual;

import mp3.classes.layer.*;
import mp3.classes.utils.Formater;
import mp3.classes.utils.WindowTools;
import java.awt.event.MouseEvent;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import mp3.classes.objects.product.Product;
import mp3.classes.visual.sub.billsView;
import mp3.classes.visual.sub.offersView;


/**
 *
 * @author  anti43
 */
public class ProductPicker extends javax.swing.JFrame {

    private static String[][] getListe() {
        return liste;
    }

    private static void setListe(String[][] list) {
        ProductPicker.liste = list;
    }
    private billsView frame;
    private Product p;
    private static String[][] liste;
    private String[][] list;
    private offersView frame1;
    private boolean order=false;

public static void update(){
        Product n = new Product(QueryClass.instanceOf());
        ProductPicker.setListe( n.select("id,produktnummer,name,hersteller", "produktnummer", "", "produktnummer", true));
}

    public ProductPicker(offersView aThis) {
        initComponents ();
        this.frame1=aThis;
        p = new Product(QueryClass.instanceOf());
        new WindowTools(this);
 
        if(ProductPicker.getListe()==null){
            list = p.select("id,produktnummer,name,hersteller", "produktnummer", "", "produktnummer", true);
            ProductPicker.setListe(list);
        } else {
        
            list=ProductPicker.getListe();
        }
        
        String k = "id, " + "Nummer,Name,Hersteller";

        this.jTable1.setModel(new DefaultTableModel(list, k.split(",")));
        Formater.stripFirst(jTable1);
        
        this.setVisible(rootPaneCheckingEnabled);
        this.jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        order =true;
    }
    
    /** Creates new form CustomerPicker
     * @param frame 
     */
    public ProductPicker (billsView frame) {
        initComponents ();
        this.frame=frame;
        p = new Product(QueryClass.instanceOf());
        new WindowTools(this);
 
        if(ProductPicker.getListe()==null){
            list = p.select("id,produktnummer,name,hersteller", "produktnummer", "", "produktnummer", true);
            ProductPicker.setListe(list);
        } else {
        
            list=ProductPicker.getListe();
        }
        
        String k = "id, " + "Nummer,Name,Hersteller";

        this.jTable1.setModel(new DefaultTableModel(list, k.split(",")));
        Formater.stripFirst(jTable1);
        this.jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.setVisible(rootPaneCheckingEnabled);
        
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pick");
        setAlwaysOnTop(true);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("W�hlen Sie eine Produkt"));

        jLabel1.setText("Nummer");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jTable1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTable1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton1.setText("OK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel2.setText("Hersteller");

        jButton2.setText("Abbruch");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jCheckBox1.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("EAN");

        jCheckBox2.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Name");

        jCheckBox3.setBackground(new java.awt.Color(255, 255, 255));
        jCheckBox3.setSelected(true);
        jCheckBox3.setText("Text");

        jLabel3.setText("Einf�gen:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(224, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(jCheckBox2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jCheckBox1)))
                        .addGap(5, 5, 5))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addGap(21, 21, 21))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 382, Short.MAX_VALUE)
                            .addContainerGap())
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel2))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(190, 190, 190)))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox1))
                .addGap(10, 10, 10)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap())
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(281, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        
        String[][] list = p.select("id,produktnummer,name,hersteller", "produktnummer", jTextField1.getText(), "produktnummer", true);
        String k = "id, " + "Nummer,Name,Hersteller";

        this.jTable1.setModel(new DefaultTableModel(list, k.split(",")));
        Formater.stripFirst(jTable1);

        
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        String[][] list = p.select("id,produktnummer,name,hersteller", "hersteller", jTextField2.getText(), "produktnummer", true);
        String k = "id, " + "Nummer,Name,Hersteller";

        this.jTable1.setModel(new DefaultTableModel(list, k.split(",")));
        Formater.stripFirst(jTable1);

    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jTable1MouseClicked (java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        boolean ean;
        boolean text;
        boolean name;
        
        if(jCheckBox1.isSelected()) {
            ean = true;
        } else {
            ean = false;
        }
        if(jCheckBox2.isSelected()) {
            name = true;
        } else {
            name = false;
        }
        
        if(jCheckBox3.isSelected()) {
            text = true;
        } else {
            text = false;
        }
        
        boolean idOk = true;
        Integer id = 0;
     
        try {
            id = Integer.valueOf((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
        } catch (Exception numberFormatException) {
            idOk = false;
        }



        if (idOk) {

            try {
                if(!order) {
                    frame.addToBill(new Product(QueryClass.instanceOf(), id),ean,name,text);
                }else{
                     frame1.addToOrder(new Product(QueryClass.instanceOf(), id),ean,name,text);
                }
                this.dispose();
            } catch (Exception exception) {
//                exception.printStackTrace();
            }
        } 

    }//GEN-LAST:event_jTable1MouseClicked

    private void jButton1ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        jTable1MouseClicked(new MouseEvent(frame, WIDTH, WIDTH, WIDTH, WIDTH, WIDTH, WIDTH, rootPaneCheckingEnabled));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed (java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    @SuppressWarnings("static-access")
    private void jTable1KeyPressed (java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTable1KeyPressed
        // TODO add your handling code here:
        
        if(evt.getKeyCode() == evt.VK_ENTER    ) {
            jTable1MouseClicked(new MouseEvent(frame, WIDTH, WIDTH, WIDTH, WIDTH, WIDTH, WIDTH, rootPaneCheckingEnabled));
        }
    }//GEN-LAST:event_jTable1KeyPressed
    
  
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    public javax.swing.JCheckBox jCheckBox1;
    public javax.swing.JCheckBox jCheckBox2;
    public javax.swing.JCheckBox jCheckBox3;
    public javax.swing.JLabel jLabel1;
    public javax.swing.JLabel jLabel2;
    public javax.swing.JLabel jLabel3;
    public javax.swing.JPanel jPanel1;
    public javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTable jTable1;
    public javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
    
}
