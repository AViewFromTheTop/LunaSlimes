package net.lunade.slime.mixin;

import net.lunade.slime.SlimeMethods;
import net.lunade.slime.config.getter.ConfigValueGetter;
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
        if (Mob.class.cast(this) instanceof Slime slime && b == (byte) 61 && ConfigValueGetter.jumpAntic()) {
            SlimeMethods.setSquish(slime, -0.05F);
            ((SlimeInterface) slime).lunaSlimes$setJumpAnticTicks(3);
        }
    }

	@Inject(method = "getDefaultLootTable", at = @At("TAIL"), cancellable = true)
	public void lunaSlimes$modifyMagmaCubeLootTable(CallbackInfoReturnable<ResourceKey<LootTable>> info) {
		if (Mob.class.cast(this) instanceof MagmaCube magmaCube) {
			if (ConfigValueGetter.useSplitting()) {
				ResourceLocation resourceLocation = BuiltInRegistries.ENTITY_TYPE.getKey(magmaCube.getType());
				ResourceKey<LootTable> lootTable = ResourceKey.create(
					Registries.LOOT_TABLE,
					ResourceLocation.tryBuild("lunaslimes", "entities/" + resourceLocation.getPath())
				);
				info.setReturnValue(lootTable);
			}
		}
	}

}
