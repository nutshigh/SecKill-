package com.example.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Util {
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    public static final String salt="9d5b364d";

    public static String inputPassToBackend(String src){
        String str = "" + salt.charAt(0) + salt.charAt(2) + src + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String BackendPassToDB(String src,String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + src + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }

    public static String inputPassToDBPass(String inputPass){
        String fromPass = inputPassToBackend(inputPass);
        String dbPass = BackendPassToDB(fromPass,salt);
        return dbPass;
    }

    public static void main(String[] args) {
        String src = "123456";
        System.out.println(inputPassToBackend(src));
        System.out.println(inputPassToDBPass(src));
    }
}
