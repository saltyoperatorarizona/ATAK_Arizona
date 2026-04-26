package com.atakmap.android.camfeed;

import android.content.Context;
import android.content.Intent;

import com.atakmap.android.dropdown.DropDownMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;

public class CamFeedMapComponent extends DropDownMapComponent {

    private CamFeedDropDownReceiver dropDownReceiver;

    @Override
    public void onCreate(Context context, Intent intent, MapView mapView) {
        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, mapView);

        dropDownReceiver = new CamFeedDropDownReceiver(mapView, context);

        AtakBroadcast.DocumentedIntentFilter filter =
            new AtakBroadcast.DocumentedIntentFilter();
        filter.addAction(CamFeedDropDownReceiver.SHOW_PLUGIN);
        this.registerDropDownReceiver(dropDownReceiver, filter);
    }

    @Override
    protected void onDestroyImpl(Context context, MapView mapView) {
        if (dropDownReceiver != null) {
            dropDownReceiver.dispose();
            dropDownReceiver = null;
        }
        super.onDestroyImpl(context, mapView);
    }
}
