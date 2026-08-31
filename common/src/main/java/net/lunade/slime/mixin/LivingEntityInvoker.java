package net.lunade.slime.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityInvoker {

	@Invoker("shouldDropLoot")
	boolean lunaSlimes$shouldDropLoot(ServerLevel level);

	@Invoker("dropEquipment")
	void lunaSlimes$dropEquipment(ServerLevel level);
}
