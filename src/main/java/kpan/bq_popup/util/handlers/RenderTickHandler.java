package kpan.bq_popup.util.handlers;

import kpan.bq_popup.QuestCompletePopup;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import org.lwjgl.opengl.GL11;

public class RenderTickHandler {

	@SubscribeEvent
	public void postRender(RenderTickEvent event) {
		if (event.phase == Phase.END) {
			try {
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glPushMatrix();
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPushMatrix();
				Minecraft mc = Minecraft.getMinecraft();
				if (mc.entityRenderer == null)
					return;
				mc.entityRenderer.setupOverlayRendering();

				QuestCompletePopup.render();

				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				GL11.glPopMatrix();
				GL11.glMatrixMode(GL11.GL_PROJECTION);
				GL11.glPopMatrix();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
