package com.daxton.unrealchest.application;

import me.arasple.mc.trmenu.module.display.MenuSession;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class TrMenuUtil {

    public static ItemStack getItemStack(Player player, int slot) {

        MenuSession menuSession = MenuSession.Companion.getSession(player);
        if(slot > 53){
            PlayerInventory inventory = player.getInventory();
            ItemStack itemStack = inventory.getItem(getSlot(slot));
            if(itemStack != null){
                return itemStack;
            }
        }else {
            if(menuSession.getIcon(slot) != null){
                return menuSession.getIcon(slot).getDefIcon().getDisplay().get(menuSession);
            }
        }
        return new ItemStack(Material.AIR);
    }

    public static int getSlot(int slot) {
        switch (slot){
            case 54: return 9;case 55: return 10;case 56: return 11;case 57: return 12;case 58: return 13;case 59: return 14;case 60: return 15;case 61: return 16;case 62: return 17;
            case 63: return 18;case 64: return 19;case 65: return 20;case 66: return 21;case 67: return 22;case 68: return 23;case 69: return 24;case 70: return 25;case 71: return 26;
            case 72: return 27;case 73: return 28;case 74: return 29;case 75: return 30;case 76: return 31;case 77: return 32;case 78: return 33;case 79: return 34;case 80: return 35;
            case 81: return 0;case 82: return 1;case 83: return 2;case 84: return 3;case 85: return 4;case 86: return 5;case 87: return 6;case 88: return 7;case 89: return 8;
            default: return 0;
        }
    }


}
