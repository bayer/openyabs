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
package mpv5.ui.panels;

import enoa.handler.TemplateHandler;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTextPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import mpv5.db.common.*;
import mpv5.db.objects.Product;
import mpv5.globals.Headers;
import mpv5.globals.Messages;
import mpv5.db.objects.Contact;
import mpv5.db.objects.Favourite;
import mpv5.db.objects.Item;
import mpv5.db.objects.MailMessage;
import mpv5.db.objects.ProductList;
import mpv5.db.objects.ProductlistSubItem;
import mpv5.db.objects.SubItem;
import mpv5.db.objects.Tax;
import mpv5.db.objects.Template;
import mpv5.logging.Log;
import mpv5.ui.dialogs.BigPopup;
import mpv5.ui.dialogs.Popup;
import mpv5.ui.dialogs.subcomponents.ControlPanel_Groups;
import mpv5.ui.dialogs.subcomponents.ProductSelectDialog2;
import mpv5.ui.frames.MPView;
import mpv5.ui.popups.FileTablePopUp;
import mpv5.ui.toolbars.DataPanelTB;
import mpv5.db.objects.User;
import mpv5.db.objects.ValueProperty;
import mpv5.handler.FormatHandler;
import mpv5.ui.beans.MPCBSelectionChangeReceiver;
import mpv5.ui.dialogs.DialogForFile;
import mpv5.ui.dialogs.ScheduleDayEvent;
import mpv5.ui.dialogs.Search2;
import mpv5.ui.dialogs.subcomponents.ItemTextAreaDialog;
import mpv5.ui.dialogs.subcomponents.ProductSelectDialog;
import mpv5.utils.ui.TableViewPersistenceHandler;
import mpv5.utils.arrays.ArrayUtilities;
import mpv5.utils.date.DateConverter;
import mpv5.utils.export.Export;
import mpv5.utils.files.FileDirectoryHandler;
import mpv5.utils.jobs.Job;
import mpv5.utils.models.MPComboBoxModelItem;
import mpv5.utils.models.MPTableModel;
import mpv5.utils.numberformat.FormatNumber;
import mpv5.utils.renderer.ButtonEditor;
import mpv5.utils.renderer.ButtonRenderer;
import mpv5.utils.tables.TableCalculator;
import mpv5.utils.renderer.TableCellRendererForDezimal;
import mpv5.utils.renderer.TableTabAction;
import mpv5.utils.renderer.TextAreaCellEditor;
import mpv5.utils.renderer.TextAreaCellRenderer;
import mpv5.utils.tables.TableFormat;
import mpv5.utils.ui.TextFieldUtils;

/**
 *
 * 
 */
public class ItemPanel extends javax.swing.JPanel implements DataPanel, MPCBSelectionChangeReceiver, ExportablePanel {

    private static final long serialVersionUID = 1L;
    private Item dataOwner;
    private DataPanelTB tb;
    private SearchPanel sp;
    private Integer dataTableContent = null;
    private TableCalculator itemMultiplier;
    private TableCalculator netCalculator;
    private TableCalculator netCalculator2;
    private boolean loading = true;
    private TableViewPersistenceHandler th;
    private java.util.ResourceBundle bundle = mpv5.i18n.LanguageManager.getBundle();

    /** Creates new form ContactPanel
     * @param context
     * @param type
     */
    public ItemPanel(Context context, int type) {
        initComponents();

        inttype_ = type;
        sp = new SearchPanel(context, this);
        sp.setVisible(true);
        tb = new mpv5.ui.toolbars.DataPanelTB(this);
        toolbarpane.add(tb, BorderLayout.CENTER);
        dataOwner = (Item) DatabaseObject.getObject(context);
        if (type >= 0) {
            dataOwner.setInttype(type);
            this.type.setText(Item.getTypeString(type));
        } else {
            this.type.setText("");
        }

        refresh();
        shipping.set_ValueClass(Double.class);

        addedby.setText(mpv5.db.objects.User.getCurrentUser().getName());
        contactname.setSearchEnabled(true);
        contactname.setContext(Context.getCustomer());
        contactname.getComboBox().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final MPComboBoxModelItem item = contactname.getSelectedItem();
                if (item != null && item.isValid()) {
                    Runnable runnable = new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Contact dbo = (Contact) DatabaseObject.getObject(Context.getContact(), Integer.valueOf(item.getId()));
                                contactcity.setText(dbo.__getCity());
                                contactcompany.setText(dbo.__getCompany());
                                contactid.setText(dbo.__getCNumber());
                            } catch (NodataFoundException ex) {
                            }
                        }
                    };
                    SwingUtilities.invokeLater(runnable);
                }
            }
        });

        accountselect.setContext(Context.getAccounts());
        accountselect.setSearchEnabled(true);
        groupnameselect.setContext(Context.getGroup());
        groupnameselect.setSearchEnabled(true);

        date1.setDate(new Date());
        try {
            date3.setDate(DateConverter.addDays(new Date(), Integer.valueOf(mpv5.db.objects.User.getCurrentUser().getProperties().getProperty("bills.warn.days"))));
            date2.setDate(new Date());
        } catch (Exception e) {
            date3.setDate(DateConverter.addDays(new Date(), 14));
            date2.setDate(new Date());
        }
//        itemtable.getTableHeader().addMouseListener(new MouseListener() {
//
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                if (e.getButton() == MouseEvent.BUTTON2) {
//                    MPTableModel m = (MPTableModel) itemtable.getModel();
//                    if (m.getRowCount() > 0) {
//                        m.addRow(5);
//                    } else {
//                        itemtable.setModel(SubItem.toModel(new SubItem[]{
//                                    SubItem.getDefaultItem(), SubItem.getDefaultItem(),
//                                    SubItem.getDefaultItem(), SubItem.getDefaultItem(),
//                                    SubItem.getDefaultItem(), SubItem.getDefaultItem()
//                                }));
//                        formatTable();
//                    }
//                } else if (e.getButton() == MouseEvent.BUTTON3) {
//                    MPTableModel m = (MPTableModel) itemtable.getModel();
//                    Product p = (Product) Popup.SelectValue(Context.getProduct());
//                    if (p != null) {
//                        int row = m.getLastValidRow(new int[]{4});
//                        m.setRowAt(new SubItem(p).getRowData(row), row + 1, 1);
//                    }
//                }
//            }
//
//            @Override
//            public void mousePressed(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseReleased(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseEntered(MouseEvent e) {
//            }
//
//            @Override
//            public void mouseExited(MouseEvent e) {
//            }
//        });

        InputMap inputMap = itemtable.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        KeyStroke tab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0);
        Action oldAction = itemtable.getActionMap().get(inputMap.get(tab));
        itemtable.getActionMap().put(inputMap.get(tab), new TableTabAction(notes, oldAction, false));
        KeyStroke shifttab = KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.SHIFT_MASK);
        oldAction = itemtable.getActionMap().get(inputMap.get(shifttab));
        itemtable.getActionMap().put(inputMap.get(shifttab), new TableTabAction(date3, oldAction, true));

