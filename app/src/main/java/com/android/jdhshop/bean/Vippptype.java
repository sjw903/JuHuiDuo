package com.android.jdhshop.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author ;// Administrator
 *     e-mail ;// szp
 *     time   ;// 2019/07/22
 *     desc   ;//
 *     version: 1.0
 * </pre>
 */

public class Vippptype {
    public String  id  ;//  125  ,
    public String  tb_brand_name  ;//  PROYA/珀莱雅  ,
    public String  fq_brand_name  ;//  珀莱雅  ,
    public String  brand_logo  ;// http://img01.taobaocdn.com:80/tfscom/TB1dWj0DwHqK1RjSZFgXXa7JXXa ,
    public String brandcat  ;//  5  ,
    public String introduce  ;//.
    public List<Vipptitem> item = new ArrayList<>(  );
}
