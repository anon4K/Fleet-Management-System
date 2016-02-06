package com.tipsycoder.ui.panels.forms.fleet;

import com.tipsycoder.core.Fleet;
import com.tipsycoder.helper.*;
import com.tipsycoder.ui.panels.forms.FormTemplate;
import net.miginfocom.swing.MigLayout;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by TipsyCoder on 2/10/15.
 */
public class FleetStarterForm extends FormTemplate implements ActionListener{
    protected Fleet fleet;

    // Add Fleet UI First Step
    private JSpinner spinVehicleAmount;
    private JPanel mainPanel;
    private JFrame parent;
    // End

    protected JFormattedTextField ftfAlias;
    protected JTextField tfDateCreated, tfModifiedDate, tfModifiedBy;
    protected JButton btnVehicles;
    private final String BTN_VEHICLES = "viewVehicles";

    public FleetStarterForm(JFrame parent) {
        super("Fleet", "alias", parent);
        this.parent = parent;
        setUpTextFields();
        setUpButtons();
        setUpAddUI();
        setUpListeners();

        viewAll(false);
    }

    private void setUpListeners() {
        btnAdd.addActionListener(this);
        btnViewAll.addActionListener(this);
        btnSearch.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClear.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnVehicles.addActionListener(this);
    }

    private void setUpButtons() {
        btnVehicles = new JButton("View Vehicles");
        btnVehicles.setActionCommand(BTN_VEHICLES);
    }

    private void setUpAddUI() {
        fleet = new Fleet();
        mainPanel = new JPanel(new MigLayout("debug"));

        SpinnerModel model = new SpinnerNumberModel(1, //initial value
                        1, //min
                        50, //max
                        1);                //step
        spinVehicleAmount = new JSpinner(model);

        mainPanel.add(new JLabel("Vehicle Amount"));
        mainPanel.add(spinVehicleAmount, "pushx, growx, wrap");
    }

    @Override
    protected void clearForm() {
        ftfAlias.setText("");
        cbArea.setSelectedIndex(0);
        tfModifiedDate.setText("");
        tfDateCreated.setText("");
        tfModifiedBy.setText("");
    }

    private void showMe() {
        spinVehicleAmount.setValue(1);

        int res = JOptionPane.showConfirmDialog(parent, mainPanel, "Create Fleet", JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE);

        if(res == JOptionPane.YES_OPTION) {
            fleet.setVehicleAmount((Integer)spinVehicleAmount.getValue());
            fleet.setAlias(ftfAlias.getText());
            fleet.setArea(cbArea.getSelectedItem().toString());
            fleet.setDivision(cbDivision.getSelectedItem().toString());
            fleet.setStation(cbStation.getSelectedItem().toString());

            FleetAddVehiclesPanel vPanel = new FleetAddVehiclesPanel(parent, fleet.getVehicleAmount(), cbStation.getSelectedItem().toString());

            if(vPanel.isEmpty()) {
                Kit.warningBox("Not Available", "No Vehicles Available At " + cbStation.getSelectedItem().toString(), parent );
                return;
            }

            fleet.setVehicleAmount((Integer)spinVehicleAmount.getValue());
            res = JOptionPane.showConfirmDialog(parent, vPanel, "Fleet :- " + ftfAlias.getText(), JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE);

            List<VehicleHandler> vehicleHandlers = vPanel.getVehicleHandlers();

            if(res == JOptionPane.YES_OPTION) {
                dbm.AddFleet(fleet, vehicleHandlers);
            }
        } else {

        }
    }

    public ErrorHelper validateForm() {
        ErrorHelper errorHelper = new ErrorHelper();

        if(ftfAlias.getText().isEmpty()) {
            errorHelper.setMessage("Please Enter An Alias");
        }else if(!cbStation.isEnabled()) {
            errorHelper.setMessage("Choose Station");
        }

        if(!errorHelper.getMessage().isEmpty())
            errorHelper.setError(true);
        else
            errorHelper.setMessage("No Error Found");

        return errorHelper;
    }

    private void setUpTextFields() {
        ftfAlias = new JFormattedTextField();

        tfDateCreated = new JTextField();
        tfDateCreated.setEditable(false);

        tfModifiedBy = new JTextField();
        tfModifiedBy.setEditable(false);

        tfModifiedDate = new JTextField();
        tfModifiedDate.setEditable(false);
    }

    @Override
    public void addSQL() {

    }

    @Override
    public void updateSQL() {

    }

    private void deleteFleet(String alias) {
        ErrorHelper error = validateForm();
        if(error.isError())
            Kit.warningBox("Invalid Entry", error.getMessage(), parent);
        else {
            if (dbm.isExist(alias, getTableName(), "alias")) {
                int res = JOptionPane.showConfirmDialog(
                        parent,
                        "Do Want To Delete Fleet: " + alias,
                        "Are You Sure?",
                        JOptionPane.YES_NO_OPTION);
                if (res == JOptionPane.YES_OPTION) {
                    dbm.deleteFleet(alias);
                    clearForm();
                }
            } else {
                Kit.warningBox("Invalid ID", "Invalid Alias: " + alias, parent);
            }
        }
    }

    protected void fillUpForm(List info) {
        ftfAlias.setText(info.get(0).toString());
        cbArea.getModel().setSelectedItem(info.get(1));
        cbDivision.getModel().setSelectedItem(info.get(2));
        cbStation.getModel().setSelectedItem(info.get(3));

        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM e, yyyy - h:m:ssa");
        tfDateCreated.setText(new DateTime(info.get(4)).toString(fmt));
        tfModifiedDate.setText(new DateTime(info.get(5)).toString(fmt));

        tfModifiedBy.setText(info.get(6).toString());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand() == Constants.BTN_ADD) {
            ErrorHelper errorHelper = validateForm();
            if(errorHelper.isError()) {
                Kit.warningBox("Invalid Entry", errorHelper.getMessage(), parent);
            } else {
                showMe();
            }
        }

        if(e.getActionCommand() == Constants.BTN_VIEWALL) {
            viewAll(true);
        }

        if(e.getActionCommand() == Constants.BTN_DELETE) {
            deleteFleet(ftfAlias.getText());
            viewAll(true);
        }

        if(e.getActionCommand() == Constants.BTN_SEARCH) {
            String s = JOptionPane.showInputDialog(parent, "Enter Fleet Alias", "Search", JOptionPane.PLAIN_MESSAGE);
            if((s != null) && (s.length() > 0)) {
                searchSQL(s, false);
            }else {
                Kit.warningBox("Error", "Invalid Entry", parent);
            }
        }

        if(e.getActionCommand() == Constants.BTN_CLEAR) {
            clearForm();
        }

       