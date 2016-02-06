package com.tipsycoder.ui.panels.forms;

import com.tipsycoder.core.DatabaseManager;
import com.tipsycoder.core.ISQL;
import com.tipsycoder.helper.*;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class FormTemplate extends JPanel implements ISQL{
    protected JButton btnAdd, btnPrint, btnUpdate, btnDelete, btnSearch, btnViewAll, btnClear;
    protected JTable sqlTable;
    private String tableName;
    protected JFrame parent;
    protected JComboBox cbDivision, cbStation, cbArea;

    private String primaryKeyFieldName;


    protected DatabaseManager dbm;

    public FormTemplate(String tableName, String primaryKeyFieldName, JFrame parent) {
        this.tableName = tableName;
        this.parent = parent;
        this.primaryKeyFieldName = primaryKeyFieldName;

        dbm = DatabaseManager.getInstance();

        setLayout(new MigLayout("debug"));

        btnAdd = new JButton("ADD");
        btnAdd.setActionCommand(Constants.BTN_ADD);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setActionCommand(Constants.BTN_UPDATE);

        btnDelete = new JButton("DELETE");
        btnDelete.setActionCommand(Constants.BTN_DELETE);

        btnSearch = new JButton("SEARCH");
        btnSearch.setActionCommand(Constants.BTN_SEARCH);

        btnViewAll = new JButton("VIEW ALL");
        btnViewAll.setActionCommand(Constants.BTN_VIEWALL);

        btnPrint = new JButton("PRINT");
        btnPrint.setActionCommand(Constants.BTN_PRINT);

        btnClear = new JButton("CLEAR");
        btnClear.setActionCommand(Constants.BTN_CLEAR);

        sqlTable = new JTable() {
            public boolean isCellEditable(int row, int column) {
                if (true) return false;
                return true;
            }
        };

        _setUpComboBoxes();
    }

    private void _setUpComboBoxes() {
        cbArea = new JComboBox(Constants.getInstance().AREA_LIST);
        cbArea.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbDivision.setModel(new DefaultComboBoxModel(dbm.cbQuery("select Division.name from Division join Area on Area.id = Division.AreaID where Area.name = '" + e.getItem().toString() + "'", "name").toArray()));
                    cbStation.setModel(new DefaultComboBoxModel(dbm.cbQuery("select Station.name from Station join Division on Division.DivisionID = Station.DivisionID where Division.name = '" + cbDivision.getSelectedItem().toString()+ "'", "name").toArray()));
                }
            }
        });

        cbDivision = new JComboBox(new DefaultComboBoxModel(dbm.cbQuery("select Division.name from Division join Area on Area.id = Division.AreaID where Area.name = '" + cbArea.getSelectedItem().toString() + "'", "name").toArray()));
        cbDivision.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    cbStation.setModel(new DefaultComboBoxModel(dbm.cbQuery("select Station.name from Station join Division on Division.DivisionID = Station.DivisionID where Division.name = '" + e.getItem().toString() + "'", "name").toArray()));
                }
            }
        });

        cbStation = new JComboBox(new DefaultComboBoxModel(dbm.cbQuery("select Station.name from Station join Division on Division.DivisionID = Station.DivisionID where Division.name = '" + cbDivision.getSelectedItem().toString() + "'", "name").toArray()));
    }

    protected void addControls() {
        /***
         * Controls Panel Begin
         */
        JPanel controlsPanel = new JPanel(new MigLayout("debug"));

        String gap = "gapleft 5px";

        controlsPanel.add(btnAdd, gap);
        controlsPanel.add(btnUpdate, gap);
        controlsPanel.add(btnDelete, gap);

        controlsPanel.add(btnSearch, gap);
        controlsPanel.add(btnViewAll, gap);
        controlsPanel.add(btnClear, gap);

        controlsPanel.add(btnPrint, gap + ", wrap");

        add(controlsPanel, "pushx, growx, span, split, wrap");

        /**
         * Controls Panel End
         */
    }

    protected void addSqlTable() {
        /**
         * Table Panel Begin
         */

        JScrollPane tablePanel = new JScrollPane(sqlTable);
        add(tablePanel, "push, grow, span, split, wrap");

        /**
         * Table Panel End
         */

        sqlTable.addMouseListener(new MouseInputAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Object id = sqlTable.getModel().getValueAt(sqlTable.getSelectedRow(), 0);
                if(id instanceof Integer)
                    searchSQL((Integer)id, false);
                else
                    searchSQL((String)id, false);

            }
        });
    }

    protected void addPopUpContext() {
        for (int i = 0; i < getComponents().length; i++) {
            if(getComponent(i) instanceof JFormattedTextField) {
                getComponent(i).addMouseListener(new ContextMenuMouseListener());
            } else if(getComponent(i) instanceof JPanel) {
                JPanel p = (JPanel)getComponent(i);
                for (Component temp : p.getComponents()) {
                    if(temp instanceof JFormattedTextField) {
                        temp.addMouseListener(new ContextMenuMouseListener());
                    }
                }
            }
        }
    }

    protected List displaySQLTable(DatabaseHelper dsh) {
        DefaultTableModel model = new DefaultTableModel(dsh.getData(), dsh.getColumnNames())
        {
            @Override
            public Class getColumnClass(int column)
            {
                for (int row = 0; row < getRowCount(); row++)
                {
                    Object o = getValueAt(row, column);

                    if (o != null)
                    {
                        return o.getClass();
                    }
                }

                return Object.class;
            }
        };
        List tempData = null;
        if(model.getDataVector().size() > 0)    {
            tempData = new ArrayList((Vector)model.getDataVector().get(0));
        }

        sqlTable.setModel(model);

        return tempData;
    }


    public String getTableName() {
        return tableName;
    }

    protected void fillUpForm(List list) {}

    protected void clearForm() {}

    @Override
    public ErrorHelper validateForm() {
        return new ErrorHelper();
    }

    @Override
    public void addSQL() {

    }

    @Override
    public void updateSQL() {

    }

    @Override
    public void deleteSQL(Integer primaryKey) {
        ErrorHelper error = validateForm();
        if(error.isError())
            Kit.warningBox("Invalid Entry", error.getMessage(), parent);
        else {
            if (dbm.isExist(primaryKey, getTableName(), primaryKeyFieldName)) {
                int res = JOptionPane.showConfirmDialog(
                        parent,
                        "Do Want To Delete " + primaryKeyFieldName + ": " + primaryKey,
                        "Are You Sure?",
                        JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    dbm.insertSQL("DELETE FROM Fleet_Vehicle_Map WHERE chassisNum = " + primaryKey, "");
                    dbm.insertSQL("DELETE FROM Vehicle_Officer_Map WHERE " + primaryKeyFieldName + " = " + primaryKey, "");
                    dbm.insertSQL("DELETE FROM " + getTableName() + " WHERE " + primaryKeyFieldName + " = " + primaryKey, "Delete " + primaryKey + " From Table " + primaryKeyFieldName + ".");
                    clearForm();
                }
            } else
                Kit.warningBox("Invalid ID", "Invalid " + primaryKeyFieldName + ": " + primaryKey, parent);
        }
    }

    @Override
    public void searchSQL(Integer primaryKey, boolean selectedFromTable) {
        DatabaseHelper dsh = dbm.runQuery("SELECT * FROM " + getTableName() + " WHERE " + primaryKeyFieldName + " = " + primaryKey, "Select " + primaryKey + " From Table " + primaryKeyFieldName + ".");
        commonSearchSQL(dsh, selectedFromTable);

    }

    private void commonSearchSQL(DatabaseHelper dsh, boolean selectedFromTable) {
        if(dsh == null)
            return;

        List list = displaySQLTable(dsh);

        if(list == null) {
            Kit.warningBox("Not Found", "No Record Found", parent);
            viewAll(false);
        } else {
            if(selectedFromTable)
                viewAll(false);
            fillUpForm(list);
        }
    }

    @Override
    public void searchSQL(String primaryKey, boolean selectedFromTable) {
        DatabaseHelper dsh = dbm.runQuery("SELECT * FROM " + getTableName() + " WHERE " + primaryKeyFieldName + " = '" + primaryKey + "'", "Select " + primaryKey + " From Table " + primaryKeyFieldName + ".");
        commonSearchSQL(dsh, selectedFromTable);
    }

    @Override
    public void viewAll(boolean showError) {
        DatabaseHelper dsh = dbm.runQuery("SELECT * FROM " + getTableName(), "Select All From Table " + getTableName());

        if(dsh == null)
            return;

        List list = displaySQLTable(dsh);

        if(list == null && showError) {
            Kit.warningBox("Not Found", "No Record Found", parent);
        }
    }
}
