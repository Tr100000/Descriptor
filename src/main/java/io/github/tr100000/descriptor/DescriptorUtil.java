package io.github.tr100000.descriptor;

import io.github.tr100000.descriptor.gui.MobEffectTooltipFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public final class DescriptorUtil {
    private DescriptorUtil() {}

    public static MutableComponent getMobEffectDescription(MobEffectInstance instance) {
        return MobEffectHandlers.INSTANCE.get(instance.getEffect()).getDescription(instance);
    }

    public static void extractMobEffectTooltip(GuiGraphicsExtractor graphics, MobEffectTooltipFactory cache, MobEffectInstance instance, int x, int y) {
        extractMobEffectTooltip(graphics, cache.create(instance), x, y);
    }

    private static void extractMobEffectTooltip(GuiGraphicsExtractor graphics, ClientTooltipComponent tooltipComponent, int x, int y) {
        graphics.setTooltipForNextFrameInternal(Minecraft.getInstance().font, List.of(tooltipComponent), x, y, DefaultTooltipPositioner.INSTANCE, null, false);
    }
}
