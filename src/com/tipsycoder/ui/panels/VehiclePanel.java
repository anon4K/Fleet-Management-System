package com.tipsycoder.ui.panels;

import com.tipsycoder.ui.panels.forms.VehicleForm;

import javax.swing.*;

/**
 * Created by TipsyCoder on 2/7/15.
 */
public class VehiclePanel extends VehicleForm {
    public VehiclePanel(JFrame parent) {
        super(parent);
        setUpUI();
    }

    private void setUpUI() {
        String defaultLblConfig = "left, sg 1";

        add(lblChassisNum, defaultLblConfig);
        add(tfChassisNum, "pushx, growx, wrap");

        add(lblColor, defaultLblConfig);
        add(tfColor, "pushx, growx");
        add(lblYear, "gap, left");
        add(spinYear, "pushx, growx, wrap");

        add(new JLabel("Area"), defaultLblConfig);
        add(cbArea, "pushx");
        add(new JLabel("Division"), defaultLblConfig);
        add(cbDivision, "pushx, wrap");

        add(lblStation, defaultLblConfig);
        add(cbStation, "pushx, growx");
        add(lblGasAmount, defaultLblConfig);
        add(cbGasAmount, "pushx, growx, wrap");

        add(lblCondition, defaultLblConfig);
        add(cbCondition, "growx");
        add(lblStatus, defaultLblConfig + ", gap");
        add(cbStatus, "pushx, growx, wrap");

        addControls();
        addSqlTable();
        addPopUpContext();
    }

}
