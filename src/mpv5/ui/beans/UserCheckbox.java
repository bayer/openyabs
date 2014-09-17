/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mpv5.ui.beans;

import java.awt.event.ItemEvent;
import mpv5.db.objects.User;

/**
 *
 * @author klara
 */
public final class UserCheckbox extends javax.swing.JCheckBox {

   private static final long serialVersionUID = 1L;

   private String propertyname;
   private User user;
   private String prefix = "org.openyabs.uiproperty";

   /**
    * Creates new form UserCheckbox
    *
    * @param propertyname
    */
   public UserCheckbox(final String propertyname) {
      this();
      init(propertyname);
   }

   public UserCheckbox() {
      initComponents();
   }

   @Override
   public void setName(String n) {
      super.setName(n);
      if (propertyname == null) {
         init(n);
      }
   }

   private void checkboxItemStateChanged(ItemEvent evt) {
      User.getCurrentUser().setProperty(prefix + "$" + propertyname, String.valueOf(isSelected()));
   }

   /**
    * This method is called from within the constructor to initialize the form.
    * WARNING: Do NOT modify this code. The content of this method is always
    * regenerated by the Form Editor.
    */
   @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    /**
    * @return the propertyname
    */
   public String getPropertyname() {
      return propertyname;
   }

   /**
    * @param propertyname the propertyname to set
    */
   public void setPropertyname(String propertyname) {
      this.propertyname = propertyname;
   }

   /**
    * Load the property value for the current user
    */
   public void load() {
      setSelected((getUser() != null ? getUser() : mpv5.db.objects.User.getCurrentUser()).getProperties().getProperty(
              prefix, propertyname));
   }

   /**
    * @return the user
    */
   public User getUser() {
      return user;
   }

   /**
    * @param user the user to set
    */
   public void setUser(User user) {
      this.user = user;
   }

   private void init(String propertyname) {
      this.propertyname = propertyname;
      addItemListener(new java.awt.event.ItemListener() {
         public void itemStateChanged(java.awt.event.ItemEvent evt) {
            checkboxItemStateChanged(evt);
         }

      });
      load();
   }
}
