package com.daxton.unrealchest.command;

import com.daxton.unrealchest.UnrealChest;
import com.daxton.unrealchest.controller.UnrealChestController;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class UnrealChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1){
            if(args[0].equalsIgnoreCase("reload")){

                if(sender instanceof Player){
                    Player player = (Player) sender;
                    if(!player.isOp()){
                        return true;
                    }
                    player.sendMessage("[UnrealChest] Reload");
                }
                UnrealChest.unrealCorePlugin.sendSystemLogger("Reload");
                UnrealChestController.reload();
                return true;
            }
            if(args[0].equalsIgnoreCase("open")){
                Player player = (Player) sender;
                if(!player.isOp()){
                    return true;
                }
                Inventory fakeChestInventory = Bukkit.createInventory(null, 54, "普通仓库");
                player.openInventory(fakeChestInventory);
            }
        }

        if (args.length == 2){
            if(sender instanceof Player){
                Player player = (Player) sender;
                if(!player.isOp()){
                    return true;
                }
                if(args[0].equalsIgnoreCase("open")){
                    Inventory fakeChestInventory = Bukkit.createInventory(null, 54, args[1]);
                    player.openInventory(fakeChestInventory);
                }


            }
        }

        return false;
    }

}
