package com.mansourappdevelopment.maktab;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static java.lang.Math.abs;

public class Utils {

    public static String getArabicNumbers(String number) {
        char[] arabicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number.length(); i++) {
            if (Character.isDigit(number.charAt(i))) {
                builder.append(arabicChars[(int) (number.charAt(i)) - 48]);
            } else {
                builder.append(number.charAt(i));
            }
        }
        return builder.toString();
    }


    public static String getDaysNamesInArabic(int day) {
        return switch (day) {
            case 1 -> "الاثنين";
            case 2 -> "الثلاثاء";
            case 3 -> "الاربعاء";
            case 4 -> "الخميس";
            case 5 -> "الجمعه";
            case 6 -> "السبت";
            case 7 -> "الاحد";
            default -> "";
        };
    }

    public static SortedSet<String> readFileUsingFileReader(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fileReader = new FileReader(file, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String company;
        SortedSet<String> companies = new TreeSet<>();
        while ((company = bufferedReader.readLine()) != null) {
            companies.add(company);
        }
        bufferedReader.close();
        return companies;
    }


}
