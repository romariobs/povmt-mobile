package com.les.povmt.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by felipe on 12/11/16.
 */

public class ImageUtils {
    private static final int IMAGE_WIDTH = 500;
    private static final int IMAGE_HEIGHT = IMAGE_WIDTH;

    public static void getBitmap (String pathOfInputImage, String pathOfOutputImage) {
        try
        {
            int inWidth = 0;
            int inHeight = 0;

            InputStream in = new FileInputStream(pathOfInputImage);

            // decode image size (decode metadata only, not the whole image)
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            in = null;

            // save width and height
            inWidth = options.outWidth;
            inHeight = options.outHeight;

            int dstWidth = IMAGE_WIDTH;
            int dstHeight = IMAGE_HEIGHT;

            // decode full image pre-resized
            in = new FileInputStream(pathOfInputImage);
            options = new BitmapFactory.Options();
            // calc rought re-size (this is no exact resize)
            options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
            // decode full image
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            // calc exact destination size
            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);

            // resize bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);

            // save image
            try
            {
                FileOutputStream out = new FileOutputStream(pathOfOutputImage);
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
            }
            catch (Exception e)
            {
                Log.e("Image", e.getMessage(), e);
            }
        }
        catch (IOException e)
        {
            Log.e("Image", e.getMessage(), e);
        }
    }

    public static boolean deleteFile (String path) {
        File file = new File(path);

        if (file.exists()) {
            return file.delete();
        }

        return  false;
    }
}
