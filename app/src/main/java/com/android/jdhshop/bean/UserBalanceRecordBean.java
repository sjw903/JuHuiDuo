package com.android.jdhshop.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yohn on 2018/10/9.
 */

public class UserBalanceRecordBean {
    public Tbk tbk;
    public Referrer referrer;
    public String amount;//	可提现余额
    public String amount_buy;//	购买返利金额
    public String amount_referrer;//	推荐返利金额
    public static class Tbk implements Parcelable {
        public String amount;//	累计预估
        public String amount_last;//	上月预估
        public String amount_last_finish;//	上月结算
        public String amount_last_over;//	上月失效
        public String amount_current;//	本月预估
        public String amount_current_finish;//	本月结算
        public String amount_current_over;//	本月失效

        protected Tbk(Parcel in) {
            amount = in.readString();
            amount_last = in.readString();
            amount_last_finish = in.readString();
            amount_last_over = in.readString();
            amount_current = in.readString();
            amount_current_finish = in.readString();
            amount_current_over = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString( amount );
            dest.writeString( amount_last );
            dest.writeString( amount_last_finish );
            dest.writeString( amount_last_over );
            dest.writeString( amount_current );
            dest.writeString( amount_current_finish );
            dest.writeString( amount_current_over );
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Tbk> CREATOR = new Creator<Tbk>() {
            @Override
            public Tbk createFromParcel(Parcel in) {
                return new Tbk( in );
            }

            @Override
            public Tbk[] newArray(int size) {
                return new Tbk[size];
            }
        };
    }
    public static class Referrer implements Parcelable {
        public String amount;//	累计推荐收益
        public String last_mouth;//	上月推荐收益
        public String current_mouth;//	本月推荐收益


        protected Referrer(Parcel in) {
            amount = in.readString();
            last_mouth = in.readString();
            current_mouth = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString( amount );
            dest.writeString( last_mouth );
            dest.writeString( current_mouth );
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Referrer> CREATOR = new Creator<Referrer>() {
            @Override
            public Referrer createFromParcel(Parcel in) {
                return new Referrer( in );
            }

            @Override
            public Referrer[] newArray(int size) {
                return new Referrer[size];
            }
        };
    }
}
