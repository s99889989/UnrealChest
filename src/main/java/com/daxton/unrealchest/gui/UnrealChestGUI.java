package com.daxton.unrealchest.gui;

import com.daxton.unrealchest.UnrealChest;
import com.daxton.unrealchest.application.CustomValueConvert;
import com.daxton.unrealchest.application.PlayerFunction;

import com.daxton.unrealchest.application.TrMenuUtil;
import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.application.method.SchedulerFunction;
import com.daxton.unrealcore.application.method.SchedulerRunnable;
import com.daxton.unrealcore.been.display.type.HoverType;
import com.daxton.unrealcore.common.type.MouseActionType;
import com.daxton.unrealcore.common.type.MouseButtonType;
import com.daxton.unrealcore.display.been.module.ModuleData;
import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;
import com.daxton.unrealcore.display.content.module.ModuleComponents;
import com.daxton.unrealcore.display.content.module.control.ButtonModule;


import com.daxton.unrealcore.display.content.module.control.SlotModule;
import com.daxton.unrealcore.display.content.module.display.ItemModule;

import com.daxton.unrealgui.UnrealGUI;
import com.daxton.unrealgui.application.UnrealGUIAPI;
import com.daxton.unrealgui.controller.GUIController;

import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
public class UnrealChestGUI extends UnrealCoreGUI {

    //佔位符列表
    public Map<String, String> customValue = new HashMap<>();
    //更新佔位符
    SchedulerRunnable schedulerRunnable;
    //是否是TrMenu
    private boolean trMenu;

    public UnrealChestGUI(String guiName, FileConfiguration fileConfiguration) {
        super(guiName, fileConfiguration);
        applyFunctionToFields(this::placeholder);
    }

    @Override
    public void opening() {
        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            placeholderChange();
        }
    }

    //更新佔位符
    public void placeholderChange(){
        schedulerRunnable = new SchedulerRunnable() {
            @Override
            public void run() {
                if(getPlayer() == null){
                    cancel();
                    return;
                }
                Map<String, String> customValueMap = new HashMap<>();
                customValue.forEach((content, contentChange) -> {
                    String value = PlaceholderAPI.setPlaceholders(getPlayer(), "%"+content+"%");
                    customValueMap.put(contentChange, value);
                });
                UnrealCoreAPI.setCustomValueMap(getPlayer(), customValueMap);
            }
        };
        schedulerRunnable.runTimer(UnrealChest.unrealCorePlugin.getJavaPlugin(), 0, 5);
    }

    public String placeholder(String content){
        if(content.startsWith("{") && content.endsWith("}") && content.length() > 30){
            content = content.replace("{", "<[").replace("}", "]>");
            return CustomValueConvert.valueNBT(content, customValue);
        }
        return CustomValueConvert.value(content, customValue);
    }

    @Override
    public void buttonClick(ButtonModule buttonModule, MouseButtonType button, MouseActionType action) {

        if(button == MouseButtonType.Left && action == MouseActionType.Off){
            String toGUI = getFileConfiguration().getString(buttonModule.getFilePath()+".ToGUI");
            if(toGUI != null){
                if(Bukkit.getPluginManager().getPlugin("UnrealGUI") != null){
                    UnrealGUIAPI.openGUI(getPlayer(), toGUI);
                }
            }

            List<String> commandList = getFileConfiguration().getStringList(buttonModule.getFilePath()+".Command");
            if(commandList.isEmpty()){
                String commandString = getFileConfiguration().getString(buttonModule.getFilePath()+".Command");
                if(commandString != null){
                    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                        PlayerFunction.onCommand(getPlayer(), PlaceholderAPI.setPlaceholders(getPlayer(), commandString));
                    }else {
                        PlayerFunction.onCommand(getPlayer(), commandString);
                    }
                }
            }else {
                commandList.forEach(command->{
                    if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                        PlayerFunction.onCommand(getPlayer(), PlaceholderAPI.setPlaceholders(getPlayer(), command));
                    }else {
                        PlayerFunction.onCommand(getPlayer(), command);
                    }
                });
            }


        }

        super.buttonClick(buttonModule, button, action);
    }

    @Override
    public void hover(ModuleComponents moduleComponents, HoverType hoverType, boolean haveItem) {
        String hover = getFileConfiguration().getString(moduleComponents.getFilePath()+".Hover", "");
        List<ModuleData> moduleDataList = GUIController.moduleDataMap.get(hover);
        if(moduleDataList == null){
            return;
        }
        if(hoverType == HoverType.LEAVE){
            List<String> stringList = GUIController.moduleIDList(moduleDataList);
            UnrealCoreAPI.inst(getPlayer()).getGUIHelper().removeTopModule(stringList);
        }
        if(hoverType == HoverType.ENTER){
            SchedulerFunction.runLater(UnrealGUI.unrealCorePlugin.getJavaPlugin(), ()->{
                if(moduleComponents instanceof SlotModule || moduleComponents instanceof ItemModule){
                    if(!haveItem){
                        return;
                    }
                }

                UnrealCoreAPI.inst(getPlayer()).getGUIHelper().addTopModule(moduleDataList);
            }, 1);

        }

        super.hover(moduleComponents, hoverType, haveItem);
    }

    @Override
    public void close() {
        if(schedulerRunnable != null){
            schedulerRunnable.cancel();
        }

        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            List<String> customValueList = new ArrayList<>();
            customValue.forEach((content, contentChange) -> {
                customValueList.add(content);
            });
            UnrealCoreAPI.customValueMultiRemove(getPlayer(), customValueList);
        }
        customValue.clear();

        super.close();
    }

//     if(Bukkit.getPluginManager().getPlugin("UnrealCore") != null){
//        List<ModuleData> moduleDataList = GUIController.moduleDataMap.get(hover);
//        if(hoverType == HoverType.ENTER){
//            if(moduleComponents instanceof SlotModule){
//                SlotModule slotModule = (SlotModule) moduleComponents;
//
//                if(trMenu){
//                    ItemStack itemStack = TrMenuUtil.getItemStack(getPlayer(), slotModule.getSlot());
//                    if (itemStack.getType() == Material.AIR) {
//                        super.hover(moduleComponents, hoverType);
//                        return;
//                    }
//                }else {
//                    int size = getPlayer().getOpenInventory().getTopInventory().getSize()+(getPlayer().getOpenInventory().getBottomInventory().getSize()-5);
//                    if(slotModule.getSlot() < size){
//                        ItemStack itemStack = getPlayer().getOpenInventory().getItem(slotModule.getSlot());
//                        if (itemStack == null || itemStack.getType() == Material.AIR) {
//                            super.hover(moduleComponents, hoverType);
//                            return;
//                        }
//                    }
//                }
//
//            }
//            if(moduleComponents instanceof ItemModule){
//                ItemModule itemModule = (ItemModule) moduleComponents;
//                ItemStack itemStack = itemModule.getItem();
//                if (itemStack == null || itemStack.getType() == Material.AIR) {
//                    super.hover(moduleComponents, hoverType);
//                    return;
//                }
//            }
//            if(moduleDataList != null){
//                UnrealCoreAPI.inst(getPlayer()).getGUIHelper().addTopModule(moduleDataList);
//            }
//        }else {
//            if(moduleDataList != null){
//                List<String> stringList = GUIController.moduleIDList(moduleDataList);
//                UnrealCoreAPI.inst(getPlayer()).getGUIHelper().removeTopModule(stringList);
//            }
//        }
//    }

}
