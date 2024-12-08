package kpan.bq_popup.mixin.client;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.FTBQuestsNetClient;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import kpan.bq_popup.DisplayedPopup;
import kpan.bq_popup.QuestCompletePopup;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FTBQuestsNetClient.class)
public class FTBQuestsNetClientMixin {
    /**
     * @author Katatsumuri.pan
     * @reason to show pop-up
     */
    @Overwrite(remap = false)
    public static void displayCompletionToast(long id) {
        QuestObject object = ClientQuestFile.INSTANCE.get(id);
        if (object != null) {
            if (object instanceof Task)
                MinecraftClient.getInstance().getToastManager().add(new ToastQuestObject(object));
            else
                QuestCompletePopup.add(object);
        }

        ClientQuestFile.INSTANCE.getQuestScreen().ifPresent(screen -> {
            screen.refreshQuestPanel();
            screen.refreshChapterPanel();
            screen.refreshViewQuestPanel();
        });
    }

    @Inject(at = @At("RETURN"), method = "syncTeamData", remap = false)
    private static void syncTeamData(boolean self, TeamData data, CallbackInfo ci) {
        DisplayedPopup.onLoad();
    }
}