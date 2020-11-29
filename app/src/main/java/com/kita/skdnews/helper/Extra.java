package com.kita.skdnews.helper;

import android.content.Context;
import android.util.Log;

import androidx.room.TypeConverter;

import com.kita.skdnews.MainActivity;
import com.kita.skdnews.R;
import com.kita.skdnews.db.DSNews;
import com.kita.skdnews.db.DSSettings;
import com.kita.skdnews.models.Article;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Extra {
    private static String TAG = Extra.class.getSimpleName();
    static DateFormat df = new SimpleDateFormat(MainActivity.TIME_STAMP_FORMAT);

    @TypeConverter
    public static Date fromTimestamp(String value) {
        if (value != null) {
            try {
                return df.parse(value);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    }

    @TypeConverter
    public static String dateToTimestamp(Date value) {
        return value == null ? null : df.format(value);
    }

    public static String DateToTimeFormat(Context context, String oldstringDate){
        PrettyTime p = new PrettyTime(new Locale(context.getString(R.string.country)));
        String isTime = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", MainActivity.local);
            Date date = sdf.parse(oldstringDate);
            isTime = p.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return isTime;
    }

    public static String DateFormat(String oldstringDate){
        String newDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", MainActivity.local);
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(oldstringDate);
            newDate = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            newDate = oldstringDate;
        }
        return newDate;
    }

    public static void newsToDB(Context context, List<Article> articles){
        for (Article article: articles){
            if (MainActivity.db.getDAONews().getNewsByUrl(String.valueOf(article.getUrl())) == null){
                try {
                    DSNews news = new DSNews(
                            String.valueOf(article.getSource().getId()),
                            String.valueOf(article.getSource().getName()),
                            String.valueOf(article.getAuthor()),
                            String.valueOf(article.getTitle()),
                            String.valueOf(article.getDescription()),
                            String.valueOf(article.getUrl()),
                            String.valueOf(article.getUrlToImage()));

                    SimpleDateFormat sdf = new SimpleDateFormat(MainActivity.TIME_STAMP_FORMAT);
                    news.setDate(sdf.parse(article.getPublishedAt().replace("T"," ")));

                    MainActivity.db.getDAONews().insertNews(news);
                }catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
    }

    public static void updateSetting(){
        DSSettings st = MainActivity.db.getDAOSetting().getSettingByID(1);

        DSSettings newst = new DSSettings(1, MainActivity.Country,
                MainActivity.Language,
                MainActivity.Category,
                MainActivity.API_KEY);

        if (st == null)
            MainActivity.db.getDAOSetting().insertSetting(newst);
        else MainActivity.db.getDAOSetting().updateSetting(newst);
    }

    public static List<String> getLabelFromResource(String[] arr){
        List<String> rslt = new ArrayList<String>();
        for (String c : arr){
            rslt.add(c.split(";")[1]);
        }
        return rslt;
    }

    public static int getIndexOfList(String[] arr, String str){
        int rslt = -1;
        for (int i = 0; i < arr.length; i++){
            String s = arr[i].split(";")[0];
            if (s.equals(str)){
                rslt = i;
                break;
            }
        }
        return rslt;
    }
}
