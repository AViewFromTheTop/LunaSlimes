package net.lunade.slime.registry;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

public final class LunaSlimesAttachmentTypes {
	public static final AttachmentType<Integer> WOBBLE_ANIM_PROGRESS = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "wobble_anim_progress"),
		builder -> {
			builder.persistent(Codec.INT);
			builder.syncWith(ByteBufCodecs.VAR_INT, AttachmentSyncPredicate.all());
			builder.initializer(() -> 0);
		}
	);
	public static final AttachmentType<Float> SIZE = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "size"),
		builder -> {
			builder.persistent(Codec.FLOAT);
			builder.syncWith(ByteBufCodecs.FLOAT, AttachmentSyncPredicate.all());
			builder.initializer(() -> 0F);
		}
	);
	public static final AttachmentType<Boolean> JUMP_ANTIC = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "jump_antic"),
		builder -> {
			builder.persistent(Codec.BOOL);
			builder.syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all());
			builder.initializer(() -> false);
		}
	);
	public static final AttachmentType<Integer> MERGE_COOLDOWN = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "merge_cooldown"),
		builder -> {
			builder.persistent(Codec.INT);
			builder.initializer(() -> 0);
		}
	);
	public static final AttachmentType<Integer> JUMP_DELAY = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "jump_delay"),
		builder -> {
			builder.persistent(Codec.INT);
			builder.initializer(() -> 0);
		}
	);

	public static final AttachmentType<Boolean> CAN_SQUISH = AttachmentRegistry.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "can_squish"),
		builder -> builder.initializer(() -> false)
	);

	public static void init() {}

	private LunaSlimesAttachmentTypes() {
		throw new UnsupportedOperationException("LunaSlimesAttachmentTypes contains only static declarations.");
	}

}
