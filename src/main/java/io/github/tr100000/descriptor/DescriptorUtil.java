package io.github.tr100000.descriptor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;

public final class DescriptorUtil {
    private DescriptorUtil() {}

    public static Component getMobEffectDescription(Holder<MobEffect> mobEffect) {
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
}
