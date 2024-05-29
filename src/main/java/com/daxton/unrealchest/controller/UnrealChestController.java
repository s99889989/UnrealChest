package com.daxton.unrealchest.controller;

import com.daxton.unrealchest.UnrealChest;
import com.daxton.unrealchest.application.ListUtil;
import com.daxton.unrealchest.been.TrPlayer;
import com.daxton.unrealchest.gui.UnrealChestGUI;
import com.daxton.unrealcore.application.UnrealCoreAPI;

import com.daxton.unrealcore.application.base.PluginUtil;
import com.daxton.unrealcore.application.base.YmlFileUtil;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.entity.Player;

import java.util.ArrayList;
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

        createConfig();

        unrealCoreGUIMap.clear();

        unrealCoreGUIMap = UnrealChest.unrealCorePlugin.findYmlMap("gui");


        FileConfiguration chestConfig = UnrealChest.unrealCorePlugin.getYmlFile("config.yml");
        if(chestConfig.contains("ChestList")){
            YmlFileUtil.sectionList(chestConfig, "ChestList").forEach(key->{
                String title = chestConfig.getString("ChestList."+key+".Title");
                String guiName = chestConfig.getString("ChestList."+key+".GUI");
                chestMap.put(title, guiName);
            });
        }else {
            YmlFileUtil.sectionList(chestConfig, "").forEach(key->{
                String value = chestConfig.getString(key);
                if(value != null){
                    chestMap.put(key, value);
                }
            });
        }


    }
    //重新讀取設定
    public static void reload(){
        if(Bukkit.getPluginManager().getPlugin("UnrealCore") == null){
            return;
        }

        chestMap.clear();

        removeChest();
        load();
        setChest();
    }

    //以箱子名稱打開GUI
    public static void openChest(Player player, String title, boolean trMenu){

        title = ListUtil.toPatternName(new ArrayList<>(chestMap.keySet()), title);
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

    //建立設定檔
    public static void createConfig(){
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/config.yml", "config.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/EChest9.yml", "gui/EChest9.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/EChest18.yml", "gui/EChest18.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/EChest27.yml", "gui/EChest27.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/EChest36.yml", "gui/EChest36.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/EChest45.yml", "gui/EChest45.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/EChest54.yml", "gui/EChest54.yml", false);
        PluginUtil.resourceCopy(UnrealChest.unrealCorePlugin.getJavaPlugin(), "resource/gui/HChest54.yml", "gui/HChest54.yml", false);
    }

}
