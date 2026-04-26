package com.atakmap.android.camfeed.source;

import com.atakmap.android.camfeed.model.CameraFeed;
import java.util.List;

/**
 * Common interface for all camera data sources.
 */
public interface CameraSource {

    /** Human-readable name shown in the UI. */
    String getName();

    /**
     * Fetch cameras near the given coordinates within the given radius.
     * Must be called from a background thread — implementations block.
     *
     * @param lat       Reference latitude
     * @param lon       Reference longitude
     * @param radiusKm  Search radius in kilometres
     * @param callback  Called on success or error (implementors MUST call exactly one)
     */
    void fetchCameras(double lat, double lon, int radiusKm, Callback callback);

    interface Callback {
        void onSuccess(List<CameraFeed> cameras);
        void onError(String errorMessage);
    }
}
