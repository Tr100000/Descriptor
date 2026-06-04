package io.github.tr100000.descriptor.gui;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Map;

public class MobEffectTooltipCache {
    private final Map<MobEffectInstance, ClientTooltipComponent> cache = new Object2ObjectOpenHashMap<>();

    public ClientTooltipComponent getOrCreate(MobEffectInstance effectInstance) {
        return cache.computeIfAbsent(effectInstance, MobEffectTooltipComponent::new);
    }
}
