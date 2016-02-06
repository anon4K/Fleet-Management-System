package com.tipsycoder.ui.tabs;

import com.tipsycoder.ui.panels.FleetStarterPanel;
import com.tipsycoder.ui.panels.OfficerPanel;
import com.tipsycoder.ui.panels.VehiclePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class MainTabPane extends JPanel {
        private JTabbedPane mainTabPane;

        public MainTabPane(JFrame parent) {
            super(new GridLayout(1, 1));
            mainTabPane = new JTabbedPane();

            mainTabPane.addTab("Officer Records", new OfficerPanel(parent));
            mainTabPane.setMnemonicAt(0, KeyEvent.VK_1);

            mainTabPane.addTab("Vehicle Records", new VehiclePanel(parent));
            mainTabPane.setMnemonicAt(1, KeyEvent.VK_2);

            mainTabPane.addTab("Fleet Records", new FleetStarterPanel(parent));
            mainTabPane.setMnemonicAt(2, KeyEvent.VK_3);

            add(mainTabPane);

        }
}
