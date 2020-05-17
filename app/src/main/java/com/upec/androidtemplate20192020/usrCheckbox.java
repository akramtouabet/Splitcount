package com.upec.androidtemplate20192020;

import android.content.Context;
import android.widget.CheckBox;

/**
 * Edited by Akram TOUABET on 29/03/2020 at 04:40
 */
public class usrCheckbox extends androidx.appcompat.widget.AppCompatCheckBox {
    private String username;
    private boolean isChecked;

    public usrCheckbox(Context context, String username) {
        super(context);
        this.username = username;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
