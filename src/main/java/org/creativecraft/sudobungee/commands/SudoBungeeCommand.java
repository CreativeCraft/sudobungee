package org.creativecraft.sudobungee.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.creativecraft.sudobungee.SudoBungeePlugin;
import net.md_5.bungee.api.plugin.Listener;
import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;

import java.util.concurrent.TimeUnit;

@CommandAlias("%sudobungee")
@CommandPermission("sudobungee.use")
@Description("Simple cross-server Sudo for BungeeCord.")
public final class SudoBungeeCommand extends BaseCommand implements Listener {
    private final SudoBungeePlugin plugin;

    public SudoBungeeCommand(SudoBungeePlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Connect a player to another server then execute a command.
     *
     * @param sender  The command sender.
     * @param target  The target player.
     * @param server  The destination server.
     * @param command The command.
     */
    @Default
    @CommandCompletion("@players @servers @nothing")
    @Syntax("<player> <server> [command]")
    public void onDefault(CommandSender sender, String target, String server, String[] command) {
        ProxiedPlayer player = plugin.getProxy().getPlayer(target);
        ServerInfo destination = plugin.getProxy().getServerInfo(server);

        if (player == null) {
            plugin.sendMessage(
                sender,
                plugin
                    .localize("messages.sudobungee.invalid-player")
                    .replace("{player}", target)
            );

            return;
        }

        if (destination == null) {
            plugin.sendMessage(
                sender,
                plugin
                    .localize("messages.sudobungee.invalid-server")
                    .replace("{server}", server)

            );

            return;
        }

        if (command.length == 0) {
            plugin.sendMessage(
                sender,
                plugin.localize("messages.sudobungee.invalid-command")
            );

            return;
        }

        if (player.getServer().getInfo().equals(destination)) {
            player.chat(
                String.format("/%s", String.join(" ", command))
            );

            plugin.sendMessage(
                sender,
                plugin
                    .localize("messages.sudobungee.success")
                    .replace("{player}", player.getName())
                    .replace("{command}", String.join(" ", command))
                    .replace("{server}", destination.getName())
            );

            return;
        }

        player.connect(destination, (aBoolean, throwable) -> {
            ProxyServer.getInstance().getScheduler().schedule(this.plugin, new Runnable() {
                public void run() {
                    player.chat(
                        String.format("/%s", String.join(" ", command))
                    );
                }
            }, plugin.getConfig().getInt("delay", 250), TimeUnit.MILLISECONDS);

            plugin.sendMessage(
                sender,
                plugin
                    .localize("messages.sudobungee.success")
                    .replace("{player}", player.getName())
                    .replace("{command}", String.join(" ", command))
                    .replace("{server}", destination.getName())
            );
        });
    }
}
