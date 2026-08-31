package net.lunade.slime.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Mob.class)
public interface MobInvoker {

	@Invoker("dropCustomDeathLoot")
	void lunaSlimes$dropCustomDeathLoot(ServerLevel level, DamageSource source, boolean hitByPlayer);
}
