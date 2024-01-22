package kpan.bq_popup.client;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftbquests.client.ClientQuestFile;
import dev.ftb.mods.ftbquests.quest.Chapter;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.Queue;

public class QuestCompletePopup {
	private static final PositionedSoundInstance QUEST_COMPLETE = PositionedSoundInstance.master(SoundHandler.QUEST_COMPLETE, 1);
	private static final PositionedSoundInstance CHAPTER_COMPLETE = PositionedSoundInstance.master(SoundHandler.CHAPTER_COMPLETE, 1);
	private static final PositionedSoundInstance ALL_CHAPTERS_COMPLETE = PositionedSoundInstance.master(SoundHandler.ALL_CHAPTERS_COMPLETE, 1);
	private final QuestObject object;
	private int tick = 0;
	private QuestCompletePopup(QuestObject object) { this.object = object; }

	private boolean tick() {
		if (tick >= 100)
			return true;
		MinecraftClient mc = MinecraftClient.getInstance();
		if (tick == 0) {
			if (object instanceof Chapter)
				mc.getSoundManager().play(CHAPTER_COMPLETE);
			else if (object instanceof ClientQuestFile)
				mc.getSoundManager().play(ALL_CHAPTERS_COMPLETE);
			else
				mc.getSoundManager().play(QUEST_COMPLETE);
		}
		tick++;
		return false;
	}
	private void render1(MatrixStack matrixStack) {
		MinecraftClient mc = MinecraftClient.getInstance();
		var res = mc.getWindow();
		int w = res.getScaledWidth();
		int h = res.getScaledHeight();
		int y = h / 3;
		float alphaf;
		if (tick < 20)
			alphaf = tick / 20f;
		else if (tick < 60)
			alphaf = 1;
		else
			alphaf = 1 - (tick - 60) / 40f;
		int alpha = Math.max((int) (alphaf * 255), 16);
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
		object.getIcon().draw(matrixStack, w / 2 - 8, y, 16, 16);
		y += 16 + 2;

		Text text = new TranslatableText(object.getObjectType().translationKey + ".completed").formatted(Formatting.BOLD, Formatting.UNDERLINE);
		int color;
		if (object instanceof Chapter)
			color = 0xFF88FF;
		else if (object instanceof ClientQuestFile)
			color = 0x88FF88;
		else
			color = 0xFFFF00;
		mc.textRenderer.drawWithShadow(matrixStack, text, w / 2f - mc.textRenderer.getWidth(text) / 2f, y, color | (alpha << 24));
		y += mc.textRenderer.fontHeight + 2;
		text = object.getTitle();
		mc.textRenderer.drawWithShadow(matrixStack, text, w / 2f - mc.textRenderer.getWidth(text) / 2f, y, 0xFFFFFF | (alpha << 24));
	}

	private static final Queue<QuestCompletePopup> TITLES = new ArrayDeque<>();
	public static void add(QuestObject object) {
		TITLES.add(new QuestCompletePopup(object));
	}
	public static void render(MatrixStack matrixStack) {
		if (TITLES.isEmpty())
			return;
		MinecraftClient mc = MinecraftClient.getInstance();
		if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen))
			return;
		QuestCompletePopup title = TITLES.peek();
		title.render1(matrixStack);
	}
	public static void ticking(MinecraftClient mc) {
		if (mc.world == null)
			TITLES.clear();
		if (TITLES.isEmpty())
			return;
		if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen))
			return;
		QuestCompletePopup title = TITLES.peek();
		if (title.tick())
			TITLES.remove();
	}
}
