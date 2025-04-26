package kpan.bq_popup.fabric;

import kpan.bq_popup.ModMain;
import kpan.bq_popup.fabric.config.ModConfigHandlerFabric;
import net.fabricmc.api.ModInitializer;

public final class ModMainFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Configをロード
        ModConfigHandlerFabric.init();

        // Run our common setup.
        ModMain.init();
    }
}
