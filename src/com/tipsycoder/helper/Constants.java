package com.tipsycoder.helper;

import com.tipsycoder.core.DatabaseManager;

import java.awt.*;

/**
 * Created by TipsyCoder on 2/7/15.
 */
public class Constants {
    public static final Dimension MAIN_WINDOW_SIZE = new Dimension(700, 800);
    public static final Dimension ADD_VEHICLE_PANEL_SIZE = new Dimension(490, 700);

    public static final String[] MARTIAL_LIST = {"Single", "Widowed", "Married"};
    public static final String[] GENDER_LIST = {"Male", "Female"};
    public static final String[] RANK_LIST = {"Commission"};

    public static Object[] AREA_LIST;

    public static final String[] NATIONALITY_LIST = {"Cuba", "Jamaica"};

    public static final String[] PARISH_LIST = {"St. Ann", "St. Catherine"};

    public static final String[] CONDITION_LIST = {"Great", "Good", "Bad"};
    public static final String[] GAS_AMOUNT_LIST = {"1", "2", "3", "4"};
    public static final String[] STATUS_LIST = {"Available", "Out"};

    public static final String BTN_ADD = "add";
    public static final String BTN_DELETE = "delete";
    public static final String BTN_PRINT = "print";
    public static final String BTN_VIEWALL = "viewall";
    public static final String BTN_UPDATE = "update";
    public static final String BTN_SEARCH = "search";
    public static final String BTN_CLEAR = "clear";


    private static Constants instance = null;

    protected Constants() {
        // Exists only to defeat instantiation.
        AREA_LIST = DatabaseManager.getInstance().cbQuery("SELECT * FROM Area", "name").toArray();

    }

    public static Constants getInstance() {
        if(instance == null) {
            instance = new Constants();
        }
        return instance;
    }
}
