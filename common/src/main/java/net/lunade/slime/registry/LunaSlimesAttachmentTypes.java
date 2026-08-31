package net.lunade.slime.registry;

import com.mojang.serialization.Codec;
import net.frozenblock.lib.platform.api.attachment.DataAttachmentSyncPredicate;
import net.frozenblock.lib.platform.api.attachment.DataAttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;

public final class LunaSlimesAttachmentTypes {
	public static final DataAttachmentType<Integer> WOBBLE_ANIM_PROGRESS = DataAttachmentType.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "wobble_anim_progress"),
		builder -> {
			builder.persistent(Codec.INT);
			builder.syncWith(ByteBufCodecs.VAR_INT, DataAttachmentSyncPredicate.all());
			builder.initializer(() -> 0);
		}
	);
	public static final DataAttachmentType<Float> SIZE = DataAttachmentType.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "size"),
		builder -> {
			builder.persistent(Codec.FLOAT);
			builder.syncWith(ByteBufCodecs.FLOAT, DataAttachmentSyncPredicate.all());
			builder.initializer(() -> 0F);
		}
	);
	public static final DataAttachmentType<Boolean> JUMP_ANTIC = DataAttachmentType.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "jump_antic"),
		builder -> {
			builder.persistent(Codec.BOOL);
			builder.syncWith(ByteBufCodecs.BOOL, DataAttachmentSyncPredicate.all());
			builder.initializer(() -> false);
		}
	);
	public static final DataAttachmentType<Integer> MERGE_COOLDOWN = DataAttachmentType.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "merge_cooldown"),
		builder -> {
			builder.persistent(Codec.INT);
			builder.initializer(() -> 0);
		}
	);
	public static final DataAttachmentType<Integer> JUMP_DELAY = DataAttachmentType.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "jump_delay"),
		builder -> {
			builder.persistent(Codec.INT);
			builder.initializer(() -> 0);
		}
	);

	public static final DataAttachmentType<Boolean> CAN_SQUISH = DataAttachmentType.create(
		Identifier.fromNamespaceAndPath("lunaslimes", "can_squish"),
		builder -> builder.initializer(() -> false)
	);

	public static void init() {}

	private LunaSlimesAttachmentTypes() {
		throw new UnsupportedOperationException("LunaSlimesAttachmentTypes contains only static declarations.");
	}

}
