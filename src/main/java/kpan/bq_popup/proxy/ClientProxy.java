package kpan.bq_popup.proxy;

import kpan.bq_popup.util.handlers.RenderTickHandler;
import kpan.bq_popup.util.handlers.TickHandler;
import net.minecraftforge.common.MinecraftForge;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

	@SuppressWarnings("RedundantMethodOverride")
	@Override
	public void registerOnlyClient() {
		MinecraftForge.EVENT_BUS.register(new RenderTickHandler());
		MinecraftForge.EVENT_BUS.register(new TickHandler());
	}

	@Override
	public boolean hasClientSide() { return true; }

}
