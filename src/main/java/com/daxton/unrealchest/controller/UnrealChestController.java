package com.daxton.unrealchest.controller;

import com.daxton.unrealchest.UnrealChest;
import com.daxton.unrealchest.been.TrPlayer;
import com.daxton.unrealchest.gui.UnrealChestGUI;
import com.daxton.unrealcore.application.UnrealCoreAPI;

import com.daxton.unrealcore.application.base.YmlFileUtil;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UnrealChestController {

    public static Map<String, String> chestMap = new ConcurrentHashMap<>();
    //GUI列表
    public static Map<String, FileConfiguration> unrealCoreGUIMap = new HashMap<>();
    //來判斷是否有打開TrMenu
    public static Map<String, TrPlayer> trMenuOpen = new HashMap<>();

    //讀取
    public static void load(){
        if(Bukkit.getPluginManager().getPlugin("UnrealCore") == null){
            return;
        }
        //建立設定檔

        UnrealChest.unrealCorePlugin.createConfig();

        unrealCoreGUIMap.clear();

        unrealCoreGUIMap = UnrealChest.unrealCorePlugin.findYmlMap("gui");

        chestMap.clear();
        FileConfiguration resourceConfig = UnrealChest.unrealCorePlugin.getYmlFile("config.yml");
        YmlFileUtil.sectionList(resourceConfig, "").forEach(key->{
            String value = resourceConfig.getString(key);
            if(value != null){
                chestMap.put(key, value);
            }
        });

    }
    //重新讀取設定
    public static void reload(){
        if(Bukkit.getPluginManager().getPlugin("UnrealCore") == null){
            return;
        }
        removeChest();
        load();
        setChest();
    }

    //以箱子名稱打開GUI
    public static void openChest(Player player, String title, boolean trMenu){
        if(chestMap.containsKey(title)){
            String guiName = chestMap.get(title);
            open(player, guiName, trMenu);
        }
    }
    //打開GUI
    public static void open(Player player, String guiName, boolean trMenu){
        if(unrealCoreGUIMap.containsKey(guiName)){
            FileConfiguration guiConfig = unrealCoreGUIMap.get(guiName);
            UnrealChestGUI unrealChestGUI = new UnrealChestGUI(guiName, guiConfig);
            unrealChestGUI.setTrMenu(trMenu);
            UnrealCoreAPI.inst(player).getGUIHelper().openCoreGUI(unrealChestGUI);
//            UnrealCoreAPI.openGUI(player, unrealChestGUI);
        }
    }
    //設置要改變的箱子(所有線上玩家)
    public static void setChest(){
        Bukkit.getOnlinePlayers().forEach(UnrealChestController::setChest);
    }

    //設置要改變的箱子(指定玩家)
    public static void setChest(Player player){
        chestMap.forEach((title, guiName) -> {
            UnrealCoreAPI.setChest(player, title);
        });
    }
    //移除所有箱子設置(所有線上玩家)
    public static void removeChest(){
        Bukkit.getOnlinePlayers().forEach(player -> {
            chestMap.forEach((title, guiName) -> {
                UnrealCoreAPI.removeChest(player, title);
            });
        });
    }

}
