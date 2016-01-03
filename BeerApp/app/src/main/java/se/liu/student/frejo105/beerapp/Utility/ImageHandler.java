package se.liu.student.frejo105.beerapp.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;

import cz.msebera.android.httpclient.client.HttpResponseException;
import se.liu.student.frejo105.beerapp.API.HttpClient;
import se.liu.student.frejo105.beerapp.API.RequestCompleteCallback;
import se.liu.student.frejo105.beerapp.R;

/**
 * Created by vakz on 2016-01-02.
 */
public class ImageHandler {
    /* http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html */
    private final static LruCache<String, Bitmap> memoryBitmapCache = new LruCache<String, Bitmap>((int)Runtime.getRuntime().maxMemory() / (1024*8)) {
        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount() / 1024;
        }
    };

    public static void getImage(Context context, String id, final RequestCompleteCallback<Bitmap> cb) {
        getImage(context, id, false, cb);
    }

    public static void getThumbnail(Context context, String id, final RequestCompleteCallback<Bitmap> cb)  {
        getImage(context, id, true, cb);
    }

    private static void getImage(Context context, String id, boolean isThumbnail, final RequestCompleteCallback<Bitmap> cb) {
        String filename = isThumbnail ? generateThumbnailName(id) : generateFileName(id);
        File file = new File(context.getCacheDir(), filename);
        if (inMemoryCache(filename)) {
            cb.onSuccess(memoryBitmapCache.get(filename));
        }
        else if(file.isFile()) {
            cb.onSuccess(fileToImage(context, filename, isThumbnail));
        }
        else {
            loadFromNetwork(context, id, isThumbnail, cb);
        }
    }

    private static boolean inMemoryCache(String id) {

        synchronized (memoryBitmapCache) { return memoryBitmapCache.get(id) != null; }
    }

    private static void loadFromNetwork(final Context context, final String id, final boolean isThumbnail, final RequestCompleteCallback<Bitmap> cb) {
        File file;
        String filename = isThumbnail ? generateThumbnailName(id) : generateFileName(id);
        try {
            file = new File(context.getCacheDir(), filename);
            file.createNewFile();
        }
        catch (java.io.IOException io) {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.image_fail)));
            return;
        }
        HttpClient.getImage(id, file, new RequestCompleteCallback<File>() {
            @Override
            public void onSuccess(File result) {
                cb.onSuccess(fileToImage(context, id, isThumbnail));
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                cb.onFailure(hre);
            }
        });
    }

    private static Bitmap fileToImage(Context context, String filename, boolean isThumbnail) {
        File file = new File(context.getCacheDir(), filename);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = calcDownsampling(file, context, isThumbnail);
        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        try {
            synchronized (memoryBitmapCache) { if (image != null) memoryBitmapCache.put(filename, image); }
        }
        catch (OutOfMemoryError e) {
            // Filled our cache memory allocation, just don't cache
        }

        return image;
    }

    private static int calcDownsampling(File file, Context context, boolean isThumbnail) {
        int downsampling = 1;
        int windowHeight = isThumbnail ? 300 : getWindowHeight(context);

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

    private static String generateThumbnailName(String id) {
        return String.format("%s_thumbnail.png", id);
    }

    private static String generateFileName(String id) {
        return String.format("%s.png", id);
    }
}
