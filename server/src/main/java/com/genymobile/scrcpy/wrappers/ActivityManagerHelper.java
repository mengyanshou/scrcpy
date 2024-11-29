package com.genymobile.scrcpy.wrappers;

import android.app.ActivityManagerNative;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import com.genymobile.scrcpy.FakeContext;
import com.genymobile.scrcpy.util.Ln;

public class ActivityManagerHelper {
    static IActivityManager activityManager;

    static public IActivityManager getActivityManager() {
        if (activityManager == null) {
            IBinder service = android.os.ServiceManager.getService(Context.ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= 26) {
                activityManager = IActivityManager.Stub.asInterface(service);
            } else {
                activityManager = ActivityManagerNative.asInterface(service);
            }
        }
        return activityManager;
    }


    public static int startActivity(Intent intent) {
        return startActivity(intent, null);
    }

    @SuppressWarnings("ConstantConditions")
    public static int startActivity(Intent intent, Bundle options) {
        IActivityManager manager = getActivityManager();
        try {
            return manager.startActivityAsUser(
                    /* caller */ null,
                    /* callingPackage */ FakeContext.PACKAGE_NAME,
                    /* intent */ intent,
                    /* resolvedType */ null,
                    /* resultTo */ null,
                    /* resultWho */ null,
                    /* requestCode */ 0,
                    /* startFlags */ 0,
                    /* profilerInfo */ null,
                    /* bOptions */ options,
                    /* userId */ /* UserHandle.USER_CURRENT */ -2
            );
        } catch (Throwable e) {
            Ln.e("Could not invoke method", e);
            return 0;
        }
    }


    public static void forceStopPackage(String packageName) {
        IActivityManager manager = getActivityManager();
        try {
            manager.forceStopPackage(packageName, /* userId */ -2);
        } catch (Throwable e) {
            Ln.e("Could not invoke method", e);
        }
    }
}
