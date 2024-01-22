package kpan.bq_popup.client;

import com.feed_the_beast.ftbquests.quest.QuestObject;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class OtherTeamToast extends AdvancedToastQuestObject {
	private final String team;
	public OtherTeamToast(QuestObject questObject, String team) {
		super(questObject);
		this.team = team;
	}

	@Override
	public IToast.Visibility draw(GuiToast gui, long delta) {
		Minecraft mc = gui.getMinecraft();
		drawBackground(gui);
		List<String> list = mc.fontRenderer.listFormattedStringToWidth(getSubtitle(), 125);
		int color = toastType.getColor();
		int y = 16 - Math.min(list.size() + 1, 3) * mc.fontRenderer.FONT_HEIGHT / 2;
		if (delta < 1500L) {
			int alpha = MathHelper.floor(MathHelper.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x0400_0000;
			mc.fontRenderer.drawString(team, 30, y, 0xFFFF_FFFF);
			y += mc.fontRenderer.FONT_HEIGHT;
			mc.fontRenderer.drawString(getTitle(), 30, y, color | alpha);
		} else {
			int alpha = MathHelper.floor(MathHelper.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x0400_0000;
			mc.fontRenderer.drawString(team, 30, y, 0xFFFF_FFFF);
			y += mc.fontRenderer.FONT_HEIGHT;

			for (String line : list) {
				mc.fontRenderer.drawString(line, 30, y, 0xFF_FFFF | alpha);
				y += mc.fontRenderer.FONT_HEIGHT;
			}
		}

		RenderHelper.enableGUIStandardItemLighting();
		getIcon().draw(8, 8, 16, 16);
		return delta >= 5000L ? Visibility.HIDE : Visibility.SHOW;
	}

}
