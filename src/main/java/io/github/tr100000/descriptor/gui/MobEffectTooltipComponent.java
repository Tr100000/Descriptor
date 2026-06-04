package io.github.tr100000.descriptor.gui;

import io.github.tr100000.descriptor.DescriptorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;

public class MobEffectTooltipComponent implements ClientTooltipComponent {
    private static final Minecraft minecraft = Minecraft.getInstance();
    private final Component nameComponent;
    private final Component descriptionComponent;
    private final Component idComponent;

    public MobEffectTooltipComponent(MobEffectInstance effectInstance) {
        Identifier id = BuiltInRegistries.MOB_EFFECT.getKey(effectInstance.getEffect().value());
        assert id != null;

        nameComponent = Component.translatable(id.toLanguageKey("effect"));
        descriptionComponent = DescriptorUtil.getMobEffectDescription(effectInstance.getEffect()).withStyle(ChatFormatting.GRAY);
        idComponent = Component.literal(id.toString()).withStyle(ChatFormatting.DARK_GRAY);
    }

    @Override
    public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
        graphics.text(font, nameComponent, x, y, -1);
        graphics.text(font, descriptionComponent, x, y + 10, -1);
        if (minecraft.options.advancedItemTooltips) {
            graphics.text(font, idComponent, x, y + 20, -1);
        }
    }

    @Override
    public int getHeight(Font font) {
        return minecraft.options.advancedItemTooltips ? 30 : 20;
    }

    @Override
    public int getWidth(Font font) {
        int longest = font.width(nameComponent);
        longest = Math.max(longest, font.width(descriptionComponent));
        if (minecraft.options.advancedItemTooltips) {
            longest = Math.max(longest, font.width(idComponent));
        }
        return longest;
    }
}
