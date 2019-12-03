package com.prolifera.app;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Utils {

    public static String toAppDate(String date) {
        String day = date.substring(0,2);
        String month = date.substring(3,5);
        String year = date.substring(6);
        return year+"-"+month+"-"+day;
    }

    public static String toUserDate(String date) {
        String day = date.substring(8,10);
        String month = date.substring(5,7);
        String year = date.substring(0,4);
        return day+"/"+month+"/"+year;
    }
}





//       dd/mm/yyyy
//       0123456789