package ru.kirillius.sprint.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Lavrentev on 02.10.2017.
 */

public class UserInformationInPhone {
    private static final String APP_PREFERENCES = "sprintusersettings";
    public static final String APP_PREFERENCES_USER_ID = "userId";
    public static final String APP_PREFERENCES_PIN_CODE = "pinCode";
    public static final String APP_PREFERENCES_CURRENT_USER = "currentUser";
    public static final String APP_PREFERENCES_PATIENT = "currentPatient";
    public static final String APP_PREFERENCES_ENABLED_NOTIFICATIONS = "enabledNotifications";
    public static final String APP_PREFERENCES_TOKEN_DEVICE = "tokenDevice";
    public static final String APP_PREFERENCES_USE_FINGERPRINT = "useFingerprint";
    public static final String APP_PREFERENCES_PIN_ENCODED = "pinEncoded";
    public static final String APP_PREFERENCES_CURRENT_SPRINT = "currentSprint";
    public static boolean showNotification;
    GsonBuilder builder = new GsonBuilder();
    Gson gson = builder.create();

    private SharedPreferences mSettings;
    private Context context;

    public UserInformationInPhone(Context context) {
        this.context = context;
        mSettings = context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void clearUserInfo() {
        mSettings.edit().remove(APP_PREFERENCES_PIN_CODE).commit();
        mSettings.edit().remove(APP_PREFERENCES_USER_ID).commit();
        mSettings.edit().remove(APP_PREFERENCES_CURRENT_USER).commit();
        mSettings.edit().remove(APP_PREFERENCES_USE_FINGERPRINT).commit();
        mSettings.edit().remove(APP_PREFERENCES_PIN_ENCODED).commit();
        showNotification = false;
    }

    public void saveUserPinCode(String pin) {
        /*SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_PIN_CODE, CommonHelper.md5Custom(pin));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            System.out.println("save encoded pin");
            editor.putString(APP_PREFERENCES_PIN_ENCODED, CryptoHelper.encode(pin));
        }

        editor.apply();*/
    }

    public String getUserId() {
        return mSettings.getString(APP_PREFERENCES_USER_ID, null);
    }

    public boolean checkPin(String pinCode) {
        if(mSettings.contains(APP_PREFERENCES_PIN_CODE)){
            return CommonHelper.md5Custom(pinCode).equals(mSettings.getString(APP_PREFERENCES_PIN_CODE, null));
        } else {
            return false;
        }
    }

    public String getEncodedPin() {
        System.out.println(mSettings.getString(APP_PREFERENCES_PIN_ENCODED, null));
        return mSettings.getString(APP_PREFERENCES_PIN_ENCODED, null);
    }

    public boolean existsUser() {
        return (mSettings.contains(APP_PREFERENCES_PIN_CODE)) ? true : false;
    }

    public boolean getEnableNotification() {
        return mSettings.getBoolean(APP_PREFERENCES_ENABLED_NOTIFICATIONS, false);
    }

    public void setEnabledNotification(boolean enabled) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_ENABLED_NOTIFICATIONS, enabled);
        editor.apply();
    }

    public void setTokenDevice(String token) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_TOKEN_DEVICE, token);
        editor.apply();
    }

    public String getTokenDevice() {
        if(mSettings.contains(APP_PREFERENCES_TOKEN_DEVICE)){
            return mSettings.getString(APP_PREFERENCES_TOKEN_DEVICE, null);
        } else {
            return null;
        }
    }

    public void setFingerprintFlag(boolean useFingerprint) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putBoolean(APP_PREFERENCES_USE_FINGERPRINT, useFingerprint);
        editor.apply();
    }

    public boolean getFingerprintFlag() {
        return mSettings.getBoolean(APP_PREFERENCES_USE_FINGERPRINT, false);
    }

    public void setCurrentSprint(String currentSprint) {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_CURRENT_SPRINT, currentSprint);
        editor.apply();
    }

    public String getCurrentSprint() {
        return mSettings.getString(APP_PREFERENCES_CURRENT_SPRINT, null);
    }
}
