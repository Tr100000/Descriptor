package io.github.tr100000.descriptor.gui;

import io.github.tr100000.descriptor.DescriptorUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public class MobEffectDescriptionTooltipComponent implements ClientTooltipComponent {
    private final Component name;
    private final Component description;

    public MobEffectDescriptionTooltipComponent(Identifier id, Holder<MobEffect> effectHolder) {
        name = Component.translatable(id.toLanguageKey("effect"));
        description = DescriptorUtil.getMobEffectDescription(new MobEffectInstance(effectHolder));
    }

    @Override
    public int getWidth(Font font) {
        return Math.max(font.width(name), font.width(description));
    }

    @Override
    public int getHeight(Font font) {
        return 20;
    }

    @Override
    public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
        graphics.text(font, name, x, y, -1);
        graphics.text(font, description, x, y + 10, -1);
    }
}
