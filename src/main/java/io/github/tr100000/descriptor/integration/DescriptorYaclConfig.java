package io.github.tr100000.descriptor.integration;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import io.github.tr100000.descriptor.Descriptor;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import io.github.tr100000.descriptor.config.option.AbstractOption;
import io.github.tr100000.descriptor.config.option.impl.BooleanOption;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import static io.github.tr100000.descriptor.config.DescriptorConfig.INSTANCE;
import static io.github.tr100000.descriptor.config.DescriptorConfig.configTranslated;

public final class DescriptorYaclConfig {
    private DescriptorYaclConfig() {}

    public static Screen createScreen(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(configTranslated("title"))
                .category(ConfigCategory.createBuilder()
                        .name(configTranslated("main"))
                        .option(createOption(INSTANCE.modEnabled)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(configTranslated("mob_effects"))
                                .option(createOption(INSTANCE.mobEffects.showItemTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("mob_effects.show_item_tooltips"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.showEffectsInInventoryTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("mob_effects.show_effects_in_inventory_tooltips"))
                                                .build())
                                        .build())
                                .option(createOption(INSTANCE.mobEffects.showGuiTooltips)
                                        .description(OptionDescription.createBuilder()
                                                .text(configTranslatedDesc("mob_effects.show_gui_tooltips"))
                                                .build())
                                        .build())
                                .build())
                        .build())
                .save(DescriptorConfig::save)
                .build()
                .generateScreen(parent);
    }

    private static Component configTranslatedDesc(String key) {
        return Component.translatable(String.format("config.%s.%s.desc", Descriptor.MODID, key));
    }

    private static Option.Builder<?> createOption(AbstractOption<?> configOption) {
        switch (configOption) {
            case BooleanOption booleanOption -> {
                return Option.<Boolean>createBuilder()
                        .name(booleanOption.getTitle())
                        .binding(booleanOption.getDefaultValue(), booleanOption::getValue, booleanOption::setValue)
                        .controller(TickBoxControllerBuilder::create);
            }
            default -> throw new IllegalArgumentException("Unexpected value");
        }
    }
}
