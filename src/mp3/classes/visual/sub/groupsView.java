/*
 * groupsView.java
 *
 * Created on 30. Januar 2008, 21:32
 */
package mp3.classes.visual.sub;

import mp4.frames.mainframe;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeSelectionModel;
import mp4.klassen.objekte.Dienstleistung;
import mp4.utils.windows.Position;
import mp3.classes.utils.Log;
import mp3.classes.layer.Popup;
import mp4.datenbank.verbindung.ConnectionHandler;

import mp4.einstellungen.Programmdaten;
import mp4.klassen.objekte.Product;
import mp4.panels.productsView;

import mp4.klassen.objekte.ProductGroupCategory;
import mp4.klassen.objekte.ProductGroupFamily;
import mp4.klassen.objekte.ProductGroupGroup;
import mp4.klassen.objekte.ProductGroupHandler;
import mp4.utils.trees.TreeFormat;

/**
 *
 * @author  anti43
 */
public class groupsView extends javax.swing.JFrame implements TreeSelectionListener {

    private ProductGroupHandler p;
    private ArrayList cats,  fams,  grps;
    private DefaultMutableTreeNode root,  child,  subchild,  subsubchild;
    private JTree jTree1;
    private JTree tree;
    private ProductGroupGroup grp;
    private ProductGroupFamily fag;
    private ProductGroupCategory cat;
    private boolean leaf = false;
    private boolean family = false;
    private boolean category = false;
    private mainframe frame;
    private JScrollPane jScrollPane1;
    private Product produkt;
    private JTextField field;
    private Dienstleistung produkt2;

    public groupsView(mainframe frame, Dienstleistung prod, JTextField field) {
        initComponents();
        this.frame = frame;
        this.produkt2 = prod;
        this.field = field;
        p = ProductGroupHandler.instanceOf();
        new Position(this);
        this.setTreeData(true);
        jTextField5.setText(Programmdaten.instanceOf().getWARENGRUPPEN_SEPARATOR());
        this.setVisible(rootPaneCheckingEnabled);
    }

    /** Creates new form groupsView
     * @param frame
     * @param product
     * @param field 
     */
    public groupsView(mainframe frame, Product product, JTextField field) {
        initComponents();
        this.frame = frame;
        this.produkt = product;
        this.field = field;
        p = ProductGroupHandler.instanceOf();
        new Position(this);
//
//        root = new DefaultMutableTreeNode("MP");
//        setTreeData(false);
//        tree = this.jTree1;
//        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
//        tree.addTreeSelectionListener(this);

        this.setTreeData(true);
        jTextField5.setText(Programmdaten.instanceOf().getWARENGRUPPEN_SEPARATOR());
        this.setVisible(rootPaneCheckingEnabled);
    }

    private groupsView() {
        initComponents();
        p = ProductGroupHandler.instanceOf();
        new Position(this);
        root = new DefaultMutableTreeNode("MP");
        setTreeData(false);
        tree = this.jTree1;
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        this.setVisible(rootPaneCheckingEnabled);
    }

    /** Required by TreeSelectionListener interface.
     * @param e 
     */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) {
            leaf = false;
            family = false;
            category = false;
            return;
        }

        jTextField4.setText(e.getNewLeadSelectionPath().toString());
        Object nodeInfo = node.getUserObject();

        if (nodeInfo.getClass().isInstance(new ProductGroupGroup(ConnectionHandler.instanceOf()))) {
            grp = (ProductGroupGroup) nodeInfo;
            leaf = true;
            family = false;
            category = false;

        } else if (nodeInfo.getClass().isInstance(new ProductGroupFamily(ConnectionHandler.instanceOf()))) {
            fag = (ProductGroupFamily) nodeInfo;
            leaf = false;
            family = true;
            category = false;

        } else if (nodeInfo.getClass().isInstance(new ProductGroupCategory(ConnectionHandler.instanceOf()))) {
            cat = (ProductGroupCategory) nodeInfo;
            leaf = false;
            family = false;
            category = true;
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
// <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
private void initComponents() {

jPanel1 = new javax.swing.JPanel();
jPanel3 = new javax.swing.JPanel();
jTextField1 = new javax.swing.JTextField();
jLabel1 = new javax.swing.JLabel();
jButton1 = new javax.swing.JButton();
jTextField2 = new javax.swing.JTextField();
jButton2 = new javax.swing.JButton();
jTextField3 = new javax.swing.JTextField();
jLabel3 = new javax.swing.JLabel();
jTextField4 = new javax.swing.JTextField();
jLabel4 = new javax.swing.JLabel();
jButton3 = new javax.swing.JButton();
jButton6 = new javax.swing.JButton();
jButton4 = new javax.swing.JButton();
jButton7 = new javax.swing.JButton();
jPanel2 = new javax.swing.JPanel();
jTextField5 = new javax.swing.JTextField();
jLabel5 = new javax.swing.JLabel();
jButton8 = new javax.swing.JButton();
jLabel2 = new javax.swing.JLabel();
jButton5 = new javax.swing.JButton();

setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
setTitle("Warengruppenmanager");

jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Warengruppen"));
jPanel1.setLayout(new java.awt.BorderLayout());

jTextField1.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jTextField1ActionPerformed(evt);
}
});

