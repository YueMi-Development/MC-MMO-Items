package org.yuemi.mmoitems.plugin.command.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.plugin.command.SubCommand;
import org.yuemi.mmoitems.plugin.item.ItemManager;

import java.util.Collections;
import java.util.List;

public final class ListCommand implements SubCommand {

    private final ItemManager itemManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

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
            sender.sendMessage(miniMessage.deserialize("<red>No custom items are currently registered.</red>"));
            return;
        }

        sender.sendMessage(miniMessage.deserialize("<gold>=== Registered Custom Items ===</gold>"));
        for (String id : items) {
            sender.sendMessage(miniMessage.deserialize("<yellow>- " + id + "</yellow>"));
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        return Collections.emptyList();
    }
}
