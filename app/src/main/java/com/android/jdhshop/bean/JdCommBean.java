package com.android.jdhshop.bean;

import java.io.Serializable;

public class JdCommBean implements Serializable {
    private Double couponCommission;

    public void setcouCommission(Double var1) {
        this.couponCommission = var1;
    }

    public Double getcouCommission() {
        return this.couponCommission;
    }
}

