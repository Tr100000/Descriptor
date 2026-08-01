package io.github.tr100000.descriptor.gui;

import com.mojang.brigadier.Message;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

import java.util.List;

public interface MessageWithCustomTooltip extends Message {
    default String getString() {
        return "";
    }

    List<ClientTooltipComponent> tooltip();
}
