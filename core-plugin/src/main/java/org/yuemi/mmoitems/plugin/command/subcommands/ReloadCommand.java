package org.yuemi.mmoitems.plugin.command.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.plugin.command.SubCommand;
import org.yuemi.mmoitems.plugin.item.ItemManager;

import java.util.Collections;
import java.util.List;

public final class ReloadCommand implements SubCommand {

    private final ItemManager itemManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ReloadCommand(@NotNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public @NotNull String getName() {
        return "reload";
    }

    @Override
    public @NotNull String getDescription() {
        return "Reload custom item configurations";
    }

    @Override
    public @NotNull String getPermission() {
        return "mmoitems.command.reload";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        try {
            itemManager.loadConfigs();
            sender.sendMessage(miniMessage.deserialize("<green>Custom item configurations reloaded successfully!</green>"));
        } catch (Exception e) {
            sender.sendMessage(miniMessage.deserialize("<red>Failed to reload configurations: " + e.getMessage() + "</red>"));
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
