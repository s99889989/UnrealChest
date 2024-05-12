package com.daxton.unrealchest.listener;

import com.daxton.unrealchest.UnrealChest;
import com.daxton.unrealchest.been.TrPlayer;
import com.daxton.unrealchest.controller.UnrealChestController;
import com.daxton.unrealgui.controller.GUIController;
import me.arasple.mc.trmenu.api.event.MenuOpenEvent;
import me.arasple.mc.trmenu.module.display.MenuSession;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TrMenuListener implements Listener {

    @EventHandler //當玩家打開TrMenuGUI
    public void openGUI(MenuOpenEvent event){
        Player player = event.getSession().getAgent();
        String uuidString = player.getUniqueId().toString();
        TrPlayer trPlayer = new TrPlayer();
        UnrealChestController.trMenuOpen.put(uuidString, trPlayer);
    }

}
