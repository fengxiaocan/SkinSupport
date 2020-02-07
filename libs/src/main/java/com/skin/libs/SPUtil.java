package com.skin.libs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by thinkpad on 2017/11/16.
 */

class SPUtil {

    private static final String FILE_NAME = "user_data";    //  用户sp文件

    public static void put(Context context, String key, String object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, object);
        editor.apply();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static String get(Context context, String key, String defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defaultObject);
    }

}
