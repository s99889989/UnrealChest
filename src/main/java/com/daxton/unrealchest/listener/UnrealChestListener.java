package com.daxton.unrealchest.listener;


import com.daxton.unrealchest.UnrealChest;
import com.daxton.unrealchest.been.TrPlayer;
import com.daxton.unrealchest.controller.UnrealChestController;
import com.daxton.unrealcore.application.UnrealCoreAPI;
import com.daxton.unrealcore.communication.event.PlayerConnectionSuccessfulEvent;
import com.daxton.unrealcore.display.content.gui.UnrealCoreGUI;
import com.daxton.unrealcore.display.event.gui.PlayerGUICloseEvent;
import com.daxton.unrealcore.display.event.gui.PlayerOpenChestEvent;

import com.daxton.unrealgui.controller.GUIController;
import me.arasple.mc.trmenu.module.display.MenuSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;


public class UnrealChestListener implements Listener {



    @EventHandler
    public void onChestOpen(PlayerOpenChestEvent event){
        Player player = event.getPlayer();
        String uuidString = player.getUniqueId().toString();
        String title = event.getTitle();
        boolean trMenu = false;
        if(UnrealChestController.trMenuOpen.containsKey(uuidString)){
            TrPlayer trPlayer = UnrealChestController.trMenuOpen.get(uuidString);
            if(trPlayer.isFirst()){
                trPlayer.setFirst(false);
                trPlayer.setTrMenu(true);
                return;
            }
            if(trPlayer.isTrMenu()){
                trMenu = true;
                trPlayer.setTrMenu(false);
            }
        }
        UnrealChestController.openChest(player, title, trMenu);
    }

    @EventHandler//當玩家關閉GUI
    public void onGUIClose(PlayerGUICloseEvent event) {
        Player player = event.getPlayer();
        String uuidString = player.getUniqueId().toString();
        String guiName = event.getGuiName();
        UnrealCoreGUI unrealCoreGUI = UnrealCoreAPI.inst(player).getGUIHelper().getUnrealCoreGUI();
        if(unrealCoreGUI != null){
            String dataGUIName = unrealCoreGUI.getGUIName();
            if(!guiName.equals(dataGUIName)){
                return;
            }

            if(UnrealChestController.trMenuOpen.containsKey(uuidString)){
                TrPlayer trPlayer = UnrealChestController.trMenuOpen.get(uuidString);
                if(!trPlayer.isFirst()){
                    MenuSession.Companion.getSession(event.getPlayer()).close(true,true);
                    UnrealChestController.trMenuOpen.remove(uuidString);
                }

            }
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerConnectionSuccessfulEvent event){
        Player player = event.getPlayer();
        UnrealChestController.setChest(player);
    }

    //    @EventHandler
//    public void onInventoryOpen(InventoryOpenEvent event) {
//        Player player = (Player) event.getPlayer();
//        InventoryView inventoryView = event.getView();
//        String title = inventoryView.getTitle();
//
//        UnrealChestController.setChest(player, title);
//
//    }

}
