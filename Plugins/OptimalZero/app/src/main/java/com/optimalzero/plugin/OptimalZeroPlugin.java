package com.optimalzero.plugin;

import com.atak.plugins.impl.AbstractPlugin;
import com.atak.plugins.impl.PluginContextProvider;
import gov.tak.api.plugin.IServiceController;

public class OptimalZeroPlugin extends AbstractPlugin {

    public OptimalZeroPlugin(IServiceController iServiceController) {
        super(iServiceController,
                new OptimalZeroTool(iServiceController
                        .getService(PluginContextProvider.class)
                        .getPluginContext()),
                new OptimalZeroMapComponent());
    }
}
