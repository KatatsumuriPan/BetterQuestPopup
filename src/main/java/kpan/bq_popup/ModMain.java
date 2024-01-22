package kpan.bq_popup;

import kpan.bq_popup.client.SoundHandler;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ModMain implements ModInitializer {
	public static final String MOD_ID = "better_quest_popup";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		SoundHandler.init();
	}
}