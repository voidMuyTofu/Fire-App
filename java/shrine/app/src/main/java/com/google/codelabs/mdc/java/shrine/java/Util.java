package com.google.codelabs.mdc.java.shrine.java;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Util {

    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        return bos.toByteArray();
    }

    public static Bitmap getBitmap(byte[] bytes) {

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}