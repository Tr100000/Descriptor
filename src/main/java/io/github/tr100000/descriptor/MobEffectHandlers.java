package io.github.tr100000.descriptor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import io.github.tr100000.descriptor.api.MobEffectHandler;
import io.github.tr100000.descriptor.api.MobEffectHandlerType;
import io.github.tr100000.descriptor.impl.DefaultMobEffectHandler;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;
import java.util.Objects;

public final class MobEffectHandlers extends SimpleJsonResourceReloadListener<MobEffectHandler> {
    private static final BiMap<Identifier, MobEffectHandlerType<?>> TYPE_REGISTRY = HashBiMap.create();
    public static final Codec<MobEffectHandler> CODEC = new CodecFromMap<>(TYPE_REGISTRY, Identifier.CODEC, id -> String.format("Mob effect handler %s doesn't exist", id))
            .dispatch(MobEffectHandler::getType, MobEffectHandlerType::codec);

    public static final Identifier ID = Descriptor.id("mob_effects");
    public static final MobEffectHandlers INSTANCE = new MobEffectHandlers();

    public static <T extends MobEffectHandler> void register(Identifier id, MobEffectHandlerType<T> type) {
        Objects.requireNonNull(id, "id is null");
        Objects.requireNonNull(type, "type is null");
        if (TYPE_REGISTRY.put(id, type) != null) {
            throw new IllegalStateException(String.format("%s already had a registered type", id));
        }
    }

    public static Identifier getHandlerId(MobEffectHandlerType<?> type) {
        Objects.requireNonNull(type, "type is null");
        if (TYPE_REGISTRY.inverse().containsKey(type)) {
            return TYPE_REGISTRY.inverse().get(type);
        }
        else {
            throw new IllegalArgumentException("Provided type was never registered");
        }
    }

    private final Map<Holder<MobEffect>, MobEffectHandler> handlers = new Object2ObjectOpenHashMap<>();

    private MobEffectHandlers() {
        super(CODEC, FileToIdConverter.json("descriptor/mob_effects"));
    }

    @Override
    protected void apply(Map<Identifier, MobEffectHandler> preparations, ResourceManager manager, ProfilerFiller profiler) {
        handlers.clear();

        preparations.forEach((id, handler) -> BuiltInRegistries.MOB_EFFECT.get(id)
                .ifPresentOrElse(
                        effect -> {
                            handlers.put(effect, handler);
                            Descriptor.LOGGER.debug("Handler with type {} loaded for {}", TYPE_REGISTRY.inverse().get(handler.getType()), id);
                        },
                        () -> Descriptor.LOGGER.error("Effect with id {} doesn't exist", id)
                ));

        BuiltInRegistries.MOB_EFFECT.listElements().forEach(holder -> {
            if (!handlers.containsKey(holder)) {
                handlers.put(holder, DefaultMobEffectHandler.INSTANCE);
            }
        });
    }

    public MobEffectHandler get(Holder<MobEffect> effect) {
        return handlers.get(effect);
    }
}
