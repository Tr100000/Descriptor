package io.github.tr100000.descriptor.testmod;

import com.mojang.serialization.MapCodec;
import io.github.tr100000.descriptor.api.MobEffectHandler;
import io.github.tr100000.descriptor.api.MobEffectHandlerType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Consumer;

public class TestEffectHandler implements MobEffectHandler {
    public static final TestEffectHandler INSTANCE = new TestEffectHandler();
    public static final MobEffectHandlerType<TestEffectHandler> TYPE = new MobEffectHandlerType<>(MapCodec.unit(INSTANCE));

    private TestEffectHandler() {}

    @Override
    public MobEffectHandlerType<?> getType() {
        return TYPE;
    }

    @Override
    public MutableComponent getDescription(MobEffectInstance instance) {
        return Component.translatable("effect_handler.descriptor_testmod.test.tooltip", instance.getDuration(), instance.getAmplifier(), instance.isAmbient(), instance.isVisible()).withStyle(ChatFormatting.GRAY);
    }

    @Override
    public boolean validate(Holder<MobEffect> effect, Consumer<Component> errorConsumer) {
        return true;
    }
}
