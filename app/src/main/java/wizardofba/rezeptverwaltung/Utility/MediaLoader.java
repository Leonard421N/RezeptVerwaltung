package wizardofba.rezeptverwaltung.Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;

import java.io.ByteArrayOutputStream;

public class MediaLoader {

    private static MediaLoader instance;

    private MediaLoader() {
    }

    public static synchronized MediaLoader getInstance() {
        if(MediaLoader.instance == null) {
            MediaLoader.instance = new MediaLoader();
        }
        return MediaLoader.instance;
    }

    @Nullable
    public Bitmap loadBitmapFromUri(Uri uri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 20;
        Bitmap bmpSample = BitmapFactory.decodeFile(uri.getPath(), options);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bmpSample.compress(Bitmap.CompressFormat.JPEG, 1, out);

        return bmpSample;
    }
}
