package com.tipsycoder.ui.panels.forms.fleet;

import com.tipsycoder.core.DatabaseManager;
import com.tipsycoder.helper.Constants;
import com.tipsycoder.helper.Kit;
import com.tipsycoder.helper.VehicleHandler;
import net.miginfocom.swing.MigLayout;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by TipsyCoder on 2/15/15.
 */
public class FleetAddVehiclesPanel extends JPanel implements ActionListener{
    private JPanel mainPanel;
    private JFrame parent;
    private String mStation;
    private Object modelArr[];
    private List<JComponent> vehicleList;
    private List<VehicleHandler> vehicleHandlers;

    public FleetAddVehiclesPanel(JFrame parent ,Integer vehicleAmt, String station) {
        super(new GridLayout(1, 1));

        vehicleList = new ArrayList<JComponent>();
        vehicleHandlers = new ArrayList<VehicleHandler>();
        this.parent = parent;
        mStation = station;
        mainPanel = new JPanel(new MigLayout("debug"));
        setUpMainPanel(vehicleAmt, station);

        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setPreferredSize(Constants.ADD_VEHICLE_PANEL_SIZE);

        add(mainScrollPane);
    }

    private void setUpMainPanel(Integer lVehicleAmt, String station) {
        List tempL = DatabaseManager.getInstance().cbQuery("SELECT chassisNum FROM Vehicle WHERE station = '" + station + "' AND status = 'Available'", "chassisNum");
        tempL.add(0, "Please Select Vehicle");
        modelArr = tempL.toArray();

        if(modelArr.length > 1) {
            for (int i = 1; i <= lVehicleAmt; i++) {
                JPanel p = new JPanel(new MigLayout("debug"));

                TitledBorder pTitle;
                pTitle = BorderFactory.createTitledBorder("Vehicle " + i);
                p.setBorder(pTitle);
                final JComboBox cbVehicleList = new JComboBox(new DefaultComboBoxModel(modelArr.clone()));
                vehicleList.add(cbVehicleList);

                final JButton btnAddOfficer = new JButton("Add Officers");

                btnAddOfficer.setActionCommand("addOfficer");
                btnAddOfficer.setEnabled(false);
                btnAddOfficer.addActionListener(this);

                JPanel sub_p = new JPanel(new MigLayout("debug"));

                TitledBorder sub_pTitle;
                sub_pTitle = BorderFactory.createTitledBorder("Vehicle Details");
                sub_pTitle.setBorder(sub_pTitle);

                sub_p.add(new JLabel("Condition:"));
                final JLabel conditionLbl = new JLabel("N/A");
                sub_p.add(conditionLbl);

                sub_p.add(new JLabel("Gas Amount:"));
                final JLabel gasLbl = new JLabel("N/A");
                sub_p.add(gasLbl);

                sub_p.add(new JLabel("Color:"));
                final JLabel colorLbl = new JLabel("N/A");
                sub_p.add(colorLbl);

                p.add(new JLabel("Choose Vehicle"));
                p.add(cbVehicleList, "pushx, growx");
                p.add(btnAddOfficer, "pushx, growx, wrap");
                p.add(sub_p, "pushx, growx, span, split, wrap");

                cbVehicleList.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {

                        if (cbVehicleList.getSelectedIndex() != 0 && e.getStateChange() == ItemEvent.SELECTED) {
                            List ls = DatabaseManager.getInstance().getVehicleInfo(cbVehicleList.getSelectedItem().toString());
                            btnAddOfficer.setEnabled(true);

                            conditionLbl.setText(ls.get(0).toString());
                            gasLbl.setText(ls.get(1).toString() + "L");
                            colorLbl.setText(ls.get(2).toString());
                        } else {
                            btnAddOfficer.setEnabled(false);
                            conditionLbl.setText("N/A");
                            gasLbl.setText("N/A");
                            colorLbl.setText("N/A");
                        }
                    }
                });

                cbVehicleList.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getItem() != cbVehicleList.getItemAt(0) && e.getStateChange() == ItemEvent.SELECTED) {
                            for (JComponent cb : vehicleList) {
                                if(cb instanceof JComboBox) {
                                    if(cb != e.getSource())
                                        ((JComboBox) cb).removeItem(e.getItem());
                                }
                            }
                        }

                        if(e.getItem() != cbVehicleList.getItemAt(0) && e.getStateChange() == ItemEvent.DESELECTED) {
                            for (JComponent cb : vehicleList) {
                                if(cb instanceof JComboBox) {
                                    if(cb != e.getSource())
                                        ((JComboBox) cb).addItem(e.getItem());
                                }
                            }
                        }
                    }
                });

                btnAddOfficer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if(e.getActionCommand() == "addOfficer") {
                            FleetAddOfficerPanel addOfficerPanel = new FleetAddOfficerPanel(2, mStation, cbVehicleList.getSelectedItem().toString());

                            if(addOfficerPanel.isEmpty()) {
                                Kit.warningBox("Not Available", "No Officer Available At " + mStation, parent);
                                return;
                            }

                            int res = JOptionPane.showConfirmDialog(parent, addOfficerPanel, "Create Fleet", JOptionPane.YES_OPTION, JOptionPane.PLAIN_MESSAGE);

                            if(res == JOptionPane.YES_OPTION) {
                                VehicleHandler vehicleHandler = new VehicleHandler();
                                vehicleHandler.setChassisNum(cbVehicleList.getSelectedItem().toString());
                                vehicleHandler.setOfficersID(addOfficerPanel.getInfo());
                                getVehicleHandlers().add(vehicleHandler);
                            }
                        }
                    }
                });

                mainPanel.add(p, "wrap");
            }
        }
    }

    public boolean isEmpty() {
        return modelArr.length <= 1;
    }


    @Override
    public void actionPerformed(ActionEvent e) {


    }

    public List<VehicleHandler> getVehicleHandlers() {
        return vehicleHandlers;
    }
}
