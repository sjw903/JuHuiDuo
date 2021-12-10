package com.android.jdhshop.bean;

import java.util.List;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2019/09/12
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Jymsgbean {
    public String platformType;
    public List<Item> list;
    public class Item{
        public String gasId;
        public String gasName;
        public List<oilPriceList> oilPriceList;
        public class oilPriceList{
            public String oilNo;
            public String oilName;
            public String priceYfq;
            public String priceGun;
            public String priceOfficial;
            public String oilType;
            public List<gunNos> gunNos;
            public class gunNos{
                public String gunNo;
            }
        }
    }
}
