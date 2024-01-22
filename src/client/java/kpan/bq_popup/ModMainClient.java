package kpan.bq_popup;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ModMainClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // This entrypoint is suitable for setting up client-specific logic, such as rendering.
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            QuestCompletePopup.render(drawContext);
        });
        ClientTickEvents.END_CLIENT_TICK.register(QuestCompletePopup::ticking);
    }
}