package com.tipsycoder.ui.panels;

import com.tipsycoder.ui.panels.forms.fleet.FleetStarterForm;

import javax.swing.*;

/**
 * Created by TipsyCoder on 2/15/15.
 */
public class FleetStarterPanel extends FleetStarterForm {

    public FleetStarterPanel(JFrame parent) {
        super(parent);
        setUpUI();
    }

    private void setUpUI() {
        String defaultLblConfig = "left, sg 1";

        add(new JLabel("Alias"), defaultLblConfig + ", pad 0 0 0 0");
        add(ftfAlias, "pushx, growx");
        add(new JLabel("Area"), defaultLblConfig);
        add(cbArea, "pushx, wrap");
        add(new JLabel("Division"), defaultLblConfig);
        add(cbDivision, "pushx");
        add(new JLabel("Station"), defaultLblConfig);
        add(cbStation, "pushx, growx, wrap");
        add(new JLabel("Date Created"), defaultLblConfig);
        add(tfDateCreated, "pushx, growx");
        add(btnVehicles, "pushx, growx, span, wrap");
        add(new JLabel("Last Modified Date"), defaultLblConfig);
        add(tfModifiedDate, "pushx, growx");
        add(new JLabel("Modified By"), defaultLblConfig);
        add(tfModifiedBy, "pushx, growx, wrap");

        addControls();
        addSqlTable();
        addPopUpContext();
    }
}