//        KeyStroke right = KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0);
//        oldAction = itemtable.getActionMap().get(inputMap.get(right));
//        itemtable.getActionMap().put(inputMap.get(right), new TableTabAction(notes, oldAction, false));
//        KeyStroke left = KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0);
//        oldAction = itemtable.getActionMap().get(inputMap.get(left));
//        itemtable.getActionMap().put(inputMap.get(left), new TableTabAction(date3, oldAction, true));

        number.setSearchOnEnterEnabled(true);
        number.setParent(this);
        number.setSearchField("cname");
        number.setContext(Context.getItem());

        ((SpinnerNumberModel) calculator.getSpinner().getModel()).setMinimum(-1000);
        ((SpinnerNumberModel) calculator.getSpinner().getModel()).setMaximum(1000);
        final DataPanel p = this;
        status.getComboBox().addActionListener(new ActionListener() {

            Item dato = (Item) getDataOwner();

            public void actionPerformed(ActionEvent e) {
                if (dato.__getInttype() == Item.TYPE_BILL && !loading && dataOwner.isExisting() && Integer.valueOf(status.getSelectedItem().getId()) == Item.STATUS_PAID && mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "autocreaterevenue")) {
                    if (Popup.Y_N_dialog(Messages.BOOK_NOW)) {

                        if (dato.getPanelData(p) && dato.save()) {
                            try {
                                actionAfterSave();
                                dato.createRevenue();
                            } catch (Exception ef) {
                                Log.Debug(this, ef);
                            }
                        } else {
                            showRequiredFields();
                        }
                    }
                }

                if (dato.__getInttype() == Item.TYPE_BILL && !loading && dataOwner.isExisting()
                        && Integer.valueOf(status.getSelectedItem().getId()) == Item.STATUS_PAID) {

                    //set dateend
                    date3.setDate(new Date());
                }
            }
        });

    }

    /**
     * 
     * @param items
     */
    public ItemPanel(Context items) {
        this(items, -1);
    }

    @Override
    public DatabaseObject getDataOwner() {
        return dataOwner;
    }

    @Override
    public void setDataOwner(DatabaseObject object, boolean populate) {
        loading = true;
        th.remove();
        if (object instanceof Item) {
            dataOwner = (Item) object;
            if (populate) {
                dataOwner.setPanelData(this);
                inttype_ = dataOwner.__getInttype();
                button_reminders.setEnabled(inttype_ == Item.TYPE_BILL);
                button_schedule.setEnabled(inttype_ == Item.TYPE_BILL);
                button_elevate.setEnabled(inttype_ != Item.TYPE_BILL);
                type.setText(Item.getTypeString(inttype_));
                //            typelabel.setIcon(dataOwner.getIcon());
                this.exposeData();

                setTitle();

                tb.setFavourite(Favourite.isFavourite(object));
                tb.setEditable(!object.isReadOnly());

                th.remove();
                itemtable.setModel(SubItem.toModel(((Item) object).getSubitems()));
                if (((MPTableModel) itemtable.getModel()).getEmptyRows(new int[]{4}) < 2) {
                    ((MPTableModel) itemtable.getModel()).addRow(1);
                }
                th.set();
                omodel = (MPTableModel) itemtable.getModel();

                formatTable();
                try {
                    ((MPTableModel) itemtable.getModel()).fireTableCellUpdated(0, 0);
                } catch (Exception e) {
                }
                if (object.isReadOnly()) {
                    Popup.notice(Messages.LOCKED_BY);
                }
                button_preview.setEnabled(false);
                preloadTemplates();
                validate();
            }
        } else if (object instanceof SubItem) {
            Item i;
            try {
                i = (Item) DatabaseObject.getObject(Context.getItem(), ((SubItem) object).__getItemsids());
                setDataOwner(i, populate);
            } catch (NodataFoundException ex) {
                Log.Debug(ex);
            }
        }

        properties();
        loading = false;
        th.set();
    }

    private void setTitle() {
        if (this.getParent() instanceof JViewport || this.getParent() instanceof JTabbedPane) {
            JTabbedPane jTabbedPane = null;
            String title1 = cname_;
            //this->viewport->scrollpane->tabbedpane
            if (this.getParent().getParent().getParent() instanceof JTabbedPane) {
                jTabbedPane = (JTabbedPane) this.getParent().getParent().getParent();
            } else {
                try {
                    jTabbedPane = (JTabbedPane) this.getParent();
                } catch (Exception e) {
                    //Free floating window
                    ((JFrame) this.getRootPane().getParent()).setTitle(title1);
                }
            }
            if (jTabbedPane != null) {
                jTabbedPane.setTitleAt(jTabbedPane.getSelectedIndex(), title1);
            }
        }
    }

    @Override
    public void showRequiredFields() {
        TextFieldUtils.blink(contactname.getComboBox(), Color.RED);
        contactname.requestFocus();
        Popup.notice(Messages.SELECT_A_CONTACT);
    }

    private void addFile() {
        if (dataOwner.isExisting()) {
            DialogForFile d = new DialogForFile(DialogForFile.FILES_ONLY);
            if (d.chooseFile()) {
                String s = Popup.Enter_Value(Messages.ENTER_A_DESCRIPTION);
                if (s != null) {
                    QueryHandler.instanceOf().clone(Context.getFiles(), this).insertFile(d.getFile(), dataOwner, QueryCriteria.getSaveStringFor(s));
                }
            }
        }
    }

    private void deleteFile() {
        if (dataOwner.isExisting()) {
            try {
                QueryHandler.instanceOf().clone(Context.getFiles()).removeFile(dataTable.getModel().getValueAt(dataTable.getSelectedRow(), 0).toString());
            } catch (Exception e) {
                Log.Debug(this, e.getMessage());
            }
            fillFiles();
        }
    }

    private void fileTableClicked(MouseEvent evt) {
        if (evt.getClickCount() > 1) {
            FileDirectoryHandler.open(QueryHandler.instanceOf().clone(Context.getFiles()).
                    retrieveFile(dataTable.getModel().getValueAt(dataTable.getSelectedRow(), 0).
                    toString(), new File(FileDirectoryHandler.getTempDir() + dataTable.getModel().
                    getValueAt(dataTable.getSelectedRow(), 1).toString())));
        } else if (evt.getClickCount() == 1 && evt.getButton() == MouseEvent.BUTTON3) {

            JTable source = (JTable) evt.getSource();
            int row = source.rowAtPoint(evt.getPoint());
            int column = source.columnAtPoint(evt.getPoint());

            if (!source.isRowSelected(row)) {
                source.changeSelection(row, column, false, false);
            }

            FileTablePopUp.instanceOf(dataTable).show(source, evt.getX(), evt.getY());
        }
    }

    private void fillFiles() {
        Context c = Context.getFilesToItems();
        c.addReference(Context.getFiles().getDbIdentity(), "cname", "filename");
        Object[][] data = new DatabaseSearch(c).getValuesFor(Context.DETAILS_FILES_TO_ITEMS, "itemsids", dataOwner.__getIDS());

        dataTable.setModel(new MPTableModel(data, Headers.FILE_REFERENCES.getValue()));
        TableFormat.stripFirstColumn(dataTable);
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
        leftpane = new javax.swing.JPanel();
        rightpane = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        number = new mpv5.ui.beans.LabeledTextField();
        addedby = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        button_order2 = new javax.swing.JButton();
        status = new mpv5.ui.beans.LabeledCombobox();
        accountselect = new mpv5.ui.beans.LabeledCombobox();
        groupnameselect = new mpv5.ui.beans.MPCombobox();
        staus_icon = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        itemtable = new javax.swing.JTable();
        itemPanel = new javax.swing.JPanel();
        addItem = new javax.swing.JButton();
        delItem = new javax.swing.JButton();
        upItem = new javax.swing.JButton();
        upItem1 = new javax.swing.JButton();
        jToolBar1 = new javax.swing.JToolBar();
        type = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        button_elevate = new javax.swing.JButton();
        typelabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        button_reminders = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        button_schedule = new javax.swing.JButton();
        button_preview = new javax.swing.JButton();
        button_deliverynote = new javax.swing.JButton();
        button_orderconf = new javax.swing.JButton();
        checkb_pront_oc = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        contactname = new mpv5.ui.beans.LabeledCombobox();
        contactcity = new javax.swing.JTextField();
        contactcompany = new javax.swing.JTextField();
        contactid = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        date1 = new mpv5.ui.beans.LabeledDateChooser();
        date2 = new mpv5.ui.beans.LabeledDateChooser();
        date3 = new mpv5.ui.beans.LabeledDateChooser();
        addfile = new javax.swing.JButton();
        removefile = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        shipping = new mpv5.ui.beans.LabeledTextField();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        calculator = new mpv5.ui.beans.LabeledSpinner();
        jButton1 = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        netvalue = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        taxvalue = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        value = new javax.swing.JLabel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        class NoTabTextArea extends JTextPane {
            protected void processComponentKeyEvent( KeyEvent e ) {
                if ( e.getID() == KeyEvent.KEY_PRESSED &&
                    e.getKeyCode() == KeyEvent.VK_TAB ) {
                    e.consume();
                    if (e.isShiftDown()) {
                        transferFocusBackward();
                    } else {
                        itemtable.requestFocusInWindow();
                    }
                } else {
                    super.processComponentKeyEvent( e );
                }
            }
        }
        notes = new NoTabTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        proptable = new  mpv5.ui.misc.MPTable(this) {
            public Component prepareRenderer(TableCellRenderer renderer,
                int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent)c;
                    jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));
                }
                return c;
            }
        };
        jScrollPane2 = new javax.swing.JScrollPane();
        dataTable = new  mpv5.ui.misc.MPTable(this) {
            public Component prepareRenderer(TableCellRenderer renderer,
                int rowIndex, int vColIndex) {
                Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent)c;
                    jc.setToolTipText(String.valueOf(getValueAt(rowIndex, vColIndex)));
                }
                return c;
            }
        }
        ;
        toolbarpane = new javax.swing.JPanel();

        
        setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ItemPanel.border.title_1"))); // NOI18N
        setName("ItemPanel"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        leftpane.setName("leftpane"); // NOI18N
        leftpane.setLayout(new java.awt.BorderLayout());
        add(leftpane, java.awt.BorderLayout.WEST);

        rightpane.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ItemPanel.rightpane.border.title"))); // NOI18N
        rightpane.setName("rightpane"); // NOI18N

        jPanel1.setBackground(java.awt.SystemColor.activeCaption);
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setName("jPanel1"); // NOI18N

        number.set_Label(bundle.getString("ItemPanel.number._Label")); // NOI18N
        number.setFocusable(false);
        number.setFont(number.getFont());
        number.setName("number"); // NOI18N

        addedby.setFont(addedby.getFont());
        addedby.setText(bundle.getString("ItemPanel.addedby.text")); // NOI18N
        addedby.setToolTipText(bundle.getString("ItemPanel.addedby.toolTipText")); // NOI18N
        addedby.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        addedby.setEnabled(false);
        addedby.setName("addedby"); // NOI18N

        jLabel4.setFont(jLabel4.getFont());
        jLabel4.setText(bundle.getString("ItemPanel.jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        button_order2.setFont(button_order2.getFont().deriveFont(button_order2.getFont().getStyle() & ~java.awt.Font.BOLD, button_order2.getFont().getSize()-2));
        button_order2.setText(bundle.getString("ItemPanel.button_order2.text")); // NOI18N
        button_order2.setFocusable(false);
        button_order2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_order2.setName("button_order2"); // NOI18N
        button_order2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_order2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_order2ActionPerformed(evt);
            }
        });

        status.set_Label(bundle.getString("ItemPanel.status._Label")); // NOI18N
        status.setName("status"); // NOI18N

        accountselect.set_Label(bundle.getString("ItemPanel.accountselect._Label")); // NOI18N
        accountselect.setName("accountselect"); // NOI18N
        accountselect.setSearchOnEnterEnabled(false);

        groupnameselect.setName("groupnameselect"); // NOI18N

        staus_icon.setText(bundle.getString("ItemPanel.staus_icon.text")); // NOI18N
        staus_icon.setName("staus_icon"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(number, javax.swing.GroupLayout.PREFERRED_SIZE, 355, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(accountselect, javax.swing.GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(staus_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 197, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 148, Short.MAX_VALUE)
                        .addComponent(button_order2))
                    .addComponent(groupnameselect, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addedby, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(addedby, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button_order2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(staus_icon, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(accountselect, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(groupnameselect, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(number, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel4.setBackground(new java.awt.Color(204, 204, 204));
        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        itemtable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        itemtable.setName("itemtable"); // NOI18N
        itemtable.setRowHeight(18);
        itemtable.setSelectionBackground(new java.awt.Color(161, 176, 190));
        itemtable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        itemtable.setSurrendersFocusOnKeystroke(true);
        itemtable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itemtableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(itemtable);
        itemtable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);

        itemPanel.setName("itemPanel"); // NOI18N
        itemPanel.setOpaque(false);

        addItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/add.png"))); // NOI18N
        addItem.setName("addItem"); // NOI18N
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        delItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/remove.png"))); // NOI18N
        delItem.setName("delItem"); // NOI18N
        delItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delItemActionPerformed(evt);
            }
        });

        upItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/arrow-up.png"))); // NOI18N
        upItem.setName("upItem"); // NOI18N
        upItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upItemActionPerformed(evt);
            }
        });

        upItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/arrow-down.png"))); // NOI18N
        upItem1.setName("upItem1"); // NOI18N
        upItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upItem1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout itemPanelLayout = new javax.swing.GroupLayout(itemPanel);
        itemPanel.setLayout(itemPanelLayout);
        itemPanelLayout.setHorizontalGroup(
            itemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(delItem, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(upItem, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(upItem1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        itemPanelLayout.setVerticalGroup(
            itemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemPanelLayout.createSequentialGroup()
                .addComponent(addItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upItem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(upItem1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(itemPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 809, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(itemPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
        );

        jToolBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        type.setBackground(new java.awt.Color(255, 255, 255));
        type.setFont(type.getFont().deriveFont(type.getFont().getStyle() | java.awt.Font.BOLD, type.getFont().getSize()+2));
        type.setForeground(new java.awt.Color(0, 51, 51));
        type.setText(bundle.getString("ItemPanel.type.text")); // NOI18N
        type.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createEmptyBorder(1, 3, 1, 5)));
        type.setMaximumSize(new java.awt.Dimension(100, 23));
        type.setName("type"); // NOI18N
        jToolBar1.add(type);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jToolBar1.add(jSeparator4);

        button_elevate.setText(bundle.getString("ItemPanel.button_elevate.text")); // NOI18N
        button_elevate.setEnabled(false);
        button_elevate.setFocusable(false);
        button_elevate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_elevate.setName("button_elevate"); // NOI18N
        button_elevate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_elevate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_elevateActionPerformed(evt);
            }
        });
        jToolBar1.add(button_elevate);

        typelabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/22/editcopy.png"))); // NOI18N
        typelabel.setText(bundle.getString("ItemPanel.typelabel.text")); // NOI18N
        typelabel.setName("typelabel"); // NOI18N
        jToolBar1.add(typelabel);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jToolBar1.add(jSeparator1);

        button_reminders.setText(bundle.getString("ItemPanel.button_reminders.text")); // NOI18N
        button_reminders.setEnabled(false);
        button_reminders.setFocusable(false);
        button_reminders.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_reminders.setName("button_reminders"); // NOI18N
        button_reminders.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_reminders.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_remindersActionPerformed(evt);
            }
        });
        jToolBar1.add(button_reminders);

        jButton2.setText(bundle.getString("ItemPanel.jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jSeparator7.setName("jSeparator7"); // NOI18N
        jToolBar1.add(jSeparator7);

        button_schedule.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/kalarm.png"))); // NOI18N
        button_schedule.setText(bundle.getString("ItemPanel.button_schedule.text")); // NOI18N
        button_schedule.setEnabled(false);
        button_schedule.setName("button_schedule"); // NOI18N
        button_schedule.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                button_scheduleMouseClicked(evt);
            }
        });
        button_schedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_scheduleActionPerformed(evt);
            }
        });
        jToolBar1.add(button_schedule);

        button_preview.setText(bundle.getString("ItemPanel.button_preview.text")); // NOI18N
        button_preview.setEnabled(false);
        button_preview.setFocusable(false);
        button_preview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_preview.setName("button_preview"); // NOI18N
        button_preview.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_preview.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_previewActionPerformed(evt);
            }
        });
        jToolBar1.add(button_preview);

        button_deliverynote.setText(bundle.getString("ItemPanel.button_deliverynote.text")); // NOI18N
        button_deliverynote.setEnabled(false);
        button_deliverynote.setFocusable(false);
        button_deliverynote.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_deliverynote.setName("button_deliverynote"); // NOI18N
        button_deliverynote.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_deliverynote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_deliverynoteActionPerformed(evt);
            }
        });
        jToolBar1.add(button_deliverynote);

        button_orderconf.setText(bundle.getString("ItemPanel.button_orderconf.text")); // NOI18N
        button_orderconf.setEnabled(false);
        button_orderconf.setFocusable(false);
        button_orderconf.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        button_orderconf.setName("button_orderconf"); // NOI18N
        button_orderconf.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        button_orderconf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_orderconfActionPerformed(evt);
            }
        });
        jToolBar1.add(button_orderconf);

        checkb_pront_oc.setText(bundle.getString("ItemPanel.checkb_pront_oc.text")); // NOI18N
        checkb_pront_oc.setEnabled(false);
        checkb_pront_oc.setFocusable(false);
        checkb_pront_oc.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        checkb_pront_oc.setName("checkb_pront_oc"); // NOI18N
        checkb_pront_oc.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(checkb_pront_oc);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ItemPanel.jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        contactname.set_Label(bundle.getString("ItemPanel.contactname._Label")); // NOI18N
        contactname.setName("contactname"); // NOI18N

        contactcity.setEditable(false);
        contactcity.setText(bundle.getString("ItemPanel.contactcity.text")); // NOI18N
        contactcity.setName("contactcity"); // NOI18N

        contactcompany.setEditable(false);
        contactcompany.setText(bundle.getString("ItemPanel.contactcompany.text")); // NOI18N
        contactcompany.setName("contactcompany"); // NOI18N

        contactid.setEditable(false);
        contactid.setText(bundle.getString("ItemPanel.contactid.text")); // NOI18N
        contactid.setName("contactid"); // NOI18N

        jButton3.setText(bundle.getString("ItemPanel.jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText(bundle.getString("ItemPanel.jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(contactname, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactcity, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contactcompany, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(contactid, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(contactname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactcity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactcompany, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contactid, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)))
        );

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(bundle.getString("ItemPanel.jPanel5.border.title"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        date1.set_Label(bundle.getString("ItemPanel.date1._Label")); // NOI18N
        date1.setName("date1"); // NOI18N

        date2.set_Label(bundle.getString("ItemPanel.date2._Label")); // NOI18N
        date2.setName("date2"); // NOI18N

        date3.set_Label(bundle.getString("ItemPanel.date3._Label")); // NOI18N
        date3.setName("date3"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(date1, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(date2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(date3, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(170, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                .addComponent(date1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(date2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(date3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        addfile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/add.png"))); // NOI18N
        addfile.setText(bundle.getString("ItemPanel.addfile.text")); // NOI18N
        addfile.setToolTipText(bundle.getString("ItemPanel.addfile.toolTipText")); // NOI18N
        addfile.setName("addfile"); // NOI18N
        addfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addfileActionPerformed(evt);
            }
        });

        removefile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mpv5/resources/images/16/remove.png"))); // NOI18N
        removefile.setText(bundle.getString("ItemPanel.removefile.text")); // NOI18N
        removefile.setToolTipText(bundle.getString("ItemPanel.removefile.toolTipText")); // NOI18N
        removefile.setName("removefile"); // NOI18N
        removefile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removefileActionPerformed(evt);
            }
        });

        jToolBar2.setBackground(new java.awt.Color(255, 255, 255));
        jToolBar2.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 102, 102), 1, true));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.setName("jToolBar2"); // NOI18N

        shipping.set_Label(bundle.getString("ItemPanel.shipping._Label")); // NOI18N
        shipping.setMaximumSize(new java.awt.Dimension(200, 20));
        shipping.setName("shipping"); // NOI18N
        jToolBar2.add(shipping);

        jSeparator11.setName("jSeparator11"); // NOI18N
        jSeparator11.setSeparatorSize(new java.awt.Dimension(15, 10));
        jToolBar2.add(jSeparator11);

        calculator.set_Label(bundle.getString("ItemPanel.calculator._Label")); // NOI18N
        calculator.setMaximumSize(new java.awt.Dimension(200, 20));
        calculator.setName("calculator"); // NOI18N
        jToolBar2.add(calculator);

        jButton1.setText(bundle.getString("ItemPanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton1);

        jSeparator12.setName("jSeparator12"); // NOI18N
        jSeparator12.setSeparatorSize(new java.awt.Dimension(15, 10));
        jToolBar2.add(jSeparator12);

        jLabel1.setText(bundle.getString("ItemPanel.jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jToolBar2.add(jLabel1);

        jSeparator3.setName("jSeparator3"); // NOI18N
        jSeparator3.setSeparatorSize(new java.awt.Dimension(15, 10));
        jToolBar2.add(jSeparator3);

        netvalue.setFont(new java.awt.Font("Tahoma", 1, 11));
        netvalue.setText(bundle.getString("ItemPanel.netvalue.text")); // NOI18N
        netvalue.setName("netvalue"); // NOI18N
        jToolBar2.add(netvalue);

        jSeparator9.setName("jSeparator9"); // NOI18N
        jToolBar2.add(jSeparator9);

        jLabel2.setText(bundle.getString("ItemPanel.jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jToolBar2.add(jLabel2);

        jSeparator6.setName("jSeparator6"); // NOI18N
        jSeparator6.setSeparatorSize(new java.awt.Dimension(15, 10));
        jToolBar2.add(jSeparator6);

        taxvalue.setFont(new java.awt.Font("Tahoma", 1, 11));
        taxvalue.setText(bundle.getString("ItemPanel.taxvalue.text")); // NOI18N
        taxvalue.setName("taxvalue"); // NOI18N
        jToolBar2.add(taxvalue);

        jSeparator10.setName("jSeparator10"); // NOI18N
        jToolBar2.add(jSeparator10);

        jLabel3.setText(bundle.getString("ItemPanel.jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N
        jToolBar2.add(jLabel3);

        jSeparator5.setName("jSeparator5"); // NOI18N
        jSeparator5.setSeparatorSize(new java.awt.Dimension(15, 10));
        jToolBar2.add(jSeparator5);

        value.setFont(new java.awt.Font("Tahoma", 1, 11));
        value.setText(bundle.getString("ItemPanel.value.text")); // NOI18N
        value.setName("value"); // NOI18N
        jToolBar2.add(value);

        jSplitPane1.setDividerSize(10);
        jSplitPane1.setLastDividerLocation(150);
        jSplitPane1.setName("jSplitPane1"); // NOI18N
        jSplitPane1.setOneTouchExpandable(true);

        jTabbedPane1.setMinimumSize(new java.awt.Dimension(400, 61));
        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jScrollPane1.setBorder(null);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        notes.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        notes.setText(bundle.getString("ItemPanel.notes.text")); // NOI18N
        notes.setToolTipText(bundle.getString("ItemPanel.notes.toolTipText")); // NOI18N
        notes.setName("notes"); // NOI18N
        jScrollPane1.setViewportView(notes);

        jTabbedPane1.addTab(bundle.getString("ItemPanel.jScrollPane1.TabConstraints.tabTitle"), jScrollPane1); // NOI18N

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        proptable.setAutoCreateRowSorter(true);
        proptable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "", ""
            }
        ));
        proptable.setName("proptable"); // NOI18N
        proptable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(proptable);

        jTabbedPane1.addTab(bundle.getString("ItemPanel.jScrollPane4.TabConstraints.tabTitle"), jScrollPane4); // NOI18N

        jSplitPane1.setLeftComponent(jTabbedPane1);

        jScrollPane2.setToolTipText(bundle.getString("ItemPanel.jScrollPane2.toolTipText")); // NOI18N
        jScrollPane2.setName("jScrollPane2"); // NOI18N

        dataTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        dataTable.setToolTipText(bundle.getString("ItemPanel.dataTable.toolTipText")); // NOI18N
        dataTable.setName("dataTable"); // NOI18N
        dataTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(dataTable);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout rightpaneLayout = new javax.swing.GroupLayout(rightpane);
        rightpane.setLayout(rightpaneLayout);
        rightpaneLayout.setHorizontalGroup(
            rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightpaneLayout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 812, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addfile, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(removefile, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
            .addComponent(jToolBar2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 843, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        rightpaneLayout.setVerticalGroup(
            rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightpaneLayout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(rightpaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rightpaneLayout.createSequentialGroup()
                        .addComponent(addfile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removefile)
                        .addContainerGap())
                    .addComponent(jSplitPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, Short.MAX_VALUE)))
        );

        add(rightpane, java.awt.BorderLayout.CENTER);

        toolbarpane.setName("toolbarpane"); // NOI18N
        toolbarpane.setLayout(new java.awt.BorderLayout());
        add(toolbarpane, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        if (dataOwner.isExisting()) {
            try {
                MPView.getIdentifierView().addTab(DatabaseObject.getObject(Context.getContact(), dataOwner.__getContactsids()));
            } catch (NodataFoundException ex) {
                Log.Debug(ex);
            }
        }
}//GEN-LAST:event_jButton2ActionPerformed

    private void button_order2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_order2ActionPerformed
        BigPopup.showPopup(this, new ControlPanel_Groups(), null);
}//GEN-LAST:event_button_order2ActionPerformed

    private void removefileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removefileActionPerformed
        deleteFile();
}//GEN-LAST:event_removefileActionPerformed

    private void addfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addfileActionPerformed
        if (dataOwner.isExisting()) {
            addFile();
        }
}//GEN-LAST:event_addfileActionPerformed

    private void dataTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataTableMouseClicked
        fileTableClicked(evt);
    }//GEN-LAST:event_dataTableMouseClicked

    private void itemtableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemtableMouseClicked
        if (evt != null) {
            MPTableModel m = (MPTableModel) itemtable.getModel();
            if (evt.getButton() != MouseEvent.BUTTON1) {
                SubItem it = m.getRowAt(itemtable.getSelectedRow(), SubItem.getDefaultItem());

                if (it != null) {
                    MPView.identifierView.getClistview().addElement(it);
                } else if (!m.hasEmptyRows(new int[]{4})) {
                    m.addRow(2);
                }
            }
        }
    }//GEN-LAST:event_itemtableMouseClicked

    private void button_previewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_previewActionPerformed
        preview();
    }//GEN-LAST:event_button_previewActionPerformed

    private void button_scheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_scheduleActionPerformed
    }//GEN-LAST:event_button_scheduleActionPerformed

    private void button_scheduleMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_scheduleMouseClicked

//        JCalendar.instanceOf(300, evt.getLocationOnScreen());
        if (dataOwner != null && dataOwner.isExisting()) {
            ScheduleDayEvent.instanceOf().setItem(dataOwner);
        }
    }//GEN-LAST:event_button_scheduleMouseClicked

    private void button_remindersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_remindersActionPerformed
        if (dataOwner != null && dataOwner.isExisting()) {
            BigPopup.showPopup(MPView.getIdentifierFrame().getRootPane(), new RemindPanel(dataOwner), Messages.REMINDER.toString(), true);
        }
    }//GEN-LAST:event_button_remindersActionPerformed

    private void button_elevateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_elevateActionPerformed

