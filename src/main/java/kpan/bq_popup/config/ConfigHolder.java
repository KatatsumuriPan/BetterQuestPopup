package kpan.bq_popup.config;

import kpan.bq_popup.config.core.ConfigAnnotations.Comment;
import kpan.bq_popup.config.core.ConfigAnnotations.ConfigOrder;
import kpan.bq_popup.config.core.ConfigAnnotations.Name;
import kpan.bq_popup.config.core.ConfigVersionUpdateContext;

public class ConfigHolder {

//	@Comment("Common settings(Blocks, items, etc.)")
//	@ConfigOrder(5)
//	public static Common common = new Common();

	public static class Common {

		public EnumTest enumTest = EnumTest.test2;

		public boolean boolValue = true;

		public enum EnumTest {
			TEST1,
			test2,
			Test3
		}
	}

	@Comment("Client only settings(Rendering, resources, etc.)")
	@ConfigOrder(0)
	public static Client client = new Client();

	public static class Client {

		@Name("Show Pop-up")
		@Comment("Shows pop-ups instead of toasts.")
		@ConfigOrder(0)
		public boolean showPopup = true;

		@Name("Show completed task when absent")
		@Comment("Shows completed tasks while you were absent when you log-in.")
		@ConfigOrder(1)
		public boolean showAbsentCompletedTask = true;

		@Name("Show other team task")
		@Comment("Shows completed tasks that other teams completed.")
		@ConfigOrder(2)
		public boolean showOtherTeamTask = true;

	}

	//	@Comment("Server settings(Behaviors, physics, etc.)")
	//	public static Server server = new Server();

	public static class Server {

	}

	public static void updateVersion(ConfigVersionUpdateContext context) {
		switch (context.loadedConfigVersion) {
			case "1":
				break;
			default:
				throw new RuntimeException("Unknown config version:" + context.loadedConfigVersion);
		}
	}

	public static String getVersion() { return "1"; }
}
