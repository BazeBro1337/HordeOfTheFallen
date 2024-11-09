package net.bzbr.hordeofthefallen;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Heightmap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class WaveSpawner {

    static boolean isWaveActive = false;

    private final static Map<Integer, Double> difficultyMap;
    static {
        difficultyMap = new HashMap<>();
        difficultyMap.put(Difficulty.EASY.getId(), 0.3);
        difficultyMap.put(Difficulty.NORMAL.getId(), 0.5);
        difficultyMap.put(Difficulty.HARD.getId(), 0.8);
        difficultyMap.put(Difficulty.PEACEFUL.getId(), 0.1);
    }

    public static boolean isWaveActive() {
        return isWaveActive;
    }

    public static void checkAndSpawnWave(ServerWorld world, int dayCount) {


        int waveInterval = ConfigLoader.getWaveIntervalDays();
        int waveNo = Math.max(1, dayCount/waveInterval);

        if (dayCount % waveInterval == 0) {

            spawnWave(world, ConfigLoader.getSpawnRadius(), waveNo);
            if (!isWaveActive) {
                isWaveActive = true;
            }
        }
        else {
            if (isWaveActive) {
                isWaveActive = false;
            }
        }
    }

    public static void spawnWave(ServerWorld world, int radius, int waveMultiplier) {

        for (ServerPlayerEntity player : world.getPlayers()) {

            for (ConfigLoader.MobConfig mobConfig : ConfigLoader.getWaveMobs()) {

                var playerPos = player.getBlockPos();
                EntityType<?> mobType = Registries.ENTITY_TYPE.get(new Identifier(mobConfig.getMobId()));

                if (mobType == null) {
                    System.out.println("Invalid mob ID: " + mobConfig.getMobId());
                    continue;
                }

                var mobCount = mobConfig.getCount();
                mobCount += ThreadLocalRandom.current().nextInt(0, (int)(mobCount * 0.5));
                mobCount *= Math.min(50, Math.max(1, (int)(waveMultiplier * difficultyMap.get(world.getDifficulty().getId()))));
                mobCount = Math.min(50, mobCount);

                for (int i = 0; i < mobCount; i++) {

                    BlockPos spawnPos = playerPos.add(
                            radius + world.random.nextInt(5),
                            0,
                            radius + world.random.nextInt(5)
                    );

                    spawnPos = new BlockPos(
                            spawnPos.getX(),
                            world.getTopY(Heightmap.Type.WORLD_SURFACE, spawnPos.getX(), spawnPos.getZ()),
                            spawnPos.getZ());

                    MobEntity mob = (MobEntity) mobType.create(world);
                    if (mob != null) {
                        mob.refreshPositionAndAngles(spawnPos, 0, 0);
                        world.spawnEntity(mob);
                        mob.getNavigation().startMovingTo(player, 1.0);
                    }
                }
            }
        }
    }
}
