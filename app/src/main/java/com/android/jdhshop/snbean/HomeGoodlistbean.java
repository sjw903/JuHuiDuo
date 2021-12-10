package com.android.jdhshop.snbean;

import java.io.Serializable;
import java.util.List;

/**
 * <pre>
 *  author  ;// Administrator
 *  e-mail  ;// szp
 *  time ;// 2019/12/11
 *  desc ;//
 *  version ;// 1.0
 * </pre>
 */

public class HomeGoodlistbean  implements Serializable {
    public CommodityInfo commodityInfo;
    public Pginfo pginfo;
    public CategoryInfo categoryInfo;
    public CouponInfo couponInfo;

    public class CommodityInfo  implements Serializable {
        public String  commodityName;//  赫莲露逆龄抗皱冻干粉（20对装）  ,
        public String  commodityCode;//  11560441435  ,
        public String  supplierCode;//  0071012099  ,
        public String  supplierName;// ,

        public List<Pictureurl> pictureUrl;//  Array[4] ,

        public String  sellingPoint;//  逆龄抗皱，紧致提亮，舒缓修复，收细毛孔，水润滋养  ,
        public String  monthSales;// 52 ,
        public String  snPrice;//  598.00  ,
        public String  commodityPrice;//  398.00  ,
        public String  commodityType;// 2 ,
        public String  priceType;//  乐拼购  ,
        public String  priceTypeCode;// 99 ,
        public String  baoyou;// 1 ,
        public String  rate;//  19.50  ,
        public String  saleStatus;// 0
    }
    public class Pictureurl  implements Serializable {
        public String picUrl;// https;////imgservice.suning.cn/uimg1/b2c/image/W-VjdtiW5ObjY1irLi8uCA.jpg_200w_200h_4e ,
        public String   locationId;// 1
    }

    public class Pginfo implements Serializable {
        public String pgNum;// 2 ,
        public String pgPrice ;//398.00 ,
        public String pgUrl;// https;////pin.suning.com/pgs/product/50527334845761601435.html ,
        public String  pgActionId ;// 50527334845761601435
    }
    public class CategoryInfo implements Serializable {
        public String firstSaleCategoryId ;// 329503 ,
        public String firstSaleCategoryName ;// 护肤 清洁 面膜 ,
        public String secondSaleCategoryId ;// 330004 ,
        public String secondSaleCategoryName ;// 面部护肤 ,
        public String thirdSaleCategoryId ;// 330017 ,
        public String  thirdSaleCategoryName ;// 精华 ,
        public String firstPurchaseCategoryId ;// R8236 ,
        public String  firstPurchaseCategoryName ;// 美妆 ,
        public String secondPurchaseCategoryId ;// R8052 ,
        public String secondPurchaseCategoryName ;// 面部护理 ,
        public String thirdPurchaseCategoryId ;// R8065 ,
        public String thirdPurchaseCategoryName ;// 面部护肤 ,
        public String goodsGroupCategoryId ;// R9000308 ,
        public String goodsGroupCategoryName ;// 精华
    }
    public class CouponInfo implements Serializable {
        public String couponUrl ;// https;////quan.suning.com/lqzx_recommend.do?activityId=201912090012035187&activitySecretKey=QkTe0HeUcm3thFq3J5srnAEO ,
        public String activityId ;// 201912090012035187 ,
        public String activitySecretKey ;// QkTe0HeUcm3thFq3J5srnAEO ,
        public String couponValue ;// 100.00 ,
        public String couponCount ;// 2000 ,
        public String couponStartTime ;// 2019-12-09 17;//55;//00 ,
        public String couponEndTime ;// 2019-12-12 00;//00;//59 ,
        public String startTime ;// 2019-12-09 17;//55;//00 ,
        public String endTime ;// 2019-12-12 00;//00;//59 ,
        public String  bounsLimit ;// 300.00 ,
        public String activityDescription ;// 店铺易券(100元)
    }
}
