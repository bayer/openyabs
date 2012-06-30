/*
This file is part of YaBS.

YaBS is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

YaBS is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with YaBS.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * ContactPanel.java
 *
 * Created on Nov 20, 2008, 8:17:28 AM
 */
package mpv5.ui.dialogs.subcomponents;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreeSelectionModel;
import mpv5.data.PropertyStore;
import mpv5.db.common.Context;
import mpv5.db.common.DatabaseObject;
import mpv5.db.common.NodataFoundException;
import mpv5.db.common.QueryHandler;
import mpv5.db.objects.ProductGroup;
import mpv5.globals.Messages;

import mpv5.logging.Log;
import mpv5.ui.dialogs.ControlApplet;
import mpv5.ui.dialogs.Popup;
import mpv5.ui.dialogs.Wizard;
import mpv5.ui.frames.MPView;
import mpv5.ui.panels.DataPanel;
import mpv5.usermanagement.MPSecurityManager;
import mpv5.utils.trees.TreeFormat;
import mpv5.utils.ui.TextFieldUtils;

/**
 *
 * 
 */
public class ControlPanel_ProductGroups extends javax.swing.JPanel implements ControlApplet, DataPanel {

    private static final long serialVersionUID = 1L;
    private ControlPanel_ProductGroups insta;
    private ArrayList<Object[]> arr;
    private ProductGroup dataOwner;

    /** Creates new form ContactPanel
     */
    public ControlPanel_ProductGroups() {
        if (MPSecurityManager.checkAdminAccess()) {
            initComponents();
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            gcombobox.setSearchEnabled(true);
            gcombobox.setContext(Context.getGroup());

            refresh();
        }
    }

    public ControlPanel_ProductGroups(ProductGroup aThis) {
        if (MPSecurityManager.checkAdminAccess()) {
            initComponents();
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            gcombobox.setSearchEnabled(true);
            gcombobox.setContext(Context.getGroup());

            refresh();
            setDataOwner(aThis, true);
        }
    }

    @Override
    public void showRequiredFields() {
        TextFieldUtils.blinkerRed(cname);
        TextFieldUtils.blinkerGrey(parents);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tree = new javax.swing.JTree();
        jLabel1 = new javax.swing.JLabel();
        rightpane = new javax.swing.JPanel();
        cname = new mpv5.ui.beans.LabeledTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        gcombobox = new mpv5.ui.beans.LabeledCombobox();
        path = new mpv5.ui.beans.LabeledTextField();
        parents = new mpv5.ui.beans.LabeledTextField();
        jPanel2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        java.util.ResourceBundle bundle = mpv5.i18n.LanguageManager.getBundle(); // NOI18N
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ControlPanel_ProductGroups.border.title"))); // NOI18N
        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setToolTipText(bundle.getString("ControlPanel_ProductGroups.tree.toolTipText")); // NOI18N
        tree.setLargeModel(true);
        tree.setName("tree"); // NOI18N
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tree);

