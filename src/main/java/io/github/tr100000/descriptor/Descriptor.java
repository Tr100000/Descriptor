package io.github.tr100000.descriptor;

import io.github.tr100000.descriptor.command.DescriptorCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Descriptor implements ClientModInitializer {
    public static final String MODID = "descriptor";
    public static final Logger LOGGER = LoggerFactory.getLogger("Descriptor");

    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register(DescriptorCommand::register);
    }
}
