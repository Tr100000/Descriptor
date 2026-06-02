package io.github.tr100000.descriptor.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.tr100000.descriptor.config.DescriptorConfig;

public class DescriptorModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return DescriptorConfig.canUseYaclConfig() ? DescriptorYaclConfig::createScreen : ModMenuApi.super.getModConfigScreenFactory();
    }
}
