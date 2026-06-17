package net.lunade.slime.mixin;

import java.util.Optional;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.frozenlib.LunaSlimesGameplayConfig;
import net.lunade.slime.config.frozenlib.LunaSlimesVisualsAudioConfig;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.cubemob.AbstractCubeMob;
import net.minecraft.world.entity.monster.cubemob.MagmaCube;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

	@Inject(at = @At("HEAD"), method = "handleEntityEvent")
	public void lunaSlimes$handleEntityEvent(byte event, CallbackInfo info) {
		if (!(Mob.class.cast(this) instanceof AbstractCubeMob cube) || event != EntityEvent.TENDRILS_SHIVER || !LunaSlimesVisualsAudioConfig.JUMP_ANTIC.get()) return;
		LunaSlimesUtil.setSquish(cube, -0.05F);
		if (cube instanceof SlimeInterface slimeInterface) slimeInterface.lunaSlimes$setJumpAnticTicks(3);
	}

	@Inject(method = "getLootTable", at = @At("TAIL"), cancellable = true)
	public void lunaSlimes$modifyMagmaCubeLootTable(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> info) {
		if (!(Mob.class.cast(this) instanceof MagmaCube magmaCube)) return;
		if (!LunaSlimesGameplayConfig.USE_SPLITTING.get()) return;
		final Identifier identifier = BuiltInRegistries.ENTITY_TYPE.getKey(magmaCube.getType());
		final ResourceKey<LootTable> lootTable = ResourceKey.create(
			Registries.LOOT_TABLE,
			Identifier.fromNamespaceAndPath("lunaslimes", "entities/" + identifier.getPath())
		);
		info.setReturnValue(Optional.of(lootTable));
	}

}
