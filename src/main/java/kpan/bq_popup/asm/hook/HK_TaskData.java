package kpan.bq_popup.asm.hook;

import com.feed_the_beast.ftbquests.client.ClientQuestFile;
import com.feed_the_beast.ftbquests.gui.ToastQuestObject;
import com.feed_the_beast.ftbquests.quest.ChangeProgress;
import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.QuestFile;
import com.feed_the_beast.ftbquests.quest.task.Task;
import com.feed_the_beast.ftbquests.quest.task.TaskData;
import com.feed_the_beast.ftbquests.util.ServerQuestData;
import kpan.bq_popup.client.DisplayedPopup;
import net.minecraft.client.Minecraft;
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
		if (taskData.data != ClientQuestFile.INSTANCE.self)
			return;
		Task task = taskData.task;
		Quest quest = task.quest;
		Chapter chapter = quest.chapter;
		if (!taskData.isComplete()) {
			DisplayedPopup.remove(quest);
			DisplayedPopup.remove(chapter);
			DisplayedPopup.remove(chapter.file);
			return;
		}
		boolean displayPopup = !(chapter.alwaysInvisible || quest.canRepeat || !ChangeProgress.sendNotifications.get(ChangeProgress.sendUpdates));
		boolean questComplete = quest.isComplete(taskData.data);
		boolean displayTaskToast = quest.tasks.size() > 1 && !questComplete && !task.disableToast;
		if (displayTaskToast && displayPopup) {
			Minecraft.getMinecraft().getToastGui().add(new ToastQuestObject(task));
		}
		if (!questComplete)
			return;
		boolean displayQuestToast = !quest.disableToast;
		if (displayQuestToast && displayPopup) {
			DisplayedPopup.display(quest);
		} else {
			DisplayedPopup.add(quest);
		}
		if (!chapter.isComplete(taskData.data))
			return;
		boolean displayChapterToast = !chapter.disableToast;
		if (displayChapterToast && displayPopup) {
			DisplayedPopup.display(chapter);
		} else {
			DisplayedPopup.add(chapter);
		}
		QuestFile file = chapter.file;
		if (!file.isComplete(taskData.data))
			return;
		boolean displayFileToast = !file.disableToast;
		if (displayFileToast && displayPopup) {
			DisplayedPopup.display(file);
		} else {
			DisplayedPopup.add(file);
		}
	}
}
