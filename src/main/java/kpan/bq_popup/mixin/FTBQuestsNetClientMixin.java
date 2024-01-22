package kpan.bq_popup.mixin;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import kpan.bq_popup.client.DisplayedPopup;
import kpan.bq_popup.client.QuestCompletePopup;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBQuestsNetClient.class)
public class FTBQuestsNetClientMixin {
	/**
	 * @author
	 * @reason
	 */
	@Overwrite(remap = false)
	public void displayCompletionToast(long id) {
		QuestObject object = ClientQuestFile.INSTANCE.get(id);
		if (object != null) {
			if (object instanceof Task)
				MinecraftClient.getInstance().getToastManager().add(new ToastQuestObject(object));
			else
				QuestCompletePopup.add(object);
		}

		ClientQuestFile.INSTANCE.questScreen.questPanel.refreshWidgets();
		ClientQuestFile.INSTANCE.questScreen.chapterPanel.refreshWidgets();
		ClientQuestFile.INSTANCE.questScreen.viewQuestPanel.refreshWidgets();
	}

	@Inject(at = @At("RETURN"), method = "syncTeamData", remap = false)
	public void syncTeamData(boolean self, TeamData data, CallbackInfo ci) {
		DisplayedPopup.onLoad();
	}
}