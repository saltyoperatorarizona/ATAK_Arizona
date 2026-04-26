package com.atakmap.android.camfeed.plugin;

import com.atak.plugins.impl.AbstractPlugin;
import com.atak.plugins.impl.PluginContextProvider;
import com.atakmap.android.camfeed.CamFeedMapComponent;

import gov.tak.api.plugin.IServiceController;

public class CamFeedPlugin extends AbstractPlugin {

    public CamFeedPlugin(IServiceController serviceController) {
        super(serviceController,
            new CamFeedTool(serviceController
                .getService(PluginContextProvider.class)
                .getPluginContext()),
            new CamFeedMapComponent());
    }
}
