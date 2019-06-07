package wizardofba.rezeptverwaltung.Utility;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.IOException;

import wizardofba.rezeptverwaltung.MainActivity;

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
        try {
            return MediaStore.Images.Media.getBitmap(MainActivity.getManager().getMainactivityContext().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
