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

import java.awt.Component;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import mpv5.data.PropertyStore;
import mpv5.db.common.Context;
import mpv5.db.common.DatabaseObject;
import mpv5.db.common.DatabaseSearch;
import mpv5.db.common.NodataFoundException;
import mpv5.db.common.QueryHandler;
import mpv5.db.objects.Account;
import mpv5.globals.Messages;
import mpv5.logging.Log;
import mpv5.ui.dialogs.ControlApplet;
import mpv5.ui.dialogs.Popup;
import mpv5.ui.frames.MPView;
import mpv5.ui.panels.DataPanel;
import mpv5.usermanagement.MPSecurityManager;
import mpv5.utils.models.MPComboBoxModelItem;
import mpv5.utils.trees.TreeFormat;
import mpv5.utils.ui.TextFieldUtils;

/**
 *
 * 
 */
public class ControlPanel_Accounts extends javax.swing.JPanel implements ControlApplet, DataPanel {

    private static final long serialVersionUID = 1L;
    private ControlPanel_Accounts insta;
    private ArrayList<Object[]> arr;
    private Account dataOwner;
    private ArrayList<Account> data = new ArrayList<Account>();

    /** Creates new form ContactPanel
     */
    public ControlPanel_Accounts() {
        if (MPSecurityManager.checkAdminAccess()) {
            initComponents();
            tax.set_ValueClass(Double.class);
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

            refresh();
        }

//        ((JSpinner.DefaultEditor) classv.getSpinner().getEditor()).getTextField().setEditable(false);
    }

    public ControlPanel_Accounts(Account aThis) {
        if (MPSecurityManager.checkAdminAccess()) {
            initComponents();
            tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            refresh();
            setDataOwner(aThis, true);
        }

        ((JSpinner.DefaultEditor) classv.getSpinner().getEditor()).getTextField().setEditable(false);
    }