jLabel1.setText("Neue Kategorie");

jButton1.setText("Einf�gen");
jButton1.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton1ActionPerformed(evt);
}
});

jButton2.setText("Einf�gen");
jButton2.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton2ActionPerformed(evt);
}
});

jLabel3.setText("Neue Produktgruppe");

jLabel4.setText("Warengruppe (Pfad)");

jButton3.setText("Einf�gen");
jButton3.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton3ActionPerformed(evt);
}
});

jButton6.setText("Alle l�schen");
jButton6.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton6ActionPerformed(evt);
}
});

jButton4.setText("L�schen");
jButton4.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton4ActionPerformed(evt);
}
});

jButton7.setText("Schlie�en");
jButton7.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton7ActionPerformed(evt);
}
});

jPanel2.setBackground(new java.awt.Color(227, 219, 202));
jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

jTextField5.setText("->");

jLabel5.setText("Trennzeichen");

jButton8.setText("Speichern");
jButton8.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton8ActionPerformed(evt);
}
});

javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
jPanel2.setLayout(jPanel2Layout);
jPanel2Layout.setHorizontalGroup(
jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addGroup(jPanel2Layout.createSequentialGroup()
.addContainerGap()
.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addComponent(jLabel5)
.addGroup(jPanel2Layout.createSequentialGroup()
.addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addComponent(jButton8)))
.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
);
jPanel2Layout.setVerticalGroup(
jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addGroup(jPanel2Layout.createSequentialGroup()
.addContainerGap()
.addComponent(jLabel5)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
.addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addComponent(jButton8))
.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
);

jLabel2.setText("Neue Produktfamilie");

jButton5.setText("Zu aktuellem Produkt");
jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
public void mouseClicked(java.awt.event.MouseEvent evt) {
jButton5MouseClicked(evt);
}
});
jButton5.addActionListener(new java.awt.event.ActionListener() {
public void actionPerformed(java.awt.event.ActionEvent evt) {
jButton5ActionPerformed(evt);
}
});

javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
jPanel3.setLayout(jPanel3Layout);
jPanel3Layout.setHorizontalGroup(
jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addGroup(jPanel3Layout.createSequentialGroup()
.addContainerGap()
.addComponent(jButton4)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addComponent(jButton6)
.addContainerGap(215, Short.MAX_VALUE))
.addGroup(jPanel3Layout.createSequentialGroup()
.addGap(10, 10, 10)
.addComponent(jLabel1)
.addContainerGap(311, Short.MAX_VALUE))
.addGroup(jPanel3Layout.createSequentialGroup()
.addContainerGap()
.addComponent(jLabel2)
.addContainerGap(290, Short.MAX_VALUE))
.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
.addComponent(jLabel3)
.addGap(315, 315, 315))
.addGroup(jPanel3Layout.createSequentialGroup()
.addContainerGap()
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
.addComponent(jTextField3, javax.swing.GroupLayout.Alignment.LEADING)
.addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
.addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE))
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addComponent(jButton3)
.addComponent(jButton2)
.addComponent(jButton1))
.addContainerGap(137, Short.MAX_VALUE))
.addGroup(jPanel3Layout.createSequentialGroup()
.addGap(10, 10, 10)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addComponent(jLabel4)
.addGroup(jPanel3Layout.createSequentialGroup()
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
.addGroup(jPanel3Layout.createSequentialGroup()
.addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
.addGroup(jPanel3Layout.createSequentialGroup()
.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
.addComponent(jButton5)
.addComponent(jButton7))))))
.addContainerGap())
);
jPanel3Layout.setVerticalGroup(
jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
.addContainerGap()
.addComponent(jLabel1)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
.addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addComponent(jButton1))
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addComponent(jLabel2)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
.addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addComponent(jButton2))
.addGap(15, 15, 15)
.addComponent(jLabel3)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
.addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addComponent(jButton3))
.addGap(18, 18, 18)
.addComponent(jLabel4)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
.addComponent(jButton4)
.addComponent(jButton6))
.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addGroup(jPanel3Layout.createSequentialGroup()
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
.addComponent(jButton5)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addComponent(jButton7)
.addContainerGap())
.addGroup(jPanel3Layout.createSequentialGroup()
.addGap(18, 18, 18)
.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
.addContainerGap())))
);

javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
getContentPane().setLayout(layout);
layout.setHorizontalGroup(
layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 395, javax.swing.GroupLayout.PREFERRED_SIZE))
);
layout.setVerticalGroup(
layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
);

