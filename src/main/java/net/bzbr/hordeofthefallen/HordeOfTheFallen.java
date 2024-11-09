package net.bzbr.hordeofthefallen;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HordeOfTheFallen implements ModInitializer {

	public static final String MOD_ID = "hordeofthefallen";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStarted);
		ConfigLoader.loadConfig();
		LOGGER.info("HordeOfTheFallen initialized!");
	}

	private void onServerStarted(MinecraftServer server) {
		DayCounter.initialize(server);
	}
}