        jLabel1.setText(bundle.getString("ControlPanel_ProductGroups.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 315, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        rightpane.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ControlPanel_ProductGroups.rightpane.border.title"))); // NOI18N
        rightpane.setName("rightpane"); // NOI18N

        cname.set_Label(bundle.getString("ControlPanel_ProductGroups.cname._Label")); // NOI18N
        cname.setName("cname"); // NOI18N

        jScrollPane3.setBackground(new java.awt.Color(254, 254, 254));
        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setText(bundle.getString("ControlPanel_ProductGroups.jTextArea2.text")); // NOI18N
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextArea2.setName("jTextArea2"); // NOI18N
        jScrollPane3.setViewportView(jTextArea2);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText(bundle.getString("ControlPanel_ProductGroups.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        desc.setColumns(20);
        desc.setRows(5);
        desc.setName("desc"); // NOI18N
        jScrollPane2.setViewportView(desc);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText(bundle.getString("ControlPanel_ProductGroups.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        gcombobox.set_Label(bundle.getString("ControlPanel_ProductGroups.gcombobox._Label")); // NOI18N
        gcombobox.setName("gcombobox"); // NOI18N

        path.set_Label(bundle.getString("ControlPanel_ProductGroups.path._Label")); // NOI18N
        path.setName("path"); // NOI18N

        parents.set_Label(bundle.getString("ControlPanel_ProductGroups.parents._Label")); // NOI18N
        parents.setEnabled(false);
        parents.setName("parents"); // NOI18N

        javax.swing.GroupLayout rightpaneLayout = new javax.swing.GroupLayout(rightpane);
        rightpane.setLayout(rightpaneLayout);
        rightpaneLayout.setHorizontalGroup(
            rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightpaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(cname, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(gcombobox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(path, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE)
                    .addComponent(parents, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 340, Short.MAX_VALUE))
                .addContainerGap())
        );
        rightpaneLayout.setVerticalGroup(
            rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightpaneLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(path, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(41, Short.MAX_VALUE))
        );

        add(rightpane, java.awt.BorderLayout.EAST);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton5.setText(bundle.getString("ControlPanel_ProductGroups.jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jPanel2.add(jSeparator1);

        jButton4.setText(bundle.getString("ControlPanel_ProductGroups.jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4);

        jButton3.setText(bundle.getString("ControlPanel_ProductGroups.jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3);

        jButton2.setText(bundle.getString("ControlPanel_ProductGroups.jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton2);

        jButton1.setText(bundle.getString("ControlPanel_ProductGroups.jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1);

        add(jPanel2, java.awt.BorderLayout.PAGE_END);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (Popup.Y_N_dialog(Messages.REALLY_DELETE)) {
            if (dataOwner != null) {
                DatabaseObject dato = dataOwner;
                dato.getPanelData(this);
                if (dato.__getIDS().intValue() == 1 || !dato.delete()) {
                    Popup.notice(Messages.NOT_POSSIBLE + "\n" + Messages.IN_USE);
                }
            }
            refresh();
        }
}//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        reset();
}//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (dataOwner != null) {
            DatabaseObject dato = dataOwner;
            dato.getPanelData(this);
            if (dato.save()) {
            } else {
                showRequiredFields();
            }
        }
}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (dataOwner == null) {
            dataOwner = new ProductGroup();
        }
        try {
            DatabaseObject dato = dataOwner;
            if (QueryHandler.instanceOf().clone(Context.getGroup()).checkUniqueness(Context.getGroup().getUniqueColumns(), new JTextField[]{cname.getTextField()})) {
                dato.getPanelData(this);
                dato.setIDS(-1);
                if (dato.save()) {
                } else {
                    showRequiredFields();
                }
            }
        } catch (Exception e) {
        }
        refresh();
}//GEN-LAST:event_jButton1ActionPerformed

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeMouseClicked
        evt.consume();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (evt.getButton() == MouseEvent.BUTTON2 || evt.getButton() == MouseEvent.BUTTON3) {
            if (node != null) {
                ProductGroup g = (ProductGroup) node.getUserObject();
                parents.set_Text(g.__getCname());
                path.setText(getPath(node, 0));
            }
        } else {
            if (node != null) {
                ProductGroup g = (ProductGroup) node.getUserObject();
                setDataOwner(g, true);
                path.setText(getPath(node, 1));
            }
        }
    }//GEN-LAST:event_treeMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        importf();
    }//GEN-LAST:event_jButton5ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private mpv5.ui.beans.LabeledTextField cname;
    private javax.swing.JTextArea desc;
    private mpv5.ui.beans.LabeledCombobox gcombobox;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea2;
    private mpv5.ui.beans.LabeledTextField parents;
    private mpv5.ui.beans.LabeledTextField path;
    private javax.swing.JPanel rightpane;
    private javax.swing.JTree tree;
    // End of variables declaration//GEN-END:variables
    public String description_ = "";
    public String defaults_ = "";
    public String cname_ = "";
    public int ids_;
    public int groupsids_ = 1;
    public int intaddedby_ = 1;
    public int productgroupsids_;
    public java.util.Date dateadded_ = new java.util.Date();
    public String hierarchypath_ = "";

    @Override
    public void setValues(PropertyStore values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getUname() {
        return "ProductGroups";
    }

    @Override
    public void reset() {
      if (dataOwner!=null && dataOwner.isExisting()) {
            DatabaseObject dato = dataOwner;

            dato.getPanelData(this);
            dato.reset();
            setDataOwner(dato, true);
        }
    }

    @Override
    public void refresh() {

        ArrayList<ProductGroup> data = null;
        try {
            data = DatabaseObject.getObjects(Context.getProductGroup());
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
        }

        ProductGroup g;
        try {
            g = (ProductGroup) DatabaseObject.getObject(Context.getProductGroup(), 1);
        } catch (NodataFoundException ex) {
            g = new ProductGroup(Messages.GROUPNAMES.toString());
            g.setIDS(-1);
        }

        tree.setModel(ProductGroup.toTreeModel(data, g));
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                gcombobox.triggerSearch();
                TreeFormat.expandTree(tree);
            }
        };
        SwingUtilities.invokeLater(runnable);
    }

    @Override
    public boolean collectData() {
        if (cname.getText().length() == 0) {
            return false;
        }
        cname_ = cname.get_Text();
        description_ = desc.getText();
        hierarchypath_ = path.getText();
        try {
            productgroupsids_ = DatabaseObject.getObject(Context.getProductGroup(), parents.get_Text()).__getIDS();
        } catch (NodataFoundException ex) {
            productgroupsids_ = 1;
        }

        try {
            groupsids_ = Integer.valueOf(gcombobox.getSelectedItem().getId());
        } catch (NumberFormatException numberFormatException) {
            groupsids_ = 1;
        }

        return true;
    }

    @Override
    public DatabaseObject getDataOwner() {
        return dataOwner;
    }

    @Override
    public void setDataOwner(DatabaseObject object, boolean p) {
        dataOwner = (ProductGroup) object;
        if (p) {
            dataOwner.setPanelData(this);
            this.exposeData();
        }
    }

    @Override
    public void exposeData() {
        cname.set_Text(cname_);
        desc.setText(description_);
        path.setText(hierarchypath_);
        try {
            parents.set_Text(DatabaseObject.getObject(Context.getProductGroup(), productgroupsids_).__getCname());
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
        }
        try {
//        defaults.set_Text(defaults_);
            gcombobox.setModel(DatabaseObject.getObject(Context.getGroup(), groupsids_));
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
        }
    }

    @Override
    public void paste(DatabaseObject... dbos) {
        for (DatabaseObject dbo : dbos) {
            if (dbo.getDbIdentity().equals(Context.getGroup().getDbIdentity())) {
                setDataOwner(dbo, true);
            } else {
                mpv5.YabsViewProxy.instance().addMessage(Messages.NOT_POSSIBLE.toString() + Messages.ACTION_PASTE, Color.RED);
            }
        }
    }

    @Override
    public void showSearchBar(boolean show) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Component getAndRemoveActionPanel() {
        this.remove(jPanel2);
        validate();
        return jPanel2;
    }

    @Override
    public void actionAfterSave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAfterCreate() {
    }

    private String getPath(DefaultMutableTreeNode node, int shift) {
        TreeNode[] p = node.getPath();
        String path1 = "";
        for (int i = 0; i < p.length - shift; i++) {
            TreeNode treeNode = p[i];
            path1 += treeNode.toString() + File.separator;
        }

        return path1;
    }

    private void importf() {
        Wizard w = new Wizard(false);
        w.addPanel(new wizard_CSVImport_1(w));
        w.addPanel(new wizard_CSV_ProductGroups_Import_1(w));
        w.showWiz();
    }

    public void actionBeforeCreate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void actionBeforeSave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void print() {
        mpv5.utils.export.Export.print(this);
    }
}
