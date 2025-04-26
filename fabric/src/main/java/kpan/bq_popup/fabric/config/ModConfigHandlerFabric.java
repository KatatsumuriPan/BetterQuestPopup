package kpan.bq_popup.fabric.config;

import kpan.bq_popup.ModMain;
import kpan.bq_popup.ModReference;
import kpan.bq_popup.client.PositionExpression;
import kpan.bq_popup.config.IModConfigHolder;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = ModReference.MOD_ID)
public class ModConfigHandlerFabric implements ConfigData, IModConfigHolder { // ModList.get().isLoaded("modid"); forge用のmodid判定

    public static void init() {
        AutoConfig.register(ModConfigHandlerFabric.class, GsonConfigSerializer::new);
        ModMain.INSTANCE = AutoConfig.getConfigHolder(ModConfigHandlerFabric.class).getConfig();
    }

    // 翻訳ができるTooltipを使った方が良い
    @Comment("Icon size of displayed pop-up item in pixels.\nDefault: 16")
    private int iconSizePx = 16;
    @Comment("Center x of displayed pop-up.\nUnit:\n  %: Screen width in percentage.\n  (none): Pixels.\nOperation: +, -\nDefault: 50%")
    private String centerX = "50%";
    @Comment("Center y of displayed pop-up.\nUnit:\n  %: Screen height in percentage.\n  (none): Pixels.\nOperation: +, -\nDefault: 33.3% + 16 + 1")
    private String centerY = "33.3% + 16 + 1";

    @Override
    public void validatePostLoad() {
        iconSizePx = Math.max(iconSizePx, 0);
        if (!PositionExpression.canParse(centerX))
            centerX = "0"; // ミスってることが分かりやすいように0とする
        if (!PositionExpression.canParse(centerY))
            centerY = "0";
    }

    @Override
    public int getIconSizePx() {
        return iconSizePx;
    }
    @Override
    public String getCenterX() {
        return centerX;
    }
    @Override
    public String getCenterY() {
        return centerY;
    }
}
