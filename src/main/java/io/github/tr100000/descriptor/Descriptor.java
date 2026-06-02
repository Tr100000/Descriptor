package io.github.tr100000.descriptor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.tr100000.descriptor.command.DescriptorCommand;
import io.github.tr100000.descriptor.config.DescriptorConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

public class Descriptor implements ClientModInitializer {
    public static final String MODID = "descriptor";
    public static final Logger LOGGER = LoggerFactory.getLogger("Descriptor");

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("descriptor.json");

    @Override
    public void onInitializeClient() {
        DescriptorConfig.load();

        ClientCommandRegistrationCallback.EVENT.register(DescriptorCommand::register);
    }
}
