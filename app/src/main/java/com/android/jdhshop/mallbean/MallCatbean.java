package com.android.jdhshop.mallbean;

import java.io.Serializable;
import java.util.List;

public class MallCatbean implements Serializable {
    public String  cat_id;//	商品分类ID
    public String cat_name;//	商品分类名称
    public String keywords	;//关键词
    public String   description;//	简介描述
    public String  img	;//商品分类图片
    public List<MallCatbean> list;

 }
