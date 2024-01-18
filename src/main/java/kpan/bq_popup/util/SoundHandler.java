package kpan.bq_popup.util;

import kpan.bq_popup.ModTagsGenerated;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SoundHandler {
	public static final SoundEvent QUEST_COMPLETE = createSoundEvent(new ResourceLocation(ModTagsGenerated.MODID, "quest_complete"));
	public static final SoundEvent CHAPTER_COMPLETE = createSoundEvent(new ResourceLocation(ModTagsGenerated.MODID, "chapter_complete"));
	public static final SoundEvent ALL_CHAPTERS_COMPLETE = createSoundEvent(new ResourceLocation(ModTagsGenerated.MODID, "all_chapters_complete"));

	public static void init(IForgeRegistry<SoundEvent> registry) {
		registry.register(QUEST_COMPLETE);
		registry.register(CHAPTER_COMPLETE);
		registry.register(ALL_CHAPTERS_COMPLETE);
	}

	private static SoundEvent createSoundEvent(ResourceLocation soundId) {
		return new SoundEvent(soundId).setRegistryName(soundId);
	}
}
