package com.tipsycoder.ui;

import com.tipsycoder.core.DatabaseManager;
import com.tipsycoder.helper.Constants;
import com.tipsycoder.ui.tabs.MainTabPane;

import javax.swing.*;
import java.awt.event.*;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class MainWindow extends JFrame {

    JMenuBar menuBar;

    public MainWindow() {
        super("Fleet Management System");
        DatabaseManager.getInstance().mainWindow = this;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setUpUI();

    }

    public void showWindow() {
        add(new MainTabPane(this));

        setPreferredSize(Constants.MAIN_WINDOW_SIZE);
        setResizable(false);
        pack();
        setVisible(true);
    }

    private void setUpUI() {
        menuBar = new JMenuBar();

        JMenu miFile = new JMenu("File");
        miFile.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.setMnemonic(KeyEvent.VK_Q);
        fileExit.setToolTipText("Exit Application");
        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        miFile.add(fileExit); //File->Exit Menu Item

        menuBar.add(miFile);

        setJMenuBar(menuBar);

    }

    public static void main(String[] args) {
        try {
            MainWindow mainWindow = new MainWindow();
            mainWindow.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosed(e);
                    DatabaseManager.getInstance().logOffAdmin();
                }
            });
            LoginWindow loginWindow = new LoginWindow(mainWindow);
            loginWindow.showLoginWindow("");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
