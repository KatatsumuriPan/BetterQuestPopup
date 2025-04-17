package kpan.bq_popup;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import kpan.bq_popup.client.QuestCompletePopup;
import kpan.bq_popup.config.IModConfigHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ModMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(ModReference.MOD_ID);
    public static IModConfigHolder INSTANCE; // 各ModLoaderのハンドラが設定するのでfinalでない

    public static void init() {
        ClientGuiEvent.RENDER_HUD.register((drawContext, delta) -> {
            QuestCompletePopup.render(drawContext);
        });
        ClientTickEvent.CLIENT_POST.register(QuestCompletePopup::ticking);
    }
}
