package kpan.bq_popup.forge;

import dev.architectury.platform.forge.EventBuses;
import kpan.bq_popup.ModMain;
import kpan.bq_popup.ModReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ModReference.MOD_ID)
public final class ExampleModForge {
    public ExampleModForge(FMLJavaModLoadingContext fmlJavaModLoadingContext) {
        // Submit our event bus to let Architectury API register our content on the right time.
        EventBuses.registerModEventBus(ModReference.MOD_ID, fmlJavaModLoadingContext.getModEventBus());

        // Run our common setup.
        ModMain.init();
    }
}
