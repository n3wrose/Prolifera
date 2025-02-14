package com.prolifera.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String toAppDate(String date) {
        String day = date.substring(0,2);
        String month = date.substring(3,5);
        String year = date.substring(6);
        return year+"-"+month+"-"+day;
    }

    public static String toUserDate(String date) {
        String retorno = null;
        try {
            String day = date.substring(8, 10);
            String month = date.substring(5, 7);
            String year = date.substring(0, 4);
            retorno = day+"/"+month+"/"+year;
        } catch (Exception e) { return "desconhecido";  }
        return retorno;
    }

    public static String toUserDateFull(String date) {
        String retorno = null;
        try {
            String day = date.substring(8, 10);
            String month = date.substring(5, 7);
            String year = date.substring(0, 4);
            int hour = Integer.parseInt(date.substring(11,13)) - 3;
            String minute = date.substring(14,16);
            retorno = hour+":"+ minute + " de "+day+"/"+month+"/"+year;
        } catch (Exception e) { return "desconhecido";  }
        return retorno;
    }

    public static Date stringToDate(String date) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) { Log.i("error","erro em stringtodate"+ e.getMessage()); return null;}
        return d;
    }

    public static Date stringToDateFull(String date) {
        Date d = null;
        try {
            d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (Exception e) { Log.i("error","erro em stringtodate"+ e.getMessage()); return null;}
        return d;
    }

    private static String dateToString(Date d) {
        String date = "";
        date = new SimpleDateFormat("yyyy-MM-dd").format(d);
        return date;
    }

}

//       dd/mm/yyyy
//       0123456789