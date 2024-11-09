package net.bzbr.hordeofthefallen.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FollowPlayerIgnoringObstaclesGoal extends Goal {
    private final MobEntity mob;
    private final double speed;
    private PlayerEntity player;
    private boolean isWaveActive;

    public FollowPlayerIgnoringObstaclesGoal(MobEntity mob, double speed) {
        this.mob = mob;
        this.speed = speed;
        this.isWaveActive = false;
    }

    public void activateWave(boolean isWaveActive) {
        this.isWaveActive = isWaveActive;
    }

    @Override
    public boolean canStart() {
        this.player = mob.getEntityWorld().getClosestPlayer(mob, 100.0D);
        return true;
    }

    @Override
    public void start() {
        if (player != null) {
            mob.getNavigation().startMovingTo(player, speed); // Начинаем движение к игроку
        }
    }

    @Override
    public boolean shouldContinue() {
        if (player != null) {
            return !mob.getNavigation().isIdle() && mob.squaredDistanceTo(player) > 1.0D;
        }
        return false;
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (player != null) {
            mob.getLookControl().lookAt(player.getX(), player.getY(), player.getZ()); // Следим за игроком
            mob.getNavigation().startMovingTo(player, speed); // Пытаемся двигаться к игроку
        }
    }
}
