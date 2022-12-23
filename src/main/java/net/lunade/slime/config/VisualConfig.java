package net.lunade.slime.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

import static net.lunade.slime.config.LunaSlimesConfig.text;
import static net.lunade.slime.config.LunaSlimesConfig.tooltip;

@Config(name = "visuals")
public final class VisualConfig implements ConfigData {

    public boolean growAnim = true;
    public boolean wobbleAnim = true;
    public float squishMultiplier = 2F;
    public boolean newShadows = true;
    public boolean particles = true;

    @Environment(EnvType.CLIENT)
    static void setupEntries(ConfigCategory category, ConfigEntryBuilder entryBuilder) {
        var config = LunaSlimesConfig.get().visuals;
        category.setBackground(new ResourceLocation("lunaslimes", "textures/config/visuals.png"));

        var growAnim = category.addEntry(entryBuilder.startBooleanToggle(text("grow_anim"), config.growAnim)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.growAnim = newValue)
                .setYesNoTextSupplier(bool -> text("grow_anim." + bool))
                .setTooltip(tooltip("grow_anim"))
                .build()
        );

        var wobbleAnim = category.addEntry(entryBuilder.startBooleanToggle(text("wobble_anim"), config.wobbleAnim)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.wobbleAnim = newValue)
                .setYesNoTextSupplier(bool -> text("wobble_anim." + bool))
                .setTooltip(tooltip("wobble_anim"))
                .build()
        );

        var squishMultiplier = category.addEntry(entryBuilder.startFloatField(text("squish_multiplier"), config.squishMultiplier)
                .setDefaultValue(2F)
                .setSaveConsumer(newValue -> config.squishMultiplier = newValue)
                .setTooltip(tooltip("squish_multiplier"))
                .setMin(0F)
                .setMax(5F)
                .build()
        );

        var newShadows = category.addEntry(entryBuilder.startBooleanToggle(text("new_shadows"), config.newShadows)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.newShadows = newValue)
                .setYesNoTextSupplier(bool -> text("new_shadows." + bool))
                .setTooltip(tooltip("new_shadows"))
                .build()
        );

        var particles = category.addEntry(entryBuilder.startBooleanToggle(text("particles"), config.particles)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> config.particles = newValue)
                .setYesNoTextSupplier(bool -> text("particles." + bool))
                .setTooltip(tooltip("particles"))
                .build()
        );
    }
}
