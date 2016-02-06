package com.tipsycoder.ui.panels.forms;

import com.tipsycoder.core.Vehicle;
import com.tipsycoder.helper.Constants;
import com.tipsycoder.helper.ErrorHelper;
import com.tipsycoder.helper.Kit;
import org.joda.time.DateTime;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class VehicleForm extends FormTemplate{
    protected JLabel lblChassisNum, lblCondition, lblYear, lblGasAmount, lblColor, lblStatus, lblStation;
    protected JFormattedTextField tfChassisNum, tfColor;
    protected JComboBox cbCondition, cbGasAmount, cbStatus;//, cbStation;
    protected JSpinner spinYear;

    int currentYear, minYear;

    private Vehicle currentVehicle;

    public VehicleForm(JFrame parent) {
        super("Vehicle", "chassisNum", parent);

        currentVehicle = new Vehicle();

        setUpLabels();
        setUpTxtFields();
        setUpComboBoxes();
        customSetUpBtnActions();

        viewAll(false);
    }

    private void setUpLabels() {
        lblChassisNum = new JLabel("Chassis Number: ");
        lblCondition = new JLabel("Condition: ");
        lblYear = new JLabel("Year: ");
        lblGasAmount = new JLabel("Gas Amount: ");
        lblColor = new JLabel("Color: ");
        lblStatus = new JLabel("Status:");
        lblStation = new JLabel("Station:");
    }

    private void setUpTxtFields() {
        MaskFormatter chassisFormat = Kit.createFormatter("########");
        chassisFormat.setPlaceholderCharacter('_');

        tfChassisNum = new JFormattedTextField(chassisFormat);

        tfColor = new JFormattedTextField();
    }

    private void setUpComboBoxes() {
        cbCondition = new JComboBox(Constants.CONDITION_LIST);

        java.sql.Date date = new java.sql.Date(new Date().getTime());
        DateTime dateTime = new DateTime(date);

        currentYear = dateTime.getYear();
        minYear = 1900;

        SpinnerModel yearModel = new SpinnerNumberModel(minYear, minYear, currentYear, 1);
        spinYear = new JSpinner(yearModel);
        spinYear.setEditor(new JSpinner.NumberEditor(spinYear, "#"));

        cbGasAmount = new JComboBox(Constants.GAS_AMOUNT_LIST);
        cbStatus = new JComboBox(Constants.STATUS_LIST);

    }

    @Override
    protected void clearForm() {
        tfChassisNum.setText("");
        tfColor.setText("");
        cbCondition.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        cbGasAmount.setSelectedIndex(0);
        cbArea.setSelectedIndex(0);
        spinYear.setValue(minYear);
    }

    private void refreshCurrentVehicle() {
        currentVehicle.setChassisNum(Integer.parseInt(tfChassisNum.getText()));
        currentVehicle.setColor(tfColor.getText());
        currentVehicle.setCondition(cbCondition.getSelectedItem().toString());
        currentVehicle.setStatus(cbStatus.getSelectedItem().toString());
        currentVehicle.setArea(cbArea.getSelectedItem().toString());
        currentVehicle.setDivision(cbDivision.getSelectedItem().toString());
        currentVehicle.setStation(cbStation.isEnabled() ? cbStation.getSelectedItem().toString() : "N/A");
        currentVehicle.setGasAmount(cbGasAmount.getSelectedItem().toString());
        currentVehicle.setStation(cbStation.getSelectedItem().toString());
        currentVehicle.setYear((Integer)spinYear.getValue());
    }

    @Override
    public ErrorHelper validateForm() {
        ErrorHelper errorHelper = new ErrorHelper();

        if (!tfChassisNum.getText().matches("\\d{8}")) {
            errorHelper.setMessage("Chassis Number Invalid");
        } else if(!tfColor.getText().matches("[A-Za-z]+")) {
            errorHelper.setMessage("Color Invalid");
        } else if(!cbStation.isEnabled()) {
            errorHelper.setMessage("Choose Station");
        }

        if(!errorHelper.getMessage().isEmpty())
            errorHelper.setError(true);
        else
            errorHelper.setMessage("No Error Found");

        return errorHelper;
    }

    private String getFormValues() {
        String values = "(" + Integer.parseInt(tfChassisNum.getText()) + ",'" + cbCondition.getSelectedItem().toString() + "'," + spinYear.getValue() + ",'" + cbGasAmount.getSelectedItem().toString() + "','" + tfColor.getText() + "','" + cbStatus.getSelectedItem().toString() + "','" + cbStation.getSelectedItem().toString() + "','" + cbDivision.getSelectedItem().toString() + "','" + cbArea.getSelectedItem().toString() + "')";
        System.out.println(values);
        return values;
    }

    private void customSetUpBtnActions() {
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSQL();
                viewAll(false);
            }
        });

        btnViewAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewAll(true);
            }
        });

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(parent, "Enter Chassis Number", "Search", JOptionPane.PLAIN_MESSAGE);
                if((s != null) && (s.length() > 0)) {
                    if(s.matches("\\d+"))
                        searchSQL(Integer.parseInt(s), false);
                    else {
                        Kit.warningBox("Error", "Invalid Entry", parent);
                    }
                }
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSQL();
                viewAll(false);
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ErrorHelper error = validateForm();
                if(error.isError())
                    Kit.warningBox("Invalid Entry", error.getMessage(), parent);
                else {
                    deleteSQL(Integer.parseInt(tfChassisNum.getText()));
                    viewAll(true);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
    }

    @Override
    protected void fillUpForm(List info) {
        tfChassisNum.setText(info.get(0).toString());
        cbCondition.getModel().setSelectedItem(info.get(1));
        spinYear.setValue(info.get(2));
        cbGasAmount.getModel().setSelectedItem(info.get(3));
        tfColor.setText(info.get(4).toString());
        cbStatus.getModel().setSelectedItem(info.get(5));

        cbArea.getModel().setSelectedItem(info.get(8));
        cbDivision.getModel().setSelectedItem(info.get(7));
        cbStation.getModel().setSelectedItem(info.get(6));

        refreshCurrentVehicle();
    }

    @Override
    public void addSQL() {
        ErrorHelper error = validateForm();
        if(error.isError())
            Kit.warningBox("Invalid Entry", error.getMessage(), parent);
        else {
            if (!dbm.isExist(Integer.parseInt(tfChassisNum.getText()), getTableName(), "chassisNum"))
                dbm.insertSQL("INSERT INTO " + getTableName() + " VALUES " + getFormValues(), "Insert Chassis Number: " + tfChassisNum.getText() + " In Table Vehicle.");
            else
                Kit.warningBox("Already Exist", "Badge Number : " + tfChassisNum.getText() + " Already Exist", parent);
        }
    }

    @Override
    public void updateSQL() {
        ErrorHelper error = validateForm();
        if(error.isError())
            Kit.warningBox("Invalid Entry", error.getMessage(), parent);
        else {
            dbm.insertSQL("UPDATE " + getTableName() + " SET chassisNum = " + Integer.parseInt(tfChassisNum.getText()) +
                    ", vCondition = '" + cbCondition.getSelectedItem().toString() + "', year = " + spinYear.getValue() +
                    ", gasAmount = '" + cbGasAmount.getSelectedItem().toString() + "', color = '" + tfColor.getText() +
                    "', status = '" + cbStatus.getSelectedItem().toString() + "', station = '" + cbStation.getSelectedItem().toString() +
                    "', division = '" + cbDivision.getSelectedItem().toString() + "', area = '" + cbArea.getSelectedItem().toString() + "' " +
                    " WHERE chassisNum = " + currentVehicle.getChassisNum(), "Update Chassis Number: " + tfChassisNum.getText() + " In Table Vehicle.");
        }
    }
}
