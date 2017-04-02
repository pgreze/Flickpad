package fr.pgreze.flickpad.domain.photo;

import android.content.Context;
import android.graphics.Bitmap;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import fr.pgreze.flickpad.domain.model.Photo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ImageWriter {

    public static final String IMAGES_FOLDER_NAME = "images";

    private final Context context;
    private final Picasso picasso;

    @Inject
    public ImageWriter(Context context, Picasso picasso) {
        this.context = context;
        this.picasso = picasso;
    }

    public Observable<File> write(Photo photo) {
        return write(photo.mediumUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<File> write(String url) {
        return Observable.fromCallable(() -> {
            // Create directory
            File parent = new File(context.getFilesDir(), IMAGES_FOLDER_NAME);
            if (!parent.isDirectory() && !parent.mkdir()) {
                throw new RuntimeException("Impossible to create images folder");
            }

            // Get image and keep aspect ratio with dimen max = 250
            Bitmap bitmap = picasso.load(url).resize(250, 250).centerInside().get();
            // Write image
            File file = new File(parent, String.valueOf(System.currentTimeMillis()) + ".jpeg");
            writeBitmap(bitmap, file, Bitmap.CompressFormat.JPEG);

            return file;
        });
    }

    /**
     * @param bitmap image to store
     * @param file written file path
     * @param format
     */
    private File writeBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format) {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            // Note: compression is ignored by PNG format
            bitmap.compress(format, 100, out);
        } catch (Exception e) {
            throw new RuntimeException("Store bitmap failed: " + e, e);
        } finally {
            try { if (out != null) out.close(); } catch (IOException e) {}
        }

        return file;
    }
}
