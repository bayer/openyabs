/*
 *  This file is part of MP.
 *
 *      MP is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      MP is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with MP.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * AddressPanel.java
 *
 * Created on 26.02.2009, 11:33:08
 */
package mpv5.ui.panels;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import mpv5.db.common.Context;
import mpv5.db.common.DatabaseObject;
import mpv5.db.common.DatabaseSearch;
import mpv5.db.common.NodataFoundException;
import mpv5.globals.Messages;
import mpv5.db.objects.Address;
import mpv5.db.objects.Contact;
import mpv5.logging.Log;
import mpv5.i18n.LanguageManager;
import mpv5.ui.frames.MPV5View;
import mpv5.db.objects.User;
import mpv5.utils.arrays.ArrayUtilities;
import mpv5.utils.models.MPComboBoxModelItem;
import mpv5.utils.ui.TextFieldUtils;

/**
 *
 *  anti
 */
public class AddressPanel extends javax.swing.JPanel implements DataPanel {

    private static final long serialVersionUID = 8513278191283931211L;
    private Address dataOwner = new Address();
    private Contact dataParent = new Contact();

    /** Creates new form AddressPanel */
    public AddressPanel() {
        initComponents();
        refresh();
        companyselect.setSearchOnEnterEnabled(true);
        companyselect.setContext(Context.getCompany());
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        badress = new javax.swing.JCheckBox();
        dadress = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        title = new mpv5.ui.beans.LabeledTextField();
        street = new mpv5.ui.beans.LabeledTextField();
        cname = new mpv5.ui.beans.LabeledTextField();
        prename = new mpv5.ui.beans.LabeledTextField();
        city = new mpv5.ui.beans.LabeledTextField();
        jLabel3 = new javax.swing.JLabel();
        zip = new mpv5.ui.beans.LabeledTextField();
        male = new javax.swing.JRadioButton();
        female = new javax.swing.JRadioButton();
        taxnumber = new mpv5.ui.beans.LabeledTextField();
        department = new mpv5.ui.beans.LabeledTextField();
        jLabel5 = new javax.swing.JLabel();
        countryselect = new javax.swing.JComboBox();
        companyselect = new mpv5.ui.beans.MPCombobox();

        setBackground(new java.awt.Color(227, 219, 202));
        setName("Address#"); // NOI18N

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N
        jToolBar1.setOpaque(false);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/filesave.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("mpv5/resources/languages/Panels"); // NOI18N
        jButton2.setText(bundle.getString("AddressPanel.jButton2.text")); // NOI18N
        jButton2.setToolTipText(bundle.getString("AddressPanel.jButton2.toolTipText")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setOpaque(true);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/remove.png"))); // NOI18N
        jButton1.setText(bundle.getString("AddressPanel.jButton1.text")); // NOI18N
        jButton1.setToolTipText(bundle.getString("AddressPanel.jButton1.toolTipText")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setOpaque(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        badress.setText(bundle.getString("AddressPanel.badress.text")); // NOI18N
        badress.setName("badress"); // NOI18N
        badress.setOpaque(false);
        jToolBar1.add(badress);

        dadress.setText(bundle.getString("AddressPanel.dadress.text")); // NOI18N
        dadress.setName("dadress"); // NOI18N
        dadress.setOpaque(false);
        jToolBar1.add(dadress);

        jPanel2.setBackground(new java.awt.Color(227, 219, 202));
        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jPanel2.setName("jPanel2"); // NOI18N

        title.set_Label(bundle.getString("AddressPanel.title._Label")); // NOI18N
        title.setName("title"); // NOI18N

        street.set_Label(bundle.getString("AddressPanel.street._Label")); // NOI18N
        street.setName("street"); // NOI18N

        cname.set_Label(bundle.getString("AddressPanel.cname._Label")); // NOI18N
        cname.setName("cname"); // NOI18N

        prename.set_Label(bundle.getString("AddressPanel.prename._Label")); // NOI18N
        prename.setName("prename"); // NOI18N

        city.set_Label(bundle.getString("AddressPanel.city._Label")); // NOI18N
        city.setName("city"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText(bundle.getString("AddressPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        zip.set_Label(bundle.getString("AddressPanel.zip._Label")); // NOI18N
        zip.setName("zip"); // NOI18N

        male.setFont(male.getFont().deriveFont(male.getFont().getStyle() & ~java.awt.Font.BOLD));
        male.setSelected(true);
        male.setText(bundle.getString("AddressPanel.male.text")); // NOI18N
        male.setName("male"); // NOI18N
        male.setOpaque(false);

        female.setFont(female.getFont().deriveFont(female.getFont().getStyle() & ~java.awt.Font.BOLD));
        female.setText(bundle.getString("AddressPanel.female.text")); // NOI18N
        female.setName("female"); // NOI18N
        female.setOpaque(false);

        taxnumber.set_Label(bundle.getString("AddressPanel.taxnumber._Label")); // NOI18N
        taxnumber.setFont(taxnumber.getFont().deriveFont(taxnumber.getFont().getStyle() | java.awt.Font.BOLD));
        taxnumber.setName("taxnumber"); // NOI18N

        department.set_Label(bundle.getString("AddressPanel.department._Label")); // NOI18N
        department.setFont(department.getFont().deriveFont(department.getFont().getStyle() | java.awt.Font.BOLD));
        department.setName("department"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 12));
        jLabel5.setText(bundle.getString("AddressPanel.jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        countryselect.setName("countryselect"); // NOI18N
        countryselect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryselectActionPerformed(evt);
            }
        });

        companyselect.setName("companyselect"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(street, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 89, Short.MAX_VALUE)
                                .addComponent(male)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(female)
                                .addGap(52, 52, 52))
                            .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(prename, javax.swing.GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
                            .addComponent(city, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cname, 0, 0, Short.MAX_VALUE)
                            .addComponent(zip, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(department, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(companyselect, javax.swing.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(countryselect, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(taxnumber, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(countryselect, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel5)
                        .addComponent(jLabel3))
                    .addComponent(companyselect, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(taxnumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(male)
                    .addComponent(female))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(prename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zip, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(street, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 635, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
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
    private mpv5.ui.beans.MPCombobox companyselect;
    private javax.swing.JComboBox countryselect;
    private javax.swing.JCheckBox dadress;
    private mpv5.ui.beans.LabeledTextField department;
    private javax.swing.JRadioButton female;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JRadioButton male;
    private mpv5.ui.beans.LabeledTextField prename;
    private mpv5.ui.beans.LabeledTextField street;
    private mpv5.ui.beans.LabeledTextField taxnumber;
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

    public void collectData() {
        city_ = city.get_Text();
        cname_ = cname.get_Text();
        taxnumber_ = taxnumber.get_Text();

        if (companyselect.getSelectedItem() != null) {
            company_ = String.valueOf(((MPComboBoxModelItem) companyselect.getSelectedItem()).getValue());
        } else {
            company_ = "";
        }

        if (countryselect.getSelectedItem() != null) {
            country_ = String.valueOf(((MPComboBoxModelItem) countryselect.getSelectedItem()).getValue());
        } else {
            country_ = "";
        }


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


    }

    public void exposeData() {
        city.set_Text(city_);
        cname.set_Text(cname_);
        taxnumber.set_Text(taxnumber_);
        try {
            companyselect.setSelectedIndex(MPComboBoxModelItem.getItemIDfromValue(company_, companyselect.getModel()));
        } catch (Exception e) {
        }

        try {
            countryselect.setSelectedIndex(MPComboBoxModelItem.getItemIDfromValue(country_, countryselect.getModel()));
        } catch (Exception e) {
        }

        male.setSelected(ismale_);
        female.setSelected(!ismale_);
        prename.set_Text(prename_);
        street.set_Text(street_);
        title.set_Text(title_);
        zip.set_Text(zip_);
        department.set_Text(department_);
        try {
            dataParent = (Contact) DatabaseObject.getObject(Context.getContact(), contactsids_);
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex);
            removeAddress();
        }


        badress.setSelected(inttype_ == 0 || inttype_ == 2);
        dadress.setSelected(inttype_ == 1 || inttype_ == 2);

    }

    public DatabaseObject getDataOwner() {
        return dataOwner;
    }

    public void setDataOwner(DatabaseObject object) {
        dataOwner = (Address) object;
        dataOwner.setPanelData(this);
        this.exposeData();

        if (this.getParent() instanceof JTabbedPane) {
            JTabbedPane jTabbedPane = (JTabbedPane) this.getParent();
            jTabbedPane.setTitleAt(jTabbedPane.getSelectedIndex(), Messages.CONTACT + cname_);
        }
    }

    public void refresh() {

        Runnable runnable = new Runnable() {

            public void run() {
                try {
                    countryselect.setModel(LanguageManager.getCountriesAsComboBoxModel());
                    countryselect.setSelectedIndex(MPComboBoxModelItem.getItemIDfromValue(MPV5View.getUser().__getDefcountry(), countryselect.getModel()));
                } catch (Exception e) {
                    Log.Debug(this, e);
                }
            }
        };

        SwingUtilities.invokeLater(runnable);
    }

    public void paste(DatabaseObject dbo) {
        if (dbo.getDbIdentity().equals(Context.getAddress().getDbIdentity())) {
            setDataOwner(dbo);
        } else {
            MPV5View.addMessage(Messages.NOT_POSSIBLE.toString() + Messages.ACTION_PASTE.toString());
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
}
