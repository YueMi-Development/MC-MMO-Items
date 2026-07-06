package org.yuemi.mmoitems.plugin.command.subcommands;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.plugin.command.SubCommand;
import org.yuemi.mmoitems.plugin.item.ItemManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class GiveCommand implements SubCommand {

    private final ItemManager itemManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public GiveCommand(@NotNull ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Override
    public @NotNull String getName() {
        return "give";
    }

    @Override
    public @NotNull String getDescription() {
        return "Give a custom item to a player";
    }

    @Override
    public @NotNull String getPermission() {
        return "mmoitems.command.give";
    }

    @Override
    public void execute(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length < 2) {
            sender.sendMessage(miniMessage.deserialize("<red>Usage: /mmoitems give <player> <itemId> [amount]</red>"));
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(miniMessage.deserialize("<red>Player '" + args[0] + "' not found.</red>"));
            return;
        }

        String itemId = args[1];
        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {}
        }
        if (amount < 1) amount = 1;

        try {
            ItemStack item = itemManager.createCustomItem(itemId);
            item.setAmount(amount);
            target.getInventory().addItem(item);
            sender.sendMessage(miniMessage.deserialize("<green>Gave " + amount + "x '" + itemId + "' to " + target.getName() + ".</green>"));
            target.sendMessage(miniMessage.deserialize("<green>You received " + amount + "x '" + itemId + "'.</green>"));
        } catch (IllegalArgumentException e) {
            sender.sendMessage(miniMessage.deserialize("<red>" + e.getMessage() + "</red>"));
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return itemManager.getRegisteredItemTypes().stream()
                    .filter(type -> type.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if (args.length == 3) {
            return List.of("1", "64");
        }
        return Collections.emptyList();
    }
}
