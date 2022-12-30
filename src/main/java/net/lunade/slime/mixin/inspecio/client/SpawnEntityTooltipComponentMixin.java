package net.lunade.slime.mixin.inspecio.client;

import io.github.queerbric.inspecio.tooltip.SpawnEntityTooltipComponent;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpawnEntityTooltipComponent.class)
public class SpawnEntityTooltipComponentMixin {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;load(Lnet/minecraft/nbt/CompoundTag;)V"), method = "of")
    private static void fixSlimesInOf(Entity par1, CompoundTag par2) {
        if (par1 instanceof Slime) {
            ((SlimeInterface) par2).cheatSize(1F);
        }
        par1.load(par2);
    }

}
