package com.optimalzero.plugin;

import android.content.Context;

import com.atak.plugins.impl.AbstractPluginTool;

import gov.tak.api.util.Disposable;

public class OptimalZeroTool extends AbstractPluginTool implements Disposable {

    public OptimalZeroTool(Context context) {
        super(context,
                context.getString(R.string.app_name),
                context.getString(R.string.app_desc),
                context.getResources().getDrawable(R.drawable.ic_plugin),
                OptimalZeroDropDown.SHOW_PLUGIN);
    }

    @Override
    public void dispose() {
    }
}