pack();
}// </editor-fold>//GEN-END:initComponents
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton5MouseClicked

        if (leaf) {
            try {
                if (produkt != null) {
                    produkt.setWarengruppenId(grp.getId());
                    field.setText(produkt.getProductgroupPath());
                } else if (produkt != null) {
                    produkt2.setWarengruppenId(grp.getId());
                    field.setText(produkt2.getProductgroupPath());
                }
            } catch (Exception e) {
                Log.Debug(e);
            }
            this.dispose();
        } else {
            Popup.notice("Sie m�ssen eine Produktgruppe ausw�hlen");
        }
    }//GEN-LAST:event_jButton5MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        if (leaf) {
            grp.destroy();
            setTreeData(true);

        } else if (family) {

            fag.destroy();
            setTreeData(true);
        } else if (category) {

            cat.destroy();
            setTreeData(true);
        } else {

            Log.Debug("Nichts ausgew�hlt.");
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (!jTextField1.getText().equals("")) {
            ProductGroupCategory gr = new ProductGroupCategory(ConnectionHandler.instanceOf());
            gr.setName(jTextField1.getText());
            gr.save();

            ProductGroupFamily fg = new ProductGroupFamily(ConnectionHandler.instanceOf());
            fg.setName("Default Famile");
            fg.setKategorieid(gr.getId());
            fg.save();

            ProductGroupGroup gj = new ProductGroupGroup(ConnectionHandler.instanceOf());
            gj.setName("Default Gruppe");
            gj.setFamilienid(fg.getId());
            gj.save();

            setTreeData(true);
        }


    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (category) {
            if (!jTextField2.getText().equals("")) {
                ProductGroupFamily fg = new ProductGroupFamily(ConnectionHandler.instanceOf());
                fg.setName(jTextField2.getText());
                fg.setKategorieid(cat.getId());
                fg.save();

                ProductGroupGroup gj = new ProductGroupGroup(ConnectionHandler.instanceOf());
                gj.setName("Default Gruppe");
                gj.setFamilienid(fg.getId());
                gj.save();
                setTreeData(true);
            }
        } else {
            Popup.notice("Sie m�ssen eine Kategorie w�hlen");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        if (family) {
            if (!jTextField3.getText().equals("")) {
                ProductGroupGroup gj = new ProductGroupGroup(ConnectionHandler.instanceOf());
                gj.setName(jTextField3.getText());
                gj.setFamilienid(fag.getId());
                gj.save();
                setTreeData(true);
            }

        } else {
            Popup.notice("Sie m�ssen eine Produktfamilie w�hlen");
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if (Popup.Y_N_dialog("Wirklich alle Produktgruppen, Familien und Kategorien l�schen?")) {
            this.p.deleteAll();
            this.setTreeData(true);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
}//GEN-LAST:event_jButton5ActionPerformed

private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
    Programmdaten.instanceOf().setWARENGRUPPEN_SEPARATOR(jTextField5.getText());
}//GEN-LAST:event_jButton8ActionPerformed
// Variables declaration - do not modify//GEN-BEGIN:variables
private javax.swing.JButton jButton1;
private javax.swing.JButton jButton2;
private javax.swing.JButton jButton3;
private javax.swing.JButton jButton4;
private javax.swing.JButton jButton5;
private javax.swing.JButton jButton6;
private javax.swing.JButton jButton7;
private javax.swing.JButton jButton8;
private javax.swing.JLabel jLabel1;
private javax.swing.JLabel jLabel2;
private javax.swing.JLabel jLabel3;
private javax.swing.JLabel jLabel4;
private javax.swing.JLabel jLabel5;
private javax.swing.JPanel jPanel1;
private javax.swing.JPanel jPanel2;
private javax.swing.JPanel jPanel3;
private javax.swing.JTextField jTextField1;
private javax.swing.JTextField jTextField2;
private javax.swing.JTextField jTextField3;
private javax.swing.JTextField jTextField4;
private javax.swing.JTextField jTextField5;
// End of variables declaration//GEN-END:variables
    public void setTreeData(boolean refresh) {
        this.jTree1 = null;
        jPanel1.removeAll();
        jPanel1.validate();
        jPanel1.updateUI();

        child = null;
        subchild = null;
        subsubchild = null;
        root = new DefaultMutableTreeNode("Warengruppen");


        cats = p.getCats(refresh);

        for (int i = 0; i < cats.size(); i++) {

            child = new DefaultMutableTreeNode(
                    cats.get(i));

            fams = p.getFams(((ProductGroupCategory) cats.get(i)).getID());
            for (int b = 0; b < fams.size(); b++) {

                subchild = new DefaultMutableTreeNode(
                        fams.get(b));
                grps = p.getGrps(((ProductGroupFamily) fams.get(b)).getID());
                for (int h = 0; h < grps.size(); h++) {

                    subsubchild = new DefaultMutableTreeNode(
                            grps.get(h));
                    subchild.add(subsubchild);
                }
                child.add(subchild);
            }
            root.add(child);
        }

        this.jTree1 = new JTree(root);
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.setViewportView(jTree1);
        jScrollPane1.validate();

        tree = this.jTree1;
        TreeModel model = tree.getModel();
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addTreeSelectionListener(this);
        tree.setExpandsSelectedPaths(true);

        this.jPanel1.add(jScrollPane1, BorderLayout.CENTER);

        TreeFormat.expandTree(tree);

        this.validate();
    }
}
