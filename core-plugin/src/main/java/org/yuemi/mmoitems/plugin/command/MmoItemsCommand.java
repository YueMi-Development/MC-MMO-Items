package org.yuemi.mmoitems.plugin.command;

import org.jetbrains.annotations.NotNull;
import org.yuemi.mmoitems.plugin.command.subcommands.GiveCommand;
import org.yuemi.mmoitems.plugin.command.subcommands.ListCommand;
import org.yuemi.mmoitems.plugin.command.subcommands.ReloadCommand;
import org.yuemi.mmoitems.plugin.item.ItemManager;

public final class MmoItemsCommand extends BaseCommand {

    public MmoItemsCommand(@NotNull ItemManager itemManager) {
        register(new ReloadCommand(itemManager));
        register(new ListCommand(itemManager));
        register(new GiveCommand(itemManager));
    }

    @Override
    protected String getCommandLabel() {
        return "MMO Items";
    }
}
