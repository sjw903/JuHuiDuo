package com.android.jdhshop.ziyuan;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util {
 
    /**
     * 服务器下载资源压缩包
     *
     * @param context
     * @param is
     */
    public static String download(Context context, InputStream is, String fileName) {
        File file = context.getDir("resources", Context.MODE_PRIVATE);
        String resourceFilePath = file.getAbsolutePath() + "/" + fileName;
        FileOutputStream fos = null;
        try {
            File resourceFile = new File(resourceFilePath);
            if (resourceFile.exists()) {
                resourceFile.delete();
            }
            fos = new FileOutputStream(resourceFile);
            byte[] buf = new byte[2048];
            int len;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resourceFilePath;
    }
 
}