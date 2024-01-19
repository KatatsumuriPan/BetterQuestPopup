package kpan.bq_popup;

import kpan.bq_popup.client.QuestCompletePopup;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public class ModMainClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
			QuestCompletePopup.render(matrixStack);
		});
		ClientTickEvents.END_CLIENT_TICK.register(QuestCompletePopup::ticking);
	}
}