package se.liu.student.frejo105.beerapp.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.WindowManager;

import java.io.File;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.api.HttpCallback;
import se.liu.student.frejo105.beerapp.api.HttpClient;
import se.liu.student.frejo105.beerapp.R;

/**
 * Created by vakz on 2016-06-20.
 */
public class ImageHandler {
    /* http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html */
    private final static LruCache<String, Bitmap> memoryBitmapCache = new LruCache<String, Bitmap>((int)Runtime.getRuntime().maxMemory() / (1024*8)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount() / 1024;
        }
    };

    public static void getImage(Context context, int id, final HttpCallback<Bitmap> cb) {
        if (id < 0) {
            cb.onFailure(new HttpResponseException(400, context.getResources().getString(R.string.invalid_id)));
            return;
        }
        String filename = generateFileName(id);
        // Check if we have the image already loaded into memory
        if (inMemoryCache(filename)) {
            cb.onSuccess(memoryBitmapCache.get(filename));
            return;
        }

        File file = new File(context.getCacheDir(), filename);
        if (file.isFile()) {
            cb.onSuccess(fileToImage(context, filename));
        } else {
            loadFromNetwork(context, id, cb);
        }
    }

    private static void loadFromNetwork(final Context context, final int id, final HttpCallback<Bitmap> cb) {
        File file;
        try {
            file = new File(context.getCacheDir(), generateFileName(id));
            file.createNewFile();
        }
        catch (java.io.IOException io) {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.image_fail)));
            return;
        }
        HttpClient.getImage(id, file, new HttpCallback<File>() {
            @Override
            public void onSuccess(File result) {
                cb.onSuccess(fileToImage(context, generateFileName(id)));
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                cb.onFailure(hre);
            }
        });
    }

    private static Bitmap fileToImage(Context context, String filename) {
        File file = new File(context.getCacheDir(), filename);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = calcDownsampling(file, context);
        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        try {
            synchronized (memoryBitmapCache) { if (image != null) memoryBitmapCache.put(filename, image); }
        }
        catch (OutOfMemoryError e) {
            // Filled our cache memory allocation, just don't cache
        }

        return image;
    }

    private static int calcDownsampling(File file, Context context) {
        int downsampling = 1;
        int windowHeight = getWindowHeight(context);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int height = options.outHeight;
        while (height / (downsampling*2) > windowHeight) {
            downsampling *= 2;
        }
        return downsampling;
    }

    private static int getWindowHeight(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager w = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        w.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    private static boolean inMemoryCache(String id) {
        synchronized (memoryBitmapCache) { return memoryBitmapCache.get(id) != null; }
    }

    private static String generateFileName(int id) {
        return String.format("%s.jpg", id);
    }
}
