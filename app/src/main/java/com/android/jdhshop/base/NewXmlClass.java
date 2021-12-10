package com.android.jdhshop.base;

import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

public class NewXmlClass {
    //创建xml传递数据
    public static void  WriteXmlStr(String banbenh)
    {
        try {
            File listversionfile = new File(
                    "/sdcard/com.android.jdhshop/config.xml");
            FileOutputStream fos = new FileOutputStream(listversionfile);
            // 获得一个序列化工具
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "utf-8");
            // 设置文件头
            serializer.startDocument("utf-8", true);
            serializer.startTag(null, "persons");

            serializer.startTag(null, "person");
            serializer.attribute(null, "id", String.valueOf(0));
            // name
            serializer.startTag(null, "icon");
            serializer.text("icon");
            serializer.endTag(null, "icon");
            // 版本号字段
            serializer.startTag(null, "image_version");
            serializer.text(banbenh+"");
            serializer.endTag(null, "image_version");

            serializer.endTag(null, "person");

            serializer.endTag(null, "persons");
            serializer.endDocument();
            fos.close();
            //Toast.makeText(MainActivity.this, "写入成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(MainActivity.this, "写入失败", Toast.LENGTH_SHORT).show();
        }
    }


}
