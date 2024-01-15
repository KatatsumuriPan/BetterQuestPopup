package kpan.bq_popup.util.handlers;

import kpan.bq_popup.QuestCompletePopup;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class TickHandler {
	public static int tick = 0;
	@SubscribeEvent
	public void onTick(TickEvent.ClientTickEvent event) {
		if (event.phase == Phase.START) {
			tick++;
			QuestCompletePopup.ticking();
		}
	}

}
