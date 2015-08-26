package com.gschat.core;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.github.gsdocker.gsrpc.ArchType;
import com.github.gsdocker.gsrpc.Device;
import com.github.gsdocker.gsrpc.OSType;

import java.util.UUID;

/**
 * the system information facade class
 */
final class SystemInfo {
    private static Device device;

    public static synchronized Device getDevice(Context context){

        if (device == null){
            device = new Device();

            device.setOS(OSType.Android);
            device.setOSVersion(Build.VERSION.RELEASE);
            device.setArch(ArchType.ARM);
            device.setType(Build.FINGERPRINT);

            TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);

            String tmDevice, tmSerial, androidId;

            tmDevice = "" + tm.getDeviceId();

            tmSerial = "" + tm.getSimSerialNumber();

            androidId = ""
                    + android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(),
                    ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());

            device.setID(deviceUuid.toString());
        }

        return device;
    }


    public static String toString(Device device) {
        return String.format(
                "%s:%s:%s:%s:%s",
                device.getID(),
                device.getType(),
                device.getArch(),
                device.getOS(),
                device.getOSVersion());
    }
}
