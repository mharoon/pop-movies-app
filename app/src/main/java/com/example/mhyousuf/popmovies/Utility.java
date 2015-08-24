package com.example.mhyousuf.popmovies;

/**
 * Created by mhyousuf on 8/16/2015.
 */
public class Utility {

    public static String extractYearFromDate(String date){
        String year = "0000";
        if(date != null) {
            String str[] = date.split("-");
            if (str.length > 0) {
                year = str[0];
            }
        }
        return year;
    }
}
