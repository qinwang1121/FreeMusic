package com.qw.freemusic.utils;

import com.qw.freemusic.MainApplication;

/**
 * created by QY
 * description:
 */
public class ResourceUtil {
    public static String getString(int id){
        return MainApplication.sContext.getString(id);
    }

    public static int getColor(int id) {
        return MainApplication.sContext.getColor(id);
    }
}
