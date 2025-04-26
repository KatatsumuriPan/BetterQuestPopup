package kpan.bq_popup.forge.config;

import kpan.bq_popup.ModMain;
import kpan.bq_popup.config.IModConfigHolder;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfigHandlerForge implements IModConfigHolder {

    public static final ModConfigHandlerForge INSTANCE;
    public static final ForgeConfigSpec FORGE_CONFIG_SPEC;

    static {
        Pair<ModConfigHandlerForge, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder()
                .configure(ModConfigHandlerForge::new);
        INSTANCE = pair.getLeft();
        FORGE_CONFIG_SPEC = pair.getRight();
    }

    public static void init(FMLJavaModLoadingContext fmlJavaModLoadingContext) {
        fmlJavaModLoadingContext.registerConfig(Type.CLIENT, ModConfigHandlerForge.FORGE_CONFIG_SPEC);
        ModMain.INSTANCE = INSTANCE;
    }

    public IntValue iconSizePx;
    public ConfigValue<String> centerX;
    public ConfigValue<String> centerY;

    private ModConfigHandlerForge(ForgeConfigSpec.Builder builder) {
        iconSizePx = builder.comment("Icon size of displayed pop-up item in pixels.\nDefault: 16").defineInRange("iconSizePx", 16, 0, Integer.MAX_VALUE);
        centerX = builder.comment("Center x of displayed pop-up.\nUnit:\n  %: Screen width in percentage.\n  @: Icon size in percentage.\n  (none): Pixels.\nOperation: +, -\nDefault: \"50%\"").define("centerX", "50%");
        centerY = builder.comment("Center y of displayed pop-up.\nUnit:\n  %: Screen height in percentage.\n  @: Icon size in percentage.\n  (none): Pixels.\nOperation: +, -\nDefault: \"33.3% + 100@ + 1\"").define("centerY", "33.3% + 100@ + 1");
    }

    @Override
    public int getIconSizePx() {
        return iconSizePx.get();
    }
    @Override
    public String getCenterX() {
        return centerX.get();
    }
    @Override
    public String getCenterY() {
        return centerY.get();
    }
}
