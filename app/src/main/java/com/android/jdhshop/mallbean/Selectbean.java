package com.android.jdhshop.mallbean;

import java.io.Serializable;

public class Selectbean implements Serializable {
    private String string="";
    private String value="";

    public Selectbean() {
    }

    public Selectbean(String string, String value) {
        this.string = string;
        this.value = value;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
