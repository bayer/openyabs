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
package mpv5.utils.renderer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultCellEditor;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
import mpv5.logging.Log;

public class TextAreaCellEditor extends DefaultCellEditor implements ActionListener {

    private final JTable table;
    protected JTextArea textArea;   
    protected JTextComponent dialogsTextComponent;
    protected JDialog dialog;
    protected final static String OK = "OK";
    protected final static String CANCEL = "CANCEL";

    public TextAreaCellEditor(JTable table) {
        super(new JTextField());
        this.table = table;
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        editorComponent = textArea;

        delegate = new DefaultCellEditor.EditorDelegate() {

            @Override
            public void setValue(Object value) {
                textArea.setText(value != null ? value.toString() : "");
            }

            @Override
            public Object getCellEditorValue() {
                return textArea.getText();
            }
        };
    }


    /**
     * Set this renderer to the given column
     * @param column
     * @param editable
     */
    public void setEditorTo(int column) {
        TableColumn col = table.getColumnModel().getColumn(column);
        col.setCellEditor(this);
    }


    public void setDialog(JDialog dialog,JTextComponent dialogsTextComponent){
        this.dialog = dialog;
        this.dialogsTextComponent = dialogsTextComponent;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table,Object value,boolean isSelected,int row,int column){
        if(value==null)value="";
        Component component = super.getTableCellEditorComponent(table, value, isSelected, row, column);
        dialogsTextComponent.setText(value.toString());
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                dialog.setLocation(textArea.getLocationOnScreen());
                dialog.setVisible(true);
            }
        });
        return component;
    }

    @Override
    public boolean stopCellEditing(){
        dialog.setVisible(false);
        return super.stopCellEditing();
    }

    @Override
    public void cancelCellEditing(){
        dialog.setVisible(false);
        super.cancelCellEditing();
    }

    public void actionPerformed(ActionEvent e) {
        if(OK.equals(e.getActionCommand())){
            dialog.setVisible(false);
            if(dialogsTextComponent!=null){
                textArea.setText(dialogsTextComponent.getText());
            }else{
                Log.Debug(this, "dialogsTextArea is null error");
            }
        }else if(CANCEL.equals(e.getActionCommand())){
            dialog.setVisible(false);
        }
        fireEditingStopped();   //Make the renderer reappear
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                table.requestFocusInWindow();
            }
        });
    }
}
