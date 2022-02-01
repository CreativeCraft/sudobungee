package org.creativecraft.sudobungee.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.creativecraft.sudobungee.SudoBungeePlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MessagesConfig {
    private final SudoBungeePlugin plugin;
    private Configuration messages;
    private File messagesFile;

    /**
     * Initialize the MessagesConfig instance.
     *
     * @param plugin ExamplePlugin
     */
    public MessagesConfig(SudoBungeePlugin plugin) {
        this.plugin = plugin;

        registerConfig();
    }

    /**
     * Register the messages configuration.
     */
    public void registerConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");

        if (!messagesFile.exists()) {
            try (InputStream inputStream = plugin.getResourceAsStream("messages.yml")) {
                Files.copy(inputStream, messagesFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            messages = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                new File(plugin.getDataFolder(), "messages.yml")
            );
        } catch (Exception e) {
            //
        }
    }

    /**
     * Retrieve the messages configuration.
     *
     * @return Configuration
     */
    public Configuration getMessages() {
        return messages;
    }

    /**
     * Retrieve the messages file.
     *
     * @return File
     */
    public File getMessagesFile() {
        return messagesFile;
    }

    /**
     * Save the messages configuration.
     */
    public void saveMessages() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(
                messages,
                new File(plugin.getDataFolder(), "messages.yml")
            );
        } catch (Exception e) {
            //
        }

    }
}
