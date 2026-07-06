package org.yuemi.mmoitems.plugin;

import org.yuemi.mmoitems.plugin.config.migration.ConfigMigrator;
import org.yuemi.mmoitems.plugin.item.ItemManager;
import org.yuemi.mmoitems.plugin.listener.ItemLifecycleListener;
import org.yuemi.mmoitems.plugin.command.MmoItemsCommand;
import org.yuemi.mmoitems.plugin.command.subcommands.GiveCommand;
import org.yuemi.mmoitems.plugin.command.subcommands.ListCommand;
import org.yuemi.mmoitems.plugin.command.subcommands.ReloadCommand;

import org.yuemi.mmoitems.plugin.item.MmoItemProvider;

import java.io.File;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmoitems.api.MmoItemsApi;

public final class MmoItemsPlugin extends JavaPlugin {

    private MmoItemsApi api;
    private ItemManager itemManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        migrateConfig();

        this.itemManager = new ItemManager(this);
        this.itemManager.loadConfigs();

        new ItemLifecycleListener(this, itemManager).register();

        MmoItemsCommand cmd = new MmoItemsCommand();
        cmd.registerSubCommand(new GiveCommand(itemManager));
        cmd.registerSubCommand(new ListCommand(itemManager));
        cmd.registerSubCommand(new ReloadCommand(itemManager));
        var mmoCommand = getCommand("mmoitems");
        if (mmoCommand != null) {
            mmoCommand.setExecutor(cmd);
            mmoCommand.setTabCompleter(cmd);
        }

        this.api = new MmoItemsApiImpl(itemManager);

        // Register ItemProvider with YueMiLibs ItemsApi
        try {
            org.yuemi.libs.api.items.ItemsApi itemsApi = org.yuemi.libs.api.items.ItemsApiProvider.getApi();
            if (itemsApi != null) {
                itemsApi.registerProvider("mmoitems", new MmoItemProvider(itemManager));
                getLogger().info("Successfully registered MMOItems as an ItemProvider in YueMiLibs!");
            }
        } catch (Throwable t) {
            getLogger().warning("Could not register ItemsApi provider: " + t.getMessage());
        }

        getServer().getServicesManager().register(
                MmoItemsApi.class,
                api,
                this,
                ServicePriority.Normal
        );
    }

    @Override
    public void onDisable() {
        if (api != null) {
            getServer().getServicesManager().unregister(MmoItemsApi.class, api);
        }
    }

    private void migrateConfig() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (configFile.exists()) {
            ConfigMigrator migrator = new ConfigMigrator(this);
            migrator.migrate(configFile);
            reloadConfig();
        }
    }
}

