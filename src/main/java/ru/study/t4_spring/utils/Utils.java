package ru.study.t4_spring.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class Utils {
    public static Date mapStringToDate(String str) {
        try {
            if (str != null && str.length() == 19) {
                return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(str);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    public static String toUpperCaseChar(String str) {
        StringBuilder sb = new StringBuilder();
        Scanner lineScan = new Scanner(str);
        while (lineScan.hasNext()) {
            String word = lineScan.next();
            if (!sb.isEmpty()) sb.append(" ");
            sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));
        }
        return sb.toString();
    }
}