//        if (Popup.OK_dialog(Messages.REALLY_CHANGE2, Messages.ARE_YOU_SURE.getValue())) {
        dataOwner.setIntstatus(Item.STATUS_FINISHED);
        dataOwner.save();

        if (dataOwner.__getInttype() == Item.TYPE_OFFER) {
            Item i2 = (Item) dataOwner.clone();
            i2.setInttype(Item.TYPE_ORDER);
            i2.setIDS(-1);
            i2.defineFormatHandler(new FormatHandler(i2));
            i2.save();
            if (itemtable.getCellEditor() != null) {
                try {
                    itemtable.getCellEditor().stopCellEditing();
                } catch (Exception e) {
                }
            }
            SubItem.saveModel(i2, (MPTableModel) itemtable.getModel(), true, true);
            setDataOwner(i2, true);
            Popup.notice(i2 + Messages.INSERTED.getValue());
        } else if (dataOwner.__getInttype() == Item.TYPE_ORDER) {
            Item i2 = (Item) dataOwner.clone();
            i2.setInttype(Item.TYPE_BILL);
            i2.setIDS(-1);
            i2.defineFormatHandler(new FormatHandler(i2));
            i2.save();
            if (itemtable.getCellEditor() != null) {
                try {
                    itemtable.getCellEditor().stopCellEditing();
                } catch (Exception e) {
                }
            }
            SubItem.saveModel(i2, (MPTableModel) itemtable.getModel(), true, true);
            setDataOwner(i2, true);
            Popup.notice(i2 + Messages.INSERTED.getValue());
        }


