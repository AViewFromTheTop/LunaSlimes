package net.lunade.slime.mixin;

import java.util.Optional;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.AbstractCubeMobInterface;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

	@Inject(at = @At("HEAD"), method = "handleEntityEvent")
	public void lunaSlimes$handleEntityEvent(byte id, CallbackInfo info) {
		if (!(Mob.class.cast(this) instanceof AbstractCubeMob cube) || id != EntityEvent.TENDRILS_SHIVER || !LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get()) return;
		LunaSlimesUtil.setSquish(cube, -0.05F);
		if (cube instanceof AbstractCubeMobInterface abstractCubeMobInterface) abstractCubeMobInterface.lunaSlimes$setJumpAnticTicks(3);
	}

	@Inject(method = "getLootTable", at = @At("TAIL"), cancellable = true)
	public void lunaSlimes$modifyMagmaCubeLootTable(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> info) {
		if (!(Mob.class.cast(this).is(EntityTypes.MAGMA_CUBE))) return;
		if (!LunaSlimesGameplayConfig.USE_SPLITTING.get()) return;
		final Identifier identifier = BuiltInRegistries.ENTITY_TYPE.getKey(EntityTypes.MAGMA_CUBE);
		final ResourceKey<LootTable> lootTable = ResourceKey.create(
			Registries.LOOT_TABLE,
			Identifier.fromNamespaceAndPath("lunaslimes", "entities/" + identifier.getPath())
		);
		info.setReturnValue(Optional.of(lootTable));
	}

}
