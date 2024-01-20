package kpan.bq_popup.asm.hook;

import com.feed_the_beast.ftbquests.client.ClientQuestFile;
import com.feed_the_beast.ftbquests.quest.ChangeProgress;
import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.QuestFile;
import com.feed_the_beast.ftbquests.quest.task.Task;
import com.feed_the_beast.ftbquests.quest.task.TaskData;
import com.feed_the_beast.ftbquests.util.ServerQuestData;
import kpan.bq_popup.client.AdvancedToastQuestObject;
import kpan.bq_popup.client.DisplayedPopup;
import kpan.bq_popup.client.OtherTeamToast;
import kpan.bq_popup.config.ConfigHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class HK_TaskData {

	public static void onSetProgress(TaskData<?> taskData) {
		if (taskData.data instanceof ServerQuestData)
			return;
		onSetProgressClient(taskData);
	}

	@SideOnly(Side.CLIENT)
	private static void onSetProgressClient(TaskData<?> taskData) {
		boolean isSelf = taskData.data == ClientQuestFile.INSTANCE.self;
		if (!ConfigHolder.client.showOtherTeamTask && !isSelf)
			return;
		Task task = taskData.task;
		Quest quest = task.quest;
		Chapter chapter = quest.chapter;
		if (!taskData.isComplete()) {
			if (isSelf) {
				DisplayedPopup.remove(quest);
				DisplayedPopup.remove(chapter);
				DisplayedPopup.remove(chapter.file);
			}
			return;
		}
		boolean displayPopup = !(chapter.alwaysInvisible || quest.canRepeat || !ChangeProgress.sendNotifications.get(ChangeProgress.sendUpdates));
		boolean questComplete = quest.isComplete(taskData.data);
		boolean displayTaskToast = quest.tasks.size() > 1 && !questComplete && !task.disableToast;
		String teamName = taskData.data.getDisplayName().getFormattedText();

		if (displayTaskToast && displayPopup) {
			if (isSelf)
				AdvancedToastQuestObject.addToast(task);
			else
				AdvancedToastQuestObject.addToast(new OtherTeamToast(task, teamName));
		}
		if (!questComplete)
			return;
		boolean displayQuestToast = !quest.disableToast;
		if (displayQuestToast && displayPopup) {
			if (isSelf)
				DisplayedPopup.display(quest);
			else
				AdvancedToastQuestObject.addToast(new OtherTeamToast(quest, teamName));
		} else {
			if (isSelf)
				DisplayedPopup.add(quest);
		}
		if (!chapter.isComplete(taskData.data))
			return;
		boolean displayChapterToast = !chapter.disableToast;
		if (displayChapterToast && displayPopup) {
			if (isSelf)
				DisplayedPopup.display(chapter);
			else
				AdvancedToastQuestObject.addToast(new OtherTeamToast(chapter, teamName));
		} else {
			if (isSelf)
				DisplayedPopup.add(chapter);
		}
		QuestFile file = chapter.file;
		if (!file.isComplete(taskData.data))
			return;
		boolean displayFileToast = !file.disableToast;
		if (displayFileToast && displayPopup) {
			if (isSelf)
				DisplayedPopup.display(file);
			else
				AdvancedToastQuestObject.addToast(new OtherTeamToast(file, teamName));
		} else {
			if (isSelf)
				DisplayedPopup.add(file);
		}
	}
}
