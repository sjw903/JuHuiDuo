package com.android.jdhshop.mallbean;

import java.util.List;

public class ExpressDetailBean {
    public List<ExpressDetailBean> logisticsMsg;
    public String time;//	到达时间
    public String ftime;//	派送时间
    public String context;//	物流配送详情
    public String location;//	物流站点名称

    @Override
    public String toString() {
        return "ExpressDetailBean{" +
                "logisticsMsg=" + logisticsMsg +
                ", time='" + time + '\'' +
                ", ftime='" + ftime + '\'' +
                ", context='" + context + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
