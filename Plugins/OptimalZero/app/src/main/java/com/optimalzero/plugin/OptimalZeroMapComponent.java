package com.optimalzero.plugin;

import android.content.Context;
import android.content.Intent;

import com.atakmap.android.dropdown.DropDownMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;

public class OptimalZeroMapComponent extends DropDownMapComponent {

    private OptimalZeroDropDown dropDownReceiver;

    @Override
    public void onCreate(Context context, Intent intent, MapView mapView) {
        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, mapView);
        dropDownReceiver = new OptimalZeroDropDown(mapView, context);
        AtakBroadcast.DocumentedIntentFilter filter =
                new AtakBroadcast.DocumentedIntentFilter();
        filter.addAction(OptimalZeroDropDown.SHOW_PLUGIN);
        registerDropDownReceiver(dropDownReceiver, filter);
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
