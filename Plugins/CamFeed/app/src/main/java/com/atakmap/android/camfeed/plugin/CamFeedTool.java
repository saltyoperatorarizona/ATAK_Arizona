package com.atakmap.android.camfeed.plugin;

import android.content.Context;

import com.atak.plugins.impl.AbstractPluginTool;
import com.atakmap.android.camfeed.CamFeedDropDownReceiver;
import com.atakmap.android.camfeed.R;

import gov.tak.api.util.Disposable;

public class CamFeedTool extends AbstractPluginTool implements Disposable {

    public CamFeedTool(Context context) {
        super(context,
            context.getString(R.string.app_name),
            context.getString(R.string.app_name),
            context.getResources().getDrawable(R.drawable.ic_plugin),
            CamFeedDropDownReceiver.SHOW_PLUGIN);
    }

    @Override
    public void dispose() {}
}
