package com.brace.android.b31.test;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Admin
 * Date 2019/11/4
 */
public class Test1 {

    public static void main(String[] arg){
        byte str1 = (byte) 0xFD;
        byte[] array = new byte[]{(byte) 0x73, (byte) 0xE6,(byte) 0x1B,0x2E, (byte) 0x8E, (byte) 0xFD, (byte) 0xF8, (byte) 0xF8};
        System.out.println("---str1="+ Arrays.toString(array)+"\n");


        String localelLanguage = Locale.getDefault().getLanguage();
        String locals = Locale.getDefault().getLanguage();
        System.out.println("---------localelLanguage="+localelLanguage+"\n"+locals+"\n");






    }
}
