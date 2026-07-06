package org.yuemi.mmoitems.plugin;

import org.yuemi.mmoitems.plugin.config.migration.ConfigMigrator;
import org.yuemi.mmoitems.plugin.item.ItemManager;
import org.yuemi.mmoitems.plugin.listener.ItemLifecycleListener;

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

        this.api = new MmoItemsApiImpl(itemManager);

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

