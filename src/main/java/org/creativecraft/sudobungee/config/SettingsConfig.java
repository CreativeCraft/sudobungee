package org.creativecraft.sudobungee.config;

import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.creativecraft.sudobungee.SudoBungeePlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class SettingsConfig {
    private final SudoBungeePlugin plugin;
    private Configuration config;
    private File configFile;

    /**
     * Initialize the SettingsConfig instance.
     *
     * @param plugin ExamplePlugin
     */
    public SettingsConfig(SudoBungeePlugin plugin) {
        this.plugin = plugin;

        registerConfig();
    }

    /**
     * Register the config configuration.
     */
    public void registerConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try (InputStream inputStream = plugin.getResourceAsStream("config.yml")) {
                Files.copy(inputStream, configFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                new File(plugin.getDataFolder(), "config.yml")
            );
        } catch (Exception e) {
            //
        }
    }

    /**
     * Retrieve the config configuration.
     *
     * @return Configuration
     */
    public Configuration getConfig() {
        return config;
    }

    /**
     * Retrieve the config file.
     *
     * @return File
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * Save the config configuration.
     */
    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(
                config,
                new File(plugin.getDataFolder(), "config.yml")
            );
        } catch (Exception e) {
            //
        }

    }
}
