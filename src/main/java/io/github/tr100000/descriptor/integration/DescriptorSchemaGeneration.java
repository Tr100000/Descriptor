package io.github.tr100000.descriptor.integration;

import com.mojang.serialization.Codec;
import io.github.tr100000.codec2schema.api.Codec2SchemaPlugin;
import io.github.tr100000.codec2schema.api.CodecValueLister;
import io.github.tr100000.codec2schema.api.SchemaExporter;
import io.github.tr100000.codec2schema.api.ValueStringPair;
import io.github.tr100000.descriptor.CodecFromMap;
import io.github.tr100000.descriptor.MobEffectHandlers;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class DescriptorSchemaGeneration implements Codec2SchemaPlugin {
    @Override
    public void registerHandlers() {
        CodecValueLister.LISTERS.add(new CodecFromMapLister());
    }

    @Override
    public void generateSchemas(SchemaExporter exporter) {
        exporter.accept(MobEffectHandlers.CODEC, "resources/descriptor/mob_effect.json");
    }

    private static class CodecFromMapLister implements CodecValueLister {
        @Override
        public <T> @Nullable List<ValueStringPair<T>> possibleValues(Codec<T> codec) {
            if (codec instanceof CodecFromMap<?, T> codecFromMap) {
                return codecFromMap.map().values().stream()
                        .map(v -> new ValueStringPair<>(v, codecFromMap.map().inverse().get(v).toString()))
                        .toList();
            }
            else return null;
        }
    }
}