    public void showRequiredFields() {
        TextFieldUtils.blinkerRed(cname);
        TextFieldUtils.blinkerRed(parents);
        TextFieldUtils.blinkerRed(tax);
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
        parents = new mpv5.ui.beans.LabeledTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        desc = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        groupnameselect = new javax.swing.JComboBox();
        tax = new mpv5.ui.beans.LabeledTextField();
        jLabel5 = new javax.swing.JLabel();
        typeselect = new javax.swing.JComboBox();
        classv = new mpv5.ui.beans.LabeledSpinner();
        idtf = new mpv5.ui.beans.LabeledTextField();
        jPanel6 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("mpv5/resources/languages/Panels"); // NOI18N
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ControlPanel_Accounts.border.title"))); // NOI18N
        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(227, 219, 202));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        tree.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        tree.setToolTipText(bundle.getString("ControlPanel_Accounts.tree.toolTipText")); // NOI18N
        tree.setName("tree"); // NOI18N
        tree.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                treeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tree);

        jLabel1.setText(bundle.getString("ControlPanel_Accounts.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 121, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                .addContainerGap())
        );

        add(jPanel1, java.awt.BorderLayout.CENTER);

        rightpane.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ControlPanel_Accounts.rightpane.border.title"))); // NOI18N
        rightpane.setName("rightpane"); // NOI18N

        cname.set_Label(bundle.getString("ControlPanel_Accounts.cname._Label")); // NOI18N
        cname.setName("cname"); // NOI18N

        parents.set_Label(bundle.getString("ControlPanel_Accounts.parents._Label")); // NOI18N
        parents.setName("parents"); // NOI18N

        jScrollPane3.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTextArea2.setBackground(new java.awt.Color(238, 238, 238));
        jTextArea2.setColumns(20);
        jTextArea2.setEditable(false);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(5);
        jTextArea2.setText(bundle.getString("ControlPanel_Accounts.jTextArea2.text")); // NOI18N
        jTextArea2.setWrapStyleWord(true);
        jTextArea2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTextArea2.setName("jTextArea2"); // NOI18N
        jTextArea2.setOpaque(false);
        jScrollPane3.setViewportView(jTextArea2);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel2.setText(bundle.getString("ControlPanel_Accounts.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        desc.setColumns(20);
        desc.setRows(5);
        desc.setName("desc"); // NOI18N
        jScrollPane2.setViewportView(desc);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel3.setText(bundle.getString("ControlPanel_Accounts.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        jLabel4.setText(bundle.getString("ControlPanel_Accounts.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        groupnameselect.setName("groupnameselect"); // NOI18N

        tax.set_Label(bundle.getString("ControlPanel_Accounts.tax._Label")); // NOI18N
        tax.setName("tax"); // NOI18N

        jLabel5.setText(bundle.getString("ControlPanel_Accounts.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        typeselect.setName("typeselect"); // NOI18N

        classv.set_Label(bundle.getString("ControlPanel_Accounts.classv._Label")); // NOI18N
        classv.setName("classv"); // NOI18N

        idtf.set_Label(bundle.getString("ControlPanel_Accounts.idtf._Label")); // NOI18N
        idtf.setEnabled(false);
        idtf.setName("idtf"); // NOI18N

        javax.swing.GroupLayout rightpaneLayout = new javax.swing.GroupLayout(rightpane);
        rightpane.setLayout(rightpaneLayout);
        rightpaneLayout.setHorizontalGroup(
            rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightpaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                    .addComponent(jLabel3)
                    .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(rightpaneLayout.createSequentialGroup()
                        .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(43, 43, 43)
                        .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(typeselect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(groupnameselect, 0, 271, Short.MAX_VALUE)))
                    .addComponent(parents, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(classv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightpaneLayout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(idtf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        rightpaneLayout.setVerticalGroup(
            rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightpaneLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(idtf, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parents, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(classv, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(groupnameselect, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(typeselect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(rightpane, java.awt.BorderLayout.EAST);

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jButton4.setText(bundle.getString("ControlPanel_Accounts.jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton4);

        jButton3.setText(bundle.getString("ControlPanel_Accounts.jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton3);

        jButton2.setText(bundle.getString("ControlPanel_Accounts.jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton2);

        jButton1.setText(bundle.getString("ControlPanel_Accounts.jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel6.add(jButton1);

        add(jPanel6, java.awt.BorderLayout.PAGE_END);
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

        if (parents.hasText()) {
            if (dataOwner == null) {
                dataOwner = new Account();
            }
            DatabaseObject dato = dataOwner;
            if (QueryHandler.instanceOf().clone(Context.getAccounts()).checkUniqueness(Context.getAccounts().getUniqueColumns(), new JTextField[]{cname.getTextField()})) {
                dato.getPanelData(this);
                dato.setIDS(-1);
                if (dato.save()) {
                } else {
                    showRequiredFields();
                }
            }

            refresh();
        } else {
            showRequiredFields();
        }
}//GEN-LAST:event_jButton1ActionPerformed

    private void treeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_treeMouseClicked
        try {
            evt.consume();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

            if (evt.getClickCount() == 1) {
                if (node != null) {
                    Account g = (Account) node.getUserObject();
                    parents.set_Text(g.__getCName());
                }
            } else {
                if (node != null) {
                    Account g = (Account) node.getUserObject();
                    setDataOwner(g, true);
                }
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_treeMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private mpv5.ui.beans.LabeledSpinner classv;
    private mpv5.ui.beans.LabeledTextField cname;
    private javax.swing.JTextArea desc;
    private javax.swing.JComboBox groupnameselect;
    private mpv5.ui.beans.LabeledTextField idtf;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextArea jTextArea2;
    private mpv5.ui.beans.LabeledTextField parents;
    private javax.swing.JPanel rightpane;
    private mpv5.ui.beans.LabeledTextField tax;
    private javax.swing.JTree tree;
    private javax.swing.JComboBox typeselect;
    // End of variables declaration//GEN-END:variables
    public String description_ = "";
    public String defaults_ = "";
    public String cname_ = "";
    public int ids_;
    public int groupsids_ = 1;
    public int intaddedby_ = 1;
    public java.util.Date dateadded_ = new java.util.Date();
    public double taxvalue_ = 0;
    public int intaccountclass_ = 0;
    public int intaccounttype_ = 0;
    public int intparentaccount_ = 1;
    public int profitfid_;
    public int taxuid_;
    public int taxfid_;
    public String frame_ = "";
    public String hierarchypath_ = "";
    public int inttaxfid_;
    public int inttaxuid_;
    public int intprofitfid_;

    public void setValues(PropertyStore values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getUname() {
        return "Accounts";
    }

    public void reset() {
        if (dataOwner.isExisting()) {
            DatabaseObject dato = dataOwner;

            dato.getPanelData(this);
            dato.reset();
            setDataOwner(dato, true);
        }
    }

    public ControlApplet instanceOf() {
        if (insta == null) {
            insta = new ControlPanel_Accounts();
        }
        return insta;
    }

    public void refresh() {


        try {
            data = DatabaseObject.getObjects(Context.getAccounts());
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
            data = new ArrayList<Account>();
        }

        Account g = null;
        if (!MPView.getUser().isGroupRestricted()) {
            g = null;
            try {
                g = (Account) DatabaseObject.getObject(Context.getAccounts(), 1);
            } catch (NodataFoundException ex) {
                g = (Account) DatabaseObject.getObject(Context.getAccounts());
                g.setCName(Messages.ACCOUNTNAMES.toString());
                g.setIDS(-1);
            }
        } else {

            if (data.size() == 0) {
                g = new Account();
                g.setCName(MPView.getUser().__getCName());
                g.setIDS(-1);
                g.setIntparentaccount(1);
            } else {
                g = data.get(0);
            }

        }

        try {
            tree.setModel(Account.toTreeModel(data, g));
            TreeFormat.expandTree(tree);
        } catch (Exception e) {
            Log.Debug(this, e.getMessage());
        }

        if (!MPView.getUser().isGroupRestricted()) {
            groupnameselect.setModel(new DefaultComboBoxModel(
                    MPComboBoxModelItem.toItems(new DatabaseSearch(Context.getGroup()).getValuesFor(Context.getGroup().getSubID(), null, ""))));
        } else {
            groupnameselect.setModel(new DefaultComboBoxModel(
                    MPComboBoxModelItem.toItems(new DatabaseSearch(Context.getGroup()).getValuesFor(Context.getGroup().getSubID(), "ids", MPView.getUser().__getGroupsids()))));
        }

        groupnameselect.setSelectedIndex(MPComboBoxModelItem.getItemID(MPView.getUser().__getGroupsids(), groupnameselect.getModel()));

        typeselect.setModel(new DefaultComboBoxModel(MPComboBoxModelItem.toItems(
                new Object[][]{
                    {Account.ASSET, Messages.ASSET},
                    {Account.COST, Messages.COST},
                    {Account.EQUITY, Messages.EQUITY},
                    {Account.EXPENSE, Messages.EXPENSE},
                    {Account.INCOME, Messages.INCOME},
                    {Account.LIABILITY, Messages.LIABILITY}
                })));
//        try {
//            classv.getSpinner().setModel(new SpinnerListModel(QueryHandler.getConnection().clone(Context.getAccounts()).getColumn("intaccountclass", -1)));
//        } catch (NodataFoundException ex) {
//            Log.Debug(this, ex.getMessage());
//            classv.getSpinner().setModel(new SpinnerListModel());
//        }
//
//        try {
//            classv.getSpinner().setValue(0);
//        } catch (Exception e) {
//        }

    }

    public boolean collectData() {
        if (cname.getText().length() == 0) {
            return false;
        }
        cname_ = cname.get_Text();
        description_ = desc.getText();
//        defaults_ = defaults.get_Text();
        try {
            intparentaccount_ = Integer.valueOf(new DatabaseSearch(Context.getAccounts()).searchFor(new String[]{"ids"}, "cname", parents.get_Text(), true)[0].toString());
        } catch (NodataFoundException ex) {
            intparentaccount_ = 1;
        }

        if (groupnameselect.getSelectedItem() != null) {
            groupsids_ = Integer.valueOf(((MPComboBoxModelItem) groupnameselect.getSelectedItem()).getId());
        } else {
            groupsids_ = 1;
        }

        taxvalue_ = Double.valueOf(tax.getText());
        intaccountclass_ = Integer.valueOf(String.valueOf(classv.getSpinner().getValue()));
        intaccounttype_ = Integer.valueOf(((MPComboBoxModelItem) typeselect.getSelectedItem()).getId());
    return true;
    }

    public DatabaseObject getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(DatabaseObject object, boolean pop) {
        dataOwner = (Account) object;
        if (pop) {
            dataOwner.setPanelData(this);
            this.exposeData();
        }
    }

    public void exposeData() {
        cname.set_Text(cname_);
        desc.setText(description_);
        try {
            parents.set_Text(DatabaseObject.getObject(Context.getAccounts(), intparentaccount_).__getCName());
            groupnameselect.setSelectedIndex(MPComboBoxModelItem.getItemID(groupsids_, groupnameselect.getModel()));
            typeselect.setSelectedIndex(MPComboBoxModelItem.getItemID(intaccounttype_, typeselect.getModel()));
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex);
        }

        tax.setText(taxvalue_);
        classv.set_Value(intaccountclass_);
        idtf.setText(String.valueOf(ids_));
    }

    public void paste(DatabaseObject dbo) {
        if (dbo.getDbIdentity().equals(Context.getAccounts().getDbIdentity())) {
            setDataOwner(dbo, true);
        } else {
            MPView.addMessage(Messages.NOT_POSSIBLE.toString() + Messages.ACTION_PASTE);
        }
    }

    public void showSearchBar(boolean show) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Component getAndRemoveActionPanel() {
        this.remove(jPanel6);
        validate();
        return jPanel6;
    }

    @Override
    public void actionAfterSave() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAfterCreate() {
    }
}
