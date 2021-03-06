package org.xdty.gallery.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.data.DataFetcher;

import org.xdty.gallery.model.Media;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MediaDataFetcher implements DataFetcher<InputStream> {

    private static final String TAG = MediaDataFetcher.class.getSimpleName();
    private final Media mediaFile;
    private final Context context;
    private InputStream data;

    public MediaDataFetcher(Context context, Media mediaFile) {
        this.context = context;
        this.mediaFile = mediaFile;
    }

    @SuppressWarnings("unchecked")
    @Override
    public InputStream loadData(Priority priority) throws Exception {
        Log.e(TAG, "loading: " + mediaFile.getUri());
        if (mediaFile.isImage()) {
            data = mediaFile.getInputStream();
        } else {
            List<Media> children = mediaFile.children();
            for (Media media : children) {
                if (media.isImage()) {
                    data = media.getInputStream();
                    break;
                }
            }
        }
        return data;
    }

    @Override
    public void cleanup() {
        Log.d(TAG, "cleanup: " + mediaFile.getUri());
        if (data != null) {
            try {
                data.close();
            } catch (IOException e) {
                if (Log.isLoggable(TAG, Log.VERBOSE)) {
                    Log.v(TAG, "failed to close data", e);
                }
            }
        }
    }

    @Override
    public String getId() {
        Log.d(TAG, "getId: " + mediaFile.getUri());
        return mediaFile.getUri();
    }

    @Override
    public void cancel() {
        Log.d(TAG, "cancel: " + mediaFile.getUri());
    }
}
