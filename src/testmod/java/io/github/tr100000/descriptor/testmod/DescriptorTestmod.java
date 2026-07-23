package io.github.tr100000.descriptor.testmod;

import io.github.tr100000.descriptor.MobEffectHandlers;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.resources.Identifier;

public class DescriptorTestmod implements ClientModInitializer {
    public static final String MODID = "descriptor-testmod";

    @Override
    public void onInitializeClient() {
        MobEffectHandlers.register(id("test"), TestEffectHandler.TYPE);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MODID, path);
    }
}
