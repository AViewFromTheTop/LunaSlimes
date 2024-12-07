package net.lunade.slime.mixin;

import java.util.Optional;
import net.lunade.slime.LunaSlimesUtil;
import net.lunade.slime.config.getter.LunaSlimesConfigValueGetter;
import net.lunade.slime.impl.SlimeInterface;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {

	@Inject(at = @At("HEAD"), method = "handleEntityEvent")
	public void lunaSlimes$handleEntityEvent(byte b, CallbackInfo info) {
		if (Mob.class.cast(this) instanceof Slime slime && b == (byte) 61 && LunaSlimesConfigValueGetter.jumpAntic()) {
			LunaSlimesUtil.setSquish(slime, -0.05F);
			((SlimeInterface) slime).lunaSlimes$setJumpAnticTicks(3);
		}
	}

	@Inject(method = "getLootTable", at = @At("TAIL"), cancellable = true)
	public void lunaSlimes$modifyMagmaCubeLootTable(CallbackInfoReturnable<Optional<ResourceKey<LootTable>>> info) {
		if (Mob.class.cast(this) instanceof MagmaCube magmaCube) {
			if (LunaSlimesConfigValueGetter.useSplitting()) {
				ResourceLocation resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(magmaCube.getType());
				ResourceKey<LootTable> lootTable = ResourceKey.create(
					Registries.LOOT_TABLE,
					ResourceLocation.tryBuild("lunaslimes", "entities/" + resourceLocation.getPath())
				);
				info.setReturnValue(Optional.of(lootTable));
			}
		}
	}

}
