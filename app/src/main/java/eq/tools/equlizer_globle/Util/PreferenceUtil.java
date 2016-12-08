package eq.tools.equlizer_globle.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Administrator on 2016/11/22 0022.
 */
public class PreferenceUtil {
    private static SharedPreferences sp;

    private static SharedPreferences getPreferencs(Context context){
        if(sp==null){
            sp= PreferenceManager.getDefaultSharedPreferences(context);
        }
        return sp;
    }

    // 取得布尔值，可以设置默认值，设置缺省值
    public static boolean getBoolean(Context context,String key, boolean defValue) {
        return getPreferencs(context).getBoolean(key, defValue);
    }
    // 取得布尔值，缺省值为false
    public static boolean getBoolean(Context context,String key) {
        return getPreferencs(context).getBoolean(key, false);
    }

    // 设置布尔值
    public static void putBoolean(Context context,String key,boolean value){
        getPreferencs(context).edit().putBoolean(key, value).commit();
    }



    // 取得字符串，可以设置默认值，设置缺省值
    public static String getString(Context context,String key, String defValue) {
        return getPreferencs(context).getString(key, defValue);
    }
    // 取得字符串，缺省值为null
    public static String getString(Context context,String key) {
        return getPreferencs(context).getString(key, null);
    }

    // 设置字符串
    public static void putString(Context context,String key,String value){
        getPreferencs(context).edit().putString(key, value).commit();
    }


    // 取得整数，可以设置默认值，设置缺省值
    public static int getInt(Context context,String key, int defValue) {
        return getPreferencs(context).getInt(key, defValue);
    }
    // 取得整数，缺省值为0
    public static int getInt(Context context,String key) {
        return getPreferencs(context).getInt(key, 0);
    }

    // 设置整数
    public static void putInt(Context context,String key,int value){
        getPreferencs(context).edit().putInt(key, value).commit();
    }
}
