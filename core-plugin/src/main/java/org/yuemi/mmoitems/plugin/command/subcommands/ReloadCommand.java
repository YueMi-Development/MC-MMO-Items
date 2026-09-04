package org.yuemi.mmoitems.plugin.command.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.plugin.command.BaseSubCommand;
import org.yuemi.mmoitems.plugin.item.ItemManager;

import java.util.List;

public final class ReloadCommand extends BaseSubCommand {

    private final ItemManager itemManager;

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
            sendMessage(sender, "<green>Custom item configurations reloaded successfully!</green>");
        } catch (Exception e) {
            sendMessage(sender, "<red>Failed to reload configurations: " + e.getMessage() + "</red>");
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return List.of();
    }
}
