package com.daxton.unrealchest.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnrealChestTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> commandList = new ArrayList<>();

        if(sender instanceof Player){
            Player player = (Player) sender;
            if(!player.isOp()){
                return commandList;
            }
        }

        if (args.length == 1){
            String[] subCommands = {"reload", "open"};
            commandList = Arrays.stream(subCommands).filter(s -> s.startsWith(args[0])).collect(Collectors.toList());
        }


        return commandList;
    }


}
