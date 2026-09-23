package io.github.tr100000.descriptor.config.option.impl;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import io.github.tr100000.descriptor.config.option.SimpleOption;
import net.minecraft.network.chat.Component;

public class FloatOption extends SimpleOption<Float> {
    public final float minValue;
    public final float maxValue;
    public final Controller controller;
    private final Codec<Float> codec;

    public FloatOption(Float defaultValue, String name, Component title, float minValue, float maxValue, Controller controller) {
        super(defaultValue, name, title);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.controller = controller;
        this.codec = Codec.floatRange(minValue, maxValue);
    }

    @Override
    public <V> DataResult<V> encode(DynamicOps<V> ops) {
        return codec.encodeStart(ops, getValue());
    }

    @Override
    public <V> void decode(DynamicOps<V> ops, V input) {
        codec.decode(ops, input).map(Pair::getFirst).ifSuccess(this::setValue);
    }

    public enum Controller {
        Slider,
        Field
    }
}
