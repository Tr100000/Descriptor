package io.github.tr100000.descriptor.api;

import com.mojang.serialization.MapCodec;

public record MobEffectHandlerType<T extends MobEffectHandler>(MapCodec<T> codec) {}
