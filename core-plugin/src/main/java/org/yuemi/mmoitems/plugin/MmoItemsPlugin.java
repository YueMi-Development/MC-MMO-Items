package org.yuemi.mmoitems.plugin;

import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.yuemi.mmoitems.api.MmoItemsApi;

public final class MmoItemsPlugin extends JavaPlugin {

    private MmoItemsApi api;

    @Override
    public void onEnable() {
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
}
