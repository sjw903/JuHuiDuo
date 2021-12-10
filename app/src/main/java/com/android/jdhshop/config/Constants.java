
package com.android.jdhshop.config;

import android.os.Environment;

import com.android.jdhshop.CaiNiaoApplication;
import com.android.jdhshop.common.SPUtils;

/**
 * 常量:接口地址与第三方申请的key
 */
public class Constants {
    public static boolean is_debug = false;

    public static String RESOURCE_CODE = "hw";

    //淘宝渠道方面
    public static String qd_app_key = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "tbk_appkey", "");
    public static String qd_app_secret = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "tbk_ecret", "");
    public static String qd_app_code = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "tbk_relation_code", "");
    public static String JD_CLIENT_ID = "75deb8d0c88a321ad4f968da0766bdaa";
    public static String JD_SECRET = "8c5544f515104806a5af86cda90dbf3b";
    public static String JD_POSITIONID = "3003276594";
    public static final String XUANPIN_ID = "19874972";
    public static final String TOKEN = "token";// 登录信息
    public static final String UID = "uid";// 登录信息
    public static final String GROUP_ID = "group_id";// 登录信息
    public static final String ACCOUT = "accout";// 登录信息
    public static final String PASSWORD = "password";// 登录信息
    public static String JD_APP_KEY_NEW = "6fab3143f92a739d";
    //
    public static final String APP_ID = "wxd930ea5d5a258f4f";
    public static final String KEY = "V00005623Y88064229";

    public static final String SHENG_INFO = "shengInfo";
    public static final String SHI_INFO = "shiInfo";
    public static final String SHI_ID_INFO = "shiIdInfo";
    public static final String QU_INFO = "quInfo";
    public static final String QU_ID_INFO = "quIdInfo";
    public static final String TO_FINISH_SHENG_SHI = "toFinishShengShi";
    //微信的appid与秘钥
    public static String WX_APP_ID = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "wx_appid", "");
    public static String WX_APP_SECRET = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "wx_secret", "");
    //历史记录
    public static final String HISTORICAL_RECORDS = "HISTORICAL_RECORDS";    //程序IP
//    public static final String APP_IP = "https://app.juduohui.cn"; // 预上线

    public static final String APP_IP = "https://test.xinniankeji.com"; // test
    //     public static final String APP_IP = "https://juduohui.xinniankeji.com"; // 10.0.0.21
    public static final String APP_IPZIYUAN = "https://app.juduohui.cn";//1.70版本域名
    public static String SHARE_URL = "https://app.juduohui.cn";
    public static String SHARE_URL_REGISTER = "https://app.juduohui.cn";
    public static final String JIAYOU_IP = "http://tp.jdlgg.com";
    public static String VSUP_URL = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "v_s", "").equals("") ? "http://apis.vephp.com" : SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "v_s", "");
    public static String V_URL = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "v_c", "").equals("") ? "http://api.vephp.com" : SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "v_c", "");

    public static int HEART_BEAT_TIME = 3 * 60 * 1000;


    public static void setInfo() {
        WX_APP_ID = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "wx_appid", "");
        WX_APP_SECRET = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "wx_secret", "");
        qd_app_key = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "tbk_appkey", "");
        qd_app_secret = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "tbk_appsecret", "");
        qd_app_code = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "tbk_relation_code", "");
        JD_CLIENT_ID = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "jd_key", "");
        JD_SECRET = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "jd_secret", "");
        JD_POSITIONID = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "jd_pos_id", "");
        JD_APP_KEY_NEW = SPUtils.getStringData(CaiNiaoApplication.getAppContext(), "JD_APP_KEY_NEW", "");
