package com.couchbase.demo.lite.app.android;

import android.widget.Toast;

import com.couchbase.demo.lite.app.MainActivity;

/**
 * Just a helper class to show error messages
 *
 * Created by david on 26.05.14.
 */
public class AndroidError
{

    public static void show(String msg)
    {
        Toast.makeText(MainActivity.getCtx(),msg, Toast.LENGTH_LONG).show();
    }


}
