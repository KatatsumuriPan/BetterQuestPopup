package kpan.bq_popup.util.handlers;

import kpan.bq_popup.ModMain;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@EventBusSubscriber
public class RegistryHandler {

	@SuppressWarnings("InstantiationOfUtilityClass")
	public static void preInitRegistries(@SuppressWarnings("unused") FMLPreInitializationEvent event) {
		ModMain.proxy.registerOnlyClient();
	}

	public static void initRegistries() {
	}

	public static void postInitRegistries() {
	}

	public static void serverRegistries(@SuppressWarnings("unused") FMLServerStartingEvent event) {
	}

//	@SubscribeEvent
//	public void onEnchantmentRegister(RegistryEvent.Register<Enchantment> event) {
//	}

//	@SubscribeEvent
//	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
//	}

//	@SubscribeEvent
//	public static void onDataSerializerRegister(RegistryEvent.Register<DataSerializerEntry> event) {
//	}

}
