package com.android.jdhshop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *     author : yangzhuyuan
 *     e-mail : 1050867066@qq.com
 *     time   : 2020/04/29
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Wphbean implements Serializable {
    public List<Wphbean> goodsInfoList;
    public String goodsId;
    public String goodsName;
    public String goodsDesc;
    public String destUrl;
    public String  goodsThumbUrl;
    public String goodsMainPicture;
    public String           categoryId;
    public String categoryName;
    public String         sourceType;
    public String marketPrice;
    public String vipPrice;
    public String       commissionRate;
    public String commission;
    public String         discount;
    public String         cat1stId;
    public String  cat1stName;
    public String    cat2ndId;
    public String cat2ndName;
    public String  brandStoreSn;
    public String   brandName;
    public String       brandLogoFull;
    public String  schemeEndTime;
    public String      sellTimeFrom;
    public String  sellTimeTo;
    public String    weight;
//    public String storeInfo;
    public Item storeInfo;
    public class Item implements Serializable{
        public String storeName;
    }

}



