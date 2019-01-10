package ru.kirillius.sprint.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Base64;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.kirillius.sprint.R;
import ru.kirillius.sprint.activities.MainActivity;
import ru.kirillius.sprint.activities.SettingsActivity;

/**
 * Created by Lavrentev on 02.10.2017.
 */

public class CommonHelper {
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();
    private static int currentMenuItemId;

    public static String md5Custom(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            // тут можно обработать ошибку
            // возникает она если в передаваемый алгоритм в getInstance(,,,) не существует
            e.printStackTrace();
        }

        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);

        while( md5Hex.length() < 32 ){
            md5Hex = "0" + md5Hex;
        }

        return md5Hex;
    }

    public static String getFormattingDate(Date date, String format) {
        if(date==null)
            return "";

        String defaultFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter=new SimpleDateFormat((format!=null) ? format : defaultFormat, new Locale("ru"));
        String currentDate=formatter.format(date);
        return currentDate;
    }

    public static Calendar getDateFromFormattingString(String date, String format) {
        Calendar calendar = Calendar.getInstance();
        String defaultFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter=new SimpleDateFormat((format!=null) ? format : defaultFormat);

        try {
            calendar.setTime(formatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static String getDayFromDate(Calendar date) {
        return String.valueOf(date.get(Calendar.DATE));
    }

    public static Date getDateFromString(String date, String format) {
        String defaultFormat = "yyyy-MM-dd";
        SimpleDateFormat formatter=new SimpleDateFormat((format!=null) ? format : defaultFormat);
        try {
            return formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getNumberDayOfWeek(Calendar calendar) {
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            return 8;
        else
            return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static Calendar getStartWeek(Calendar date) {
        date.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        date.clear(Calendar.MINUTE);
        date.clear(Calendar.SECOND);
        date.clear(Calendar.MILLISECOND);

        date.setFirstDayOfWeek(Calendar.MONDAY);
        date.set(Calendar.DAY_OF_WEEK, date.getFirstDayOfWeek());

        return date;
    }

    public static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line;
        String result = "";
        while((line = bufferedReader.readLine()) != null){
            result += line;
        }
        if(inputStream!=null){
            inputStream.close();
        }
        return result;
    }

    public static HashMap<String, String> getAuth(Context context) throws UnsupportedEncodingException {
        HashMap<String, String> result = new HashMap<String, String>();
        result.put("key", context.getResources().getString(R.string.trello_key));
        result.put("token", context.getResources().getString(R.string.trello_token));
        return result;
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager conMgr =  (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        return (netInfo == null) ? false : true;
    }

    public static String FilterNullValues(String str) {
        return (str!=null ? ((!str.equals("null")) ? str : "") : "");
    }

    public static boolean like(String str, String expr) {
        expr = expr.toLowerCase(); // ignoring locale for now
        expr = expr.replace(".", "\\."); // "\\" is escaped to "\" (thanks, Alan M)
        // ... escape any other potentially problematic characters here
        expr = expr.replace("?", ".");
        expr = expr.replace("%", ".*");
        str = str.toLowerCase();
        return str.matches(expr);
    }

    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    public static String mapToString(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String key : map.keySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            String value = map.get(key);
            try {
                stringBuilder.append((key != null ? URLEncoder.encode(key, "UTF-8") : ""));
                stringBuilder.append("=");
                stringBuilder.append(value != null ? URLEncoder.encode(value, "UTF-8") : "");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("This method requires UTF-8 encoding support", e);
            }
        }

        return stringBuilder.toString();
    }

    public static Intent onClickMainMenu(int id, Context context) {
        Intent result = null;
        boolean reqFinish = true;
        setMenuItemId(id);

        switch (id) {
            case R.id.mainItem:
                result = new Intent(context, MainActivity.class);
                reqFinish=true;
                break;
            case R.id.settings:
                result = new Intent(context, SettingsActivity.class);
                reqFinish=false;
                break;
        }

        if(result!=null)
            result.putExtra("finish", reqFinish);
        return result;
    }

    private static void setMenuItemId(int id){
        currentMenuItemId = id;
    }

    public static int getMenuItemId(){
        return currentMenuItemId;
    }
}
