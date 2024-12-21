package kpan.bq_popup.mixin.client;

import dev.ftb.mods.ftbquests.api.QuestFile;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import java.util.Date;
import kpan.bq_popup.client.DisplayedPopup;
import kpan.bq_popup.client.OtherTeamToast;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TeamData.class)
public class TeamDataMixin {
    @Inject(at = @At("RETURN"), method = "setCompleted", remap = false)
    public void setCompleted(long id, Date time, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValueZ())
            return;
        TeamData teamData = (TeamData) (Object) this;
        if (teamData.getFile().isServerSide())
            return;
        boolean isSelf = ClientQuestFile.INSTANCE != null && teamData == ClientQuestFile.INSTANCE.selfTeamData;
        QuestObject object = teamData.getFile().get(id);
        String teamName = teamData.getName();
        if (!teamData.isCompleted(object)) {
            if (isSelf) {
                DisplayedPopup.remove(object);
            }
            return;
        }
        if (object instanceof Task task) {
            boolean displayPopup = !task.getQuest().getChapter().isAlwaysInvisible();// QuestObjectBase.sendNotifications.get(true)はサーバー限定なので取得不可
            boolean displayTaskToast = task.getQuest().getTasks().size() > 1 && !teamData.isCompleted(task.getQuest()) && !getDisableToast(task);
            if (displayTaskToast && displayPopup) {
                if (!isSelf)
                    MinecraftClient.getInstance().getToastManager().add(new OtherTeamToast(task, teamName));
            }
        } else if (object instanceof Quest quest) {
            boolean displayPopup = !quest.getChapter().isAlwaysInvisible();
            boolean displayQuestToast = !getDisableToast(quest);
            if (displayQuestToast && displayPopup) {
                if (isSelf)
                    DisplayedPopup.add(quest);
                else
                    MinecraftClient.getInstance().getToastManager().add(new OtherTeamToast(quest, teamName));
            } else {
                if (isSelf)
                    DisplayedPopup.add(quest);
            }
        } else if (object instanceof Chapter chapter) {
            boolean displayPopup = !chapter.isAlwaysInvisible();
            boolean displayChapterToast = !getDisableToast(chapter);
            if (displayChapterToast && displayPopup) {
                if (isSelf)
                    DisplayedPopup.add(chapter);
                else
                    MinecraftClient.getInstance().getToastManager().add(new OtherTeamToast(chapter, teamName));
            } else {
                if (isSelf)
                    DisplayedPopup.add(chapter);
            }
        } else if (object instanceof QuestFile file) {
            boolean displayPopup = true;// chapter.alwaysInvisibleの参照はちょっと無理そう
            boolean displayFileToast = !getDisableToast((QuestObject) file);
            if (displayFileToast && displayPopup) {
                if (isSelf)
                    DisplayedPopup.add((QuestObject) file);
                else
                    MinecraftClient.getInstance().getToastManager().add(new OtherTeamToast((QuestObject) file, teamName));
            } else {
                if (isSelf)
                    DisplayedPopup.add((QuestObject) file);
            }
        }
    }


    private static boolean getDisableToast(QuestObject questObject) {
        try {
            return (boolean) ReflectionUtil.getFieldValue(QuestObject.class.getDeclaredField("disableToast"), questObject);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}