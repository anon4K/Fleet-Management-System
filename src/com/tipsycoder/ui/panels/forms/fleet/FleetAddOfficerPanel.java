package com.tipsycoder.ui.panels.forms.fleet;

import com.tipsycoder.core.DatabaseManager;
import com.tipsycoder.helper.DatabaseHelper;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by TipsyCoder on 2/15/15.
 */
public class FleetAddOfficerPanel extends JPanel {
    private List<JComponent> officerList;
    private String mStation, mChassisNum;
    private Object officerModel[];

    public FleetAddOfficerPanel(Integer vehicleType, String station, String chassisNum) {
        super(new MigLayout());
        mStation = station;
        mChassisNum = chassisNum;
        officerList = new ArrayList<JComponent>();
        setUpMainPanel();
    }

    private void setUpMainPanel() {
        List<Object> ls = getOfficerInfo();
        if(ls.size() > 0) {
            ls.add(0, "Please Select Officer");
            officerModel = ls.toArray();

            for (int i = 0; i < 4; i++) {
                officerList.add(new JLabel("Badge Number"));
                final JComboBox cbOfficer = new JComboBox(officerModel.clone());

                cbOfficer.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if(e.getItem() != cbOfficer.getItemAt(0) && e.getStateChange() == ItemEvent.SELECTED) {
                            for (JComponent cb : officerList) {
                                if(cb instanceof JComboBox) {
                                    if(cb != e.getSource())
                                        ((JComboBox) cb).removeItem(e.getItem());
                                }
                            }
                        }

                        if(e.getItem() != cbOfficer.getItemAt(0) && e.getStateChange() == ItemEvent.DESELECTED) {
                            for (JComponent cb : officerList) {
                                if(cb instanceof JComboBox) {
                                    if(cb != e.getSource())
                                        ((JComboBox) cb).addItem(e.getItem());
                                }
                            }
                        }
                    }
                });
                officerList.add(cbOfficer);
            }

            int cnt = 0;

            while (cnt < officerList.size()) {
                add(officerList.get(cnt));
                officerList.remove(cnt);
                add(officerList.get(cnt), "pushx, growx, wrap");
                cnt++;
            }
        }
    }

    public boolean isEmpty() {
        return officerModel == null;
    }

    private ArrayList<Object> getOfficerInfo() {
        DatabaseHelper dsh = DatabaseManager.getInstance().runQuery("SELECT badgeNum, firstName FROM Officer WHERE station = '" + mStation + "'", "");
        ArrayList<Object> officerList = new ArrayList<Object>();

        for (Object item : dsh.getData()) {
            String tempStr = ((Vector<Object>)item).elementAt(0).toString()  + " - " + ((Vector<Object>)item).elementAt(1).toString();
            officerList.add(tempStr);
        }

        return officerList;
    }

    public List<String> getInfo() {
        List<String> ls = new ArrayList<String>();

        for (JComponent cb : officerList) {
            if(cb instanceof JComboBox) {
                if(((JComboBox) cb).getSelectedIndex() != 0) {
                    ls.add(((JComboBox) cb).getSelectedItem().toString().substring(0, 9));
                }
            }
        }
        return ls;
    }

    public List<String> getOfficerList() {
        List<String> badgeList = new ArrayList<String>();
        for (JComponent item : officerList) {
            if(item instanceof JComboBox) {
                badgeList.add(((JComboBox)item).getSelectedItem().toString());
            }
        }
        return badgeList;
    }
}
