package com.android.jdhshop.bean;

/**
 * <pre>
 *     author : Administrator
 *     e-mail : szp
 *     time   : 2020/05/11
 *     desc   :
 *     version: 1.0
 * </pre>
 */

public class SjxxsPjbean {
    private int Image;
    private String Name;
    private String Time;
    private String Text;
    private int Img;
   public String comment_id;
    public String      user_id;
    public String   order_id;
    public String     goods_id;
    public String   merchant_id;
    public String     score;
    public String  content;
    public String    per_consumption;
    public String   img;
    public String       comment_time;
    public String  have_img;
    public String      sku;
    public String  user_vavtar;
    public String user_nickname;

    public SjxxsPjbean (int image, String name,  String time,String text, int img){
        this.Image = image;
        this.Name = name;
        this.Time = time;
        this.Text = text;
        this.Img = img;
    }
    public int getImage() {
        return Image;
    }
    public String getName() {
        return Name;
    }

    public String getTime() {
        return Time;
    }
    public String getText() {
        return Text;
    }
    public int getImg() {
        return Img;
    }
}
