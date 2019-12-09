package com.brace.android.b31.bean;

/**
 * Created by Admin
 * Date 2019/11/27
 */
public class DeviceStyleBean {


    private int id;

    private boolean isChecked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "DeviceStyleBean{" +
                "id=" + id +
                ", isChecked=" + isChecked +
                '}';
    }
}
