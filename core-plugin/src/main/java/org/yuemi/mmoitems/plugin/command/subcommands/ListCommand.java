package org.yuemi.mmoitems.plugin.command.subcommands;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.plugin.command.BaseSubCommand;
import org.yuemi.mmoitems.plugin.item.ItemManager;

import java.util.List;

public final class ListCommand extends BaseSubCommand {

    private final ItemManager itemManager;

    public ListCommand(@NotNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public @NotNull String getName() {
        return "list";
    }

    @Override
    public @NotNull String getDescription() {
        return "List all registered custom items";
    }

    @Override
    public @NotNull String getPermission() {
        return "mmoitems.command.list";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        var items = itemManager.getRegisteredItemTypes();
        if (items.isEmpty()) {
            sendMessage(sender, "<red>No custom items are currently registered.</red>");
            return;
        }

        sendMessage(sender, "<gold>=== Registered Custom Items ===</gold>");
        for (String id : items) {
            sendMessage(sender, "<yellow>- " + id + "</yellow>");
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return List.of();
    }
}
