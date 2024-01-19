package kpan.bq_popup.client;


import com.mojang.blaze3d.systems.RenderSystem;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftbquests.gui.ToastQuestObject;
import dev.ftb.mods.ftbquests.quest.QuestObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.util.math.MathHelper;

import java.util.List;
import java.util.Objects;

public class OtherTeamToast extends ToastQuestObject {
	private final QuestObject object;
	private final String team;
	public OtherTeamToast(QuestObject questObject, String team) {
		super(questObject);
		object = questObject;
		this.team = team;
	}

	@Override
	public Toast.Visibility draw(MatrixStack matrixStack, ToastManager gui, long delta) {
		GuiHelper.setupDrawing();
		MinecraftClient mc = gui.getGame();
		mc.getTextureManager().bindTexture(TEXTURE);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		gui.drawTexture(matrixStack, 0, 0, 0, 0, 160, 32);
		List<OrderedText> list = mc.textRenderer.wrapLines(getSubtitle(), 125);
		int color = isImportant() ? 0xff88ff : 0xffff00;
		int y = 16 - Math.min(list.size() + 1, 3) * mc.textRenderer.fontHeight / 2;
		{
			if (delta < 1500L) {
				int alpha = MathHelper.floor(MathHelper.clamp((float) (1500L - delta) / 300.0F, 0.0F, 1.0F) * 255.0F) << 24 | 0x400_0000;
				mc.textRenderer.drawWithShadow(matrixStack, team, 30.0F, y, color | alpha);
				y += mc.textRenderer.fontHeight;
				mc.textRenderer.drawWithShadow(matrixStack, getTitle(), 30.0F, y, color | alpha);
			} else {
				int alpha = MathHelper.floor(MathHelper.clamp((float) (delta - 1500L) / 300.0F, 0.0F, 1.0F) * 252.0F) << 24 | 0x400_0000;
				Objects.requireNonNull(mc.textRenderer);
				for (OrderedText s : list) {
					mc.textRenderer.drawWithShadow(matrixStack, s, 30.0F, (float) y, 0xff_ffff | alpha);
					Objects.requireNonNull(mc.textRenderer);
					y += mc.textRenderer.fontHeight;
				}
			}
		}

		GuiHelper.setupDrawing();
		DiffuseLighting.enableGuiDepthLighting();
		getIcon().draw(matrixStack, 8, 8, 16, 16);
		return delta >= 5000L ? Visibility.HIDE : Visibility.SHOW;
	}

}
