package kpan.bq_popup.client;

import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.client.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.ChapterGroup;
import dev.ftb.mods.ftbquests.quest.Quest;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import dev.ftb.mods.ftbquests.quest.TeamData;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import kpan.bq_popup.ModMain;
import kpan.bq_popup.ModReference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.apache.commons.lang3.StringUtils;

public class DisplayedPopup {
    public static LongSet popupDisplayed = new LongOpenHashSet();
    private static final File cacheDir = new File(new File(MinecraftClient.getInstance().runDirectory, ModReference.MOD_ID), "completed-cache");

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
                MinecraftClient.getInstance().getToastManager().add(new ToastQuestObject(object));
        } else {
            QuestCompletePopup.add(object);
            add(object);
        }
    }

    public static void onLoad() {
        ClientQuestFile questFile = ClientQuestFile.INSTANCE;
        readFromFile();
        if (!popupDisplayed.isEmpty()) {
            TeamData teamData = questFile.selfTeamData;
            for (ChapterGroup chapterGroup : questFile.getChapterGroups()) {
                for (Chapter chapter : chapterGroup.getChapters()) {
                    for (Quest quest : chapter.getQuests()) {
                        if (teamData.isCompleted(quest) && !popupDisplayed.contains(quest.id))
                            QuestCompletePopup.add(quest);
                    }
                    if (teamData.isCompleted(chapter) && !popupDisplayed.contains(chapter.id))
                        QuestCompletePopup.add(chapter);
                }
            }
            if (teamData.isCompleted(questFile) && !popupDisplayed.contains(questFile.id))
                QuestCompletePopup.add(questFile);
        }
        sync(questFile);
    }

    public static void sync(ClientQuestFile questFile) {
        popupDisplayed.clear();
        TeamData teamData = questFile.selfTeamData;
        for (ChapterGroup chapterGroup : questFile.getChapterGroups()) {
            for (Chapter chapter : chapterGroup.getChapters()) {
                for (Quest quest : chapter.getQuests()) {
                    if (teamData.isCompleted(quest))
                        popupDisplayed.add(quest.id);
                }
                if (teamData.isCompleted(chapter))
                    popupDisplayed.add(chapter.id);
            }
        }
        if (teamData.isCompleted(questFile))
            popupDisplayed.add(questFile.id);
        writeToFile();
    }

    private static void readFromFile() {
        popupDisplayed.clear();
        ServerInfo currentServerEntry = MinecraftClient.getInstance().getCurrentServerEntry();
        if (currentServerEntry == null)
            return;
        try {
            if (!cacheDir.exists())
                return;
            for (String line : Files.readAllLines(new File(cacheDir, sanitizeFileName(currentServerEntry.address)).toPath(), StandardCharsets.UTF_8)) {
                if (line.isEmpty())
                    continue;
                popupDisplayed.add(Long.parseLong(line));
            }
        } catch (IOException | NumberFormatException e) {
            ModMain.LOGGER.error("Error while reading the cache.", e);
        }
    }
    private static void writeToFile() {
        ServerInfo currentServerEntry = MinecraftClient.getInstance().getCurrentServerEntry();
        if (currentServerEntry != null) {
            File file = new File(cacheDir, sanitizeFileName(currentServerEntry.address));
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

                    for (Long id : popupDisplayed) {
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