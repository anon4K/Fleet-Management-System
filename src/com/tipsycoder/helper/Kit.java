package com.tipsycoder.helper;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

/**
 * Created by TipsyCoder on 2/7/15.
 */
public class Kit {
    /**
     * Implodes the specified items, gluing them using the specified glue replacing nulls with the specified
     * null placeholder.
     * @param glue              The text to use between the specified items.
     * @param nullPlaceholder   The placeholder to use for items that are <code>null</code> value.
     * @param items             The items to implode.
     * @return  A <code>String</code> containing the items in their order, separated by the specified glue.
     */
    public static final String implode(String glue, String nullPlaceholder, String ... items) {
        StringBuilder sb = new StringBuilder();
        for (String item : items) {
            if (item != null) {
                sb.append(item);
            } else {
                //sb.append(nullPlaceholder);
            }
            sb.append(glue);
        }
        return sb.delete(sb.length() - glue.length(), sb.length()).toString();
    }

    public static void warningBox(String title, String msg, JFrame parent) {
        JOptionPane.showMessageDialog(parent, msg, title, JOptionPane.ERROR_MESSAGE);
    }

    public static MaskFormatter createFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
        } catch (java.text.ParseException exc) {
            System.err.println("formatter is bad: " + exc.getMessage());
            System.exit(-1);
        }
        return formatter;
    }
}
