package com.android.jdhshop.bean;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/14
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Hyzxbean {
    private int Image;
    private String Text;
//    private String Text2;
//    private String Text3;
//    private String Text4;
//    private String Text5;
//      private String Text6;

      private String Text7;

    public Hyzxbean(int image, String text ,String text2,String text3,String text4,String text5,String text6, String text7) {
        this.Image = image;
        this.Text = text;
//        this.Text2 = text2;
//        this.Text3 = text3;
//        this.Text4 = text4;
//        this.Text5 = text5;
//        this.Text6 = text6;
//
        this.Text7 = text7;

    }
    public int getImage() {
        return Image;
    }
    public String getText() {
        return Text;
    }
//    public String getText2() {
//        return Text2;
//    }
//    public String getText3() {
//        return Text3;
//    }
//    public String getText4() {
//        return Text4;
//    }
//    public String getText5() {
//        return Text5;
//    }
//    public String getText6() {
//        return Text6;
//    }

    public String getText7() {
        return Text7;
    }
}


