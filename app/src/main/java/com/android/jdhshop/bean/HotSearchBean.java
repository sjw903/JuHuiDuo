package com.android.jdhshop.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 热门搜索 实体类
 * Created by yohn on 2018/8/25.
 */

public class HotSearchBean {
    private List<HotSearchChildBean> list;

    public List<HotSearchChildBean> getList() {
        return list;
    }

    public void setList(List<HotSearchChildBean> list) {
        this.list = list;
    }
    public static class HotSearchChildBean implements Parcelable {
        private String id;//	ID
        private String search;//	搜索的名称
        private String num;//	搜索次数

        public HotSearchChildBean() {
        }

        protected HotSearchChildBean(Parcel in) {
            id = in.readString();
            search = in.readString();
            num = in.readString();
        }

        public static final Creator<HotSearchChildBean> CREATOR = new Creator<HotSearchChildBean>() {
            @Override
            public HotSearchChildBean createFromParcel(Parcel in) {
                return new HotSearchChildBean( in );
            }

            @Override
            public HotSearchChildBean[] newArray(int size) {
                return new HotSearchChildBean[size];
            }
        };

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSearch() {
            return search;
        }

        public void setSearch(String search) {
            this.search = search;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString( id );
            dest.writeString( search );
            dest.writeString( num );
        }
    }

}
