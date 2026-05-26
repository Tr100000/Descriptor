package io.github.tr100000.descriptor;

import io.github.tr100000.descriptor.gui.MobEffectTooltipCache;
import io.github.tr100000.descriptor.gui.MobEffectTooltipComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

import java.util.List;

public final class DescriptorUtil {
    private DescriptorUtil() {}

    public static MutableComponent getMobEffectDescription(Holder<MobEffect> mobEffect) {
        String key = getMobEffectDescriptionKey(mobEffect);
        if (I18n.exists(key)) {
            return Component.translatable(getMobEffectDescriptionKey(mobEffect)).withStyle(ChatFormatting.GRAY);
        }
        else {
            return Component.translatable("effect.missing_desc").withStyle(ChatFormatting.GRAY);
        }
    }

    public static String getMobEffectDescriptionKey(Holder<MobEffect> mobEffect) {
        Identifier effectId = BuiltInRegistries.MOB_EFFECT.getKey(mobEffect.value());
        assert effectId != null;
        return getMobEffectDescriptionKey(effectId);
    }

    public static String getMobEffectDescriptionKey(Identifier mobEffectId) {
        return mobEffectId.toLanguageKey("effect", "desc");
    }

    public static void extractMobEffectTooltip(GuiGraphicsExtractor graphics, MobEffectTooltipCache cache, Holder<MobEffect> effect, int x, int y) {
        extractMobEffectTooltip(graphics, cache.getOrCreate(effect), x, y);
    }

    public static void extractMobEffectTooltip(GuiGraphicsExtractor graphics, Holder<MobEffect> effect, int x, int y) {
        extractMobEffectTooltip(graphics, new MobEffectTooltipComponent(effect), x, y);
    }

    public static void extractMobEffectTooltip(GuiGraphicsExtractor graphics, ClientTooltipComponent tooltipComponent, int x, int y) {
        graphics.setTooltipForNextFrameInternal(Minecraft.getInstance().font, List.of(tooltipComponent), x, y, DefaultTooltipPositioner.INSTANCE, null, false);
    }
}
