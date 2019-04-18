package com.qttx.toolslibrary.utils;

import android.Manifest.permission;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huang on 2017/9/11.
 */

public class PermissionsNameHelp {

    public static String getPermissionsMulti(List<String> list) {
        String spiteChart = "";
        if (list.size() <= 2) {
            spiteChart = "与";
        } else {
            spiteChart = "、";
        }
        StringBuffer buffer = new StringBuffer("请设置允许获取");
        for (String nameitem : list) {
            String name = getPermissionSign(nameitem);
            if (!buffer.toString().contains(name)) {
                buffer.append(name)
                        .append(spiteChart);
            }
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    public static String getPermissionsMulti(String... strings) {
        List<String> lists = new ArrayList<>();
        for (String s : strings) {
            lists.add(s);
        }
        return getPermissionsMulti(lists);
    }



    public static String getPermissionSign(String name) {

        if (name.equals(permission.CAMERA))
            return "相机权限";
        if (name.equals(permission.READ_PHONE_STATE))
            return "读取手机状态权限";
        if (name.equals(permission.RECORD_AUDIO))
            return "录音权限";
        if (name.equals(permission.READ_EXTERNAL_STORAGE) || name.equals(permission.WRITE_EXTERNAL_STORAGE))
            return "SD卡权限";
        if (name.equals(permission.READ_CALENDAR))
            return "日历权限";
        if (name.equals(permission.ACCESS_FINE_LOCATION))
            return "定位权限";
        if (name.equals(permission.BODY_SENSORS))
            return "传感器权限";
        if (name.equals(permission.SEND_SMS))
            return "发短信权限";
        return name;
    }

    public static String getPermissionsStringM(String... strings) {
        String spiteChart = "_";
        StringBuffer buffer = new StringBuffer();
        for (String nameitem : strings) {
            String name = getPermissionsStringS(nameitem);
            if (!buffer.toString().contains(name)) {
                buffer.append(name)
                        .append(spiteChart);
            }
        }
        return buffer.deleteCharAt(buffer.length() - 1).toString();
    }

    public static String getPermissionsStringS(String name) {

        if (name.equals(permission.CAMERA))
            return "camera";
        if (name.equals(permission.READ_PHONE_STATE))
            return "read_phone_state";
        if (name.equals(permission.RECORD_AUDIO))
            return "reaord_audio";
        if (name.equals(permission.READ_EXTERNAL_STORAGE))
            return "read_external_storage";
        if (name.equals(permission.WRITE_EXTERNAL_STORAGE))
            return "write_external_storage";
        if (name.equals(permission.READ_CALENDAR))
            return "read_calendra";
        if (name.equals(permission.ACCESS_FINE_LOCATION))
            return "access_fine_location";
        if (name.equals(permission.BODY_SENSORS))
            return "body_sensors";
        if (name.equals(permission.SEND_SMS))
            return "send_sms";
        return name;
    }

}
