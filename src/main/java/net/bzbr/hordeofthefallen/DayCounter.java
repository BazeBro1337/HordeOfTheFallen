package net.bzbr.hordeofthefallen;

import net.bzbr.hordeofthefallen.data.DayCounterData;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public class DayCounter {

    private static ServerWorld _overworld;
    private static int previousTimeOfDay = 0;
    private static int customDayCounter = 1;
    private static int tickCounter = 0;
    private static DayCounterData _dayData;
    private static final Identifier DAY_COUNTER_ID = new Identifier("mymod", "day_counter");

    public static void initialize(MinecraftServer server) {

        ServerTickEvents.START_SERVER_TICK.register(DayCounter::onServerTick);
        _overworld = server.getOverworld();

        var manager = _overworld.getPersistentStateManager();
        _dayData = manager.getOrCreate(DayCounterData::fromNbt, DayCounterData::new, DAY_COUNTER_ID.toString());
        customDayCounter = _dayData.getDayCounter();
    }

    private static void onServerTick(MinecraftServer server) {
        tickCounter++;

        if (tickCounter >= 1000) {

            customDayCounter = _dayData.getDayCounter();
            tickCounter = 0;

            int currentTimeOfDay = (int) _overworld.getTimeOfDay() % 24000;

            if (currentTimeOfDay < previousTimeOfDay) {

                _dayData.incrementDayCounter();
                customDayCounter = _dayData.getDayCounter();
            }

            previousTimeOfDay = currentTimeOfDay;

            if (currentTimeOfDay >= 13000 && currentTimeOfDay < 23000) {

                WaveSpawner.checkAndSpawnWave(_overworld, customDayCounter);
            }
        }
    }
}
