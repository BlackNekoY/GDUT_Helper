package com.rdc.gdut_helper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rdc.gdut_helper.constant.ConnectConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * cys_1058ccc5
 * 98ffa7f579
 * 103.6.85.132
 */


public class HttpUtil {

    public static boolean setCookie = false;
    public static boolean getCookie = false;
    public static boolean setReferer = false;

    public static String doGet(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Referer", "http://jwgl.gdut.edu.cn/");

            if(setCookie) {
                conn.setRequestProperty("Cookie", ConnectConfig.cookie);
            }
            if(getCookie) {
                ConnectConfig.cookie = conn.getHeaderField("Set-Cookie");
            }

            int code = conn.getResponseCode();
            StringBuilder sb = new StringBuilder();
            String line = null;
            String charset = "UTF-8";

            if (code >= 200 && code <= 400) {
                String contentType = conn.getContentType();
                int encodingStart = contentType.indexOf("charset=");
                if (encodingStart != -1) {
                    charset = contentType.substring(encodingStart + 8);
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), charset));
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                return sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost(String urlStr, String queryString) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            if(setReferer) {
                conn.setRequestProperty("Referer", ConnectConfig.HOST + ConnectConfig.MainScore.PATH_MAIN_SCORE);
            }else {
                conn.setRequestProperty("Referer", "http://jwgl.gdut.edu.cn/");
            }

            if(setCookie) {
                conn.setRequestProperty("Cookie",ConnectConfig.cookie);
            }
            if(getCookie) {
                ConnectConfig.cookie = conn.getHeaderField("Set-Cookie");
            }

            conn.getOutputStream().write(queryString.getBytes());
            String line = null;
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gb2312"));
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getPictureFromUrl(String urlStr) {
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Referer","http://jwgl.gdut.edu.cn/");

            if(setCookie) {
                conn.setRequestProperty("Cookie",ConnectConfig.cookie);
            }
            if(getCookie) {
                ConnectConfig.cookie = conn.getHeaderField("Set-Cookie");
            }

            int code = conn.getResponseCode();
            byte[] buf = new byte[1024];
            int len = -1;
            if (code >= 200 && code <= 300) {
                return BitmapFactory.decodeStream(conn.getInputStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


}
