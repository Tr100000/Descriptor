package io.github.tr100000.descriptor.gui;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;

import java.util.Map;

public class MobEffectTooltipCache {
    private final Map<Holder<MobEffect>, ClientTooltipComponent> cache = new Object2ObjectOpenHashMap<>();

    public ClientTooltipComponent getOrCreate(Holder<MobEffect> effect) {
        return cache.computeIfAbsent(effect, MobEffectTooltipComponent::new);
    }
}
