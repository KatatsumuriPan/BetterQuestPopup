package kpan.bq_popup.asm.hook;

import com.feed_the_beast.ftbquests.client.ClientQuestFile;
import com.feed_the_beast.ftbquests.gui.ToastQuestObject;
import com.feed_the_beast.ftbquests.net.MessageDisplayCompletionToast;
import com.feed_the_beast.ftbquests.quest.QuestObject;
import com.feed_the_beast.ftbquests.quest.task.Task;
import kpan.bq_popup.QuestCompletePopup;
import net.minecraft.client.Minecraft;

public class HK_MessageDisplayCompletionToast {

	public static void onMessage(MessageDisplayCompletionToast self) {
		if (ClientQuestFile.exists()) {
			QuestObject object = ClientQuestFile.INSTANCE.get(self.id);
			if (object != null) {
				if (object instanceof Task)
					Minecraft.getMinecraft().getToastGui().add(new ToastQuestObject(object));
				else
					QuestCompletePopup.add(object);
			}

			ClientQuestFile.INSTANCE.questTreeGui.questPanel.refreshWidgets();
			ClientQuestFile.INSTANCE.questTreeGui.chapterPanel.refreshWidgets();
			ClientQuestFile.INSTANCE.questTreeGui.viewQuestPanel.refreshWidgets();
		}

	}
}
