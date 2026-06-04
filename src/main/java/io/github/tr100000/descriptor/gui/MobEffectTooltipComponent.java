package io.github.tr100000.descriptor.gui;

import io.github.tr100000.descriptor.DescriptorUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

public class MobEffectTooltipComponent implements ClientTooltipComponent {
    private static final Minecraft minecraft = Minecraft.getInstance();

    private final MobEffectInstance instance;
    private final MobEffectTooltipSettings settings;

    private final MutableComponent nameComponent;
    private final Component descriptionComponent;
    private final Component idComponent;

    private Component lastShownNameComponent;

    public MobEffectTooltipComponent(MobEffectInstance instance, MobEffectTooltipSettings settings) {
        this.instance = instance;
        this.settings = settings;

        Identifier id = BuiltInRegistries.MOB_EFFECT.getKey(instance.getEffect().value());
        assert id != null;

        nameComponent = Component.translatable(id.toLanguageKey("effect"));
        descriptionComponent = DescriptorUtil.getMobEffectDescription(instance.getEffect()).withStyle(ChatFormatting.GRAY);
        idComponent = Component.literal(id.toString()).withStyle(ChatFormatting.DARK_GRAY);

        if (settings.showAmplifier() && instance.getAmplifier() >= 1 && instance.getAmplifier() <= 9) {
            nameComponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + (instance.getAmplifier() + 1)));
        }

        lastShownNameComponent = settings.showDuration() ? getNameComponentWithDuration() : nameComponent;
    }

    @Override
    public void extractText(GuiGraphicsExtractor graphics, Font font, int x, int y) {
        if (settings.showDuration()) {
            lastShownNameComponent = getNameComponentWithDuration();
            graphics.text(font, lastShownNameComponent, x, y, -1);
        }
        else {
            graphics.text(font, nameComponent, x, y, -1);
        }

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
        int longest = font.width(lastShownNameComponent);
        longest = Math.max(longest, font.width(descriptionComponent));
        if (minecraft.options.advancedItemTooltips) {
            longest = Math.max(longest, font.width(idComponent));
        }
        return longest;
    }

    private Component getNameComponentWithDuration() {
        assert minecraft.level != null;
        return nameComponent.copy()
                .append(" (")
                .append(MobEffectUtil.formatDuration(instance, 1.0F, minecraft.level.tickRateManager().tickrate()))
                .append(")");
    }
}
