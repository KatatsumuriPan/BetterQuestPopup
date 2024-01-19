package kpan.bq_popup.mixin;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestFile;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import dev.ftb.mods.ftbquests.quest.task.Task;
import kpan.bq_popup.client.DisplayedPopup;
import kpan.bq_popup.client.OtherTeamToast;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Date;

@Mixin(TeamData.class)
public class TeamDataMixin {
	@Inject(at = @At("RETURN"), method = "setProgress", remap = false)
	public void onLoad(Task task, long progress, CallbackInfo ci) {
		TeamData teamData = (TeamData) (Object) this;
	}

	@Inject(at = @At("RETURN"), method = "setCompleted", remap = false)
	public void setCompleted(long id, Date time, CallbackInfoReturnable<Boolean> cir) {
		if (!cir.getReturnValueZ())
			return;
		TeamData teamData = (TeamData) (Object) this;
		if (teamData.file.isServerSide())
			return;
		boolean isSelf = ClientQuestFile.INSTANCE != null && teamData == ClientQuestFile.INSTANCE.self;
		QuestObject object = teamData.file.get(id);
		String teamName = teamData.name;
		if (!teamData.isCompleted(object)) {
			if (isSelf) {
				DisplayedPopup.remove(object);
			}
			return;
		}
		if (object instanceof Task task) {
			boolean displayPopup = !task.quest.chapter.alwaysInvisible;//QuestObjectBase.sendNotifications.get(true)はサーバー限定なので取得不可
			boolean displayTaskToast = task.quest.tasks.size() > 1 && !teamData.isCompleted(task.quest) && !task.disableToast;
			if (displayTaskToast && displayPopup) {
				if (!isSelf)
					MinecraftClient.getInstance().getToastManager().add(new OtherTeamToast(task, teamName));
			}
		} else if (object instanceof Quest quest) {
			boolean displayPopup = !quest.chapter.alwaysInvisible;
			boolean displayQuestToast = !quest.disableToast;
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
			boolean displayPopup = !chapter.alwaysInvisible;
			boolean displayChapterToast = !chapter.disableToast;
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
			boolean displayPopup = true;//chapter.alwaysInvisibleの参照はちょっと無理そう
			boolean displayFileToast = !file.disableToast;
			if (displayFileToast && displayPopup) {
				if (isSelf)
					DisplayedPopup.add(file);
				else
					MinecraftClient.getInstance().getToastManager().add(new OtherTeamToast(file, teamName));
			} else {
				if (isSelf)
					DisplayedPopup.add(file);
			}
		}
	}
}