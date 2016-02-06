package com.tipsycoder.core;

import java.util.Date;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class Admin extends Officer {
    private Date appointedDate;
    private Date lastLoginDate;

    public Date getAppointedDate() {
        return appointedDate;
    }

    public void setAppointedDate(Date appointedDate) {
        this.appointedDate = appointedDate;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
}
