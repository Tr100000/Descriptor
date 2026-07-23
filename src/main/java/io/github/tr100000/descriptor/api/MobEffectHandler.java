package io.github.tr100000.descriptor.api;

import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.function.Consumer;

public interface MobEffectHandler {
    MobEffectHandlerType<?> getType();

    MutableComponent getDescription(MobEffectInstance instance);

    boolean validate(Holder<MobEffect> effect, Consumer<Component> errorConsumer);
}
