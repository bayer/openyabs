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
 * AddressPanel.java
 *
 * Created on 26.02.2009, 11:33:08
 */
package mpv5.ui.panels;

import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import mpv5.db.common.Context;
import mpv5.db.common.DatabaseObject;
import mpv5.db.common.NodataFoundException;
import mpv5.globals.Messages;
import mpv5.db.objects.Address;
import mpv5.db.objects.Contact;
import mpv5.logging.Log;
import mpv5.i18n.LanguageManager;
import mpv5.ui.frames.MPView;
import mpv5.utils.models.MPComboBoxModelItem;
import mpv5.utils.ui.TextFieldUtils;

/**
 *
 *  
 */
public class AddressPanel extends javax.swing.JPanel implements DataPanel {

    private static final long serialVersionUID = 8513278191283931211L;
    private Address dataOwner = new Address();
    private Contact dataParent = new Contact();
    private java.util.ResourceBundle bundle = mpv5.i18n.LanguageManager.getBundle();

    /** Creates new form AddressPanel */
    public AddressPanel() {
        initComponents();
        dataOwner.setCountry(mpv5.db.objects.User.getCurrentUser().__getDefcountry());
        refresh();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        department = new mpv5.ui.beans.LabeledTextField();
        jLabel5 = new javax.swing.JLabel();
        countryselect = new javax.swing.JComboBox();
        title = new mpv5.ui.beans.LabeledTextField();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        street = new mpv5.ui.beans.LabeledTextField();
        prename = new mpv5.ui.beans.LabeledTextField();
        city = new mpv5.ui.beans.LabeledTextField();
        zip = new mpv5.ui.beans.LabeledTextField();
        cname = new mpv5.ui.beans.LabeledTextField();
        dadress = new javax.swing.JCheckBox();
        badress = new javax.swing.JCheckBox();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setBackground(new java.awt.Color(227, 219, 202));
        setName("Address#"); // NOI18N

        jPanel2.setBackground(new java.awt.Color(227, 219, 202));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setName("jPanel2"); // NOI18N

        mpv5.i18n.LanguageManager.getBundle();
        department.set_Label(bundle.getString("AddressPanel.department._Label")); // NOI18N
        department.setFont(department.getFont().deriveFont(department.getFont().getStyle() | java.awt.Font.BOLD));
        department.setName("department"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 11));
        jLabel5.setText(bundle.getString("AddressPanel.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        countryselect.setName("countryselect"); // NOI18N
        countryselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryselectActionPerformed(evt);
            }
        });

        title.set_Label(bundle.getString("AddressPanel.title._Label")); // NOI18N
        title.setName("title"); // NOI18N

        male.setFont(male.getFont().deriveFont(male.getFont().getStyle() & ~java.awt.Font.BOLD));
        male.setSelected(true);
        male.setText(bundle.getString("AddressPanel.male.text")); // NOI18N
        male.setName("male"); // NOI18N
        male.setOpaque(false);

        female.setFont(female.getFont().deriveFont(female.getFont().getStyle() & ~java.awt.Font.BOLD));
        female.setText(bundle.getString("AddressPanel.female.text")); // NOI18N
        female.setName("female"); // NOI18N
        female.setOpaque(false);

        street.set_Label(bundle.getString("AddressPanel.street._Label")); // NOI18N
        street.setName("street"); // NOI18N

        prename.set_Label(bundle.getString("AddressPanel.prename._Label")); // NOI18N
        prename.setName("prename"); // NOI18N

        city.set_Label(bundle.getString("AddressPanel.city._Label")); // NOI18N
        city.setName("city"); // NOI18N

        zip.set_Label(bundle.getString("AddressPanel.zip._Label")); // NOI18N
        zip.setName("zip"); // NOI18N

        cname.set_Label(bundle.getString("AddressPanel.cname._Label")); // NOI18N
        cname.setName("cname"); // NOI18N

        dadress.setText(bundle.getString("AddressPanel.dadress.text")); // NOI18N
        dadress.setName("dadress"); // NOI18N
        dadress.setOpaque(false);

        badress.setText(bundle.getString("AddressPanel.badress.text")); // NOI18N
        badress.setName("badress"); // NOI18N
        badress.setOpaque(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/filesave.png"))); // NOI18N
        jButton2.setText(bundle.getString("AddressPanel.jButton2.text")); // NOI18N
        jButton2.setToolTipText(bundle.getString("AddressPanel.jButton2.toolTipText")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/remove.png"))); // NOI18N
        jButton1.setText(bundle.getString("AddressPanel.jButton1.text")); // NOI18N
        jButton1.setToolTipText(bundle.getString("AddressPanel.jButton1.toolTipText")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addComponent(male)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(female))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(street, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(prename, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
                                    .addComponent(city, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cname, 0, 0, Short.MAX_VALUE)
                                    .addComponent(zip, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(badress)
                                    .addComponent(dadress)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countryselect, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(countryselect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jButton2)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(male)
                    .addComponent(female))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(badress)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dadress))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(prename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(zip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(street, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        removeAddress();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        addAddress();

    }//GEN-LAST:event_jButton2ActionPerformed

    private void countryselectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryselectActionPerformed
}//GEN-LAST:event_countryselectActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox badress;
    private mpv5.ui.beans.LabeledTextField city;
    private mpv5.ui.beans.LabeledTextField cname;
    private javax.swing.JComboBox countryselect;
    private javax.swing.JCheckBox dadress;
    private mpv5.ui.beans.LabeledTextField department;
    private javax.swing.JRadioButton female;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JRadioButton male;
    private mpv5.ui.beans.LabeledTextField prename;
    private mpv5.ui.beans.LabeledTextField street;
    private mpv5.ui.beans.LabeledTextField title;
    private mpv5.ui.beans.LabeledTextField zip;
    // End of variables declaration//GEN-END:variables
    public String city_;
    public String cname_;
    public String taxnumber_;
    public String department_;
    public boolean ismale_;
    public String prename_;
    public String street_;
    public String title_;
    public String zip_;
    public String company_;
    public String country_;
    public int ids_;
    public int contactsids_;
    public int groupsids_ = 1;
    public java.util.Date dateadded_ = new java.util.Date();
    public int intaddedby_ = 4343;
    public int inttype_;

    public boolean collectData() {
        if (cname.getText().length() == 0) {
            return false;
        }
        ids_ = dataOwner.__getIDS();
        city_ = city.get_Text();
        cname_ = cname.get_Text();
        taxnumber_ = dataParent.__getTaxnumber();
        company_ = dataParent.__getCompany();
        country_ = dataParent.__getCountry();
        ismale_ = male.isSelected();
        prename_ = prename.get_Text();
        street_ = street.get_Text();
        title_ = title.get_Text();
        zip_ = zip.get_Text();
        department_ = department.get_Text();
        contactsids_ = dataParent.__getIDS();

        if (badress.isSelected() && dadress.isSelected()) {
            inttype_ = 2;
        } else if (badress.isSelected()) {
            inttype_ = 0;
        } else if (dadress.isSelected()) {
            inttype_ = 1;
        }

        if (countryselect.getSelectedItem() != null) {
            country_ = String.valueOf(((MPComboBoxModelItem) countryselect.getSelectedItem()).getValue());
        } else {
            country_ = "";
        }

        return true;
    }

    public void exposeData() {
        city.set_Text(city_);
        male.setSelected(ismale_);
        female.setSelected(!ismale_);
        prename.set_Text(prename_);
        street.set_Text(street_);
        cname.setText(cname_);
        title.set_Text(title_);
        zip.set_Text(zip_);
        department.set_Text(department_);
        try {
            dataParent = (Contact) DatabaseObject.getObject(Context.getContact(), contactsids_);
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
        }
        try {
            countryselect.setSelectedIndex(MPComboBoxModelItem.getItemIDfromValue(country_, countryselect.getModel()));
        } catch (Exception e) {
            try {
                countryselect.setSelectedIndex(MPComboBoxModelItem.getItemIDfromValue(dataParent.__getCountry(), countryselect.getModel()));
            } catch (Exception ex) {
            }
        }
        badress.setSelected(inttype_ == 0 || inttype_ == 2);
        dadress.setSelected(inttype_ == 1 || inttype_ == 2);
    }

    public DatabaseObject getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(DatabaseObject object, boolean populate) {
        dataOwner = (Address) object;
        if (populate) {
            dataOwner.setPanelData(this);
            this.exposeData();

            if (this.getParent() instanceof JTabbedPane) {
                JTabbedPane jTabbedPane = (JTabbedPane) this.getParent();
                jTabbedPane.setTitleAt(jTabbedPane.getSelectedIndex(), Messages.CONTACT + cname_);
            }
        }
    }

    public void refresh() {
        try {
            countryselect.setModel(LanguageManager.getCountriesAsComboBoxModel());
            Runnable runnable = new Runnable() {

                public void run() {
                    countryselect.setSelectedIndex(MPComboBoxModelItem.getItemID(mpv5.db.objects.User.getCurrentUser().__getDefcountry(), countryselect.getModel()));
                }
            };
            SwingUtilities.invokeLater(runnable);
        } catch (Exception e) {
        }
    }

    public void paste(DatabaseObject... dbos) {
        for (DatabaseObject dbo : dbos) {
            if (dbo.getDbIdentity().equals(Context.getAddress().getDbIdentity())) {
                setDataOwner(dbo, true);
            } else {
                MPView.addMessage(Messages.NOT_POSSIBLE.toString() + Messages.ACTION_PASTE.toString());
            }
        }

    }

    public void showRequiredFields() {
        TextFieldUtils.blinkerRed(cname);
    }

    /**
     * Removes this adress from the current user
     */
    public void removeAddress() {
        this.getParent().remove(this);
        dataOwner.delete();
    }

    /**
     * Associates this adress with the current user
     */
    public void addAddress() {
        if (dataParent.isExisting()) {
            dataOwner.setContactsids(dataParent.__getIDS());
            dataOwner.getPanelData(this);
            if (!dataOwner.save()) {
                showRequiredFields();
            } else {
                ((JTabbedPane) getParent()).setTitleAt(
                        ((JTabbedPane) getParent()).getSelectedIndex(),
                        dataOwner.__getCName());
            }
        }
    }

    /**
     * @return the dataParent
     */
    public Contact getDataParent() {
        return dataParent;
    }

    /**
     * @param dataParent the dataParent to set
     */
    public void setDataParent(Contact dataParent) {
        this.dataParent = dataParent;

    }

    public void showSearchBar(boolean show) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionAfterSave() {
    }

    @Override
    public void actionAfterCreate() {
    }

    public void actionBeforeCreate() {
    }

    public void actionBeforeSave() {
    }

    public void mail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void print() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
