package com.daxton.unrealchest.application;

import com.daxton.unrealchest.api.objecthunter.exp4j.Expression;
import com.daxton.unrealchest.api.objecthunter.exp4j.ExpressionBuilder;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class MathConvert {

    public static String convert(Player player, String content){
        if (player == null || content == null || content.isEmpty()) {
            return content;
        }
        if(Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null){
            content = PlaceholderAPI.setPlaceholders(player, content);
        }

        if(isSingleDigit(content)){
            return content;
        }

        try {
            Expression expression = new ExpressionBuilder(content).build();
            return String.valueOf(expression.evaluate());
        }catch (IllegalArgumentException | ArithmeticException exception) {
            return content;
        }
    }

    //判斷是否是純數字
    public static boolean isSingleDigit(String str) {
        String regex = "\\d";
        return Pattern.matches(regex, str);
    }

}
