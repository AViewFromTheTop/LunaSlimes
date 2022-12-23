package net.lunade.slime.mixin;

import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {

    @Shadow @Nullable private Entity displayEntity;

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/EntityType;loadEntityRecursive(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/Level;Ljava/util/function/Function;)Lnet/minecraft/world/entity/Entity;", shift = At.Shift.AFTER), method = "getOrCreateDisplayEntity")
    public void modifySlimeSize(Level level, CallbackInfoReturnable<Entity> info) {
        if (this.displayEntity instanceof Slime) {
            ((SlimeInterface) this.displayEntity).spawnerSizeFix(1F);
        }
    }

}