////        }

    }//GEN-LAST:event_button_elevateActionPerformed
    MPTableModel omodel = null;
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (omodel == null) {
            omodel = (MPTableModel) itemtable.getModel();
        }
        th.remove();
        if (omodel.getValidRows(new int[]{4}).size() > 0) {

            itemtable.setModel(omodel);
            SubItem.changeValueFields(itemtable, Integer.valueOf(calculator.get_Value().toString()), this);
            ((MPTableModel) itemtable.getModel()).fireTableCellUpdated(0, 0);
            ((MPTableModel) itemtable.getModel()).addRow(1);
        }
        th.set();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void button_deliverynoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_deliverynoteActionPerformed

        delivery();
    }//GEN-LAST:event_button_deliverynoteActionPerformed

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        ((MPTableModel) itemtable.getModel()).addRow(1);
    }//GEN-LAST:event_addItemActionPerformed

    private void delItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delItemActionPerformed
        int index = itemtable.getSelectedRow();
        if (index < 0) {
            return;
        }

        MPTableModel m = (MPTableModel) itemtable.getModel();
        SubItem.addToDeletionQueue(m.getValueAt(index, 0));
        ((MPTableModel) itemtable.getModel()).removeRow(index);
    }//GEN-LAST:event_delItemActionPerformed

    private void upItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upItemActionPerformed
        int index = itemtable.getSelectedRow();
        if (index <= 0) {
            return;
        }
        ((MPTableModel) itemtable.getModel()).moveRow(index, index, index - 1);
        itemtable.changeSelection(index - 1, itemtable.getSelectedColumn(), false, false);
    }//GEN-LAST:event_upItemActionPerformed

    private void upItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_upItem1ActionPerformed
        int index = itemtable.getSelectedRow();
        if (index < 0 || index >= itemtable.getRowCount() - 1) {
            return;
        }
        ((MPTableModel) itemtable.getModel()).moveRow(index, index, index + 1);
        itemtable.changeSelection(index + 1, itemtable.getSelectedColumn(), false, false);
    }//GEN-LAST:event_upItem1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed


        try {
            int cid = Integer.valueOf(contactname.getSelectedItem().getId());
            Contact c = (Contact) DatabaseObject.getObject(Context.getContact(), cid);
            MPView.getIdentifierView().addTab(c);
        } catch (Exception e) {
            //Nothing to show
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void button_orderconfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_orderconfActionPerformed

        confirmation();
    }//GEN-LAST:event_button_orderconfActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        Contact dbo = (Contact) Search2.showSearchFor(Context.getContact());
        if (dbo != null) {
            contactname.setModel(dbo);
            contactcity.setText(dbo.__getCity());
            contactcompany.setText(dbo.__getCompany());
            contactid.setText(dbo.__getCNumber());
        }
    }//GEN-LAST:event_jButton4ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private mpv5.ui.beans.LabeledCombobox accountselect;
    private javax.swing.JButton addItem;
    private javax.swing.JLabel addedby;
    private javax.swing.JButton addfile;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton button_deliverynote;
    private javax.swing.JButton button_elevate;
    private javax.swing.JButton button_order2;
    private javax.swing.JButton button_orderconf;
    private javax.swing.JButton button_preview;
    private javax.swing.JButton button_reminders;
    private javax.swing.JButton button_schedule;
    private mpv5.ui.beans.LabeledSpinner calculator;
    private javax.swing.JCheckBox checkb_pront_oc;
    private javax.swing.JTextField contactcity;
    private javax.swing.JTextField contactcompany;
    private javax.swing.JTextField contactid;
    private mpv5.ui.beans.LabeledCombobox contactname;
    private javax.swing.JTable dataTable;
    private mpv5.ui.beans.LabeledDateChooser date1;
    private mpv5.ui.beans.LabeledDateChooser date2;
    private mpv5.ui.beans.LabeledDateChooser date3;
    private javax.swing.JButton delItem;
    private mpv5.ui.beans.MPCombobox groupnameselect;
    private javax.swing.JPanel itemPanel;
    private javax.swing.JTable itemtable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JPanel leftpane;
    private javax.swing.JLabel netvalue;
    private javax.swing.JTextPane notes;
    private mpv5.ui.beans.LabeledTextField number;
    private javax.swing.JTable proptable;
    private javax.swing.JButton removefile;
    private javax.swing.JPanel rightpane;
    private mpv5.ui.beans.LabeledTextField shipping;
    private mpv5.ui.beans.LabeledCombobox status;
    private javax.swing.JLabel staus_icon;
    private javax.swing.JLabel taxvalue;
    private javax.swing.JPanel toolbarpane;
    private javax.swing.JLabel type;
    private javax.swing.JLabel typelabel;
    private javax.swing.JButton upItem;
    private javax.swing.JButton upItem1;
    private javax.swing.JLabel value;
    // End of variables declaration//GEN-END:variables
    public String cname_;
    public String cnumber_;
    public String description_;
    public int intaddedby_;
    public int ids_;
    public Date dateadded_;
    public int groupsids_ = 1;
    public int contactsids_;
    public int accountsids_;
    public BigDecimal netvalue_;
    public BigDecimal taxvalue_;
    public BigDecimal shippingvalue_;
    public Date datetodo_;
    public Date dateend_;
    public int intreminders_;
    public int intstatus_;
    public int inttype_;

    @Override
    public boolean collectData() {
        try {
            contactsids_ = Integer.valueOf(contactname.getSelectedItem().getId());
        } catch (Exception numberFormatException) {
            return false;
        }
        if (contactsids_ > 0) {
            try {
                accountsids_ = Integer.valueOf(accountselect.getSelectedItem().getId());
            } catch (Exception e) {
                accountsids_ = 1;
            }

            if (groupnameselect.getSelectedItem() != null) {
                groupsids_ = Integer.valueOf(groupnameselect.getSelectedItem().getId());
                Log.Debug(this, groupnameselect.getSelectedItem().getId());
            } else {
                groupsids_ = 1;
            }

            if (dateadded_ == null) {
                dateadded_ = new Date();
            }
            intaddedby_ = User.getUserId(addedby.getText());
            description_ = notes.getText();
            dateadded_ = date1.getDate();

            if (cnumber_ == null) {
                cname_ = "<not set>";
            } else {
                cname_ = cnumber_;
            }

            netvalue_ = FormatNumber.parseDezimal(netvalue.getText());
            taxvalue_ = FormatNumber.parseDezimal(taxvalue.getText());

            try {
                shippingvalue_ = FormatNumber.parseDezimal(shipping.getText());
            } catch (Exception e) {
                shippingvalue_ = new BigDecimal("0");
            }

            datetodo_ = date2.getDate();
            dateend_ = date3.getDate();
            intstatus_ = Integer.valueOf(status.getSelectedItem().getId());

        } else {
            showRequiredFields();
            return false;
        }

        return true;
    }

    @Override
    public void exposeData() {

        number.setText(cname_);
        date1.setDate(dateadded_);
        date2.setDate(datetodo_);
        date3.setDate(dateend_);
        notes.setText(description_);

        shipping.setText(FormatNumber.formatDezimal(shippingvalue_));
        button_reminders.setToolTipText(Messages.REMINDERS + String.valueOf(intreminders_));
        //  discountpercent.setValue(discountvalue_);

        status.setSelectedItem(intstatus_);
        staus_icon.setIcon(dataOwner.getIcon());
        try {
            accountselect.setModel(DatabaseObject.getObject(Context.getAccounts(), accountsids_));
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
        }
        try {
            groupnameselect.setModel(DatabaseObject.getObject(Context.getGroup(), groupsids_));
        } catch (NodataFoundException ex) {
            Log.Debug(this, ex.getMessage());
        }

        addedby.setText(User.getUsername(intaddedby_));
        try {
            Contact owner = (Contact) DatabaseObject.getObject(Context.getContact(), contactsids_);
            contactname.setModel(owner);
            contactcity.setText(owner.__getCity());
            contactcompany.setText(owner.__getCompany());
            contactid.setText(String.valueOf(owner.__getCNumber()));
            contactsids_ = owner.__getIDS();
        } catch (NodataFoundException ex) {
            Log.Debug(ex);
        }

        fillFiles();
    }

    @Override
    public void refresh() {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                try {
                    if (th != null) {
                        th.remove();
                    }
                    groupnameselect.setModel(MPComboBoxModelItem.toModel(DatabaseObject.getObject(Context.getGroup(), mpv5.db.objects.User.getCurrentUser().__getGroupsids())));
                    groupnameselect.setSelectedIndex(0);
                    sp.refresh();

                    try {
                        accountselect.setModel(DatabaseObject.getObject(Context.getAccounts(), mpv5.db.objects.User.getCurrentUser().__getIntdefaultaccount()));
                    } catch (NodataFoundException nodataFoundException) {
                        Log.Debug(this, nodataFoundException.getMessage());
                    }
                    fillFiles();

                    List<Integer> skip = new Vector<Integer>();
                    if (inttype_ == Item.TYPE_BILL) {
                        skip.add(new Integer(0));
//                        skip.add(new Integer(1));
                        skip.add(new Integer(2));
//                        skip.add(new Integer(5));
                    } else {
                        skip.add(new Integer(3));
                        skip.add(new Integer(4));
                    }
                    status.setModel(Item.getStatusStrings(), MPComboBoxModelItem.COMPARE_BY_ID, skip);
                    try {
                        status.setSelectedIndex(mpv5.db.objects.User.getCurrentUser().__getIntdefaultstatus());
                    } catch (Exception e) {
                    }
                    itemtable.setModel(SubItem.toModel(new SubItem[]{
                                SubItem.getDefaultItem(), SubItem.getDefaultItem(),
                                SubItem.getDefaultItem(), SubItem.getDefaultItem(),
                                SubItem.getDefaultItem(), SubItem.getDefaultItem()
                            }));
                    formatTable();
                    shipping.setText(FormatNumber.formatDezimal(0d));
                    if (th != null) {
                        th.set();
                    }
                } catch (Exception e) {
                    Log.Debug(this, e);
                }
            }
        };

        SwingUtilities.invokeLater(runnable);

    }

    /**
     * 
     */
    public void formatTable() {

        prepareTable();

        //"Internal ID", Position, "Count", "Measure", "Text", "Netto Price", "Tax Rate", "Total Price", "Tax value", "Net 2", "Product ID", "", "", "Link", "Optional"
        TableFormat.resizeCols(itemtable, new Integer[]{0, 23, 53, 63, 100, 63, 63, 63, 0, 0, 63, 20, 0, 0, 100},
                new Boolean[]{true, true, true, true, false, true, true, true, true, true, true, true, true, true, false});
        MPTableModel model = (MPTableModel) itemtable.getModel();
        model.setCanEdits(new boolean[]{false, false, true, true, true, true, true, true, false, false, true, true, false, false, true});
        TableFormat.changeBackground(itemtable, 1, Color.LIGHT_GRAY);
        if (mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "hidecolumnquantity")) {
            TableFormat.stripColumn(itemtable, 2);
            model.setCellEditable(0, 2, false);
        }
        if (mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "hidecolumnmeasure")) {
            TableFormat.stripColumn(itemtable, 3);
            model.setCellEditable(0, 3, false);
        }


        if (mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "hideproductscolumn")) {
            TableFormat.stripColumn(itemtable, 10);
            model.setCellEditable(0, 10, false);
        }

        if (mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "hidetaxcolumn")) {
            TableFormat.stripColumn(itemtable, 6);
            model.setCellEditable(0, 6, false);
        }

        TextAreaCellEditor r = new TextAreaCellEditor(itemtable);
        ProductSelectDialog2 productSelectDialog = new ProductSelectDialog2(MPView.getIdentifierFrame(), true, itemtable);
        productSelectDialog.okButton.addActionListener(r);
        productSelectDialog.cancelButton.addActionListener(r);
        r.setDialog(productSelectDialog, productSelectDialog.getIDTextField());
        r.setEditorTo(10);
        itemtable.moveColumn(10, 3);

        if (!mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "showoptionalcolumn")) {
            TableFormat.stripColumn(itemtable, 14);
            model.setCellEditable(0, 14, false);
        } else {
            itemtable.moveColumn(14, 5);
        }
        th = new TableViewPersistenceHandler(itemtable, this);
        TableViewPersistenceHandler tableViewPersistenceHandler2 = new TableViewPersistenceHandler(proptable, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void paste(DatabaseObject... dbos) {
        th.remove();
        if (itemtable.getCellEditor() != null) {
            try {
                itemtable.getCellEditor().stopCellEditing();
            } catch (Exception e) {
            }
        }

        ((MPTableModel) itemtable.getModel()).removeEmptyRows(new int[]{4});

        BigDecimal tpvs = null;
        for (DatabaseObject dbo : dbos) {
            if (dbo.getContext().equals(Context.getItem())
                    || dbo.getContext().equals(Context.getBill())
                    || dbo.getContext().equals(Context.getOffer())
                    || dbo.getContext().equals(Context.getOrder())) {
                Item o = (Item) dbo.clone();

                if (mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "pasten")) {
                    SubItem s = new SubItem();
                    s.setQuantityvalue(new BigDecimal("1"));
//                    s.setItemsids(o.__getIDS());
                    s.setInternalvalue(((Item) dbo).__getNetvalue());
                    s.setExternalvalue(((Item) dbo).__getNetvalue());
                    s.setTotalnetvalue(((Item) dbo).__getNetvalue());
                    s.setTotalbrutvalue(((Item) dbo).__getNetvalue().add(((Item) dbo).__getTaxvalue()));
                    if (s.__getTotalnetvalue().doubleValue() > 0d) {
                        BigDecimal tp = s.__getTotalbrutvalue().subtract(s.__getTotalnetvalue()).multiply(new BigDecimal("100")).divide(s.__getTotalnetvalue(), 2, RoundingMode.HALF_UP);
                        if (tpvs == null) {
                            tpvs = tp;
                        }
                        if (tpvs.equals(tpvs)) {
                            s.setTaxpercentvalue(tp);
                        } else {
                            Popup.warn(Messages.TAXES_NOT_EQUAL);
                            break;
                        }
                    }
                    s.setCName(((Item) dbo).__getCName());
                    s.setDescription(Messages.GOOSE1 + " " + ((Item) dbo).__getCnumber() + " " + Messages.GOOSE2 + " " + DateConverter.getDefDateString(o.__getDateadded()));
//                   if (mpv5.db.objects.User.getCurrentUser().getProperties().hasProperty("deftax")) {
//                        int taxid = mpv5.db.objects.User.getCurrentUser().getProperties().getProperty("deftax", new Integer(0));
//                        BigDecimal deftax = Tax.getTaxValue(taxid);
//                        s.setTaxpercentvalue(deftax);
//                    }

//                    Log.PrintArray(s.toStringArray());

                    ((MPTableModel) itemtable.getModel()).addRow(s.getRowData(((MPTableModel) itemtable.getModel()).getLastValidRow(new int[]{4}) + 1));
                } else {
                    o.setIntstatus(Item.STATUS_IN_PROGRESS);
                    o.setInttype(inttype_);
                    o.setCnumber("");
                    o.setCName("");
                    o.setDateadded(new Date());
                    o.setDatetodo(new Date());
                    o.setDateend(new Date());

                    SubItem[] subs = new SubItem[0];
                    subs = o.getSubitems();
                    o.setIDS(-1);
                    setDataOwner(o, true);

                    MPTableModel t = SubItem.toModel(subs, true);

                    itemtable.setModel(t);
                    omodel = (MPTableModel) itemtable.getModel();
                    formatTable();
                    ((MPTableModel) itemtable.getModel()).fireTableCellUpdated(0, 0);
                }
            } else if (dbo.getContext().equals(Context.getContact())) {
                dataOwner.setContactsids(((Contact) dbo).__getIDS());
                setDataOwner(dataOwner, true);
            } else if (dbo.getContext().equals(Context.getProduct())) {
                ((MPTableModel) itemtable.getModel()).addRow(
                        new SubItem((Product) dbo).getRowData(((MPTableModel) itemtable.getModel()).getRowCount() + 1));
            } else if (dbo.getContext().equals(Context.getProductlist())) {
                try {
                    SubItem[] subs = new SubItem[0];
                    if (dataOwner != null) {
                        subs = dataOwner.getSubitems();
                    }
                    ArrayList<ProductlistSubItem> l = ProductList.getReferencedObjects(dbo, Context.getProductListItems(), new ProductlistSubItem());
                    MPTableModel t = SubItem.toModel(subs);
                    int count = t.getRowCount();
                    for (int i = 0; i < l.size(); i++) {
                        ProductlistSubItem productlistSubItem = l.get(i);
                        productlistSubItem.setIDS(-1);
                        t.addRow(productlistSubItem.getRowData(i + count + 1));
                    }
                    itemtable.setModel(t);
                    omodel = (MPTableModel) itemtable.getModel();
                    formatTable();
                    ((MPTableModel) itemtable.getModel()).fireTableCellUpdated(0, 0);
                } catch (NodataFoundException ex) {
                    Log.Debug(this, ex.getMessage());
                }
            } else {
                MPView.addMessage(Messages.NOT_POSSIBLE.toString() + Messages.ACTION_PASTE.toString());
                Log.Debug(this, dbo.getContext() + " to " + Context.getItem());
            }
        }

        try {
            itemtable.changeSelection(0, 0, true, false);
        } catch (Exception e) {
            Log.Debug(e);
        }

        itemMultiplier.calculateOnce();
        netCalculator2.calculateOnce();
        netCalculator.calculateOnce();

        th.set();
    }

    @Override
    public void showSearchBar(boolean show) {
        leftpane.removeAll();
        if (show) {
            leftpane.add(sp, BorderLayout.CENTER);
            sp.search();
        }

        validate();
    }

    @Override
    public void actionAfterSave() {
        saveSubItems(true);
        omodel = (MPTableModel) itemtable.getModel();
        setTitle();
    }

    @Override
    public void actionAfterCreate() {
        th.remove();
        sp.refresh();
        ArrayUtilities.replaceColumn(itemtable, 0, null);
        saveSubItems(false);
        omodel = (MPTableModel) itemtable.getModel();
        setTitle();
        th.set();
    }

    private void saveSubItems(boolean deleteRemovedSubitems) {
        if (itemtable.getCellEditor() != null) {
            try {
                itemtable.getCellEditor().stopCellEditing();
            } catch (Exception e) {
            }
        }

        try {
            itemtable.changeSelection(0, 0, true, false);
        } catch (Exception e) {
            Log.Debug(e);
        }

        if (dataOwner.__getInttype() != Item.TYPE_BILL) {
            Product.createProducts(SubItem.saveModel(dataOwner, (MPTableModel) itemtable.getModel(), deleteRemovedSubitems), dataOwner);
        } else {
            SubItem.saveModel(dataOwner, (MPTableModel) itemtable.getModel(), deleteRemovedSubitems);
        }

        for (int i = 0; i < usedOrders.size(); i++) {
            Item o = usedOrders.get(i);
            o.setIntstatus(Item.STATUS_FINISHED);
            o.save(true);
        }
    }

    @Override
    public void changeSelection(MPComboBoxModelItem to, Context c) {
        try {
            DatabaseObject o = DatabaseObject.getObject(c, Integer.valueOf(to.getId()));
            int i = itemtable.getSelectedRow();
            if (i >= 0) {
                Object opt = itemtable.getModel().getValueAt(i, 14);
                ((MPTableModel) itemtable.getModel()).setRowAt(new SubItem((Product) o).getRowData(i), i, 4);
                itemtable.setValueAt(opt, i, 14);
            }
        } catch (Exception ex) {
        }
    }

    public void actionBeforeCreate() {
        status.setSelectedIndex(Item.STATUS_IN_PROGRESS);
        date1.setDate(new Date());
        try {
            date3.setDate(DateConverter.addDays(new Date(), Integer.valueOf(mpv5.db.objects.User.getCurrentUser().getProperties().getProperty("bills.warn.days"))));
            date2.setDate(new Date());
        } catch (Exception e) {
            date3.setDate(DateConverter.addDays(new Date(), 14));
            date2.setDate(new Date());
        }
    }

    public void actionBeforeSave() throws ChangeNotApprovedException {
        if (dataOwner.isExisting()) {
            if ((dataOwner.__getIntstatus() != Item.STATUS_PAID && dataOwner.__getIntstatus() != Item.STATUS_CANCELLED) || Popup.Y_N_dialog(Messages.REALLY_CHANGE_DONE_ITEM)) {

                if (!mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "nowarnings")) {

                    if (!Popup.Y_N_dialog(Messages.REALLY_CHANGE)) {
                        throw new ChangeNotApprovedException(dataOwner);
                    }
                }
            } else {
                throw new ChangeNotApprovedException(dataOwner);
            }
        }
    }
    private List<Item> usedOrders = new Vector<Item>();

    private void prepareTable() {
        TableCellRendererForDezimal t = new TableCellRendererForDezimal(itemtable);
        t.setRendererTo(6);
        t.setRendererTo(5);
        t.setRendererTo(2);
        TableCellRendererForDezimal tc = new TableCellRendererForDezimal(itemtable, new java.awt.Color(161, 176, 190));
        tc.setRendererTo(7);

//        CellEditorWithMPComboBox r = new CellEditorWithMPComboBox(Context.getProduct(), itemtable);
//        r.setEditorTo(4, this);
        TextAreaCellRenderer textAreaCellRenderer = new TextAreaCellRenderer(itemtable);
        textAreaCellRenderer.setRendererTo(4);
        TextAreaCellEditor r = new TextAreaCellEditor(itemtable);
        ItemTextAreaDialog itemTextAreaDialog = new ItemTextAreaDialog(MPView.getIdentifierFrame(), true);
        itemTextAreaDialog.okButton.addActionListener(r);
        itemTextAreaDialog.cancelButton.addActionListener(r);
        r.setDialog(itemTextAreaDialog, itemTextAreaDialog.textArea);
        r.setEditorTo(4);

        itemMultiplier = new TableCalculator(itemtable, new int[]{2, 5, 6}, new int[]{7}, new int[]{6}, TableCalculator.ACTION_MULTIPLY, new int[]{7});
        ((MPTableModel) itemtable.getModel()).addCalculator(itemMultiplier);
        itemMultiplier.addLabel(value, 7);

        netCalculator2 = new TableCalculator(itemtable, new int[]{2, 5}, new int[]{9}, new int[]{}, TableCalculator.ACTION_MULTIPLY, new int[]{9});
        ((MPTableModel) itemtable.getModel()).addCalculator(netCalculator2);
        netCalculator2.addLabel(netvalue, 9);

        netCalculator = new TableCalculator(itemtable, new int[]{7, 9}, new int[]{8}, new int[]{}, TableCalculator.ACTION_SUBSTRACT, new int[]{8});
        ((MPTableModel) itemtable.getModel()).addCalculator(netCalculator);
        netCalculator.addLabel(taxvalue, 8);

        JButton b1 = new JButton();
        b1.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
                if (!mpv5.db.objects.User.getCurrentUser().getProperties().getProperty(MPView.getTabPane(), "ordersoverproducts")) {
                    ProductSelectDialog.instanceOf((MPTableModel) itemtable.getModel(), itemtable.getSelectedRow(), e, Integer.valueOf(itemtable.getModel().getValueAt(itemtable.getSelectedRow(), 10).toString()), itemtable.getModel().getValueAt(itemtable.getSelectedRow(), 12 + 1), itemtable.getModel().getValueAt(itemtable.getSelectedRow(), 14));
                } else {
                    SubItem s = new SubItem();
                    Item o = (Item) Popup.SelectValue(Context.getOrder());
                    if (o != null) {
                        s.setQuantityvalue(new BigDecimal("1"));
                        s.setItemsids(o.__getIDS());
                        s.setExternalvalue(o.__getNetvalue().add(o.__getTaxvalue()));
                        s.setTotalnetvalue(o.__getNetvalue());
                        s.setCName(o.__getCName());
                        s.setDescription(Messages.TYPE_ORDER + " " + o.__getCnumber() + " " + DateConverter.getDefDateString(o.__getDateadded()));

                        ((MPTableModel) itemtable.getModel()).setRowAt(s.getRowData(itemtable.getSelectedRow()), itemtable.getSelectedRow(), 1);

                        usedOrders.add(o);
                    }
                }

                if (((MPTableModel) itemtable.getModel()).getEmptyRows(new int[]{4}) < 2) {
                    ((MPTableModel) itemtable.getModel()).addRow(1);
                }
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });

        JButton b2 = new JButton();
        b2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MPTableModel m = (MPTableModel) itemtable.getModel();
                int row = itemtable.getSelectedRow();
                SubItem.addToDeletionQueue(m.getValueAt(row, 0));
                m.setRowAt(SubItem.getDefaultItem().getRowData(row), row, 1);
            }
        });

        itemtable.getColumnModel().getColumn( itemtable.getColumnModel().getColumnIndex("A")).setCellRenderer(new ButtonRenderer());
        itemtable.getColumnModel().getColumn( itemtable.getColumnModel().getColumnIndex("A")).setCellEditor(new ButtonEditor(b1));
        itemtable.getColumnModel().getColumn( itemtable.getColumnModel().getColumnIndex("C")).setCellRenderer(new ButtonRenderer());
        itemtable.getColumnModel().getColumn( itemtable.getColumnModel().getColumnIndex("C")).setCellEditor(new ButtonEditor(b2));
    }

    private void delivery() {
        PreviewPanel pr;
        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, TemplateHandler.TYPE_DELIVERY_NOTE)) {
                pr = new PreviewPanel();
                pr.setDataOwner(dataOwner);
                new Job(Export.createFile(TemplateHandler.loadTemplate(dataOwner, TemplateHandler.TYPE_DELIVERY_NOTE), dataOwner), pr).execute();
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        }
    }

    private void confirmation() {
        PreviewPanel pr;
        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, TemplateHandler.TYPE_ORDER_CONFIRMATION)) {

                pr = new PreviewPanel();
                pr.setDataOwner(dataOwner);
                new Job(Export.createFile(TemplateHandler.loadTemplate(dataOwner, TemplateHandler.TYPE_ORDER_CONFIRMATION), dataOwner), pr).execute();
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        }
    }

    public void mail() {

        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, dataOwner.__getInttype())) {

                try {
                    Contact cont = (Contact) (Contact.getObject(Context.getContact(), dataOwner.__getContactsids()));
                    Export.mail(TemplateHandler.loadTemplate(dataOwner, dataOwner.__getInttype()), dataOwner, cont);
                } catch (NodataFoundException ex) {
                    Log.Debug(ex);
                }
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        } else {
            Popup.notice(Messages.NOT_POSSIBLE + "\n" + Messages.NOT_SAVED_YET);
        }
    }

    public void print() {
        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, dataOwner.__getInttype())) {
                if (!checkb_pront_oc.isSelected()) {
                    Export.print(TemplateHandler.loadTemplate(dataOwner, dataOwner.__getInttype()), dataOwner);
                } else {
                    Export.print(new Template[]{TemplateHandler.loadTemplate(dataOwner, dataOwner.__getInttype()), TemplateHandler.loadTemplate(dataOwner, TemplateHandler.TYPE_ORDER_CONFIRMATION)}, dataOwner);
                }
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        }
    }

    private void preview() {
        PreviewPanel pr;
        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, dataOwner.__getInttype())) {
                pr = new PreviewPanel();
                pr.setDataOwner(dataOwner);
                new Job(Export.createFile(TemplateHandler.loadTemplate(dataOwner, dataOwner.__getInttype()), dataOwner), pr).execute();
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        }
    }

    private void preloadTemplates() {
        Runnable runnable = new Runnable() {

            public void run() {
                TemplateHandler.loadTemplateFor(button_preview, dataOwner, dataOwner.__getInttype());
                TemplateHandler.loadTemplateFor(button_deliverynote, dataOwner, TemplateHandler.TYPE_DELIVERY_NOTE);
                TemplateHandler.loadTemplateFor(new JComponent[]{button_orderconf, checkb_pront_oc}, dataOwner, TemplateHandler.TYPE_ORDER_CONFIRMATION);
                TemplateHandler.loadTemplateFor(button_reminders, dataOwner, TemplateHandler.TYPE_REMINDER);

                if (TemplateHandler.isLoaded(dataOwner, dataOwner.__getInttype())) {
                    button_preview.setText(Messages.ACTION_PREVIEW.getValue());
                } else {
                    button_preview.setText(Messages.OO_NO_TEMPLATE.getValue());
                }
            }
        };
        new Thread(runnable).start();
    }

    public void pdf() {
        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, dataOwner.__getInttype())) {
                new Job(Export.createFile(dataOwner.getFormatHandler().toUserString(), TemplateHandler.loadTemplate(dataOwner, dataOwner.__getInttype()), dataOwner), new DialogForFile(User.getSaveDir(dataOwner))).execute();
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        }
    }

    public void odt() {
        if (dataOwner != null && dataOwner.isExisting()) {
            if (TemplateHandler.isLoaded(dataOwner, dataOwner.__getInttype())) {
                new Job(Export.sourceFile(dataOwner.getFormatHandler().toUserString(), TemplateHandler.loadTemplate(dataOwner, dataOwner.__getInttype()), dataOwner), new DialogForFile(User.getSaveDir(dataOwner))).execute();
            } else {
                Popup.notice(Messages.NO_TEMPLATE_LOADED + " (" + mpv5.db.objects.User.getCurrentUser() + ")");
            }
        }
    }

    private void properties() {
        final MPTableModel m = new MPTableModel(ValueProperty.getProperties(dataOwner));
        final MPTableModel mold = m.clone();

        if (m.getDataVector().isEmpty()) {
            proptable.setModel(new MPTableModel(
                    Arrays.asList(new ValueProperty[]{new ValueProperty("", "", dataOwner)})));
        } else {
            proptable.setModel(m);
        }

        m.addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                if (dataOwner.isExisting()) {
                    if (e.getColumn() == 0 && e.getType() == TableModelEvent.DELETE) {
                        ValueProperty.deleteProperty(dataOwner, String.valueOf(mold.getData()[e.getLastRow()][0]));
                        m.removeTableModelListener(this);
                        properties();
                    } else if (e.getColumn() == 1 && m.getValueAt(e.getLastRow(), 0) != null && String.valueOf(m.getValueAt(e.getLastRow(), 0)).length() > 0) {
                        ValueProperty.addOrUpdateProperty(String.valueOf(m.getData()[e.getLastRow()][0]).replaceAll("[^\\w]", ""), String.valueOf(m.getData()[e.getLastRow()][1]), dataOwner);
                        m.removeTableModelListener(this);
                        properties();
                    }
                }
            }
        });
    }
}
