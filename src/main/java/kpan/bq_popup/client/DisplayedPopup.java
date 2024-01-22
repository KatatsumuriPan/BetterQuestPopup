package kpan.bq_popup.client;

import com.feed_the_beast.ftbquests.client.ClientQuestFile;
import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.QuestObject;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import kpan.bq_popup.ModMain;
import kpan.bq_popup.ModTagsGenerated;
import kpan.bq_popup.config.ConfigHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.common.config.Configuration.UnicodeInputStreamReader;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class DisplayedPopup {
	public static IntSet popupDisplayed = new IntOpenHashSet();
	private static final File cacheDir = new File(new File(new File(Minecraft.getMinecraft().gameDir, "config"), ModTagsGenerated.MODID), "completed-cache");

	public static void add(QuestObject object) {
		popupDisplayed.add(object.id);
		writeToFile();
	}
	public static void remove(QuestObject object) {
		popupDisplayed.remove(object.id);
		writeToFile();
	}

	public static void display(QuestObject object) {
		if (popupDisplayed.contains(object.id)) {
			if (object instanceof Quest)
				AdvancedToastQuestObject.addToast(object);
		} else {
			if (ConfigHolder.client.showPopup)
				QuestCompletePopup.add(object);
			else
				AdvancedToastQuestObject.addToast(object);
			add(object);
		}
	}

	public static void onLoad() {
		ClientQuestFile questFile = ClientQuestFile.INSTANCE;
		readFromFile();
		if (!popupDisplayed.isEmpty() && ConfigHolder.client.showAbsentCompletedTask) {
			for (Chapter chapter : questFile.chapters) {
				for (Quest quest : chapter.quests) {
					if (quest.isComplete(questFile.self) && !popupDisplayed.contains(quest.id)) {
						if (ConfigHolder.client.showPopup)
							QuestCompletePopup.add(quest);
						else
							AdvancedToastQuestObject.addToast(quest);
					}
				}
				if (chapter.isComplete(questFile.self) && !popupDisplayed.contains(chapter.id)) {
					if (ConfigHolder.client.showPopup)
						QuestCompletePopup.add(chapter);
					else
						AdvancedToastQuestObject.addToast(chapter);
				}
			}
			if (questFile.isComplete(questFile.self) && !popupDisplayed.contains(questFile.id)) {
				if (ConfigHolder.client.showPopup)
					QuestCompletePopup.add(questFile);
				else
					AdvancedToastQuestObject.addToast(questFile);
			}
		}
		sync(questFile);
	}

	public static void sync(ClientQuestFile questFile) {
		popupDisplayed.clear();
		for (Chapter chapter : questFile.chapters) {
			for (Quest quest : chapter.quests) {
				if (quest.isComplete(questFile.self))
					popupDisplayed.add(quest.id);
			}
			if (chapter.isComplete(questFile.self))
				popupDisplayed.add(chapter.id);
		}
		if (questFile.isComplete(questFile.self))
			popupDisplayed.add(questFile.id);
		writeToFile();
	}

	private static void readFromFile() {
		popupDisplayed.clear();
		ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
		if (currentServerData == null)
			return;
		try {
			if (!cacheDir.exists())
				return;
			UnicodeInputStreamReader input = new UnicodeInputStreamReader(new FileInputStream(new File(cacheDir, sanitizeFileName(currentServerData.serverIP))), "UTF-8");
			BufferedReader buffer = new BufferedReader(input);
			while (true) {
				String line = buffer.readLine();
				if (line == null)
					break;
				if (line.isEmpty())
					continue;
				popupDisplayed.add(Integer.parseInt(line));
			}
		} catch (IOException | NumberFormatException e) {
			ModMain.LOGGER.error("Error while reading the cache.", e);
		}
	}
	private static void writeToFile() {
		ServerData currentServerData = Minecraft.getMinecraft().getCurrentServerData();
		if (currentServerData != null) {
			File file = new File(cacheDir, sanitizeFileName(currentServerData.serverIP));
			try {
				if (file.getParentFile() != null)
					file.getParentFile().mkdirs();

				if (!file.exists() && !file.createNewFile()) {
					ModMain.LOGGER.error("Cannot create a cache");
					return;
				}

				if (file.canWrite()) {
					FileOutputStream fos = new FileOutputStream(file);
					BufferedWriter buffer = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8));

					for (Integer id : popupDisplayed) {
						buffer.write(id + "\n");
					}

					buffer.close();
					fos.close();
				} else {
					ModMain.LOGGER.error("Cannot write the cache file");
				}
			} catch (IOException e) {
				ModMain.LOGGER.error("Error while saving the cache.", e);
			}
		}
	}

	private static String sanitizeFileName(String name) {
		return StringUtils.replaceAll(name, "[/\\:*?\"<>|]", "-");
	}
}
