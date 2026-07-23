package io.github.tr100000.descriptor.gui;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.effect.MobEffectInstance;

public class MobEffectTooltipFactory {
    private final MobEffectTooltipSettings settings;

    public MobEffectTooltipFactory(MobEffectTooltipSettings settings) {
        this.settings = settings;
    }

    public ClientTooltipComponent create(MobEffectInstance instance) {
        return new MobEffectTooltipComponent(instance, settings);
    }
}
