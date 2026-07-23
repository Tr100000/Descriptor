package io.github.tr100000.descriptor.impl;

import com.mojang.serialization.MapCodec;
import io.github.tr100000.descriptor.api.MobEffectHandler;
import io.github.tr100000.descriptor.api.MobEffectHandlerType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Objects;
import java.util.function.Consumer;

public final class DefaultMobEffectHandler implements MobEffectHandler {
    public static final DefaultMobEffectHandler INSTANCE = new DefaultMobEffectHandler();
    public static final MobEffectHandlerType<DefaultMobEffectHandler> TYPE = new MobEffectHandlerType<>(MapCodec.unit(INSTANCE));

    private DefaultMobEffectHandler() {}

    @Override
    public MobEffectHandlerType<?> getType() {
        return TYPE;
    }

    @Override
    public MutableComponent getDescription(MobEffectInstance instance) {
        String key = getDescriptionKey(instance.getEffect());
        if (Language.getInstance().has(key)) {
            return Component.translatable(key).withStyle(ChatFormatting.GRAY);
        }
        else {
            return Component.translatable("effect_handler.descriptor.default.missing_desc").withStyle(ChatFormatting.GRAY);
        }
    }

    @Override
    public boolean validate(Holder<MobEffect> effect, Consumer<Component> errorConsumer) {
        if (Language.getInstance().has(getDescriptionKey(effect))) {
            return true;
        }
        else {
            errorConsumer.accept(Component.translatable("effect_handler.descriptor.default.error"));
            return false;
        }
    }

    private static String getDescriptionKey(Holder<MobEffect> effect) {
        return Objects.requireNonNull(BuiltInRegistries.MOB_EFFECT.getKey(effect.value())).toLanguageKey("effect", "desc");
    }
}
