package com.doubleclick.menu.App;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.util.Objects;

/**
 * Created By Eslam Ghazy on 9/21/2022
 */
public class App extends Application {

    /*
     *
     * MD5: 52:E8:A1:D6:D2:42:41:E2:8B:DB:D7:AF:0E:57:BF:CE
     * SHA1: F3:67:56:60:18:49:2B:50:A0:DE:36:0A:9E:90:3E:F2:CC:EF:19:E9
     * SHA-256: CB:58:70:BD:4E:65:3E:CB:95:AE:41:8D:62:F6:37:F1:E7:4C:9B:58:DC:99:17:1C:32:F3:00:A5:B8:81:AF:DB
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < Objects.requireNonNull(children).length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
