package kpan.bq_popup;

import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.QuestObject;
import kpan.bq_popup.asm.hook.HK_RenderItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayDeque;
import java.util.Queue;

public class QuestCompletePopup {
	private final QuestObject object;
	private int tick = 0;
	private QuestCompletePopup(QuestObject object) { this.object = object; }

	private boolean tick() {
		if (tick >= 100)
			return true;
		Minecraft mc = Minecraft.getMinecraft();
		boolean is_important = object instanceof Chapter;
		if (tick == 0) {
			if (is_important)
				mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
			else
				mc.getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F));
		}
		tick++;
		return false;
	}
	private void render1() {
		boolean is_important = object instanceof Chapter;
		Minecraft mc = Minecraft.getMinecraft();
		var res = new ScaledResolution(mc);
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
		GlStateManager.enableBlend();
		GlStateManager.color(1, 1, 1, alphaf);
		HK_RenderItem.alpha = alphaf;
		object.getIcon().draw(w / 2 - 8, y, 16, 16);
		HK_RenderItem.alpha = 1;
		y += 16 + 2;

		String text = TextFormatting.BOLD.toString() + TextFormatting.UNDERLINE + I18n.format(object.getObjectType().getTranslationKey() + ".completed");
		mc.fontRenderer.drawStringWithShadow(text, w / 2f - mc.fontRenderer.getStringWidth(text) / 2f, y, (is_important ? 0xFF88FF : 0xFFFF00) | (alpha << 24));
		y += mc.fontRenderer.FONT_HEIGHT + 2;
		text = object.getTitle();
		mc.fontRenderer.drawStringWithShadow(text, w / 2f - mc.fontRenderer.getStringWidth(text) / 2f, y, 0xFFFFFF | (alpha << 24));
	}

	private static final Queue<QuestCompletePopup> TITLES = new ArrayDeque<>();
	public static void add(QuestObject object) {
		TITLES.add(new QuestCompletePopup(object));
	}
	public static void render() {
		if (TITLES.isEmpty())
			return;
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat))
			return;
		QuestCompletePopup title = TITLES.peek();
		title.render1();
	}
	public static void ticking() {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.world == null)
			TITLES.clear();
		if (TITLES.isEmpty())
			return;
		if (mc.currentScreen != null && !(mc.currentScreen instanceof GuiChat))
			return;
		QuestCompletePopup title = TITLES.peek();
		if (title.tick())
			TITLES.remove();
	}
}
