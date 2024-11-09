package net.bzbr.hordeofthefallen.mixin;

import net.bzbr.hordeofthefallen.WaveSpawner;
import net.bzbr.hordeofthefallen.entity.ai.FollowPlayerIgnoringObstaclesGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin extends HostileEntity {

    protected ZombieEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow
    @Final
    protected abstract void initGoals();

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addCustomGoalDuringWave(CallbackInfo ci) {

        if (WaveSpawner.isWaveActive()) {
            this.goalSelector.add(2, new FollowPlayerIgnoringObstaclesGoal(this, 2.0));
        }
    }
}
