package kpan.bq_popup;


import kpan.bq_popup.ModMain;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class SoundHandler {
    public static final Identifier QUEST_COMPLETE_ID = new Identifier(ModMain.MOD_ID, "quest_complete");
    public static final Identifier CHAPTER_COMPLETE_ID = new Identifier(ModMain.MOD_ID, "chapter_complete");
    public static final Identifier ALL_CHAPTERS_COMPLETE_ID = new Identifier(ModMain.MOD_ID, "all_chapters_complete");
    public static final SoundEvent QUEST_COMPLETE = createSoundEvent(QUEST_COMPLETE_ID);
    public static final SoundEvent CHAPTER_COMPLETE = createSoundEvent(CHAPTER_COMPLETE_ID);
    public static final SoundEvent ALL_CHAPTERS_COMPLETE = createSoundEvent(ALL_CHAPTERS_COMPLETE_ID);

    public static void init() {
        Registry.register(Registries.SOUND_EVENT, QUEST_COMPLETE_ID, QUEST_COMPLETE);
        Registry.register(Registries.SOUND_EVENT, CHAPTER_COMPLETE_ID, CHAPTER_COMPLETE);
        Registry.register(Registries.SOUND_EVENT, ALL_CHAPTERS_COMPLETE_ID, ALL_CHAPTERS_COMPLETE);
    }

    private static SoundEvent createSoundEvent(Identifier soundId) {
        return SoundEvent.of(soundId);
    }
}
