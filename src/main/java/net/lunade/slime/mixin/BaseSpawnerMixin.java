package net.lunade.slime.mixin;

import net.minecraft.world.level.BaseSpawner;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BaseSpawner.class)
public class BaseSpawnerMixin {

    /*
    @Shadow @Nullable private Entity displayEntity;

    @Inject(at = @At("HEAD"), method = "clientTick")
    public void clientTick(Level level, BlockPos blockPos, CallbackInfo info) {
        if (this.displayEntity instanceof Slime) {
            ((SlimeInterface)this.displayEntity).lunaSlimes$cheatSize(1F);
        }
    }
     */

}
