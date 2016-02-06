package com.tipsycoder.helper;

import java.util.List;

/**
 * Created by TipsyCoder on 3/8/15.
 */
public class VehicleHandler {
    private String chassisNum;
    private List<String> officersID;

    public String getChassisNum() {
        return chassisNum;
    }

    public void setChassisNum(String chassisNum) {
        this.chassisNum = chassisNum;
    }

    public List<String> getOfficersID() {
        return officersID;
    }

    public void setOfficersID(List<String> officersID) {
        this.officersID = officersID;
    }
}
