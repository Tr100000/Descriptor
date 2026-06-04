package io.github.tr100000.descriptor.gui;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.Map;

public class MobEffectTooltipCache {
    private final MobEffectTooltipSettings settings;
    private final Map<MobEffectInstance, ClientTooltipComponent> cache = new Object2ObjectOpenHashMap<>();

    public MobEffectTooltipCache() {
        this(MobEffectTooltipSettings.DEFAULT);
    }

    public MobEffectTooltipCache(MobEffectTooltipSettings settings) {
        this.settings = settings;
    }

    public ClientTooltipComponent getOrCreate(MobEffectInstance instance) {
        return cache.computeIfAbsent(instance, i -> new MobEffectTooltipComponent(i, settings));
    }
}
