package com.android.jdhshop.bean;

/**
 * 领域数据
 */
public class DomainBean {
    public int id;//导航id
    public String type_name;//本地导航名称
    public String remote_name;//远程导航名称
    public String remote_type;//远程导航标识
    public int state;//导航状态
    public int sort;//排序
    public String update_time;//修改时间

    @Override
    public String toString() {
        return "DomainBean{" +
                "id=" + id +
                ", type_name='" + type_name + '\'' +
                ", remote_name='" + remote_name + '\'' +
                ", remote_type='" + remote_type + '\'' +
                ", state=" + state +
                ", sort=" + sort +
                ", update_time='" + update_time + '\'' +
                '}';
    }
}
