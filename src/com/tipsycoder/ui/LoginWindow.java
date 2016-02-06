package com.tipsycoder.ui;

import com.tipsycoder.core.DatabaseManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.util.Arrays;

/**
 * Created by TipsyCoder on 2/13/15.
 */
public class LoginWindow extends JPanel {
    private JFormattedTextField tftUsername;
    private JPasswordField pfPassword;
    private MainWindow parent;
    private JLabel lblError;

    public LoginWindow(MainWindow parent) {
        super(new MigLayout("Debug"));

        this.parent = parent;
        if(parent.isVisible()) parent.setVisible(false);

        tftUsername = new JFormattedTextField();
        pfPassword = new JPasswordField();
        lblError = new JLabel();

        add(lblError, "span, wrap");

        add(new JLabel("Username"));
        add(tftUsername, "pushx, growx, wrap");
        add(new JLabel("Password"));
        add(pfPassword, "pushx, growx, wrap");
    }

    public void showLoginWindow(String errMsg) {
        lblError.setText(errMsg);
        int res = JOptionPane.showConfirmDialog(parent, this, "Login Information", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        System.out.println("Username: " + tftUsername.getText() + "\nPassword: " + pfPassword.getPassword());

        if(res == JOptionPane.OK_OPTION) {
            if(DatabaseManager.getInstance().loginAdmin(tftUsername.getText(), pfPassword.getPassword()))
                parent.showWindow();
            else {
                showLoginWindow("Invalid Badge Number/Password Combination");
            }
        } else {
            System.exit(0);
        }
    }

}
