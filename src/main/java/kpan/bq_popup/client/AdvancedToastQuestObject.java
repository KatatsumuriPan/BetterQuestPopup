package kpan.bq_popup.client;

import com.feed_the_beast.ftblib.lib.gui.GuiHelper;
import com.feed_the_beast.ftbquests.gui.ToastQuestObject;
import com.feed_the_beast.ftbquests.quest.Chapter;
import com.feed_the_beast.ftbquests.quest.Quest;
import com.feed_the_beast.ftbquests.quest.QuestFile;
import com.feed_the_beast.ftbquests.quest.QuestObject;
import com.feed_the_beast.ftbquests.quest.task.Task;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class AdvancedToastQuestObject extends ToastQuestObject {

	public static void addToast(QuestObject questObject) {
		addToast(new AdvancedToastQuestObject(questObject));
	}
	public static void addToast(AdvancedToastQuestObject toast) {
		GuiToast toastGui = Minecraft.getMinecraft().getToastGui();
		if (toast.toastType == ToastType.QUEST_FILE) {
			for (IToast iToast : toastGui.toastsQueue) {
				if (iToast instanceof AdvancedToastQuestObject t) {
					if (t.toastType == ToastType.CHAPTER) {
						t.hasPlayedSound = true;
					}
				}
			}
		}
		toastGui.add(toast);

	}

	protected final QuestObject object;
	public final ToastType toastType;
	private boolean hasPlayedSound = false;

	public AdvancedToastQuestObject(QuestObject q) {
		super(q);
		object = q;
		toastType = ToastType.getToastType(q);
	}

	@Override
	public IToast.Visibility draw(GuiToast gui, long delta) {
		Minecraft mc = gui.getMinecraft();
		drawBackground(gui);
		List<String> list = mc.fontRenderer.listFormattedStringToWidth(getSubtitle(), 125);
		int color = toastType.getColor();
		if (list.size() == 1) {
			mc.fontRenderer.drawString(getTitle(), 30, 7, color | 0xFF00_0000);
			mc.fontRenderer.drawString(list.get(0), 30, 18, -1);
		} else {
			int i1;
			if (delta < 1500L) {
				i1 = MathHelper.floor(MathHelper.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x0400_0000;
				mc.fontRenderer.drawString(getTitle(), 30, 11, color | i1);
			} else {
				i1 = MathHelper.floor(MathHelper.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x0400_0000;
				int y = 16 - list.size() * mc.fontRenderer.FONT_HEIGHT / 2;

				for (String s : list) {
					mc.fontRenderer.drawString(s, 30, y, 0xFF_FFFF | i1);
					y += mc.fontRenderer.FONT_HEIGHT;
				}
			}
		}

		if (!hasPlayedSound && delta > 0L) {
			hasPlayedSound = true;
			playSound(mc.getSoundHandler());
		}

		RenderHelper.enableGUIStandardItemLighting();
		getIcon().draw(8, 8, 16, 16);
		return delta >= 5000L ? Visibility.HIDE : Visibility.SHOW;
	}
	protected static void drawBackground(GuiToast gui) {
		GuiHelper.setupDrawing();
		gui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		gui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
	}

	@Override
	public void playSound(SoundHandler handler) {
		switch (toastType) {
			case CHAPTER -> handler.playSound(QuestCompletePopup.CHAPTER_COMPLETE);
			case QUEST_FILE -> handler.playSound(QuestCompletePopup.ALL_CHAPTERS_COMPLETE);
		}
	}

	public enum ToastType {
		TASK,
		QUEST,
		CHAPTER,
		QUEST_FILE,
		;

		public static ToastType getToastType(QuestObject q) {
			final ToastType toastType;
			if (q instanceof Task)
				toastType = ToastType.TASK;
			else if (q instanceof Quest)
				toastType = ToastType.QUEST;
			else if (q instanceof Chapter)
				toastType = ToastType.CHAPTER;
			else if (q instanceof QuestFile)
				toastType = ToastType.QUEST_FILE;
			else
				throw new IllegalArgumentException("Invalid QuestObject!:" + q);
			return toastType;
		}

		public int getColor() {
			switch (this) {
				case TASK -> {
					return 0xFFFF00;
				}
				case QUEST -> {
					return 0xFFFF00;
				}
				case CHAPTER -> {
					return 0xFF88FF;
				}
				case QUEST_FILE -> {
					return 0x88FF88;
				}
				default -> throw new IllegalStateException("Unexpected value: " + this);
			}
		}
	}
}
