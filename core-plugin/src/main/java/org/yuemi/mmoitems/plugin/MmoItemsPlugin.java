package org.yuemi.mmoitems.plugin;

import org.yuemi.mmoitems.plugin.config.migration.ConfigMigrator;
import java.io.File;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmoitems.api.MmoItemsApi;

public final class MmoItemsPlugin extends JavaPlugin {

    private MmoItemsApi api;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        migrateConfig();
        this.api = new MmoItemsApiImpl();

        getServer().getServicesManager().register(
                MmoItemsApi.class,
                api,
                this,
                ServicePriority.Normal
        );
    }

    @Override
    public void onDisable() {
        getServer().getServicesManager().unregister(MmoItemsApi.class, api);
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
