package se.liu.student.frejo105.beerapp.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.LruCache;

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
        File file = new File(context.getCacheDir(), generateFileName(id));
        if (inMemoryCache(id)) {
            cb.onSuccess(memoryBitmapCache.get(id));
        }
        else if(file.isFile()) {
            cb.onSuccess(fileToImage(context, id));
        }
        else {
            loadFromNetwork(context, id, cb);
        }
    }

    private static boolean inMemoryCache(String id) {

        synchronized (memoryBitmapCache) { return memoryBitmapCache.get(id) != null; }
    }

    private static void loadFromNetwork(final Context context, final String id, final RequestCompleteCallback<Bitmap> cb) {
        File file;
        try {
            file = new File(context.getCacheDir(), generateFileName(id));
            file.createNewFile();
        }
        catch (java.io.IOException io) {
            cb.onFailure(new HttpResponseException(400, Resources.getSystem().getString(R.string.image_fail)));
            return;
        }
        HttpClient.getImage(id, file, new RequestCompleteCallback<File>() {
            @Override
            public void onSuccess(File result) {
                cb.onSuccess(fileToImage(context, id));
            }

            @Override
            public void onFailure(HttpResponseException hre) {
                cb.onFailure(hre);
            }
        });
    }

    private static Bitmap fileToImage(Context context, String id) {
        File file = new File(context.getCacheDir(), generateFileName(id));
        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
        synchronized (memoryBitmapCache) { if (image != null) memoryBitmapCache.put(id, image); }
        return image;
    }

    private static String generateFileName(String id) {
        return String.format("%s.png", id);
    }
}
