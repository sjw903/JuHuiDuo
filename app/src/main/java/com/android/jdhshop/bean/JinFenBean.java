package com.android.jdhshop.bean;

import java.util.List;

public class JinFenBean {
    public List<Item> list;

    public class Item {
        public CouponInfo couponInfo;

        public class CouponInfo {
            public List<ItemChild> couponList;

            public class ItemChild {
                public String discount;
                public String link;
            }
        }

        public String skuId;
        public String skuName;//
        public String inOrderCount30Days;//
        public String materialUrl;

        public ItemShare commissionInfo;//

        public class ItemShare {
            public String commission;//
            public String commissionShare;//
            public String couponCommission;
            public String startTime;
            public String endTime;
        }

        public Price priceInfo;//

        public class Price {
            public String price;//
        }

        public ImgList imageInfo;

        public class ImgList {
            public List<Img> imageList;

            public class Img {
                public String url;//
            }
        }
    }
}
