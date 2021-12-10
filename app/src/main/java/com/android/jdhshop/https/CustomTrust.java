package com.android.jdhshop.https;

import android.annotation.SuppressLint;

import org.greenrobot.greendao.annotation.NotNull;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Dns;

public class CustomTrust {

    public static class myDNS implements Dns {

        @NotNull
        @Override
        public List<InetAddress> lookup(@NotNull String s) throws UnknownHostException {
            List<InetAddress> iii = new ArrayList<>();
            return SYSTEM.lookup(s);
        }
    }

    @SuppressLint("TrulyRandom")
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new HTTPSTrustManager()},
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }
        return sSLSocketFactory;
    }

    public static class HTTPSTrustManager implements X509TrustManager {

        private static TrustManager[] trustManagers;
        private static final X509Certificate[] _AcceptedIssuers = new X509Certificate[]{};

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(
                X509Certificate[] x509Certificates, String s)
                throws java.security.cert.CertificateException {
            // To change body of implemented methods use File | Settings | File
            // Templates.
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(
                X509Certificate[] x509Certificates, String s)
                throws java.security.cert.CertificateException {
            // To change body of implemented methods use File | Settings | File
            // Templates.
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return _AcceptedIssuers;
        }

        public static void allowAllSSL() {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    // TODO Auto-generated method stub
                    return true;
                }

            });

            SSLContext context = null;
            if (trustManagers == null) {
                trustManagers = new TrustManager[]{new HTTPSTrustManager()};
            }

            try {
                context = SSLContext.getInstance("TLS");
                context.init(null, trustManagers, new SecureRandom());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (KeyManagementException e) {
                e.printStackTrace();
            }
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }
    }

    public static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}