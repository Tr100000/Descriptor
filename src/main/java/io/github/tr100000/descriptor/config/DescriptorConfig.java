package io.github.tr100000.descriptor.config;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.github.tr100000.descriptor.Descriptor;
import io.github.tr100000.descriptor.config.option.GroupOption;
import io.github.tr100000.descriptor.config.option.impl.BooleanOption;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StrictJsonParser;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.util.function.Predicate;

public class DescriptorConfig extends GroupOption<DescriptorConfig> {
    public static final DescriptorConfig INSTANCE = new DescriptorConfig();

    private DescriptorConfig() {}

    public BooleanOption modEnabled = add(new BooleanOption(true, "mod_enabled", configTranslated("mod_enabled")));
    public MobEffectsSettings mobEffects = add(new MobEffectsSettings());

    public static boolean check(Predicate<DescriptorConfig> predicate) {
        return INSTANCE.modEnabled.getValue() && predicate.test(INSTANCE);
    }

    @Override
    public String getName() {
        throw new IllegalStateException();
    }

    public static class MobEffectsSettings extends GroupOption<MobEffectsSettings> {
        public BooleanOption showItemTooltips = add(new BooleanOption(true, "show_item_tooltips", configTranslated("mob_effects.show_item_tooltips")));
        public BooleanOption showEffectsInInventoryTooltips = add(new BooleanOption(true, "show_effects_in_inventory_tooltips", configTranslated("mob_effects.show_effects_in_inventory_tooltips")));
        public BooleanOption showGuiTooltips = add(new BooleanOption(true, "show_gui_tooltips", configTranslated("mob_effects.show_gui_tooltips")));

        public BooleanOption extraDetailsInGuiTooltip = add(new BooleanOption(true, "extra_details_in_gui_tooltip", configTranslated("mob_effects.extra_details_in_gui_tooltip")));
        public BooleanOption extraDetailsInEffectsInInventoryTooltip = add(new BooleanOption(false, "extra_details_in_effects_in_inventory_tooltip", configTranslated("mob_effects.extra_details_in_effects_in_inventory_tooltip")));

        @Override
        public String getName() {
            return "mob_effects";
        }
    }

    @ApiStatus.Internal
    public static Component configTranslated(String key) {
        return Component.translatable(String.format("config.%s.%s", Descriptor.MODID, key));
    }

    @ApiStatus.Internal
    public static void load() {
        try {
            if (Files.notExists(Descriptor.CONFIG_PATH)) {
                Descriptor.LOGGER.info("Config file not found");
                save();
                return;
            }

            JsonElement json = StrictJsonParser.parse(Files.newBufferedReader(Descriptor.CONFIG_PATH));
            INSTANCE.decode(JsonOps.INSTANCE, json);

            Descriptor.LOGGER.info("Loaded config file");
        }
        catch (IOException e) {
            Descriptor.LOGGER.error("Failed to load config file", e);
        }
    }

    @ApiStatus.Internal
    public static void save() {
        try {
            Files.deleteIfExists(Descriptor.CONFIG_PATH);

            DataResult<JsonElement> jsonResult = INSTANCE.encode(JsonOps.INSTANCE);
            if (jsonResult.isSuccess()) {
                JsonElement json = jsonResult.getOrThrow();
                Files.writeString(Descriptor.CONFIG_PATH, Descriptor.GSON.toJson(json));
            }
            else {
                Descriptor.LOGGER.error("Failed to encode config");
            }
        }
        catch (IOException e) {
            Descriptor.LOGGER.error("Failed to save config file", e);
        }
    }

    @ApiStatus.Internal
    public static boolean canUseYaclConfig() {
        return FabricLoader.getInstance().isModLoaded("yet_another_config_lib_v3");
    }
}