//        CaiNiaoApplication.registerWeChat(CaiNiaoApplication.getAppContext());
    }

    // 总
    public static final String GET_APP_SET = APP_IP + "/api/Index/getAppSet";

    /**
     * 代表支付成功的返回值
     */
    public static final String VALUE_ALIPAY_SUCCESS = "9000";

    //获取顶级淘宝商品分类列表
    public static final String HOME_TAOBAOCAT_GETTOPCATLIST_URL = APP_IP + "/api/TaobaoCat/getTopCatList";
    //获取子级淘宝商品分类列表
    public static final String HOME_TAOBAOCAT_GETSUBLISTBYPARENT_URL = APP_IP + "/api/TaobaoCat/getSubListByParent";
    //获取淘宝客商品列表
    public static final String HOME_TBK_GETTBKLIST_URL = APP_IP + "/api/Tbk/getTbkList";
    //获取淘宝客商品列表新版
    public static final String HOME_TBK_GETTBKLIST_NEW_URL = APP_IP + "/api/Tbk/getTbkList_new";
    //获取推荐淘宝客商品列表
    public static final String HOME_TBK_GETTOPLIST_URL = APP_IP + "/api/Tbk/getTopList";
    //获取淘宝客商品详情
    public static final String HOME_TBK_GETGOODSMSG_URL = APP_IP + "/api/Tbk/getGoodsMsg";
    //获取生成淘口令数据
    public static final String CREATE_TP_WD = APP_IP + "/api/Tbk/createTpwd";
    public static String GET_HAODAN_PHB = APP_IP + "/api/Haodanku/getSalesList";

    //根据商品链接/淘口令查询淘宝商品信息
    public static final String HOME_TBK_SEARCHTKL_URL = APP_IP + "/api/Tbk/searchTkl";
    public static final String SEARCHTKL = APP_IP + "/api/Tbk/searchTkl";
    public static final String setFriendRemark = APP_IP + "/api/User/setFriendRemark";
    // 第三方转链接口
    public static String TklDescrpt = "https://taodaxiang.com/taopass/parse/get";

    //注册
    public static final String REGISTER = APP_IP + "/api/UserAccount/register";
    // 注销
    public static final String UNREGISTER = APP_IP + "/api/UserAccount/unregister";
    //注册-验证码
    public static final String SEND_USER_REGISTER = APP_IP + "/api/Sms/sendUserRegister";
    //登录
    public static final String LOGIN = APP_IP + "/api/UserAccount/login";
    //发送用户找回密码验证码
    public static final String SEND_USER_FINDPWD = APP_IP + "/api/Sms/sendUserFindpwd";
    // 使用手机找回密码
    public static final String FINDPWD_BY_PHONE = APP_IP + "/api/UserAccount/findPwdByPhone";
    //退出登录
    public static final String LOGIN_OUT = APP_IP + "/api/User/loginout";
    //修改密码
    public static final String CHANGE_PWD = APP_IP + "/api/User/changePwd";
    //绑定第三方
    public static final String BIND_THIRD = APP_IP + "/api/UserAccount/loginOauth";
    // 获取邀请链接
    public static final String INVITE_LIST_URL = APP_IP + "/api/Invitation/getList";
    //绑定手机号
    public static final String BIND_Phone = APP_IP + "/api/User/changeBandingPhone";
    //消息中心列表
    public static final String MESSAGE_ARTICLE_GETARTICLELIST_URL = APP_IP + "/api/Article/getArticleList";
    //文章内容
    public static final String MESSAGE_ARTICLE_GETARTICLEMSG_URL = APP_IP + "/api/Article/getArticleMsg";
    //  选品库
    public static final String XUANPINKU = APP_IP + "/api/Tbk/getFavoritesUatm";

    //获取用户收藏商品列表
    public static final String MESSAGE_GOODSCOLLECT_GETCOLLECTLIST_URL = APP_IP + "/api/TbGoodsCollect/getCollectList";

    //获取商家活动列表
    public static final String GETSJHDlist = APP_IP + "/api/MerchantActivity/getActivityList";

    //参加活动
    public static final String ADDHD = APP_IP + "/api/MerchantActivity/enterActivity";

    //用户是否收藏商品
    public static final String MESSAGE_GOODSCOLLECT_IS_COLLECT_URL = APP_IP + "/api/TbGoodsCollect/is_collect";
    public static final String GET_NINEBY_NEW_HD = Constants.APP_IP + "/api/Haodanku/getGoodsList";
    public static final String GET_NINEBY_NEW_HD_CJQ = Constants.APP_IP + "/api/Haodanku/getItemList";
    //收藏商品
    public static final String MESSAGE_GOODSCOLLECT_COLLECT_URL = APP_IP + "/api/TbGoodsCollect/collect";
    public static final String HOME_TBK_GETTBKLIST_NEW_URL_HD = APP_IP + "/api/Haodanku/supersearch";
    //取消收藏
    public static final String MESSAGE_GOODSCOLLECT_CANCELCOLLECT_URL = APP_IP + "/api/TbGoodsCollect/cancelCollect";
    public static final String GETFREEGOODS = APP_IP + "/api/Tbk/getFreeGoodsList";
    //检查版本信息
    public static final String MESSAGE_ARTICLE_VERSION_URL = APP_IP + "/api/Article/version";
    //申请淘宝订单奖
    public static final String ORDER_APPLY = APP_IP + "/api/TaobaoOrder/apply";
    //获取淘宝订单列表
    public static final String GET_ORDER_LIST = APP_IP + "/api/TaobaoOrder/getOrderList";
    //获取淘宝订单列表（新）
    public static final String GET_ORDER_LIST_NEW = APP_IP + "/api/TaobaoOrder/getOrderList_new";
    //获取用户信息
    public static final String GET_USER_MSG = APP_IP + "/api/User/getUserMsg";
    //编辑用户头像
    public static final String EDIT_USER_AVATAR = APP_IP + "/api/User/editUserAvatar";
    //编辑用户头像
    public static final String EDIT_USER_MSG = APP_IP + "/api/User/editUserMsg";
    //获取用户余额变动记录
    public static final String GET_BALANCE_RECORD = APP_IP + "/api/UserBalanceRecord/getBalanceRecord";
    //用户申请提现
    public static final String DRAW = APP_IP + "/api/UserDrawApply/draw";
    //获取热门搜索
    public static final String GET_HOT_SEARCH = APP_IP + "/api/Tbk/getHotSearch";
    //获取淘宝客商品列表（新）
    public static final String GET_TBKLIST_NEW = APP_IP + "/api/Tbk/getTbkList_new";

    //判断是否绑定了淘宝账号
    public static final String WHETHER_BINDING_TAOBAO = APP_IP + "/api/User/whetherBindingTaobao";
    public static final String BINDING_TAOBAO = APP_IP + "/api/User/bindingTaobao";
    //获取费用配置
    public static final String GET_FEE = APP_IP + "/api/UserGroup/getFee";
    //会员升级
    public static final String UPGRADE = APP_IP + "/api/UserGroup/upgrade";

    //获取淘宝客商品列表（新）
    public static final String GET_GETTEAMLIST_NEW = APP_IP + "/api/User/getTeamList";
    //获取banner
    public static final String GET_BANNER = APP_IP + "/api/Banner/getBannerList";
    //获取banner信息
    public static final String GET_BANNER_DETAIL = APP_IP + "/api/Banner/getBannerMsg";
    //获取9.9包邮
    public static final String GET_NINEBY = APP_IP + "/api/Tbk/get99List";
    //获取9.9包邮官方接口
    public static final String GET_NINEBY_NEW = Constants.VSUP_URL + "/super?vekey=V00005623Y88064229&start_price=1&end_price=10&sort=total_sales_des&pagesize=6&coupon=1&freeship=1";
    //获取奖比例
    public static final String GET_RATE = APP_IP + "/api/User/getCommissionRate";
    //获取24小时排行榜
    public static final String GET_TOPGOODS = Constants.V_URL + "/products?vekey=V00005623Y88064229&sort=total_sales_des&pagesize=6";
    //获取24小时排行榜
    public static final String GET_TUIJIAN = Constants.V_URL + "/products?vekey=V00005623Y88064229&sort=tk_rate_des&pagesize=6";
    public static final String ABOUT_GOODS = Constants.VSUP_URL + "/super?vekey=V00005623Y88064229&para=";
    //获取超级券
    public static final String GET_TOPGOODS_CJ = Constants.V_URL + "/products?vekey=V00005623Y88064229&sort=coupon_des&pagesize=6";
    //获取淘抢购
    public static final String GET_TQG = APP_IP + "/api/Tbk/getTqgJu";
    //获取每日爆款
    public static final String GET_DAYBK = APP_IP + "/api/BbsArticle/getGoodsList";
    //获取汇客学堂
    public static final String GET_XUETANG = APP_IP + "/api/BbsArticle/getGoodsList";
    //获取每日素材
    public static final String GET_DAYSC = APP_IP + "/api/BbsArticle/getArticleList";
    //更新分享
    public static final String UPDATE_SHARE_NUM = APP_IP + "/api/BbsArticle/updateShareNum";
    //获取京东商品分类
    public static final String GET_JD_KIND = APP_IP + "/api/JingdongCat/getTopCatList";
    //检查第三方是否绑定
    public static final String IS_BIND = APP_IP + "/api/UserAccount/checkRegisterOauth";
    //获取原手机验证码
    public static final String GET_PHONE_CODE = APP_IP + "/api/User/sendChangeBandCode";
    //更换手机号发送验证码
    public static final String CHANGE_PHONE_SENDSMS = APP_IP + "/api/Sms/sendChangeBanding";
    public static final String GET_YZM_BIND = APP_IP + "/api/Sms/send";
    //更换手机号
    public static final String CHNAGE_PHONE = APP_IP + "/api/User/changeBandingPhone";
    //验证手机号验证码
    public static final String CHECK_CODE = APP_IP + "/api/Sms/checkCode";
    public static String MALL_ORDER_TYPE = "0";
    //获取拼多多分类
    public static final String GET_PDD_KIND = APP_IP + "/api/Pdd/getTopCatList";
    //拼多多商品详情
    public static final String GET_PDD_DETAIL = APP_IP + "/api/Pdd/getGoodsDetail";
    //获取会员中心数据
    public static final String GET_VIP_DATA = APP_IP + "/api/UserBalanceRecord/statistics2";
    //留言
    public static final String FEEDBACK = APP_IP + "/api/Message/online";
    //拼多多订单列表
    public static final String GET_PDD_ORDER = APP_IP + "/api/PddOrder/getOrderList";
    //奖提现明细记录
    public static final String COMMISSION_PUT_DETAIL = APP_IP + "/api/UserBalanceRecord/getBalanceRecord2";
    //收益统计
    public static final String INCOME_STATICS = APP_IP + "/api/UserBalanceRecord/statistics";
    //团队收益统计
    public static final String TEAM_INCOME = APP_IP + "/api/User/getTeamStatistics";
    //聚划算
    public static final String GET_GOODS_JHS = Constants.V_URL + "/pintuan?vekey=V00005623Y88064229&pid=mm_13494594_2143700326_111001300246&page_size=10";
    //发送支付宝绑定发送验证码
    public static final String SEND_ALICOUNT_YAM = APP_IP + "/api/UserDrawApply/sendAlipayCode";
    //变更支付宝
    public static final String CHANGEZFB = APP_IP + "/api/UserDrawApply/changeAlipay";
    //获取京东订单接口
    public static final String GETJD_ORDERLIST = APP_IP + "/api/JingdongOrder/getOrderList";
    public static final String HD_DAY_BK_NEW = APP_IP + "/api/Haodanku/selectedItem";

    //京东收藏接口
    public static final String COLLECT_JD_GOOD = APP_IP + "/api/JingdongCollect/collect";
    //京东取消收藏
    public static final String DE_COLLECT_JD_GOOD = APP_IP + "/api/JingdongCollect/cancelCollect";
    //获取京东收藏列表
    public static final String GETJD_COLLECT_LIST = APP_IP + "/api/JingdongCollect/getCollectList";
    //判断用户jd是否收藏
    public static final String IS_JD_GOOD_COLLECT = APP_IP + "/api/JingdongCollect/is_collect";
    //pdd收藏接口
    public static final String COLLECT_PDD_GOOD = APP_IP + "/api/PddCollect/collect";
    //pdd取消收藏
    public static final String DE_COLLECT_PDD_GOOD = APP_IP + "/api/PddCollect/cancelCollect";
    //获取pdd收藏列表
    public static final String GETPDD_COLLECT_LIST = APP_IP + "/api/PddCollect/getCollectList";
    //判断pdd用户是否收藏
    public static final String IS_PDD_GOOD_COLLECT = APP_IP + "/api/PddCollect/is_collect";
    //获取可提现金额
    public static final String GET_KETIXIAN = APP_IP + "/api/UserBalanceRecord/drawStatistics";
    //获取专属客服
    public static final String GET_KEFU = APP_IP + "/api/User/getService";
    //解除绑定淘宝
    public static final String UNBING_TAOBAO = APP_IP + "/api/User/unbindTaobao";
    //获取我的页面团队收益
    public static final String GET_MINEPAGE_TEAM = APP_IP + "/api/UserBalanceRecord/teamStatistics";
    //获取淘宝年货节
    public static final String NIANHUOJIE = APP_IP + "/api/TbActivity/getNewYearShoppingFestivalMsg";
    //春节红包
    public static final String HONGBAO = APP_IP + "/api/UserBalanceRecord/receiveBonus";
    //签到天数
    public static final String ATTEND_NUMS = APP_IP + "/api/UserSign/getContinuousDay";
    public static final String ATTEND_RECORD = APP_IP + "/api/UserSign/getSignRecord";
    public static final String ATTEND = APP_IP + "/api/UserSign/singin";
    public static final String HOME_RECOMMEND_GOODS = APP_IP + "/api/Tbk/getHotGoodsList";
    public static final String ISRECEIVER_BONUS = APP_IP + "/api/UserBalanceRecord/isReceiveBonus";
    public static final String GETBK_NEW = APP_IP + "/api/Tbk/getHotGoodsList";
    public static final String BIND_RELATION_ID = APP_IP + "/api/User/bindingTbRid";
    public static final String UNBIND_RELATION_ID = APP_IP + "/api/User/unbindTbRid";
    public static final String ISBIND_RELATION_ID = APP_IP + "/api/User/whetherBindingTbRid";
    public static final String GOODS_VEDIO = "http://apipro.vephp.com/itemdetail?vekey=V00005623Y88064229&onlydetail=1";
    public static final String SHOP_DETAIL = "http://apipro.vephp.com/itemdetail?vekey=V00005623Y88064229&moreinfo=1&onlyglobalinfo=1";
    public static final String SET = APP_IP + "/api/Diy/set";
    public static final String GOODS_SMOKE = APP_IP + "/api/Tbk/getGoodsSmoke";
    public static final String GETPPLIST = APP_IP + "/api/Haodanku/getBrandList";
    public static final String getBrandMsg = APP_IP + "/api/Haodanku/getBrandMsg";
    public static final String isSignToday = APP_IP + "/api/UserSign/isSignToday";
    public static final String ranking = APP_IP + "/api/UserBalanceRecord/ranking";
    public static final String GROUP_LIST = APP_IP + "/api/UserGroup/getGroupList";
    public static final String GET_DOU_KIND = APP_IP + "/api/Haodanku/getDouHuoCat";
    public static final String dgOptimusMaterial = APP_IP + "/api/Tbk/dgOptimusMaterial";
    public static final String GET_DOU_LIST = APP_IP + "/api/Haodanku/getDouHuoItemList";
    public static final String TBACTIVITY = APP_IP + "/api/TbActivity/getactivity";
    public static String lng = "";
    public static String lat = "";
    //登录
    public static final String empower = JIAYOU_IP + "/api/Gas/empower";
    //获取列表
    public static final String index = JIAYOU_IP + "/api/Gas/getGasList";
    //获取详情
    public static final String gas_detail = JIAYOU_IP + "/api/Gas/getGasDetail";
    //获取用户订单
    public static final String order = JIAYOU_IP + "/api/Gas/order";
    public static final String JINFEN = APP_IP + "/api/Jingdong/getRecommendGoods";
    //京东新接口
    public static String JDNEWGOODS_LIST = "http://api-gw.haojingke.com/index.php/v1/api/jd/goodslist";
    public static String JDNEWGOODS_DETAIL = "http://api-gw.haojingke.com/index.php/v1/api/jd/goodsdetail";
    public static String JDNEWGOODS_COUPON = "http://api-gw.haojingke.com/index.php/v1/api/jd/getunionurl?apikey=" + JD_APP_KEY_NEW + "&type=1";
    //------------------------------------------苏宁专区--------------------------
    public static final String sn_appip = APP_IP;
    //获取高佣专区类目列表
    public static final String GET_COMMISSIONCATEGORY = sn_appip + "/api/Suning/getCommissionCategory";
    //小编推荐商品
    public static final String GET_RECOMMENDGOODS = sn_appip + "/api/Suning/getRecommendGoods";
    //3、获取高佣专区商品列表
    public static final String getCommissionGoodsList = sn_appip + "/api/Suning/getCommissionGoodsList";
    //商品详细信息
    public static final String getGoodsInfo = sn_appip + "/api/Suning/getGoodsInfo";
    //获取地址列表
    public static final String ADDRESSLIST = APP_IP + "/api/ConsigneeAddress/getAddressList";
    //添加地址
    public static final String ADD_ADDRESS = APP_IP + "/api/ConsigneeAddress/addAddress";
    //删除地址
    public static final String DELETE_ADDRESS = APP_IP + "/api/ConsigneeAddress/delAddress";
    //编辑收货地址
    public static final String EDIT_ADDRESS = APP_IP + "/api/ConsigneeAddress/editAddress";
    //获取自营商城顶级分类列表
    public static final String SLEF_MALL_CAT = APP_IP + "/api/GoodsCat/getParentCatList";
    //获取自营商城子分类列表
    public static final String SLEF_MALL_CAT_SUB = APP_IP + "/api/GoodsCat/getSubCatList";
    //获取自营商城子分类列表
    public static final String SLEF_MALL_GOODS = APP_IP + "/api/Goods/getGoodsList";
    //获取商品详情
    public static final String SELF_GOODS_DETAIL = APP_IP + "/api/Goods/getGoodsMsg";
    //商城商品购买下单
    public static final String MALL_BUY_GOODS = APP_IP + "/api/Order/order";
    //获取支付表单
    public static final String GET_ORDER_FORM = APP_IP + "/api/Order/getPayForm";
    //获取用户订单列表
    public static final String GET_USER_ORDER_LIST = APP_IP + "/api/Order/getOrderList";
    //购物车下单
    public static final String SHOPCAR_ORDER_BUY = APP_IP + "/api/Order/orderByShopcart";
    //取消订单
    public static final String CANCLE_MALL_ORDER = APP_IP + "/api/Order/cancel";
    //获取订单详情
    public static final String GET_ORDER_DETAIL = APP_IP + "/api/Order/getOrderMsg";
    //加入购物车
    public static final String ADD_TO_SHOP_CAR = APP_IP + "/api/Shopcart/add";
    //获取购物车列表
    public static final String GET_SHOP_CAR_LIST = APP_IP + "/api/Shopcart/getShopcartList";
    //申请退款
    public static final String APPLY_TUIKUAN = APP_IP + "/api/Order/applyDrawback";
    //确认收货
    public static final String CONFIRM_ORDER = APP_IP + "/api/Order/confirmOrder";
    //填写快递单号
    public static final String EXPRESS_DANHAO = APP_IP + "/api/Order/fillInRefundExpressNum";
    //查看物流
    public static final String EXPRESS_DETAIL = APP_IP + "/api/Order/getLogisticsMsg";
    public static final String getLogistics = APP_IP + "/api/Order/getLogistics";

    //获取用户订单列表
    public static final String GET_USER_ORDER_LIST_UPDATE = APP_IP + "/api/UserOrder/getList";
    //取消订单
    public static final String CANCLE_MALL_ORDER_UPDATE = APP_IP + "/api/UserOrder/cancel";
    //获取订单详情
    public static final String GET_ORDER_DETAIL_UPDATE = APP_IP + "/api/UserOrder/getMsg";
    //申请退款
    public static final String APPLY_TUIKUAN_UPDATE = APP_IP + "/api/UserOrder/applyDrawback";
    //确认收货
    public static final String CONFIRM_ORDER_UPDATE = APP_IP + "/api/UserOrder/confirm";
    //填写快递单号
    public static final String EXPRESS_DANHAO_UPDATE = APP_IP + "/api/UserOrder/fillInRefundExpressNum";
    public static final String EXPRESS_DETAIL_UPDATE = APP_IP + "/api/UserOrder/getLogisticsMsg";


    public static double jds;
    public static double wds;
    public static String citys = "";
    //获取商户分类
    public static final String getMerchantGroup = APP_IP + "/api/O2oCat/getTopList";
    //获取商户列表
    public static final String getMerchantList = APP_IP + "/api/Merchant/getList";
    //获取商户服务列表
    public static final String getMerchantAuth = APP_IP + "/api/MerchantAuth/getAuth";
    //获取商户信息
    public static final String getMsg = APP_IP + "/api/Merchant/getMsg";
    //获取商户店铺介绍
    public static final String getIntroduce = APP_IP + "/api/Merchant/getIntroduce";
    //获取商户资质文件
    public static final String getQC = APP_IP + "/api/Merchant/getQC";
    //获取商户相册
    public static final String getimgList = APP_IP + "/api/MerchantImg/getList";
    //获取商户相册类型列表
    public static final String getTypelist = APP_IP + "/api/MerchantImg/getTypeList";
    //获取公告列表
    public static final String getggList = APP_IP + "/api/MerchantNotice/getList";
    //获取商品列表
    public static final String getservicelist = APP_IP + "/api/O2oGoods/getList";
    //获取服务详情
    public static final String getServiceMsg = APP_IP + "/api/O2oGoods/getMsg";
    //立即购买
    public static final String addorder = APP_IP + "/api/O2oOrder/order";
    //获取支付表单
    public static final String getPayForm = APP_IP + "/api/O2oOrder/getPayForm";
    //获取用户订单列表
    public static final String getOrderList = APP_IP + "/api/O2oOrder/getList";
    //获取订单信息
    public static final String getOrderMsg = APP_IP + "/api/O2oOrder/getMsg";
    //取消订单
    public static final String cancel = APP_IP + "/api/O2oOrder/applyDrawback";
    //确认收货
    public static final String confirmOrder = APP_IP + "/api/Order/confirmOrder";
    //评价订单
    public static final String comment = APP_IP + "/api/O2oGoodsComment/comment";
    //用户是否收藏商户
    public static final String is_collect = APP_IP + "/api/MerchantCollect/is_collect";
    //收藏商户
    public static final String collect = APP_IP + "/api/MerchantCollect/collect";
    //取消收藏
    public static final String cancelCollect = APP_IP + "/api/MerchantCollect/cancelCollect";
    //获取充值优惠券列表
    public static final String getCouplist = APP_IP + "/api/CouponsCard/getCoupLists";
    //会员充值优惠券
    public static final String recharge = APP_IP + "/api/CouponsCard/recharge";
    //获取用户订单列表(新）
    public static final String setMeituans = APP_IP + "/api/MeiTuan/getOrderList";

    // 淘宝、天猫商品详情页面网址
    public static String AliGoodDescUrl = "https://detail.tmall.com/item.htm?id=";

    // Page参数保存的的时长
    public static int CacheSaveTime = 24 * 60 * 60;

    // 价格趋势数据源相关
    public static String trendAPI_URL = "https://www.inn-chain.com/GetProductData";
    public static String trendKEY = "YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4";
    public static int trendChannel_TAOBAO = 0;
    public static int trendChannel_PDD = 1;
    public static int trendChannel_JD = 2;

    //微信登录,注册
    public static final String weixLogin = APP_IP + "/api/UserAccount/wechat/register";
    public static final String weixLogin_l = APP_IP + "/api/UserAccount/wechat/miniprogram/register";

    //pdd小卡片详情用到的搜索接口
    public static final String pddSearch = "https://api-gw.haojingke.com/index.php/v1/api/pdd/goodslist";


    //个性化设置显示
    public static final String GETai_assistan_set = APP_IP + "/api/XhAssistant/AssistantList";

    //获取临时助理群系统分配ID
    public static final String getTmpGroupID = APP_IP + "/api/XhAssistant/getTmpGroupID";
    //提交提交建群审核数据
    public static final String getPushImage = APP_IP + "/api/XhAssistant/regAssistant";
    //微信群信息
    public static final String getWXQinXinXI = APP_IP + "/api/XhAssistant/MyWxGroupInfo";
    //挑选助理
    public static final String getTiaoXuanAI = APP_IP + "/api/XhAssistant/updateAssistant";
    //往推品库添加商品
    public static final String getAddShngPin = APP_IP + "/api/XhAssistantProduct/AssistantProductAdd";
    //检查用户的推品库中是否有该商品
    public static final String getJianCHa = APP_IP + "/api/XhAssistantProduct/assistantProductCheck";
    //获取用户待推广商品
    public static final String getYongHuDaiTui = APP_IP + "/api/XhAssistantProduct/assistantUserProductList";
    //推品库中对商品的操作
    public static final String getTuiPinCaoZuo = APP_IP + "/api/XhAssistantProduct/assistantProductHandle";
    //获取用户历史推广商品
    public static final String getLiSHITUIGUANG = APP_IP + "/api//XhAssistantProduct/assistantProductHistory";
    //获取系统待推广商品
    public static final String getXITONGDAITUI = APP_IP + "/api/XhAssistantProduct/assistantLocalProductList";
    //设置微信群助理属性
    public static final String getSETAI = APP_IP + "/api/XhAssistant/AssistantSet";

    //获取加密数据
    public static final String getHUOQUJIAMI = APP_IP + "/api/UploaderUser/userUploadSign";


    public static String MAIN_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/com.android.jdhshop";//CaiNiaoApplication.getInstances().getExternalFilesDir("resources").getPath();
    //zip下载后解压文件存放目录
    public static final String BLACKTECH_HOT_UPDATE_FILE_PATH = MAIN_FILE_PATH + "/images";
    // 下载的缓存文件后缀名
    public static final String UXUE_TEMP_FILE_SUFFIX = ".zip";
    //版本号和需要更新的资源包
    public static final String DOWNLOADZIP = APP_IPZIYUAN + "/api/System/versionOssUrl";

    public static String ZIYUAN_PATH = BLACKTECH_HOT_UPDATE_FILE_PATH + "/";//"/sdcard/com.android.jdhshop/resources/.image/";
    public static String CrashLogPath = MAIN_FILE_PATH + "/logs/";
    // 商品平台定义
    public static final String PLATFORM_JD = "2"; // 京东
    public static final String PLATFORM_PDD = "1"; // 拼多多
    public static final String PLATFORM_TB = "3"; // 淘宝

    //常驻通知接口内容展开
    public static final String NOTIFICATION_ICON = APP_IP + "/api/Inform/residentInform";
    //常驻通知接口内容收起
    public static final String NOTIFICATION_TEXT = APP_IP + "/api/Inform/residentInformText";
    //用户一键提交log信息
    public static final String PUSHUSERLOG = APP_IPZIYUAN + "/api/applog/addinfo";
    // 常驻通知点击ACTION常量
    public static final String NOTIFICATION_ACTION = "juduohui_ago_notification";
    // 常驻通知服务名称
    public static final String AGO_NOTIFICATION_SERVICE = "com.android.jdhshop.juduohui.NotificationService";
    // 普通通知CHANNEL
    public static final String NORMAL_NOTIFICATION_CHANNEL = "jdhshop_normal_notice";

    // 激励视频广告正常播放完毕
    public static final int AD_REWARD_PLAY_SUCCESS = 0;
    public static final int AD_REWARD_PLAY_ERROR = 1;

    // 阅读赚钱相关
    public static final String GET_READ_CONFIG = APP_IP + "/api/UserGold/getconfig"; // 阅读配置
    public static final String GET_READ_DAYTIME = APP_IP + "/api/UserGold/getDaytime"; // 今日阅读相关统计数据
    public static final String GET_XH_MEDIA_LIB_TYPE = APP_IP + "/api/XhmediaLib/getXhmediaLibtype"; // 分单分类
    public static final String GET_XH_MEDIA_LIB_LIST = APP_IP + "/api/XhmediaLib/getXhmediaLiblist"; // 文章列表
    public static final String GET_XH_MEDIA_LIB_COMMENT_LIST = APP_IP + "/api/XhmediaLib/getXhmediaLibcomment"; // 文章评论列表
    public static final String GET_XH_MEDIA_LIB = APP_IP + "/api/XhmediaLib/getXhmediaLib"; // 文章详情
    public static final String ADD_MEDIA_LIB_COMMENT = APP_IP + "/api/XhmediaLib/addXhmediaLibcomment"; // 文章评论
    public static final String ADD_MEDIA_LIB_COMMENT_LIKE = APP_IP + "/api/XhmediaLib/XhmediaLibCommentlike"; //文章评论的点赞
    public static final String ADD_MEDIA_LIB_LIKE = APP_IP + "/api/XhmediaLib/XhmediaLiblike"; // 喜欢
    public static final String NEWS_READ_ADD_GOLDS = APP_IP + "/api/UserGold/getnumgold"; // 获取金币
    public static final String NEWS_READ_PING_BACK = APP_IP + "/api/UserGold/verificgold"; // 获取金币验证
    public static final String MEDIA_LIB_REPRINT_ADD = APP_IP + "/api/MediaLibReprint/ReprintAdd"; // 转载
    public static final String MEDIA_LIB_SET_REPORT = APP_IP + "/api/ReportStr/setreport";
    public static final String MEDIA_LIB_REPRINT_DEL = APP_IP + "/api/MediaLibReprint/ReprintDel";
    public static final String MEDIA_LIB_REPRINT_HELP_LIST = APP_IP + "/api/MediaLibReprint/getXhmediaLibicon";
    public static final String MEDIA_LIB_REPORT_OPTION = APP_IP + "/api/ReportStr/getreportlist";
    public static final String MEDIA_LIB_REPORT_LIST = APP_IP + "/api/MediaLibReprint/getXhmediaLiblist";
    public static final String MEDIA_LIB_REPORT_RECHECK = APP_IP + "/api/MediaLibReprint/ReprintCheckTwo";
    public static final String MEDIA_LIB_REPORT_DEL = APP_IP + "/api/MediaLibReprint/ReprintDel";
    public static final String MEDIA_LIB_REPRINT_INFO = APP_IP + "/api/MediaLibReprint/GetMediaInfo";
    public static final String MEDIA_LIB_REPRINT_TRANS_URL = APP_IP + "/api/MediaLibReprint/chargeUrl";
    public static final String AD_IS_CAN_SHOW_REWARD = APP_IP + "/api/UserHuisign/isCanWatch";

    // 获得上传签名
    public static final String GET_UPLOAD_SIGN = APP_IP + "/api/UploaderUser/userUploadSign";

    // 广告相关
    public static final String GET_ADVERTISEMENT_LIST = APP_IP + "/api/UserHuisign/getAdvertisementList";
    public static final String ADD_ADVERTISEMENT_ORDER = APP_IP + "/api/UserHuisign/addAdvertisementOrder";
    public static final String ADVERTISEMENT_VERIFY = APP_IP + "/api/UserHuisign/getHuiPoint_qthird";
    // 通知
    // 用户修改普通信息接收状态修改接口
    public static final String UPDATE_PT_NOTIFICATION_STATUS = APP_IP + "/api/GeneralInform/updateInform";
    // 用户修改公众号信息接收状态修改接口
    public static final String UPDATE_WX_NOTIFICATION_STATUS = APP_IP + "/api/GeneralInform/updateAccount";
    // 保持用户在线接口
    public static final String USER_HEART_BEAT = GET_APP_SET;

    //获取个人主页信息
    public static final String GET_USER_HOME = APP_IP + "/api/UserHomepage/getUserhome";

    //添加个人主页信息数据
    public static final String GET_ADD_USER_HOME = APP_IP + "/api/UserHomepage/addUserhome";

    //文章导航数据获取接口
    public static final String GET_MEDIA_LIB_TYPE = APP_IP + "/api/XhmediaLib/getXhmediaLibtype";

    //获取用户粉丝列表
    public static final String GET_FANS_LIST = APP_IP + "/api/UserHomepage/getFanslist";

    //获取用户已关注列表
    public static final String GET_FOLLOW_LIST = APP_IP + "/api/UserHomepage/getFollowlist";

    //用户关注其他用户个人主页
    public static final String UPDATE_FOLLOW_LIST = APP_IP + "/api/UserHomepage/updateFollow";

    //获取用相关推荐个人主页列表
    public static final String GET_LIKE_LIST = APP_IP + "/api/UserHomepage/getLikeist";

    //个人中心文章置顶功能
    public static final String GET_MEDIA_TOP = APP_IP + "/api/UserHomepage/getMediatop";
}