package com.android.jdhshop.bean;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/09
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class Sjxxsbean {
    private String Time;
    private String Summary;
    private int Img;
    private String Tx;
    private int Praice;
    private String Tx1;
    private int Comment;
    private String Tx2;
    private int Share;
    public Sjxxsbean (String time,String summary,int img,String tx,int praice,String tx1,int comment, String tx2,int share){
        this.Time = time;
        this.Summary = summary;
        this.Img = img;
        this.Tx = tx;
        this.Praice = praice;
        this.Tx1 = tx1;
        this.Comment = comment;
        this.Tx2 = tx2;
        this.Share = share;
    }
    public String getTime() {
        return Time;
    }
    public String getSummary() {
        return Summary; }
    public int getImg() {
        return Img;
    }
    public String getTx() {
        return Tx;
    }
    public int getPraice() {
        return Praice;
    }
    public String getTx1() {
        return Tx1;
    }
    public int getComment() {
        return Comment;
    }
    public String getTx2() {
        return Tx2;
    }
    public int getShare() {
        return Share;
    }
}




