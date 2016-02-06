package com.tipsycoder.ui.panels;

import com.tipsycoder.ui.panels.forms.OfficerForm;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class OfficerPanel extends OfficerForm {
    public OfficerPanel(JFrame parent) {
        super(parent);
        setUpUI();
    }

    private void setUpUI() {
        String defaultLblConfig = "left, sg 1";


        add(lblBadgeNum, defaultLblConfig + ", split 4");
        add(tfBadgeNum, "pushx, wrap");

        /***
         * Personal Information Panel Begin
         */
        JPanel personalPanel = new JPanel(new MigLayout("debug"));

        TitledBorder pTitle;
        pTitle = BorderFactory.createTitledBorder("Personal Information");
        personalPanel.setBorder(pTitle);

        personalPanel.add(lblFirstName, defaultLblConfig);
        personalPanel.add(tfFirstName, "pushx");
        personalPanel.add(lblLastName, defaultLblConfig);
        personalPanel.add(tfLastName, "pushx, wrap");

        personalPanel.add(lblPNum, defaultLblConfig);
        personalPanel.add(tfPNum, "pushx");
        personalPanel.add(lblMStatus, defaultLblConfig);
        personalPanel.add(cbMStatus, "pushx, wrap");

        personalPanel.add(lblDOB, defaultLblConfig);
        personalPanel.add(dpDOB, "pushx");
        personalPanel.add(lblGender, defaultLblConfig);
        personalPanel.add(cbGender, "pushx, wrap");

        personalPanel.add(lblSAddress, defaultLblConfig + ", wrap");
        personalPanel.add(tfSAddress, "pushx, span, growx, wrap");

        personalPanel.add(lblParish, defaultLblConfig);
        personalPanel.add(cbParish, "pushx");
        personalPanel.add(lblNationality, defaultLblConfig);
        personalPanel.add(cbNationality, "pushx, wrap");

        add(personalPanel, "pushx, growx, span, split, wrap");

        /**
         * Personal Information Panel End
         */


        /***
         * Staff Information Panel Begin
         */
        JPanel staffPanel = new JPanel(new MigLayout("debug"));

        TitledBorder sTitle;
        sTitle = BorderFactory.createTitledBorder("Staff Information");
        staffPanel.setBorder(sTitle);

        staffPanel.add(lblArea, defaultLblConfig);
        staffPanel.add(cbArea, "pushx");
        staffPanel.add(lblDivision, defaultLblConfig);
        staffPanel.add(cbDivision, "pushx, wrap");

        staffPanel.add(lblRank, defaultLblConfig);
        staffPanel.add(cbRank, "pushx");
        staffPanel.add(lblStation, defaultLblConfig);
        staffPanel.add(cbStation, "pushx, wrap");

        staffPanel.add(lblNote, defaultLblConfig + ", wrap");
        staffPanel.add(taNote, "span, growx, wrap");

        add(staffPanel, "pushx, growx, span, split, wrap");

        /**
         * Staff Information Panel End
         */

        addControls();
        addSqlTable();
        addPopUpContext();
    }


}
