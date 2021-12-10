package com.android.jdhshop.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
 
/**
 * Created by cruze on 2015/7/29.
 */
public class TxtTool {
 
    public static String readTxt(String path){
        String str = "";
        try {
            File urlFile = new File(path);
            InputStreamReader isr = new InputStreamReader(new FileInputStream(urlFile), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
 
            String mimeTypeLine = null ;
            while ((mimeTypeLine = br.readLine()) != null) {
                str = str+mimeTypeLine;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  str;
    }
 
}