package Util;

import android.util.Log;

public class LogUtil {
    private static boolean isLogEnable = true;
    public static void d(String tag, String msg){
        if(isLogEnable)
            Log.d(tag,msg);
    }

    public static void v(String tag, String msg){
        if(isLogEnable)
            Log.v(tag,msg);
    }

    public static void e(String tag, String msg){
        if(isLogEnable)
            Log.e(tag,msg);
    }
}
