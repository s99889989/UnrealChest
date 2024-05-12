package com.daxton.unrealchest;

import com.daxton.unrealchest.command.UnrealChestCommand;
import com.daxton.unrealchest.command.UnrealChestTab;
import com.daxton.unrealchest.controller.UnrealChestController;
import com.daxton.unrealchest.listener.TrMenuListener;
import com.daxton.unrealchest.listener.UnrealChestListener;
import com.daxton.unrealcore.UnrealCorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class UnrealChest extends JavaPlugin {

    public static UnrealCorePlugin unrealCorePlugin;

    @Override
    public void onEnable() {
        unrealCorePlugin = new UnrealCorePlugin(this);

        Objects.requireNonNull(Bukkit.getPluginCommand("unrealchest")).setExecutor(new UnrealChestCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("unrealchest")).setTabCompleter(new UnrealChestTab());


        Bukkit.getPluginManager().registerEvents(new UnrealChestListener(), unrealCorePlugin.getJavaPlugin());

        UnrealChestController.load();


        if(Bukkit.getServer().getPluginManager().getPlugin("TrMenu") != null){
            Bukkit.getPluginManager().registerEvents(new TrMenuListener(), unrealCorePlugin.getJavaPlugin());
        }

    }

    @Override
    public void onDisable() {

    }

}
