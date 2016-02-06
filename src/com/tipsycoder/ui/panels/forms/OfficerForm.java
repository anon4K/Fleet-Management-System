package com.tipsycoder.ui.panels.forms;

import com.tipsycoder.core.Officer;
import com.tipsycoder.helper.Constants;
import com.tipsycoder.helper.DateLabelFormatter;
import com.tipsycoder.helper.ErrorHelper;
import com.tipsycoder.helper.Kit;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.SqlDateModel;
import org.joda.time.DateTime;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class OfficerForm extends FormTemplate {
    protected JLabel lblBadgeNum, lblFirstName, lblLastName, lblRank, lblDivision, lblStation, lblArea;
    protected JLabel lblDOB, lblGender, lblSAddress, lblParish, lblNationality, lblPNum, lblMStatus, lblNote;
    protected JFormattedTextField tfBadgeNum, tfFirstName, tfLastName, tfSAddress, tfPNum;
    protected JComboBox cbRank, cbGender, cbParish, cbNationality, cbMStatus;
    protected JTextArea taNote;
    protected JDatePickerImpl dpDOB;

    private Officer currentOfficer;

    public OfficerForm(JFrame parent) {
        super("Officer", "badgeNum", parent);

        currentOfficer = new Officer();

        setUpLabels();
        setUpTxtFields();
        setUpComboBoxes();
        setUpDatePickers();
        customSetUpBtnActions();

        viewAll(false);
    }

    protected void setUpLabels() {
        lblBadgeNum = new JLabel("Badge Number:");
        lblFirstName = new JLabel("First Name:");
        lblLastName = new JLabel("Last Name:");
        lblArea = new JLabel("Area:");
        lblDivision = new JLabel("Division:");
        lblRank = new JLabel("Rank:");
        lblStation = new JLabel("Station:");
        lblDOB = new JLabel("Date Of Birth:");
        lblGender = new JLabel("Gender:");
        lblSAddress = new JLabel("Street Address:");
        lblParish = new JLabel("Parish:");
        lblNationality = new JLabel("Nationality:");
        lblPNum = new JLabel("Phone Number:");
        lblMStatus = new JLabel("Martial Status:");
        lblNote = new JLabel("Note Worthy:");
    }

    protected void setUpTxtFields() {
        MaskFormatter badgeNumFormat = Kit.createFormatter("#########");
        badgeNumFormat.setPlaceholderCharacter('_');

        tfBadgeNum = new JFormattedTextField(badgeNumFormat);
        tfBadgeNum.setColumns(13);

        tfFirstName = new JFormattedTextField();
        tfFirstName.setColumns(13);

        tfLastName = new JFormattedTextField();
        tfLastName.setColumns(13);

        MaskFormatter phoneFormat = Kit.createFormatter("1-876-###-####");
        phoneFormat.setPlaceholderCharacter('_');

        tfPNum = new JFormattedTextField(phoneFormat);
        tfPNum.setColumns(13);

        tfSAddress = new JFormattedTextField();

        taNote = new JTextArea();
        taNote.setRows(4);

    }

    protected void setUpComboBoxes() {
        cbRank = new JComboBox(Constants.RANK_LIST);

        cbGender = new JComboBox(Constants.GENDER_LIST);

        cbMStatus = new JComboBox(Constants.MARTIAL_LIST);
        cbNationality = new JComboBox(Constants.NATIONALITY_LIST);
        cbParish = new JComboBox(Constants.PARISH_LIST);
    }

    @Override
    protected void clearForm() {
        tfBadgeNum.setText("");
        tfFirstName.setText("");
        tfLastName.setText("");
        cbRank.setSelectedIndex(0);
        cbArea.setSelectedIndex(0);

        java.sql.Date date = new java.sql.Date(new Date().getTime());
        DateTime dateTime = new DateTime(date);
        dpDOB.getModel().setDate(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        dpDOB.getModel().setSelected(true);

        cbMStatus.setSelectedIndex(0);
        cbParish.setSelectedIndex(0);
        tfSAddress.setText("");
        taNote.setText("");
        cbNationality.setSelectedIndex(0);
        tfPNum.setText("");
    }

    protected void setUpDatePickers() {
        SqlDateModel model = new SqlDateModel();

        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        dpDOB = new JDatePickerImpl(datePanel, new DateLabelFormatter());
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
                String s = (String)JOptionPane.showInputDialog(parent, "Enter Badge Number", "Search", JOptionPane.PLAIN_MESSAGE);
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
                    deleteSQL(Integer.parseInt(tfBadgeNum.getText()));
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
    public ErrorHelper validateForm() {
        ErrorHelper errorHelper = new ErrorHelper();

        if(!tfBadgeNum.getText().matches("\\d+")) {
            errorHelper.setMessage("Badge Number Invalid");
        }else if(!tfFirstName.getText().matches("[A-Za-z]+")) {
            errorHelper.setMessage("First Name Invalid");
        }else if(!tfLastName.getText().matches("[A-Za-z]+")) {
            errorHelper.setMessage("Last Name Invalid");
        }else if(!tfPNum.getText().matches("1\\-876\\-\\d{3}\\-\\d{4}")) {
            errorHelper.setMessage("Phone Number Invalid\nThe Format Is: 1-876-555-5555");
        }else if(tfSAddress.getText().isEmpty()) {
            errorHelper.setMessage("Street Address Empty");
        }else if(!cbDivision.isEnabled()) {
            errorHelper.setMessage("Choose A Division");
        }else if(!cbStation.isEnabled()) {
            errorHelper.setMessage("Choose A Station");
        }

        if(!errorHelper.getMessage().isEmpty())
            errorHelper.setError(true);
        else
            errorHelper.setMessage("No Error Found");

        return errorHelper;
    }

    private String getFormValues() {
        String values = "(" + Integer.parseInt(tfBadgeNum.getText()) + ",'" + tfFirstName.getText() + "','" + tfLastName.getText() + "','" + cbRank.getSelectedItem().toString() + "','" + cbDivision.getSelectedItem().toString() + "','" + cbStation.getSelectedItem().toString() + "','" + ((java.sql.Date)dpDOB.getModel().getValue()).toString() + "','" + cbMStatus.getSelectedItem().toString() + "','" + cbParish.getSelectedItem().toString() + "','" + tfSAddress.getText() + "','" + taNote.getText() + "','" + cbNationality.getSelectedItem().toString() + "','" + tfPNum.getText() + "','" + cbArea.getSelectedItem().toString() + "')";
        System.out.println(values);
        return values;
    }

    @Override
    protected void fillUpForm(List info) {
        tfBadgeNum.setText(info.get(0).toString());
        tfFirstName.setText(info.get(1).toString());
        tfLastName.setText(info.get(2).toString());
        cbArea.getModel().setSelectedItem(info.get(13));
        cbRank.getModel().setSelectedItem(info.get(3));
        cbDivision.getModel().setSelectedItem(info.get(4));
        cbStation.getModel().setSelectedItem(info.get(5));

        DateTime dateTime = new DateTime(info.get(6));
        dpDOB.getModel().setDate(dateTime.getYear(), dateTime.getMonthOfYear() - 1, dateTime.getDayOfMonth());
        dpDOB.getModel().setSelected(true);

        cbMStatus.getModel().setSelectedItem(info.get(7));
        cbParish.getModel().setSelectedItem(info.get(8));
        tfSAddress.setText(info.get(9).toString());
        taNote.setText(info.get(10).toString());
        cbNationality.getModel().setSelectedItem(info.get(11));
        tfPNum.setText(info.get(12).toString());

        refreshCurrentOfficer();
    }

    private void refreshCurrentOfficer() {
        currentOfficer.setBadgeNum(Integer.parseInt(tfBadgeNum.getText()));
        currentOfficer.setFirstName(tfFirstName.getText());
        currentOfficer.setLastName(tfLastName.getText());
        currentOfficer.setRank(cbRank.getSelectedItem().toString());
        currentOfficer.setArea(cbArea.getSelectedItem().toString());
        currentOfficer.setDivision(cbDivision.getSelectedItem().toString());
        currentOfficer.setStation(cbStation.isEnabled() ? cbStation.getSelectedItem().toString() : "N/A");
        currentOfficer.setDob((java.sql.Date)dpDOB.getModel().getValue());
        currentOfficer.setmStatus(cbMStatus.getSelectedItem().toString());
        currentOfficer.setParish(cbParish.getSelectedItem().toString());
        currentOfficer.setAddress(tfSAddress.getText());
        currentOfficer.setNoteWorthy(taNote.getText());
        currentOfficer.setNationality(cbNationality.getSelectedItem().toString());
        currentOfficer.setpNumber(tfPNum.getText());
    }

    @Override
    public void addSQL() {
        ErrorHelper error = validateForm();
        if(error.isError())
            Kit.warningBox("Invalid Entry", error.getMessage(), parent);
        else {
            if (!dbm.isExist(Integer.parseInt(tfBadgeNum.getText()), getTableName(), "badgeNum"))
                dbm.insertSQL("INSERT INTO " + getTableName() + " VALUES " + getFormValues(), "Insert Badge Number: " + tfBadgeNum.getText() + " In Table Officer.");
            else
                Kit.warningBox("Already Exist", "Badge Number : " + tfBadgeNum.getText() + " Already Exist", parent);
        }
    }

    @Override
    public void updateSQL() {
        ErrorHelper error = validateForm();
        if(error.isError())
            Kit.warningBox("Invalid Entry", error.getMessage(), parent);
        else {
            dbm.insertSQL("UPDATE " + getTableName() + " SET badgeNum = " + Integer.parseInt(tfBadgeNum.getText()) +
                    ", firstName = '" + tfFirstName.getText() + "', lastName = '" + tfLastName.getText() +
                    "', rank = '" + cbRank.getSelectedItem().toString() + "', division = '" + cbDivision.getSelectedItem().toString() +
                    "', station = '" + cbStation.getSelectedItem().toString() + "', dob = '" + (dpDOB.getModel().getValue()).toString() +
                    "', mStatus = '" + cbMStatus.getSelectedItem().toString() + "', parish = '" + cbParish.getSelectedItem().toString() +
                    "', address =  '" + tfSAddress.getText() + "', noteWorthy = '" + taNote.getText() + "', nationality = '" + cbNationality.getSelectedItem().toString() +
                    "', pNumber = '" + tfPNum.getText() + "', area = '" + cbArea.getSelectedItem().toString() +
                    "' WHERE badgeNum = " + currentOfficer.getBadgeNum(), "Update Badge Number: " + tfBadgeNum.getText() + " In Table Officer.");
        }
    }
}
