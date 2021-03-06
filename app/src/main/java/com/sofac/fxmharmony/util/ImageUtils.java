package com.sofac.fxmharmony.util;


import android.graphics.Bitmap;


public class ImageUtils {

    public static Bitmap scaleDownImage(Bitmap realImage, float maxImageSize,
                                        boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}