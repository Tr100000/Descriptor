package io.github.tr100000.descriptor.gui;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

import java.util.List;

public record ComponentWithTooltip(ClientTooltipComponent tooltipComponent) implements Component {
    @Override
    public Style getStyle() {
        return Style.EMPTY;
    }

    @Override
    public ComponentContents getContents() {
        return new Contents();
    }

    @Override
    public List<Component> getSiblings() {
        return List.of();
    }

    @Override
    public FormattedCharSequence getVisualOrderText() {
        return new Formatted(tooltipComponent);
    }

    private static final class Contents implements ComponentContents {
        @Override
        public MapCodec<? extends ComponentContents> codec() {
            return MapCodec.unit(Contents::new);
        }

        @Override
        public String toString() {
            return "";
        }
    }

    public record Formatted(ClientTooltipComponent tooltipComponent) implements FormattedCharSequence {
        @Override
        public boolean accept(FormattedCharSink output) {
            return true;
        }
    }
}
