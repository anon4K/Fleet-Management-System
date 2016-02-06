package com.tipsycoder.core;


import com.tipsycoder.helper.ErrorHelper;

/**
 * Created by TipsyCoder on 2/7/15.
 */
public interface ISQL {

    public void addSQL();
    public void updateSQL();
    public void deleteSQL(Integer primaryKey);
    public void searchSQL(Integer primaryKey,boolean selectedFromTable);
    public void searchSQL(String primaryKey,boolean selectedFromTable);
    public void viewAll(boolean showError);
    public ErrorHelper validateForm();
}
