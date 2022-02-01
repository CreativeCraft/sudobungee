package org.creativecraft.sudobungee;

import co.aikar.commands.BungeeCommandManager;
import co.aikar.commands.CommandReplacements;
import co.aikar.commands.MessageType;
import de.themoep.minedown.MineDown;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.bstats.bungeecord.MetricsLite;
import org.creativecraft.sudobungee.commands.SudoBungeeCommand;
import org.creativecraft.sudobungee.config.MessagesConfig;
import org.creativecraft.sudobungee.config.SettingsConfig;

public final class SudoBungeePlugin extends Plugin {
    public static SudoBungeePlugin plugin;
    private SettingsConfig settingsConfig;
    private MessagesConfig messagesConfig;

    /**
     * Enable the plugin.
     */
    @Override
    public void onEnable() {
        plugin = this;

        registerConfigs();
        registerCommands();

        new MetricsLite(this, 14146);
    }

    /**
     * Load the plugin.
     */
    @Override
    public void onLoad() {
        //
    }

    /**
     * Disable the plugin.
     */
    @Override
    public void onDisable() {
        //
    }

    /**
     * Register the plugin configuration.
     */
    public void registerConfigs() {
        settingsConfig = new SettingsConfig(this);
        messagesConfig = new MessagesConfig(this);
    }

    /**
     * Register the plugin commands.
     */
    public void registerCommands() {
        BungeeCommandManager commandManager = new BungeeCommandManager(this);
        CommandReplacements replacements = commandManager.getCommandReplacements();

        replacements.addReplacement("sudobungee", getConfig().getString("command", "sudobungee"));

        commandManager.setFormat(MessageType.ERROR, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);
        commandManager.setFormat(MessageType.SYNTAX, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);
        commandManager.setFormat(MessageType.HELP, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);
        commandManager.setFormat(MessageType.INFO, ChatColor.GREEN, ChatColor.WHITE, ChatColor.GRAY);

        commandManager.getCommandCompletions().registerCompletion("servers", c -> getProxy().getServers().keySet());

        commandManager.registerCommand(new SudoBungeeCommand(this));
        commandManager.enableUnstableAPI("help");
    }

    /**
     * Retrieve the plugin configuration.
     *
     * @return Configuration
     */
    public Configuration getConfig() {
        return settingsConfig.getConfig();
    }

    /**
     * Save the plugin configuration.
     */
    public void saveConfig() {
        settingsConfig.saveConfig();
    }

    /**
     * Retrieve the messages configuration.
     *
     * @return Configuration
     */
    public Configuration getMessages() {
        return messagesConfig.getMessages();
    }

    /**
     * Retrieve a localized message.
     *
     * @param  key The locale key.
     * @return String
     */
    public String localize(String key) {
        String message = messagesConfig.getMessages().getString(key);

        return ChatColor.translateAlternateColorCodes(
            '&',
            message == null ? key + " is missing." : message
        );
    }

    /**
     * Send a message formatted with MineDown.
     *
     * @param sender The command sender.
     * @param value  The message.
     */
    public void sendMessage(CommandSender sender, String value) {
        sender.sendMessage(
            MineDown.parse(messagesConfig.getMessages().getString("messages.generic.prefix") + value)
        );
    }

    /**
     * Send a raw message formatted with MineDown.
     *
     * @param sender The command sender.
     * @param value  The message.
     */
    public void sendRawMessage(CommandSender sender, String value) {
        sender.sendMessage(
            MineDown.parse(value)
        );
    }
}
